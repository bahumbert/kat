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
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.support.table.ShellTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Iterator;
import java.util.List;

/**
 * KAT Executor command: kat:schedule-list
 * This command allows user to get a list of all scheduled jobs. This list prints all parameters for each job
 */
@Command(scope = "kat", name = "schedule-list", description = "Get list of scheduled TALEND jobs -- May be filtered using options (AND applies if different options are used)")
@Service
public class KatScheduleList implements Action{

    private final Logger logger = LoggerFactory.getLogger(KatScheduleList.class);

    @Option(name = "job", aliases = { "--job" }, description = "Name of job")
    String jobName;

    @Option(name = "project", aliases = { "--project" }, description = "Project of job")
    String project;

    @Option(name = "version", aliases = { "--version" }, description = "Version of job")
    String version;

    @Option(name = "context", aliases = { "--context" }, description = "Context of job")
    String context;

    @Option(name = "running", aliases = { "--running" }, description = "Running state of job (yes or no)")
    String running;

    @Override
    public Object execute() throws Exception {

        // Prepares table layout for output
        ShellTable table = new ShellTable();
        table.column("Job Properties");
        table.column("Schedule");
        table.column("State");
        table.column("Running");

        // Get complete list of jobs
        List<JobProperties> jobList = JobList.getList();
        Iterator<JobProperties> jobIterator = jobList.iterator();

        // Init variables
        boolean firstIteration = true;
        boolean showJob;
        String isRunning;

        // Iterate though list and output parameters using pre-defined table layout
        while (jobIterator.hasNext()) {

            // Get next job in the list
            JobProperties job = jobIterator.next();

            // By default, job is displayed in job list
            showJob = true;

            // Unless it is filtered out on...
            // ...job name
            if(jobName != null)
                if(job.getName().indexOf(jobName) == -1) showJob = false;
            // ...job project
            if(project != null)
                if(job.getProject().indexOf(project) == -1) showJob = false;
            // ...job version
            if(version != null)
                if(job.getVersion().indexOf(version) == -1) showJob = false;
            // ...job context
            if(context != null)
                if(job.getContext().indexOf(context) == -1) showJob = false;
            // ...job is running or not
            if(running != null) {
                if (job.isRunning() && !running.equals("yes")) showJob = false;
                if (!job.isRunning() && !running.equals("no")) showJob = false;
            }

            // If all criteria have been met, we add job to job list
            if(showJob) {

                // If it is not the first job being displayed, issue a separation blank line
                if(!firstIteration) table.addRow().addContent("", "", "", "");
                else firstIteration = false;

                // Transform boolean value of job running to yes or no
                if (job.isRunning()) isRunning = "yes";
                else isRunning = "no";

                // Add line describing job to table
                table.addRow().addContent("PID         : " + job.getPid(), job.getScheduling(), job.getState(), isRunning);
                table.addRow().addContent("CONFIG FILE : " + job.getPropertyFile(), "", "", "");
                table.addRow().addContent("PROJECT     : " + job.getProject(), "", "", "");
                table.addRow().addContent("NAME        : " + job.getName(), "", "", "");
                table.addRow().addContent("VERSION     : " + job.getVersion(), "", "", "");
                table.addRow().addContent("CONTEXT     : " + job.getContext(), "", "", "");
                table.addRow().addContent("LABEL       : " + job.getLabel(), "", "", "");
                table.addRow().addContent("DEPLOY DATE : " + job.getDeployDate(), "", "", "");
            }
        }

        // Outputs results to console
        table.print(System.out);

        return null;
    }
}