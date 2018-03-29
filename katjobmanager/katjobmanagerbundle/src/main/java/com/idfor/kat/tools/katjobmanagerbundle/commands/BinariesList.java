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

package com.idfor.kat.tools.katjobmanagerbundle.commands;

import com.idfor.kat.tools.katjobmanagerbundle.beans.BinariesProperties;
import com.idfor.kat.tools.katjobmanagerbundle.run.BinariesUtils;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.support.table.ShellTable;

import java.util.Iterator;
import java.util.List;

@Service
@Command(scope = "kat", name = "binaries-list", description = "Gets a list of deployed binaries -- May be filtered using options (AND applies if different options are used)")
public class BinariesList implements Action {

    @Option(name = "job", aliases = { "--job" }, description = "Name of job")
    String jobName;

    @Option(name = "project", aliases = { "--project" }, description = "Project of job")
    String project;

    @Option(name = "version", aliases = { "--version" }, description = "Version of job")
    String version;

    @Override
    public Object execute() throws Exception {

        // Prepares table layout for output
        ShellTable table = new ShellTable();
        table.column("Projet");
        table.column("Job Name");
        table.column("Version");
        table.column("Contexts");

        // Gets binaries List
        List<BinariesProperties> binariesList = BinariesUtils.getDeployedBinaries();

        // Init variables
        boolean showBinaries;

        // Iterate though list and output found binaries using pre-defined table layout
        Iterator<BinariesProperties> binariesIterator = binariesList.iterator();
        while (binariesIterator.hasNext()) {

            // Get next binaries in the list
            BinariesProperties binaries = binariesIterator.next();

            // By default, binaries are displayed in binaries list
            showBinaries = true;

            // Unless it is filtered out on...
            // ...job name
            if(jobName != null)
                if(binaries.getName().indexOf(jobName) == -1) showBinaries = false;
            // ...job project
            if(project != null)
                if(binaries.getProject().indexOf(project) == -1) showBinaries = false;
            // ...job version
            if(version != null)
                if(binaries.getVersion().indexOf(version) == -1) showBinaries = false;

            // If all criteria have been met, we add binaries to binaries list
            if(showBinaries) {

                // Get all existing contexts in one string
                String contextList = "";
                Iterator<String> contextIterator = binaries.getContextList().iterator();
                while (contextIterator.hasNext()) {
                    if (!contextList.equals("")) contextList += ", ";
                    contextList += contextIterator.next();
                }

                // Adds row to table
                table.addRow().addContent(binaries.getProject(), binaries.getName(), binaries.getVersion(), contextList);
            }
        }

        // Outputs results to console
        table.print(System.out);

        return null;
    }
}