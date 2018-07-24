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

import java.util.ArrayList;
import java.util.List;

/**
 * Bean used to hold JVM options for a given job (identified by its schedule file)
 * A status (can be activated or de-activated) and a string containing the JVM options is stored in this bean
 */
public class JvmOptions {

    private String scheduleFile;
    private String options;
    private boolean isActive;

    public String getScheduleFile() {
        return scheduleFile;
    }

    public String getOptions() {
        return options;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setScheduleFile(String scheduleFile) {
        this.scheduleFile = scheduleFile;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setActive(String active) {
        isActive = Boolean.parseBoolean(active);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}