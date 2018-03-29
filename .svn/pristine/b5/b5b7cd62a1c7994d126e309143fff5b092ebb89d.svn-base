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

package com.idfor.kat.tools.katexecbundle.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class holds a collection of JobProperties objects. All jobs currently scheduled
 * in the KAT Executor are stored in this collection. This collection allows to store
 * more data than the Karaf common scheduler and helps users to interact with scheduled
 * jobs in a more convenient way.
 * @see JobProperties
 */
public class JobList {

    // The collection itself
    private static List<JobProperties> jobList;

    /**
     * This method finds a scheduled job from collection based on its pid value
     * @param pid Process ID of the job as set by the job factory
     * @return a JobProperties object describing the found job
     */
    public static JobProperties findByPid(String pid){

        // We haven't found the job yet
        JobProperties job = null;

        Iterator<JobProperties> jobIterator = jobList.iterator();

        while (jobIterator.hasNext()) {
            JobProperties nextJob = jobIterator.next();
            if(nextJob.getPid().equals(pid)) job = nextJob;
        }
        return job;
    }

    /**
     * Inits bean
     */
    public static void init(){
        jobList = new ArrayList<>();
    }

    /**
     * Destroys bean
     */
    public static void destroy(){
        jobList = null;
    }

    /**
     * This method is used to verify if a job has at least one active scheduling
     * @param job name of job to search for
     * @param version version of job to search for
     * @return true if job has at least one scheduling, false otherwise
     */
    public static boolean isScheduled(String job, String version){

        // We haven't found any scheduling yet
        boolean isScheduled = false;

        Iterator<JobProperties> jobIterator = jobList.iterator();

        while (jobIterator.hasNext()) {
            JobProperties nextJob = jobIterator.next();
            if(nextJob.getName().equals(job) && nextJob.getVersion().equals(version)) isScheduled = true;
        }
        return isScheduled;
    }

    /**
     * This method sets the flag running for job with passed pid job
     * @param pid Process ID of the job as set by the job factory
     * @param isRunning true if the job is running, false otherwise
     */
    public static void setRunning(String pid, boolean isRunning){

        Iterator<JobProperties> jobIterator = jobList.iterator();

        while (jobIterator.hasNext()) {
            JobProperties nextJob = jobIterator.next();
            if(nextJob.getPid().equals(pid)) nextJob.setRunning(isRunning);
        }
    }

    /**
     * Adds a job in collection of deployed jobs
     * @param job the job object to add
     */
    public static void add(JobProperties job){
        jobList.add(job);
    }

    /**
     * Removes a job from collection of deployed jobs
     * @param pid the pid of job to delete
     * @return true if job has been successfuly found and removed, false otherwise
     */
    public static boolean remove(String pid){

        // We assume we haven't found job yet
        boolean found = false;

        // Iterates through job collection to find passed pid
        Iterator<JobProperties> jobIterator = jobList.iterator();
        while (jobIterator.hasNext()) {
            JobProperties job = jobIterator.next();

            // Job has been found so remove it from collection and sets return value
            if(job.getPid().equals(pid)){
                jobIterator.remove();
                found = true;
            }
        }
        return found;
    }

    /**
     * Gets a list of scheduled jobs
     * @return the complete list of schedules jobs
     */
    public static List<JobProperties> getList(){
        return jobList;
    }
}