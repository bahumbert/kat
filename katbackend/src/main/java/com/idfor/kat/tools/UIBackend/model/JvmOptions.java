/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idfor.kat.tools.UIBackend.model;

public class JvmOptions {


    private Boolean active;
    private String options;
    private String scheduleFile;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getOptions() {
        return options;
    }

    public void setRecipients(String recipients) {
        this.options = options;
    }

    public String getScheduleFile() {
        return scheduleFile;
    }

    public void setScheduleFile(String scheduleFile) {
        this.scheduleFile = scheduleFile;
    }

}
