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

package com.idfor.kat.tools.katjobmanagerbundle.beans;

import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;

/**
 * Bean used to monitor and hold KAT Executor main configuration. It implements a ManagedService
 * to continuously track changes made to main configuration file and immediately apply these changes
 * to KAT Executor
 */
public class KatJobManagerProperties implements ManagedService {

    private final static Logger LOGGER = LoggerFactory.getLogger(KatJobManagerProperties.class);

    private static String defaultBinariesPath;
    private static String nexusUrl;
    private static String nexusUserId;
    private static String nexusPwd;

    public static String getDefaultBinariesPath() {
        return defaultBinariesPath;
    }

    public static String getNexusUrl() {
        return nexusUrl;
    }

    public static String getNexusUserId() {
        return nexusUserId;
    }

    public static String getNexusPwd() {
        return nexusPwd;
    }

    public static void setDefaultBinariesPath(String path) {
        defaultBinariesPath = path;
    }

    public static void setNexusUrl(String nexusUrl) {
        KatJobManagerProperties.nexusUrl = nexusUrl;
    }

    public static void setNexusUserId(String nexusUserId) {
        KatJobManagerProperties.nexusUserId = nexusUserId;
    }

    public static void setNexusPwd(String nexusPwd) {
        KatJobManagerProperties.nexusPwd = nexusPwd;
    }

    /**
     * Method used to update the monitored properties
     * @param properties a set of properties to update
     */
    public static void update(Dictionary properties){

        String propertyName;

        // The configuration file exists present
        if (properties != null) {

            // BINARIES PATH
            propertyName = "katjobmanager.default.binaries.path";
            if (properties.get(propertyName) != null) setDefaultBinariesPath((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // NEXUS URL
            propertyName = "katjobmanager.nexus.url";
            if (properties.get(propertyName) != null) setNexusUrl((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // NEXUS USERID
            propertyName = "katjobmanager.nexus.userid";
            if (properties.get(propertyName) != null) setNexusUserId((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // NEXUS PASSWORD
            propertyName = "katjobmanager.nexus.pwd";
            if (properties.get(propertyName) != null) setNexusPwd((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // Final info message
            LOGGER.info("Configuration of IDFOR KAT Job Manager has been updated");
        }

        // There is no valid configuration so issue an error message to log file
        else
            LOGGER.error("The configuration file com.idfor.kat.tools.katjobmanager.cfg is not present or has been deleted !");

    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Method fired by Managed Service, every time the configuration file has been modified
     */
    @Override
    public void updated(Dictionary properties) {
        update(properties);
    }
}