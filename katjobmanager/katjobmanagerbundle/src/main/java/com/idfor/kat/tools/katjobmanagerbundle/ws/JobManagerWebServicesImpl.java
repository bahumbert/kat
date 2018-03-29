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

package com.idfor.kat.tools.katjobmanagerbundle.ws;

import com.idfor.kat.tools.katjobmanagerbundle.beans.*;
import com.idfor.kat.tools.katjobmanagerbundle.run.*;
import org.apache.felix.utils.properties.Properties;
import org.apache.karaf.cave.deployer.api.Deployer;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.commons.io.FileUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class that implements the WS definitions for KAT Executor
 */
public class JobManagerWebServicesImpl implements JobManagerWebServices {

    private final static Logger LOGGER = LoggerFactory.getLogger(JobManagerWebServicesImpl.class);

    // Used to store bundle context
    private BundleContext bundleContext;

    @Reference
    Deployer deployer;

    /**
     * Gets bundle context from blueprint
     * @param bundleContext
     */
    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /**
     * Method that returns a list of deployed TALEND binaries
     * @return a list of deployed Talend jobs binaries
     */
    @Override
    public List<BinariesProperties> binariesList() {
        return BinariesUtils.getDeployedBinaries();
    }

    /**
     * Method that returns a list of available contexts for a deployed TALEND job
     * @return a list of contexts for a deployed Talend job
     */
    @Override
    public List<String> contextList(String job, String version){
        return ContextUtils.getContextList(job, version);
    }

    /**
     * Method that downloads a zipped archive containing a TALEND job to deploy and extracts it in the binaries directory
     * @return True if deployment of job was ok, false otherwise
     */
    @Override
    public boolean install(String artifactUrl){

        // We assume everything went OK
        boolean ok = true;

        // Get a deployer instance
        ServiceReference deployerReference = bundleContext.getServiceReference(Deployer.class.getName());
        deployer = (Deployer) bundleContext.getService(deployerReference);

        // Get job name and version from artifact url
        String jobName = artifactUrl.substring(artifactUrl.lastIndexOf("/") + 1, artifactUrl.lastIndexOf("-"));
        String version = artifactUrl.substring(artifactUrl.lastIndexOf("-") + 1, artifactUrl.lastIndexOf(".zip"));

        // Try to download and extract TALEND job in a new directory with same name as job
        try {
            deployer.extract(artifactUrl, KatJobManagerProperties.getDefaultBinariesPath() + "/" + jobName + "/" + version);
            LOGGER.info("TALEND job *** " + jobName + " *** has been successfully downloaded from Nexus and installed");
        }
        catch (Exception e){
            LOGGER.error("Installation of TALEND job *** " + jobName + " *** has failed ! Error description: " + e.getMessage());
            ok = false;
        }

        // Returns result of the web service
        return ok;
    }

    /**
     * Method that deletes binaries of a deployed job
     * @param job job name to be removed
     * @param  version job version to be removed
     * @return True if the file could be removed, false otherwise
     */
    @Override
    public boolean uninstall(String job, String version){

        // We assume everything went OK
        boolean ok = true;

        // Delete TALEND job binaries directory starting from version and if
        // job directory is empty, also remove it
        try {
            FileUtils.forceDelete(new File(KatJobManagerProperties.getDefaultBinariesPath() + "/" + job + "/" + version));
            File jobFolder = new File(KatJobManagerProperties.getDefaultBinariesPath() + "/" + job);
            if(jobFolder.list().length == 0) FileUtils.forceDelete(jobFolder);
            LOGGER.info("Version " + version + " of TALEND job *** " + job + " *** has been successfully removed from deployed binaries directory");
        }
        catch(IOException e){
            LOGGER.error("Removal of TALEND job *** " + job + " in version " + version + " *** has failed ! Error description: " + e.getMessage());
            ok = false;
        }

        // Returns result of the web service
        return ok;
    }

