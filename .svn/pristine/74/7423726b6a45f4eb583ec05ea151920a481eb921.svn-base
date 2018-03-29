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

import com.idfor.kat.tools.katjobmanagerbundle.beans.WorkflowProperties;
import com.idfor.kat.tools.katjobmanagerbundle.ws.JobManagerWebServicesImpl;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements methods to manipulate workflows
 */
public class WorkflowUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(WorkflowUtils.class);

    /**
     * TRhis method is use to list all deployed workflows on server
     *
     * @return a list of workflow properties containing for each deployed workflow
     */
    public static List<WorkflowProperties> getDeployedWorkflows() {

        // Prepares empty list
        List<WorkflowProperties> workflows = new ArrayList<>();

        // Prepares to read workflow directory to get all workflows
        String base = System.getProperty("karaf.etc");
        File workDirectory = new File(base, "katexec/workflows");
        File[] listOfFiles = workDirectory.listFiles();

        // Pattern to search for valid workflow files
        Pattern isWorkflow = Pattern.compile("^workflow-\\d+\\.json$");

        // Iterates to find all workflows
        // Gets all directories found in binaries directory
        for (int i = 0; i < listOfFiles.length; i++){
            String fileName = listOfFiles[i].getName();
            Matcher matcher = isWorkflow.matcher(fileName);
            if(matcher.matches()){
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonWorkflow = mapper.readTree(listOfFiles[i]);
                    WorkflowProperties workflow = new WorkflowProperties(fileName, getWorkflowName(jsonWorkflow));
                    workflows.add(workflow);

                    // Further exploration
                    String firstJob = getWorkflowFirstJob(jsonWorkflow);
                }
                catch (IOException e) {
                    LOGGER.warn("Unable to parse workflow file: " + fileName);
                }
            }
        }

        // Return workflow list
        return workflows;
    }

    /**
     * This method is used to create a workflow file with JSON structure received
     * @param workflow a string containing the JSON structure used to deploy and execute workflow
     * @return true if workflow could be deployed, false otherwise
     */
    public static boolean installWorkflowFile(String workflow) {

        // We assume everything went ok
        boolean ok = true;

        // Get current date and time
        // Prepares property file time stamp and readable time stamp
        Date now = new Date();
        String timeStamp = Long.toString(now.getTime());

        // Writes JSON structure to workflow configuration file
        String base = System.getProperty("karaf.etc");
        File workflowFile = new File(base, "katexec/workflows/workflow-" + timeStamp + ".json");
        try {
            FileUtils.writeStringToFile(workflowFile, workflow);
        } catch (IOException e) {
            ok = false;
        }

        // Send operation result back to caller
        return ok;
    }

    /**
     * This method is used to remove a deployed workflow file
     * @param workflowFile name of workflow file to be removed
     * @return true if workflow could be removed, false otherwise
     */
    public static boolean removeWorkflowFile(String workflowFile) {

        // We assume everything went ok
        boolean ok = true;

        // Delete workflow file from workflows directory
        try {
            String base = System.getProperty("karaf.etc");
            FileUtils.forceDelete(new File(base, "katexec/workflows/" + workflowFile));
        }
        catch(IOException e){
            ok = false;
        }

        // Send operation result back to caller
        return ok;
    }

    /**
     * Gets workflow name from JSON representation of workflow
     * @param jsonWorkflow JSON representation of the workflow
     * @return workflow name
     */
    public static String getWorkflowName(JsonNode jsonWorkflow){
        String workflowName = null;
        JsonNode name = jsonWorkflow.get("name");
        if(name != null) workflowName = name.getTextValue();
        return workflowName;
    }

    /**
     * Gets first job to execute in a workflow
     * @param jsonWorkflow JSON representation of the workflow
     * @return artifact URL of first job
     */
    public static String getWorkflowFirstJob(JsonNode jsonWorkflow){
        String firstJob = null;
        return firstJob;
    }
}