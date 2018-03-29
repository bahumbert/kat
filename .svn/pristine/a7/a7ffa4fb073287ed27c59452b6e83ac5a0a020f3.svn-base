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

package com.idfor.kat.tools.UIBackend.beans;

import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * Bean used to monitor and hold KAT Executor main configuration. It implements a ManagedService
 * to continuously track changes made to main configuration file and immediately apply these changes
 * to KAT Executor.
 */
public class KatBackendProperties implements ManagedService {

    private final static Logger LOGGER = LoggerFactory.getLogger(KatBackendProperties.class);

    private static String nexusUrl;
    private static String nexusBrowse;
    private static String nexusUserid;
    private static String nexusPwd;
    private static String nexusRepository;
    private static String nexusGroupId;
    private static String caveAddress;
    private static List<String> viewerGroups;
    private static List<String> superadminGroups;
    private static List<String> adminGroups;
    private static List<String> developerGroups;
    private static List<String> operatorGroups;

    public static String getNexusUrl() {
        return nexusUrl;
    }

    public static String getNexusBrowse() {
        return nexusBrowse;
    }

    public static String getNexusUserid() {
        return nexusUserid;
    }

    public static String getNexusPwd() {
        return nexusPwd;
    }

    public static String getNexusRepository() {
        return nexusRepository;
    }

    public static String getNexusGroupId() {
        return nexusGroupId;
    }

    public static String getCaveAddress() {
        return caveAddress;
    }

    public static List<String> getViewerGroups() {
        return viewerGroups;
    }

    public static List<String> getSuperadminGroups() {
        return superadminGroups;
    }

    public static List<String> getAdminGroups() {
        return adminGroups;
    }

    public static List<String> getDeveloperGroups() {
        return developerGroups;
    }

    public static List<String> getOperatorGroups() {
        return operatorGroups;
    }

    public static void setNexusUrl(String nexusUrl) {
        KatBackendProperties.nexusUrl = nexusUrl;
    }

    public static void setNexusBrowse(String nexusBrowse) {
        KatBackendProperties.nexusBrowse = nexusBrowse;
    }

    public static void setNexusUserid(String nexusUserid) {
        KatBackendProperties.nexusUserid = nexusUserid;
    }

    public static void setNexusPwd(String nexusPwd) {
        KatBackendProperties.nexusPwd = nexusPwd;
    }

    public static void setNexusRepository(String nexusRepository) {
        KatBackendProperties.nexusRepository = nexusRepository;
    }

    public static void setNexusGroupId(String nexusGroupId) {
        KatBackendProperties.nexusGroupId = nexusGroupId;
    }

    public static void setCaveAddress(String caveAddress) {
        KatBackendProperties.caveAddress = caveAddress;
    }

    public static void setViewerGroups(List<String> viewerGroups) {
        KatBackendProperties.viewerGroups = viewerGroups;
    }

    public static void setSuperadminGroups(List<String> superadminGroups) {
        KatBackendProperties.superadminGroups = superadminGroups;
    }

    public static void setAdminGroups(List<String> adminGroups) {
        KatBackendProperties.adminGroups = adminGroups;
    }

    public static void setDeveloperGroups(List<String> developerGroups) {
        KatBackendProperties.developerGroups = developerGroups;
    }

    public static void setOperatorGroups(List<String> operatorGroups) {
        KatBackendProperties.operatorGroups = operatorGroups;
    }

    public static void addViewerGroup(String viewerGroup){
        KatBackendProperties.viewerGroups.add(viewerGroup);
    }

    public static void addSuperadminGroup(String superAdminGroup){
        KatBackendProperties.superadminGroups.add(superAdminGroup);
    }

    public static void addAdminGroup(String adminGroup){
        KatBackendProperties.adminGroups.add(adminGroup);
    }

    public static void addDeveloperGroup(String developerGroup){
        KatBackendProperties.developerGroups.add(developerGroup);
    }

    public static void addOperatorGroup(String operatorGroup){
        KatBackendProperties.operatorGroups.add(operatorGroup);
    }

    /**
     * Method used to update the monitored properties
     * @param properties a set of properties to update
     */
    public static void update(Dictionary properties){

        String propertyName;

        // The configuration file exists present
        if (properties != null) {

            // NEXUS URL
            propertyName = "katbackend.nexus.url";
            if (properties.get(propertyName) != null) setNexusUrl((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // NEXUS BROWSE URL
            propertyName = "katbackend.nexus.browse";
            if (properties.get(propertyName) != null) setNexusBrowse((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // NEXUS USERID
            propertyName = "katbackend.nexus.userid";
            if (properties.get(propertyName) != null) setNexusUserid((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // NEXUS PWD
            propertyName = "katbackend.nexus.pwd";
            if (properties.get(propertyName) != null) setNexusPwd((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // NEXUS REPOSITORY
            propertyName = "katbackend.nexus.repository";
            if (properties.get(propertyName) != null) setNexusRepository((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // NEXUS GROUPID
            propertyName = "katbackend.nexus.groupid";
            if (properties.get(propertyName) != null) setNexusGroupId((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // CAVE ADDRESS
            propertyName = "katbackend.cave.address";
            if (properties.get(propertyName) != null) setCaveAddress((String) properties.get(propertyName));
            else LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");

            // VIEWER GROUPS
            propertyName = "katbackend.viewer.groups";
            if (properties.get(propertyName) != null) setViewerGroups(getGroupsList((String) properties.get(propertyName)));
            else {
                setViewerGroups(null);
                LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");
            }

            // SUPERADMIN GROUPS
            propertyName = "katbackend.superadmin.groups";
            if (properties.get(propertyName) != null) setSuperadminGroups(getGroupsList((String) properties.get(propertyName)));
            else {
                setSuperadminGroups(null);
                LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");
            }

            // ADMIN GROUPS
            propertyName = "katbackend.admin.groups";
            if (properties.get(propertyName) != null) setAdminGroups(getGroupsList((String) properties.get(propertyName)));
            else {
                setAdminGroups(null);
                LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");
            }

            // DEVELOPER GROUPS
            propertyName = "katbackend.developer.groups";
            if (properties.get(propertyName) != null) setDeveloperGroups(getGroupsList((String) properties.get(propertyName)));
            else {
                setDeveloperGroups(null);
                LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");
            }

            // OPERATOR GROUPS
            propertyName = "katbackend.operator.groups";
            if (properties.get(propertyName) != null) setOperatorGroups(getGroupsList((String) properties.get(propertyName)));
            else {
                setOperatorGroups(null);
                LOGGER.warn("The property " + propertyName + " has not been found in the configuration file");
            }

            // Final info message
            LOGGER.info("Configuration of IDFOR KAT Backend has been updated");
        }

        // There is no valid configuration so issue an error message to log file
        else
            LOGGER.error("The configuration file com.idfor.kat.tools.katbackend.cfg is not present or has been deleted !");

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

    private static List<String> getGroupsList(String groupsString){
        List<String> groupsList = new ArrayList<>();
        if(groupsList != null) {
            String[] groups = groupsString.split(" ");
            for (String group : groups) groupsList.add(group);
        }
        return groupsList;
    }
}