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
 * Bean used to hold properties needed by a kind of alerting. Alerting can be done through: EMAIL, SMS
 * In both cases, a status and a list of distribution is defined. Reference is
 */
public class AlertProperties {

    private String scheduleFile;
    private List<String> recipients = new ArrayList<>();
    private boolean isActive;

    public String getScheduleFile() {
        return scheduleFile;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setScheduleFile(String scheduleFile) {
        this.scheduleFile = scheduleFile;
    }

    public void addRecipients(String recipient) {
        this.recipients.add(recipient);
    }

    public void setActive(String active) {
        isActive = Boolean.parseBoolean(active);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}