    /**
     * Method that schedules a deployed TALEND job by installing a config file in KAT Executor directory
     * @param request JSON object representing required parameters for scheduling
     * @return True if job could be successfully scheduled, false otherwise
     */
    @Override
    public boolean schedule(ScheduleParameters request){

        // We assume everything went OK
        boolean ok = true;

        // Get all parameters from JSON WS parameters
        String name = request.getName();
        String version = request.getVersion();
        String context = request.getContext();
        String label = request.getLabel();
        if(label == null) label = "";
        String description = request.getDescription();
        if(description == null) description = "";
        String binaryPath = request.getBinariesPath();
        if(binaryPath == null) binaryPath = "";
        String state = request.getState();
        if(state == null) state = "paused";
        String initialJavaHeapSpace = request.getInitialJavaHeapSpace();
        if(initialJavaHeapSpace == null) initialJavaHeapSpace = "";
        String maximumJavaHeapSpace = request.getMaximumJavaHeapSpace();
        if(maximumJavaHeapSpace == null) maximumJavaHeapSpace = "";
        String scheduling = request.getScheduling();

        if(name != null && version != null && context != null && scheduling != null) {

            // Retrieve project name from deployed binaries and only deploy schedule
            // if retrieved project exists
            String project = ProjectUtils.getProject(name, version);
            if(project != null) {

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
                    props.put("job.name", name);
                    props.put("job.version", version);
                    props.put("job.context", context);
                    props.put("job.state", state);
                    props.put("job.binaries.directory", binaryPath);
                    props.put("job.deploy.date", deployDate);
                    props.put("job.label", label);
                    props.put("job.description", description);
                    props.put("job.initial.java.heap.space", initialJavaHeapSpace);
                    props.put("job.maximum.java.heap.space", maximumJavaHeapSpace);
                    props.put("job.scheduling", scheduling);
                    props.save();
                    LOGGER.info("Job *** " + project + ":" + name + ":" + version + " *** has been successfully scheduled");
                } catch (IOException e) {
                    LOGGER.error("Unable to create scheduling file for job *** " + project + ":" + name + ":" + version + " ***");
                    ok = false;
                }
            }
            else ok = false;
        }
        else ok = false;

