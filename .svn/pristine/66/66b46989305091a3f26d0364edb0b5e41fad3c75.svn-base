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
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.CommandLine;
import org.apache.karaf.shell.api.console.Completer;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.support.completers.StringsCompleter;
import java.util.Iterator;
import java.util.List;

/**
 * KAT Executor completer
 * This completer cycles through job list pids on each TAB stroke
 */
@Service
public class KatPidCompleter implements Completer {

    @Override
    public int complete(Session session, CommandLine commandLine, List<String> candidates) {
        StringsCompleter delegate = new StringsCompleter();

        // Get complete list of jobs
        List<JobProperties> jobList = JobList.getList();
        Iterator<JobProperties> jobIterator = jobList.iterator();

        // Put each pid in completer list
        while (jobIterator.hasNext()) {
            JobProperties job = jobIterator.next();
            delegate.getStrings().add(job.getPid());
        }
        return delegate.complete(session, commandLine, candidates);
    }
}