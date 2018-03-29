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

import com.idfor.kat.tools.katjobmanagerbundle.beans.KatJobManagerProperties;

import java.io.File;

/**
 * This class implements methods that are used by web services and commands to manipulate projects
  */
public class ProjectUtils {

    /**
     * Method that returns project name read from binariess of a deployed job in a given version
     * @param job name of deployed job
     * @param version of deployed job
     * @return project name of deployed job for given version
     */
    public static String getProject(String job, String version){

        // We haven't found any project yet
        String project = null;

        // Analyzes directory to find back project name
        File folder = new File(KatJobManagerProperties.getDefaultBinariesPath() + "/" + job + "/" + version + "/" + job);

        // Gets all files in directory
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null) {

            // Searches for project name
            for (int i = 0; i < listOfFiles.length; i++) {
                String currentFile = listOfFiles[i].getName();
                if (listOfFiles[i].isDirectory() && !currentFile.equals("items") && !currentFile.equals("src"))
                    project = currentFile;
            }
        }

        // Returns found project;
        return project;
    }
}