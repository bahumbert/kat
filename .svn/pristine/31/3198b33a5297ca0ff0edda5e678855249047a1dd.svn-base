/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idfor.kat.tools.katexecbundle.run;

import com.idfor.kat.tools.katexecbundle.beans.*;
import org.apache.commons.lang3.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Class used to execute a Talend job. A runner is launched by the scheduler at execution scheduled time
 * It detects the environment and formats a Windows or Linux version of the runner according to this environment
 * The runner also starts two loggers, one for the regular flow and one for the error flow
 */
public class JobRunner implements Runnable {

    private final Logger LOGGER = LoggerFactory.getLogger(JobRunner.class);

    private JobProperties job;

    /**
     * Constructor just saving job properties object passed as job reference
     * @param job description of job to be run
     */
    public JobRunner(JobProperties job){
        this.job = job;
    }

    /**
     * Method used to build job builder object to be fired by the scheduler
     * @param job properties of job to be executed
     * @return a ProcessBuilder object, corresponding to the job to be launched
     * @throws IOException
     */
    private ProcessBuilder getProcessBuilder(JobProperties job) throws IOException {

        // Get the right script file according OS
        String executionPath = job.getBinariesPath() + "/" + job.getName() + "/" + job.getVersion() + "/" + job.getName();
        String scriptFile = executionPath  + "/" + job.getName() + "_run";
        if(SystemUtils.IS_OS_WINDOWS) scriptFile += ".bat";
        if(SystemUtils.IS_OS_LINUX) scriptFile += ".sh";

        // Read .bat or .sh file and extract java command
        Stream<String> stream = Files.lines(Paths.get(scriptFile)).filter(line -> line.trim().startsWith("java"));
        String javaCommand = stream.findFirst().get().trim();

        // Gets java command parts
        String[] javaCommandParts = javaCommand.split(" ");

        // Analyzes, modifies and replaces to get final parts
        List<String> processBuilderArguments = new ArrayList<String>();
        for(String javaCommandPart: javaCommandParts){
            if(javaCommandPart.startsWith("-Xms")) processBuilderArguments.add("-Xms" + job.getInitialJavaHeapSpace());
            else if(javaCommandPart.startsWith("-Xmx")) processBuilderArguments.add("-Xmx" + job.getMaximumJavaHeapSpace());
            else if(javaCommandPart.startsWith("--context")){

                // If context is overloaded, use overloaded properties file
                if(job.getOverloadedContext() == null) processBuilderArguments.add("--context=" + job.getContext());
                else  processBuilderArguments.add("--context=" + job.getOverloadedContext());
            }
            else processBuilderArguments.add(javaCommandPart.replaceAll("\\$ROOT_PATH", executionPath));
        }

        // Closes stream
        stream.close();

        // Builds final process builder command array
        return new ProcessBuilder(processBuilderArguments);
    }

    /**
     * Method used to execute an exported Talend job. The results of execution are intercepted and redirected to log file.
     */
    public void run() {

        // Try to execute Talend job
        try {

            // We assume job will run OK
            String jobEndState = "Job ended OK";

            // Set MDC tags for easy Kibana filtering
            MDC.put("katJobProject", job.getProject());
            MDC.put("katJobName", job.getName());
            MDC.put("katJobVersion", job.getVersion());
            MDC.put("katJobContext", job.getContext());
            MDC.remove("ketJobExecTime");

            // Registers job start time
            Date jobStartTime = new Date();
            MDC.put("katJobExecutionStamp", job.getPid() + "-" + Long.toString(jobStartTime.getTime()));
            LOGGER.info("Job started...");

            // Preparation of execution parameters for JAVA VM
            // Execution directory
            String executionPath = job.getBinariesPath() + "/" + job.getName() + "/" + job.getVersion() + "/" + job.getName();

            // Construction and execution of a process builder object running exported Talend job
            ProcessBuilder processBuilder = getProcessBuilder(this.job);
            processBuilder.directory(new File(executionPath));
            Process process = processBuilder.start();

            // Reports that job has been started and add process to process list
            JobList.setRunning(job.getPid(), true);
            ProcessList.add(new ProcessProperties(job.getPid(), process));

            // We catch both regular and error streams and send them to the logger with the right logging level
            LogCatcher infoLog = new LogCatcher(process.getInputStream(), job, jobStartTime, LogCatcher.LogType.LOG4J, LogCatcher.LogLevel.INFO);
            LogCatcher errorLog = new LogCatcher(process.getErrorStream(), job, jobStartTime, LogCatcher.LogType.LOG4J, LogCatcher.LogLevel.ERROR);

            // Starts threads to intercept info and error outputs from jobs
            new Thread(infoLog).start();
            new Thread(errorLog).start();

            // Wait for the end of execution
            process.waitFor();

            // Registers job end time
            Date jobEndTime = new Date();
            String jobExecTime = Long.toString(jobEndTime.getTime() - jobStartTime.getTime());

            // Job ended with errors
            if(errorLog.getHasOutput()){
                jobEndState = "Job ended with ERRORS !!!";

                // If a mail alert is configured, send a mail
                if(job.isMailActive() && job.getMailList() != null)
                    SendMail.SendTextMessage(job.getMailList(), substitute(KatExecProperties.getMailSubject(), job), substitute(KatExecProperties.getMailBody(), job));
            }

            // Job was cancelled by user
            if(ProcessList.isKilled(job.getPid())) jobEndState = "Job CANCELED by user !!!";

            // Put MDC to retrieve execution duration
            MDC.put("katJobExecTime", jobExecTime);
            LOGGER.info(jobEndState + " - It took " + jobExecTime + "ms to complete...");

            // In case we had a problem starting the job, intercept problem and notify user
        } catch (IOException e) {
            LOGGER.error("The following job script: " + e.getMessage() + " does not exists !!!");
            LOGGER.error("Please correct configuration file " + job.getPropertyFile() + " of job pid " + job.getPid() + " or correct script file location and/or name");
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
        finally {

            // Reports that job has ended
            JobList.setRunning(job.getPid(), false);
            ProcessList.remove(job.getPid());
        }
    }

    /**
     * Substitutes keywords with corresponding job properties
     * @param originalString original string with keywords
     * @param job job properties
     * @return new string with all keywords replaced by effective job properties if found
     */
    private String substitute(String originalString, JobProperties job){
        String newString = originalString;
        newString = newString.replace("%project%", job.getProject());
        newString = newString.replace("%job%", job.getName());
        newString = newString.replace("%version%", job.getVersion());
        newString = newString.replace("%context%", job.getContext());
        return newString;
    }
}