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
 * This class holds a collection of ProcessProperties objects. All jobs currently running
 * are maintained in this list. It allows to take control over a running job and perform operations
 * for instance killing a job running forever
 *
 * @see ProcessProperties
 */
public class ProcessList {

    // The collection itself
    private static List<ProcessProperties> processList = new ArrayList<>();

    /**
     * This method searches if a job with a particular pid is running
     * @param pid Process ID of the job as set by the job factory
     */
    public static boolean isRunning (String pid){

        // We assume we haven't found any process yet
        boolean found = false;

        // Iterates through process collection to find passed pid
        Iterator<ProcessProperties> processIterator = processList.iterator();
        while (processIterator.hasNext()) {
            ProcessProperties nextProcess = processIterator.next();
            if(nextProcess.getPid().equals(pid)){
                found = true;
            }
        }
        return found;
    }

    /**
     * This method kills a running process using job pid
     * @param pid Process ID of the job as set by the job factory
     */
    public static boolean kill (String pid){

        // We assume we haven't found any process yet
        boolean found = false;

        // Iterates through process collection to find passed pid
        Iterator<ProcessProperties> processIterator = processList.iterator();
        while (processIterator.hasNext()) {
            ProcessProperties nextProcess = processIterator.next();
            if(nextProcess.getPid().equals(pid)){
                nextProcess.getProcess().destroy();
                nextProcess.setKilled(true);
                found = true;
            }
        }
        return found;
    }

    /**
     * Adds a process to collection of running processes
     * @param process the job object to add
     */
    public static void add(ProcessProperties process){
        processList.add(process);
    }

    /**
     * Checks if a running process has been killed
     * @param pid the pid of process
     * @return true if process has been killed, false otherwise
     */
    public static boolean isKilled(String pid){

        // We assume we haven't found job yet
        boolean killed = false;

        // Iterates through process collection to find passed pid
        Iterator<ProcessProperties> processIterator = processList.iterator();
        while (processIterator.hasNext()) {
            ProcessProperties process = processIterator.next();

            // Job has been found so remove it from collection and sets return value
            if(process.getPid().equals(pid)){
                if(process.isKilled()) killed = true;
            }
        }
        return killed;
    }

    /**
     * Removes a processs from collection of running processes
     * @param pid the pid of process to delete
     * @return true if process has been successfuly found and removed, false otherwise
     */
    public static boolean remove(String pid){

        // We assume we haven't found job yet
        boolean found = false;

        // Iterates through process collection to find passed pid
        Iterator<ProcessProperties> processIterator = processList.iterator();
        while (processIterator.hasNext()) {
            ProcessProperties process = processIterator.next();

            // Job has been found so remove it from collection and sets return value
            if(process.getPid().equals(pid)){
                processIterator.remove();
                found = true;
            }
        }
        return found;
    }
}