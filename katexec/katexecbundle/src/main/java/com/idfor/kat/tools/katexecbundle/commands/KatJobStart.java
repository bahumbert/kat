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

package com.idfor.kat.tools.katexecbundle.commands;

import com.idfor.kat.tools.katexecbundle.beans.JobList;
import com.idfor.kat.tools.katexecbundle.beans.JobProperties;
import com.idfor.kat.tools.katexecbundle.run.JobRunner;
import org.apache.karaf.scheduler.Scheduler;
import org.apache.karaf.shell.api.action.*;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;

/**
 * KAT Executor command: kat:job-start pid
 * This command allows user to immediately start a job in the list of scheduled jobs. The job might be in paused status
 * TAB strokes auto-complete pid argument using the pid completer
 * @see KatPidCompleter
 */
@Command(scope = "kat", name = "job-start", description = "Start a scheduled TALEND job NOW")
@Service
public class KatJobStart implements Action{

    private final Logger LOGGER = LoggerFactory.getLogger(KatJobStart.class);

    @Argument(name = "pid", required = true, description = "Process ID of deployed TALEND job to start now")
    @Completion(KatPidCompleter.class)
    String pid;

    @Reference
    Scheduler scheduler;

    @Override
    public Object execute() throws Exception {

        // Find back information about the job to be fires
        JobProperties job = JobList.findByPid(pid);

        // We have found a scheduled job with this pid so prepare  to fire it NOW
        if(job != null) {

            // Verify that job is not already running
            if (!job.isRunning()) {

                // Prepare an instance of job to be executed
                JobRunner jobToSchedule = new JobRunner(job);

                try {

                    // Try to start job instance now
                    Date now = new Date();
                    String timeStamp = Long.toString(now.getTime());
                    String nowPid = pid + "." + timeStamp;
                    scheduler.schedule(jobToSchedule, scheduler.NOW().name(nowPid));

                    // Scheduling OK so put job in the active job list and notify log
                    // Note that we first delete an eventual existing entry in case this pid is already existing
                    String infoMsg = "Job " + job.getProject() + ":" + job.getName() + ":" + job.getVersion() + " has been started";
                    LOGGER.info(infoMsg);
                    System.out.println(infoMsg);
                } catch (SchedulerException e) {
                    String errorMsg = "ERROR STARTING JOB " + pid + " !!!";
                    LOGGER.error(errorMsg);
                    throw new RuntimeException(errorMsg);
                }
            }

            // Job is already running so we don't launch it again
            else{
                System.out.println("Job with pid " + pid + " is already running");
            }
        }

        // We hove not find any job to fire so inform the user
        else throw new RuntimeException("No job with pid: " + pid + " has been found for execution");

        return null;
    }
}