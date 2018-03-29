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
import org.apache.felix.utils.properties.Properties;
import org.apache.karaf.scheduler.Scheduler;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;

/**
 * KAT Executor command: kat:job-pause pid
 * This command allows user to put a scheduled job in pause state
 * TAB strokes auto-complete pid argument using the pid completer
 * @see KatPidCompleter
 */
@Command(scope = "kat", name = "schedule-pause", description = "Pause execution of a scheduled TALEND job")
@Service
public class KatSchedulePause implements Action{

    private final Logger LOGGER = LoggerFactory.getLogger(KatSchedulePause.class);

    @Argument(name = "pid", required = true, description = "Process ID of deployed TALEND scheduled job to pause")
    @Completion(KatPidCompleter.class)
    String pid;

    @Reference
    Scheduler scheduler;

    @Override
    public Object execute() throws Exception {

        // Find back information about the job to be fires
        JobProperties job = JobList.findByPid(pid);

        // We have found a scheduled job with this pid so prepare  to fire it NOW
        if(job != null){
            if(job.getState().equals("active")) {
                try {
                        String base = System.getProperty("karaf.etc");
                        Properties props = new Properties(new File(base, "katexec/" + job.getPropertyFile()));
                        props.replace("job.state", "paused");
                        props.save();
                        String infoMsg = "Scheduling for job with pid: " + pid + " has been paused";
                        LOGGER.info(infoMsg);
                        System.out.println(infoMsg);
                    }
                catch (IOException e) {
                    String errorMsg = "Unable to pause scheduling for job with pid: " + pid;
                    LOGGER.error(errorMsg);
                    throw new RuntimeException(errorMsg);
                }
            }
            else System.out.println("Job with pid: " + pid + " is already paused");
        }

        // We have not found any job to fire so inform the user
        else throw new RuntimeException("No job with pid: " + pid + " has been found for pausing");

        return null;
    }
}