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

import com.idfor.kat.tools.katexecbundle.beans.ProcessList;
import org.apache.karaf.scheduler.Scheduler;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KAT Executor command: kat:job-kill pid
 * This command allows user to kill a running job from the list of scheduled jobs
 * TAB strokes auto-complete pid argument using the pid completer
 * @author IDFOR-Solutions
 * @see KatPidCompleter
 */
@Command(scope = "kat", name = "job-kill", description = "Kills a running TALEND jo")
@Service
public class KatJobKill implements Action{

    private final Logger LOGGER = LoggerFactory.getLogger(KatJobKill.class);

    @Argument(name = "pid", required = true, description = "Process ID of running TALEND job to kill")
    @Completion(KatPidCompleter.class)
    String pid;

    @Reference
    Scheduler scheduler;

    @Override
    public Object execute() throws Exception {

        // Try to kill running job
        boolean success = ProcessList.kill(pid);

        // We have found a running job with this pid and successfuly killed it so report success
        if(success) {
            LOGGER.info("Running process for job with pid " + pid + " has been successfuly killed by user");
            System.out.println("Running process for job with pid " + pid + " has been successfuly killed");
        }
        else {
            System.out.println("No running process has been found for job with pid " + pid);
        }
        return null;
    }
}