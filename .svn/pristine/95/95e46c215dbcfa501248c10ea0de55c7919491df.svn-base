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
import org.apache.karaf.cave.deployer.api.Deployer;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Command(scope = "kat", name = "binaries-install", description = "Install binaries of a TALEND job from Nexus repository to binaries deploy path")
public class BinariesInstall implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(BinariesInstall.class);

    @Argument(index = 0, name = "artifact", required = true, description = "Name of job artifact on Nexus")
    String artifact;

    @Argument(index = 1, name = "version", required = true, description = "Version of job artifact on Nexus")
    String version;

    @Reference
    Deployer deployer;

    @Override
    public Object execute() throws Exception {

        // Build artifact URL from job name
        String artifactUrl = KatJobManagerProperties.getNexusUrl() + "/" + artifact + "/" + version + "/" + artifact + "-" + version + ".zip";

        // Try to download and extract TALEND job in a new directory with same name as job
        try {
            deployer.extract(artifactUrl, KatJobManagerProperties.getDefaultBinariesPath() + "/" + artifact + "/" + version);
            String infoMsg = "TALEND job binaries *** " + artifact + " *** have been successfully downloaded from Nexus and installed";
            System.out.println(infoMsg);
            LOGGER.info(infoMsg);
        }
        catch (Exception e) {
            String errorMsg = "Installation of binaries for TALEND job *** " + artifact + " *** has failed ! Error description: " + e.getMessage();
            LOGGER.error(errorMsg);
            System.out.println(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        return null;
    }
}
