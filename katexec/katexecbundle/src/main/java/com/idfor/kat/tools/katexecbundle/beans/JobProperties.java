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

/**
 * Bean used to describe a deployed job in KAT Executor
 */
public class JobProperties {

    private String pid;
    private String propertyFile;
    private String project;
    private String name;
    private String version;
    private String context;
    private String overloadedContext;
    private String state;
    private String binariesPath;
    private String deployDate;
    private String label;
    private String description;
    private String initialJavaHeapSpace;
    private String maximumJavaHeapSpace;
    private String scheduling;
    private boolean isRunning;
    private String mailList;
    private boolean mailActive;
    private String smsList;
    private boolean smsActive;
    private String nextExecution;
    private String jvmOptions;
    private boolean jvmActive;

    public String getPid() {
        return pid;
    }

    public String getPropertyFile() {
        return propertyFile;
    }

    public String getProject() {
        return project;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getContext() {
        return context;
    }

    public String getOverloadedContext() {
        return overloadedContext;
    }

    public String getState() {
        return state;
    }

    public String getBinariesPath() {
        return binariesPath;
    }

    public String getDeployDate() {
        return deployDate;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getInitialJavaHeapSpace() {
        return initialJavaHeapSpace;
    }

    public String getMaximumJavaHeapSpace() {
        return maximumJavaHeapSpace;
    }

    public String getScheduling() {
        return scheduling;
    }

    public String getMailList() {
        return mailList;
    }

    public boolean isMailActive() {
        return mailActive;
    }

    public String getSmsList() {
        return smsList;
    }

    public boolean isSmsActive() {
        return smsActive;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getNextExecution() {
        return nextExecution;
    }

    public String getJvmOptions() {
        return jvmOptions;
    }

    public boolean isJvmActive() {
        return jvmActive;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setContext(String context) {

        // If this property has not been set by job properties, use higher level KAT Executor property
        if(context != null && !context.equals("")) this.context = context;
        else this.context = KatExecProperties.getDefaultContext();
    }

    public void setOverloadedContext(String overloadedContext) {
        this.overloadedContext = overloadedContext;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setBinariesPath(String binariesPath) {

        // If this property has not been set by job properties, use higher level KAT Executor property
        if(binariesPath != null && !binariesPath.equals("")) this.binariesPath = binariesPath;
        else this.binariesPath = KatExecProperties.getDefaultBinariesPath();
    }

    public void setDeployDate(String deployDate) {
        this.deployDate = deployDate;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInitialJavaHeapSpace(String initialJavaHeapSpace) {

        // If this property has not been set by job properties, use higher level KAT Executor property
        if(initialJavaHeapSpace != null && !initialJavaHeapSpace.equals("")) this.initialJavaHeapSpace = initialJavaHeapSpace;
        else this.initialJavaHeapSpace = KatExecProperties.getDefaultInitialJavaHeapSpace();
    }

    public void setMaximumJavaHeapSpace(String maximumJavaHeapSpace) {

        // If this property has not been set by job properties, use higher level KAT Executor property
        if(maximumJavaHeapSpace != null && !maximumJavaHeapSpace.equals("")) this.maximumJavaHeapSpace = maximumJavaHeapSpace;
        else this.maximumJavaHeapSpace = KatExecProperties.getDefaultMaximumJavaHeapSpace();
    }

    public void setScheduling(String scheduling) {
        this.scheduling = scheduling;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setMailList(String mailList) {
        this.mailList = mailList;
    }

    public void setMailActive(boolean mailActive) {
        this.mailActive = mailActive;
    }

    public void setSmsList(String smsList) {
        this.smsList = smsList;
    }

    public void setSmsActive(boolean smsActive) {
        this.smsActive = smsActive;
    }

    public void setNextExecution(String nextExecution) {
        this.nextExecution = nextExecution;
    }

    public void setJvmOptions(String jvmOptions) {
        this.jvmOptions = jvmOptions;
    }

    public void setJvmActive(boolean jvmActive) {
        this.jvmActive = jvmActive;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}