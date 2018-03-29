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

package com.idfor.kat.tools.katjobmanagerbundle.beans;

import java.util.List;

public class ContextFile {

    private String scheduleFile;
    private List<ContextProperties> contextProperties;

    /**
     * Contructor needed for jackson marshalling
     */
    public ContextFile(){
    }

    /**
     * Constructor to instantiate a context file object with values at creation
     * @param scheduleFile name of KAT scheduling file
     * @param contextProperties context properties
     */
    public ContextFile(String scheduleFile, List<ContextProperties> contextProperties){
        this.scheduleFile = scheduleFile;
        this.contextProperties = contextProperties;
    }

    public String getScheduleFile() {
        return scheduleFile;
    }

    public List<ContextProperties> getContextProperties() {
        return contextProperties;
    }

    public void setScheduleFile(String scheduleFile) {
        this.scheduleFile = scheduleFile;
    }

    public void setContextProperties(List<ContextProperties> contextProperties) {
        this.contextProperties = contextProperties;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}