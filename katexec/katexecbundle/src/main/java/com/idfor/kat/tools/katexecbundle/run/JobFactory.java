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

package com.idfor.kat.tools.katexecbundle.run;

import com.idfor.kat.tools.katexecbundle.beans.JobList;
import com.idfor.kat.tools.katexecbundle.beans.JobProperties;
import org.apache.karaf.scheduler.Scheduler;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.util.tracker.ServiceTracker;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Dictionary;
import java.util.Properties;

/**
 * Class that implements a managed service factory to watch changes made to {karaf.etc} directory
 * All file added / deleted / modified in this directory will modify the scheduling of corresponding scheduled jobs
 */
public class JobFactory implements ManagedServiceFactory {

    private final Logger LOGGER = LoggerFactory.getLogger(JobFactory.class);

    private BundleContext bundleContext;
    private String configurationPid;
    private ServiceRegistration registration;
    private ServiceTracker tracker;

    @Reference
    Scheduler scheduler;

    private String getPropertyFile(String fileName){
        return fileName.substring(fileName.lastIndexOf("/") + 1);
    }

    @Override
    public String getName() {
        return configurationPid;
    }

    /**
     * This method is wired to blueprint via a property. It stores the file pid passed
     * by the blueprint to construct the factory
     */
    public void setConfigurationPid(String configurationPid) {
        this.configurationPid = configurationPid;
    }

    /**
     * This method is wired to blueprint via a property. It stores the bundle context passed
     * by the blueprint
     */
    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /**
     * This method is wired to blueprint via a bean definition. It initializes the factory
     * at feature installation
     */
    public void init() {
        Dictionary servProps = new Properties();
        servProps.put(Constants.SERVICE_PID, configurationPid);
        registration = bundleContext.registerService(ManagedServiceFactory.class.getName(), this, servProps);
        tracker = new ServiceTracker(bundleContext, ConfigurationAdmin.class.getName(), null);
        tracker.open();
        LOGGER.info("Job Factory started and ready to schedule jobs");
    }

    /**
     * This method is wired to blueprint via a bean definition. It destroys the factory
     * at feature un-installation
     */
    public void destroy() {
        registration.unregister();
        tracker.close();
        LOGGER.info("Job Factory ended. No more listening to possible further jobs deployments");
    }

    /**
     * Method called when a config file has been deleted from job factory. File has been deleted so
     * corresponding job has to be removed from both Karaf scheduler and KAT Executor
     * @param pid process id of the file that has been deleted
     */
    @Override
    public void deleted(String pid) {

        // Get a scheduler instance
        ServiceReference schedulerReference = bundleContext.getServiceReference(Scheduler.class.getName());
        scheduler = (Scheduler)bundleContext.getService(schedulerReference);

        // Unschedule job instance
        scheduler.unschedule(pid);

        // Unscheduling OK from Karaf scheduler so remove job from job list of KAT Executor and notify log
        JobList.remove(pid);
        LOGGER.info("Job pid " + pid + " has been removed from the execution schedule");
    }

    /**
     * Method called when a config file has been modified from job factory. New properties are read from
     * file and actions on job scheduling are taken accordingly
     * @param pid process id of file that has been modified
     * @param jobProperties a dictionnary of all properties read form modified file
     * @throws ConfigurationException
     */
    @Override
    public void updated(String pid, Dictionary jobProperties) throws ConfigurationException {

        // Get all properties from modified config file in factory
        JobProperties job = new JobProperties();
        job.setPid(pid);
        job.setPropertyFile(getPropertyFile((String)jobProperties.get("felix.fileinstall.filename")));
        job.setProject((String)jobProperties.get("job.project"));
        job.setName((String)jobProperties.get("job.name"));
        job.setVersion((String)jobProperties.get("job.version"));
        job.setContext((String)jobProperties.get("job.context"));
        job.setOverloadedContext((String)jobProperties.get("job.context.overloaded"));
        job.setState((String)jobProperties.get("job.state"));
        job.setBinariesPath((String)jobProperties.get("job.binaries.directory"));
        job.setDeployDate((String)jobProperties.get("job.deploy.date"));
        job.setLabel((String)jobProperties.get("job.label"));
        job.setDescription((String)jobProperties.get("job.description"));
        job.setInitialJavaHeapSpace((String)jobProperties.get("job.initial.java.heap.space"));
        job.setMaximumJavaHeapSpace((String)jobProperties.get("job.maximum.java.heap.space"));
        job.setScheduling((String)jobProperties.get("job.scheduling"));
        job.setMailList((String)jobProperties.get("job.alert.mail.list"));
        job.setMailActive(Boolean.parseBoolean((String)jobProperties.get("job.alert.mail.active")));
        job.setSmsList((String)jobProperties.get("job.alert.sms.list"));
        job.setSmsActive(Boolean.parseBoolean((String)jobProperties.get("job.alert.sms.active")));
        job.setJvmOptions((String)jobProperties.get("job.jvm.options.list"));
        job.setJvmActive(Boolean.parseBoolean((String)jobProperties.get("job.jvm.options.active")));

        // Get a scheduler instance
        ServiceReference schedulerReference = bundleContext.getServiceReference(Scheduler.class.getName());
        scheduler = (Scheduler)bundleContext.getService(schedulerReference);

        // Prepare an instance of job to be scheduled
        JobRunner jobToSchedule = new JobRunner(job);

        try {

            // Try to schedule job instance
            // We only reschedule the job if it is in active status
            if(job.getState().equals("active"))
                scheduler.schedule(jobToSchedule, scheduler.EXPR(job.getScheduling()).name(pid));
            else
                scheduler.unschedule(pid);

            // Scheduling OK so put job in the active job list and notify log
            // Note that we first delete an eventual existing entry in case this pid is already existing
            JobList.remove(pid);
            JobList.add(job);

            // Notify the change of job state in the logs
            LOGGER.info("Job " + job.getProject() + ":" + job.getName() + ":" + job.getVersion() + " with pid " + pid + " has been updated");
        }
        catch(SchedulerException e){
            LOGGER.error("ERROR SCHEDULING JOB " + pid + " !!!");
        }
    }
}