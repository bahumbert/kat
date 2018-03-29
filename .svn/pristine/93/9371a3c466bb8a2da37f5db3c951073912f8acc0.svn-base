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

package com.idfor.kat.tools.katexecbundle.ws;

import com.idfor.kat.tools.katexecbundle.beans.*;
import com.idfor.kat.tools.katexecbundle.run.JobRunner;
import org.apache.felix.utils.properties.Properties;
import org.apache.karaf.scheduler.Scheduler;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * Class that implements the WS definitions for KAT Executor
 */
public class KatExecWebServicesImpl implements KatExecWebServices {

    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Logger LOGGER = LoggerFactory.getLogger(KatExecWebServicesImpl.class);

    // Used to store bundle context
    private BundleContext bundleContext;

    @Reference
    Scheduler scheduler;

    /**
     * Gets bundle context from blueprint
     * @param bundleContext
     */
    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    /**
     * Returns a list of all scheduled jobs in KAT Executor
     * @return a list of all scheduled jobs in KAT Executor
     */
    public List<JobProperties> getJobs(){

        // Iterates through job list to find out next firing time
        Iterator<JobProperties> jobIterator = JobList.getList().iterator();
        while (jobIterator.hasNext()) {
            JobProperties job = jobIterator.next();

            // By default, no next event is planned
            String nextExecution = "-";

            // We only need to set next firing event if job is active
            if(job.getState().equals("active")){
                CronTriggerImpl trigger = new CronTriggerImpl();
                Date now = new Date();

                // Finds out next firing event
                try {
                    trigger.setCronExpression(job.getScheduling());
                    Date nextFireDate = trigger.getFireTimeAfter(now);
                    nextExecution = dateFormatter.format(nextFireDate) + " (" + getTimeRemaining(now, nextFireDate) + ")";
                }
                catch(ParseException e){
                    nextExecution = "Unknown";
                    LOGGER.warn("Not able to define remaining time to next fire event for job " + job.getName());
                }
            }

            // Set final found next firing event
            job.setNextExecution(nextExecution);
        }
        return JobList.getList();
    }

    /**
     * Returns specific scheduled job with with given pid in KAT Executor
     * @param pid job process id as assigned by job factory
     * @return a job object with this pid in KAT Executor, null if not found
     */
    public JobProperties getJob(String pid){
        return JobList.findByPid(pid);
    }

    public boolean isScheduled(String job, String version){
        return JobList.isScheduled(job, version);
    }

    public boolean jobAction(JobAction request){

        // We assume everything went OK
        boolean ok = true;

        // Gets parameters from JSON request
        String action = request.getAction();
        String pid = request.getPid();

        // Find back information about the job to be fires
        JobProperties job = JobList.findByPid(pid);

        // We have found a scheduled job with this pid so prepare  to fire it NOW
        if(job != null) {

            // Get a scheduler instance
            ServiceReference schedulerReference = bundleContext.getServiceReference(Scheduler.class.getName());
            scheduler = (Scheduler) bundleContext.getService(schedulerReference);

            // Try to start a job
            if (action.equals("start")) {

                // Verify that job is not already running
                if (!job.isRunning()) {

                    // Prepares an instance of job to be executed
                    JobRunner jobToSchedule = new JobRunner(job);

                    // Try to start job instance now
                    try {
                        Date now = new Date();
                        String timeStamp = Long.toString(now.getTime());
                        String nowPid = pid + "." + timeStamp;
                        scheduler.schedule(jobToSchedule, scheduler.NOW().name(nowPid));
                    }

                    // We had a problem starting the job so return false
                    catch (SchedulerException e) {
                        ok = false;
                    }
                }

                // Job is already running so we don't launch it again
                else{
                    ok = false;
                }

            // Try to kill a job
            } else if (action.equals("kill")) {
                ok = ProcessList.kill(pid);

            // Pause or resume job scheduling
            } else if (action.equals("pause") || action.equals("resume")) {

                // Try to pause or resume job instance
                try {
                    String base = System.getProperty("karaf.etc");
                    Properties props = new Properties(new File(base, "katexec/" + job.getPropertyFile()));
                    if(action.equals("resume")) props.replace("job.state", "active");
                    else  props.replace("job.state", "paused");
                    props.save();

                    // We had a problem pausing or resuming the job so return false
                } catch (IOException e) {
                    ok = false;
                }
            }

            // There is no valid action on job
            else ok = false;
        }

        // No job found with this pid
        else ok = false;

        // Returns final value
        return ok;
    }

    private String getTimeRemaining(Date firstDate, Date secondDate){

        String timeRemaining = "";
        long dateDiff = (secondDate.getTime() - firstDate.getTime()) / 1000;
        int nbOfDays = (int) Math.floor(dateDiff / 86400);
        if(nbOfDays > 0) timeRemaining += String.valueOf(nbOfDays) + "d ";
        int nbOfHours = (int) Math.floor((dateDiff - nbOfDays * 86400) / 3600);
        if (nbOfHours > 0) timeRemaining += String.valueOf(nbOfHours) + "h ";
        int nbOfMinutes = (int) Math.floor((dateDiff - nbOfDays * 86400 - nbOfHours * 3600) / 60);
        if (nbOfMinutes > 0) timeRemaining += String.valueOf(nbOfMinutes) + "m ";
        int nbOfSeconds = (int)(dateDiff - nbOfDays * 86400 - nbOfHours * 3600 - nbOfMinutes * 60);
        timeRemaining += String.valueOf(nbOfSeconds) + "s";
        return timeRemaining;
    }
}