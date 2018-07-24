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

package com.idfor.kat.tools.katjobmanagerbundle.run;

import com.idfor.kat.tools.katjobmanagerbundle.beans.BinariesProperties;
import com.idfor.kat.tools.katjobmanagerbundle.beans.JvmOptions;
import com.idfor.kat.tools.katjobmanagerbundle.beans.KatJobManagerProperties;
import org.apache.felix.utils.properties.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements methods that are used by web services and commands to manipulate binaries
  */
public class BinariesUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(BinariesUtils.class);
    /**
     * Method that scans deploy binaries directory and returns a list of deployed jobs
     * @return a list of deployed binaries including job name, version, project and available contexts
     */
    public static List<BinariesProperties> getDeployedBinaries() {

        String version;
        String project;
        List<String> contextList;

        // Initializes working arrays
        List<String> jobList = new ArrayList<>();
        List<BinariesProperties> binariesList = new ArrayList<>();

        // Searches for deployed jobs (folders) in binaries directory
        File folder = new File(KatJobManagerProperties.getDefaultBinariesPath());
        File[] listOfFiles = folder.listFiles();

        // Gets all directories found in binaries directory
        for (int i = 0; i < listOfFiles.length; i++)
            if (listOfFiles[i].isDirectory()) jobList.add(listOfFiles[i].getName());

        // Then iterate on found directories to retrieve deployed job and version from main jar
        Iterator<String> jobIterator = jobList.iterator();
        while (jobIterator.hasNext()) {

            // Analyzes directory on step further to version numbers
            String job = jobIterator.next();
            List<String> versionList = getDeployedVersions(job);

            // Retrieve information for each existing version
            Iterator<String> versionIterator = versionList.iterator();
            while (versionIterator.hasNext()) {

                // Analyzes directory one step further to retrieve project information
                version = versionIterator.next();

                // Gets project name
                project = ProjectUtils.getProject(job, version);

                // If we have found a valid project, we dig further for contexts
                if (project != null) {

                    // Get contexts and add all to binaries list
                    contextList = ContextUtils.getContextList(project, job, version);
                    BinariesProperties binaries = new BinariesProperties(job, version, project, contextList);
                    binariesList.add(binaries);
                }
            }
        }

        // Returns binaries list
        return binariesList;
    }

    public static List<String> getDeployedVersions(String job){

        // Prepares versions list
        List<String> versions = new ArrayList<>();

        // Gets all files in directory
        File folder = new File(KatJobManagerProperties.getDefaultBinariesPath() + "/" + job);
        File[] listOfFiles = folder.listFiles();

        // Retrieve deployed versions from files
        if (listOfFiles != null)
            for (int i = 0; i < listOfFiles.length; i++)
                if (listOfFiles[i].isDirectory())
                    versions.add(listOfFiles[i].getName());

        // Returns list of available versions
        return versions;
    }

    /**
     * Gets JVM options for given schedule file
     * @param scheduleFile file used to schedule job execution
     * @return
     */
    public static JvmOptions getJvmOptions(String scheduleFile){

        // We haven't found any property yet;
        JvmOptions jvmOptions = new JvmOptions();

        try {

            // Checks scheduled job configuration file
            String base = System.getProperty("karaf.etc");
            Properties props = new Properties(new File(base, "katexec/" + scheduleFile));

            // Get JVM options and activation status
            jvmOptions.setOptions(props.get("job.jvm.options.list"));
            jvmOptions.setActive(props.get("job.jvm.options.active"));

            // Set schedule file back
            jvmOptions.setScheduleFile(scheduleFile);

        } catch (IOException e) {
            LOGGER.error("Unable to get JVM options for schedule file *** " + scheduleFile + " ***");
        }
        return jvmOptions;
    }

    /**
     * Sets JVM options for given schedule file
     * @param jvmOptions a JvmOptions object containing the schedule file, the option and their activation flag
     * @return true if setting was OK, false otherwise
     */
    public static boolean setJvmOptions(JvmOptions jvmOptions){

        // We' assume everything went OK
        boolean ok = true;

        try {

            // Prepares scheduled job configuration file for writing
            String base = System.getProperty("karaf.etc");
            Properties props = new Properties(new File(base, "katexec/" + jvmOptions.getScheduleFile()));

            // Sets JVM options and state
            props.put("job.jvm.options.list", jvmOptions.getOptions());
            props.put("job.jvm.options.active", Boolean.toString(jvmOptions.isActive()));

            // Save changes to property file
            props.save();

        } catch (IOException e) {
            ok = false;
            LOGGER.error("Unable to set JVM options for schedule file *** " + jvmOptions.getScheduleFile() + " ***");
        }
        return ok;
    }

    /**
     * Removes JVM options for given schedule file
     * @param scheduleFile file used to schedule job execution
     * @return true if removing was OK, false otherwise
     */
    public static boolean removeJvmOptions(String scheduleFile){

        // We' assume everything went OK
        boolean ok = true;

        try {

            // Prepares scheduled job configuration file for writing
            String base = System.getProperty("karaf.etc");
            Properties props = new Properties(new File(base, "katexec/" + scheduleFile));

            // Removes JVM options and state
            props.remove("job.jvm.options.list");
            props.remove("job.jvm.options.active");

            // Save changes to property file
            props.save();

        } catch (IOException e) {
            ok = false;
            LOGGER.error("Unable to remove JVM options for schedule file *** " + scheduleFile + " ***");
        }
        return ok;
    }
}