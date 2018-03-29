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

import com.idfor.kat.tools.katjobmanagerbundle.beans.ContextProperties;
import com.idfor.kat.tools.katjobmanagerbundle.beans.KatJobManagerProperties;
import org.apache.felix.utils.properties.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements methods that are used by web services and commands to manipulate contexts
  */
public class ContextUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContextUtils.class);

    /**
     * Method that returns a list of available contexts for a given job in a given version
     * The project is found back from directory structure
     * @param job name of deployed job
     * @param version of deployed job
     * @return a list od available contexts for requested job
     */
    public static List<String> getContextList(String job, String version){

        // Gets project
        String project = ProjectUtils.getProject(job, version);

        // If we have found a valid project, we dig further for contexts
        if(project != null) return getContextList(new File(getContextPath(project, job, version)));

        // Returns contexts list
        else return null;
    }

    /**
     * Returns a list of available contexts for a given job in a given version - project has already been retrieved
     * @param project name of job project
     * @param job name of deployed job
     * @param version of deployed job
     * @return a list od available contexts for requested job
     */
    public static List<String> getContextList(String project, String job, String version){
        return getContextList(new File(getContextPath(project, job, version)));
    }

    /**
     * Returns a list of available contexts in a given folder - The overloaded contexts are not returned as they are specific
     * @param folder a folder containing contexts
     * @return a list od available contexts in requested folder
     */
    private static List<String> getContextList(File folder){

        List<String> contextList = new ArrayList<>();

        // Gets list of contexts in directory
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            String currentFile = listOfFiles[i].getName();
            if (currentFile.lastIndexOf(".properties") != -1 && currentFile.lastIndexOf("-kat.job-") == -1)
                contextList.add(currentFile.substring(0, currentFile.lastIndexOf(".properties")));
        }

        // Returns contexts list
        return contextList;
    }

    /**
     * Returns full path to context directory for specified job
     * @param project name of deployed job project
     * @param job name of deployed job
     * @param version version of deployed job
     * @return Path to context directory
     */
    public static String getContextPath(String project, String job, String version){
        String jobAndVer = job + "_" + version.replace(".", "_");
        return KatJobManagerProperties.getDefaultBinariesPath() + "/" + job + "/" + version + "/" + job + "/" + project + "/" + jobAndVer + "/contexts";
    }

    /**
     * Returns full path to context directory for specified job
     * @param project name of deployed job project
     * @param job name of deployed job
     * @param version version of deployed job
     * @param context context of deployed job
     * @return Read context properties
     */
    public static List<ContextProperties> getContextProperties(String project, String job, String version, String context) throws IOException {

        List<ContextProperties> contextProperties = new ArrayList<>();

        // Get all properties as an enumeration of key / values
        Properties props = new Properties(new File(getContextPath(project, job, version) + "/" + context + ".properties"));
        Enumeration keyValues = props.propertyNames();

        // Get all key / value pairs
        // As this is an original context file, no overloading exists
        while (keyValues.hasMoreElements()) {
            String key = (String) keyValues.nextElement();
            String value = props.getProperty(key);
            contextProperties.add(new ContextProperties(false, key, value, value));
        }

        return contextProperties;
    }

    /**
     * Returns full path to context directory for specified job
     * @param project name of deployed job project
     * @param job name of deployed job
     * @param version version of deployed job
     * @param context context of deployed job
     * @return Read context properties
     */
    public static List<ContextProperties> getOverloadedContextProperties(String project, String job, String version, String context) throws IOException {

        List<ContextProperties> contextProperties = new ArrayList<>();

        // Get all properties as an enumeration of key / values
        Properties props = new Properties(new File(getContextPath(project, job, version) + "/" + context + ".properties"));
        Enumeration keyValues = props.propertyNames();

        // Get all key / value pairs
        // Checks everytime if there are comments. If there are some, it means that value might have been overload
        // If comment begins with --orig-->, active value is the overloaded one and the one after tag is the original one
        // If comment is equal to --over-->, active value is the original one and the one after tag is the overloaded one
        while (keyValues.hasMoreElements()) {

            // Gets key and active value
            String key = (String) keyValues.nextElement();
            String origValue = props.getProperty(key);

            // By default, we assume this property has not been overloaded
            String overValue = origValue;
            boolean isActive = false;

            // Verifies if there are comments
            List<String> comments = props.getComments(key);

            // Checks in case of comments if there is an overloaded value and if one is active
            Iterator<String> comment = comments.iterator();
            while(comment.hasNext()){
                String commentValue = comment.next();
                if(commentValue.indexOf("#--orig-->") == 0){
                    isActive = true;
                    overValue = origValue;
                    origValue = commentValue.substring(10); //.replace("\\\\", "\\");
                }
                else if(commentValue.indexOf("#--over-->") == 0){
                    overValue = commentValue.substring(10); //.replace("\\\\", "\\");
                }
            }

            contextProperties.add(new ContextProperties(isActive, key, origValue, overValue));
        }

        return contextProperties;
    }

    /**
     * Sets overloaded context properties
     * @param project name of deployed job project
     * @param job name of deployed job
     * @param version version of deployed job
     * @param context context of deployed job
     * @param contextProperties list of context properties with their overloaded values and state
     * @throws IOException
     */
    public static void setOverloadedContextProperties(String project, String job, String version, String context, List<ContextProperties> contextProperties) throws IOException {

        // Gets property file for modification
        Properties props = new Properties(new File(getContextPath(project, job, version) + "/" + context + ".properties"));

        // First clears all existing properties
        props.clear();

        // Iterates through list of context properties and write them to overloaded context file
        Iterator<ContextProperties> contextProperty = contextProperties.iterator();
        while(contextProperty.hasNext()){
            ContextProperties currentContextProperty = contextProperty.next();

            // If overloaded property is active, we write original one as comment with tag --orig-->
            if(currentContextProperty.isActive())
                props.put(currentContextProperty.getPropertyName(), "#--orig-->" + currentContextProperty.getOriginalValue(), currentContextProperty.getOverloadedValue().replace(  "^ ", "\\ "));

            // If overloaded property is not active and its value is different from original one, we write overloaded value as comment with tag --over-->
            // If values are equal, we just emmit original value
            else
                if(currentContextProperty.getOriginalValue().equals(currentContextProperty.getOverloadedValue()))
                    props.put(currentContextProperty.getPropertyName(), currentContextProperty.getOriginalValue().replaceFirst("^ ", "\\ "));
                else
                    props.put(currentContextProperty.getPropertyName(), "#--over-->" + currentContextProperty.getOverloadedValue(), currentContextProperty.getOriginalValue().replaceFirst("^ ", "\\ "));
        }

        // Writes content to file
        props.save();
    }

    public static boolean changeScheduleProperty(String scheduleFile, String propertyName, String propertyValue){

        // We assume everything went OK
        boolean ok = true;

        // Retrieves configuration file and tries to change value
        String base = System.getProperty("karaf.etc");
        try {
            Properties props = new Properties(new File(base, "katexec/" + scheduleFile));

            // Puts new job version value into config file
            props.put(propertyName, propertyValue);
            props.save();
        }
        catch(IOException e){
            LOGGER.error("Unable to change job version for schedule file  *** " + scheduleFile  + " ***");
            ok = false;
        }

        // Returns result of operation
        return ok;
    }
}