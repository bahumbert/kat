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

import com.idfor.kat.tools.katjobmanagerbundle.beans.KatJobManagerProperties;
import org.apache.felix.utils.properties.Properties;
import org.apache.karaf.cave.deployer.api.Deployer;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Command(scope = "kat", name = "job-schedule", description = "Schedules a deployed TALEND job")
public class JobSchedule implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(JobSchedule.class);

    @Argument(index = 0, name = "project", required = true, description = "Project of job")
    String project;

    @Argument(index = 1, name = "job", required = true, description = "Name of job")
    String jobName;

    @Argument(index = 2, name = "version", required = true, description = "Version of job")
    String version;

    @Argument(index = 3, name = "context", required = true, description = "Context of job")
    String context;

    @Argument(index = 4, name = "scheduling", required = true, description = "Cron type scheduling of job (* * * * * *) ")
    String scheduling;

    @Argument(index = 5, name = "label", description = "Label of job")
    String label;

    @Reference
    Deployer deployer;

    @Override
    public Object execute() throws Exception {

        // Get current date and time
        // Prepares property file time stamp and readable time stamp
        Date now = new Date();
        String timeStamp = Long.toString(now.getTime());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String deployDate = dateFormatter.format(now);

        // Preparing and writing scheduling file
        try {
            String base = System.getProperty("karaf.etc");
            Properties props = new Properties(new File(base, "katexec/kat.job-" + timeStamp + ".cfg"));
            props.put("job.project", project);
            props.put("job.name", jobName);
            props.put("job.version", version);
            props.put("job.context", context);
            props.put("job.state", "active");
            props.put("job.binaries.directory", "");
            props.put("job.deploy.date", deployDate);
            props.put("job.label", label);
            props.put("job.description", "");
            props.put("job.initial.java.heap.space", "");
            props.put("job.maximum.java.heap.space", "");
            props.put("job.scheduling", scheduling);
            props.save();
            String infoMsg = "Job *** " + project + ":" + jobName + ":" + version + " *** has been successfully scheduled";
            System.out.println(infoMsg);
            LOGGER.info(infoMsg);

        } catch (IOException e) {
            String errorMsg = "Unable to create scheduling file for job *** " + project + ":" + jobName + ":" + version + " ***";
            System.out.println(errorMsg);
            LOGGER.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        return null;
    }

}