        // Returns result of the web service
        return ok;
    }

    /**
     * Method that deletes a scheduling file from KAT Executor config directory
     * @param scheduleFile file to be removed, to definitively cancel scheduling
     * @return True if the file could be removed, false otherwise
     */
    @Override
    public boolean unschedule(String scheduleFile){

        // We assume everything went OK
        boolean ok = true;

        // Delete schedule file from config directory
        try {
            String base = System.getProperty("karaf.etc");
            FileUtils.forceDelete(new File(base, "katexec/" + scheduleFile));
            LOGGER.info("Scheduling file *** " + scheduleFile + " *** has been successfully removed from deployed schedules");
        }
        catch(IOException e){
            LOGGER.error("Removal of scheduling file *** " + scheduleFile + " *** has failed ! Error description: " + e.getMessage());
            ok = false;
        }

        // Returns result of the web service
        return ok;
    }

    /**
     * Method use to get a complete list of properties for the context of a scheduled job
     * First checks configuration file of the job and verify if there are overloaded properties
     * If overloaded properties exist, we read linked properties file to get values
     * @param scheduleFile config file of the scheduled job
     * @return a list of context properties with active flag, property name, default property and overloaded property
     */
    @Override
    public ContextFile listContextProperties(String scheduleFile) {

        ContextFile contextFile = null;

        try {

            // First checks scheduled job configuration file
            String base = System.getProperty("karaf.etc");
            Properties props = new Properties(new File(base, "katexec/" + scheduleFile));

            // Get values from job config file
            String project = props.get("job.project");
            String job = props.get("job.name");
            String version = props.get("job.version");
            String context = props.get("job.context");
            String overloadedContext = props.get("job.context.overloaded");

            // If there is an overloaded context, analyze this file
            if (overloadedContext != null)
                contextFile = new ContextFile(scheduleFile, ContextUtils.getOverloadedContextProperties(project, job, version, overloadedContext));

            // No overloading yet so send back original context file
            else
                contextFile = new ContextFile(scheduleFile, ContextUtils.getContextProperties(project, job, version, context));

        } catch (IOException e) {
            LOGGER.error("Unable to retrieve context properties from schedule file *** " + scheduleFile + " ***");
        }

        return contextFile;
    }

    /**
     * Sets overloaded context values for passed schedule file
     * The method adds a property to schedule file and writes overloaded properties in context of target job in binaries directory
     * @param request contains schedule file where overloaded properties should be added and a JSON tree of overloaded properties
     * @return true if everything went OK, false otherwise
     */
    @Override
    public boolean setContextProperties(ContextFile request) {

        // We assume everything went ok
        boolean ok = true;

        // Check that we have a schedule file in request
        String scheduleFile = request.getScheduleFile();
        if(scheduleFile != null) {

            // First retrieve job information from schedule file
            try {
                String base = System.getProperty("karaf.etc");
                File propsFile = new File(base, "katexec/" + scheduleFile);
                Properties props = new Properties(propsFile);
                String project = props.get("job.project");
                String job = props.get("job.name");
                String version = props.get("job.version");
                String context = props.get("job.context");
                String overloadedContext = props.get("job.context.overloaded");

                // If we don't have an overloaded file yet we have to create one
                if(overloadedContext == null){
                    Date now = new Date();
                    String timeStamp = Long.toString(now.getTime());
                    overloadedContext = context + "-kat.job-" + timeStamp;
                    props.put("job.context.overloaded", overloadedContext);
                    props.save();
                }

                // Proceed with configuration of overloaded properties
                ContextUtils.setOverloadedContextProperties(project, job, version, overloadedContext, request.getContextProperties());

            } catch (IOException e) {
                LOGGER.error("Unable to overload context properties for schedule file  *** " + scheduleFile  + " ***");
                ok = false;
            }
        }

        // No schedule file was passed in request
        else ok = false;

        // Return final status of request
        return ok;
    }

    /**
     * Deletes an overloaded context properties file and removes it from passed scheduled file
     * @param scheduleFile file where to remove overloaded context properties
     * @return true if everything went OK, false otherwise
     */
    @Override
    public boolean deleteContextProperties(String scheduleFile) {

        // We assume everything went ok
        boolean ok = true;

        try {

            // First checks scheduled job configuration file
            String base = System.getProperty("karaf.etc");
            Properties props = new Properties(new File(base, "katexec/" + scheduleFile));

            // Get value for overloaded context file
            String overloadedContext = props.get("job.context.overloaded");

            // If there is an overloaded context, removes it from schedule file
            // Also removes overloaded context file
            if (overloadedContext != null) {

                // Removes overloaded propery in schedule file
                props.remove("job.context.overloaded");
                props.save();

                // Get properties to delete overloaded context file and perform deletion
                String project = props.get("job.project");
                String job = props.get("job.name");
                String version = props.get("job.version");
                FileUtils.forceDelete(new File(ContextUtils.getContextPath(project, job, version) + "/" + overloadedContext + ".properties"));
            }

        } catch (IOException e) {
            LOGGER.error("Unable to delete overloaded context properties file for schedule file *** " + scheduleFile + " ***");
        }

        return ok;
    }

    /**
     * Gets mail alerting status and mailing list
     * @param scheduleFile file used to schedule concerned job
     * @return AlertProperties object containing mail configuration
     */
    @Override
    public AlertProperties getMailAlert(String scheduleFile) {
        return AlertUtils.getAlerts(AlertUtils.AlertTypes.EMAIL, scheduleFile);
    }

    /**
     * Sets mail alerting for passed schedule file
     * @param request contains concerned schedule file and mail properties
     * @return true if mail alerting could be set, false otherwise
     */
    @Override
    public boolean setMailAlert(AlertProperties request) {
        return AlertUtils.setAlerts(AlertUtils.AlertTypes.EMAIL, request);
    }

    /**
     * Removes mail alerting for passed schedule file
     * @param scheduleFile file used to schedule concerned job
     * @return true if mail alerting could be set, false otherwise
     */
    @Override
    public boolean removeMailAlert(String scheduleFile) {
        return AlertUtils.removeAlerts(AlertUtils.AlertTypes.EMAIL, scheduleFile);
    }

    /**
     * Gets sms alerting status and mailing list
     * @param scheduleFile file used to schedule concerned job
     * @return AlertProperties object containing sms configuration
     */
    @Override
    public AlertProperties getSmsAlert(String scheduleFile) {
        return AlertUtils.getAlerts(AlertUtils.AlertTypes.SMS, scheduleFile);
    }

    /**
     * Sets sms alerting for passed schedule file
     * @param request contains concerned schedule file and sms properties
     * @return true if sms alerting could be set, false otherwise
     */
    @Override
    public boolean setSmsAlert(AlertProperties request) {
        return AlertUtils.setAlerts(AlertUtils.AlertTypes.SMS, request);
    }

    /**
     * Removes sms alerting for passed schedule file
     * @param scheduleFile file used to schedule concerned job
     * @return true if sms alerting could be set, false otherwise
     */
    @Override
    public boolean removeSmsAlert(String scheduleFile) {
        return AlertUtils.removeAlerts(AlertUtils.AlertTypes.SMS, scheduleFile);
    }

    /**
     * Gets a list of all available versions for given job name
     * @param job name of deployed job
     * @return a list of available versions
     */
    public List<String> getVersions(String job){
        return BinariesUtils.getDeployedVersions(job);
    }

    /**
     * Changes version of a scheduled deployed job
     * @param request a JSON object containing target schedule file, property nam and value
     * @return
     */
    public boolean changeScheduleProperty(ScheduleProperty request){
        return ContextUtils.changeScheduleProperty(request.getScheduleFile(), request.getPropertyName(), request.getPropertyValue());
    }

    /**
     * Gets a list of deployed workflows on execution server
     * @return a list of deployed workflows
     */
    public List<WorkflowProperties> workflowList(){
        return WorkflowUtils.getDeployedWorkflows();
    }

    /**
     * Deploys a workflow on execution server
     * @return true if workflow could be removed, false otherwise
     */
    public boolean deployWorkflow(String workflow){
        return WorkflowUtils.installWorkflowFile(workflow);
    }

    /**
     * Removes workflow from execution server
     * @param workflowFile name of workflow to be removed
     * @return true if workflow could be removed, false otherwise
     */
    public boolean removeWorkflow(String workflowFile){
        return WorkflowUtils.removeWorkflowFile(workflowFile);
    }
}