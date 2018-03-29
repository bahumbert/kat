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

import com.idfor.kat.tools.katjobmanagerbundle.beans.AlertProperties;
import org.apache.felix.utils.properties.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Class used to handle setting of alerts via EMAIL and SMS in case a job has a failure
 * Properties are set at schedule file level for each job planned for execution
 */
public class AlertUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(AlertUtils.class);

    public enum AlertTypes { EMAIL, SMS };

    /**
     * Gets state and distribution list of alerts for given type and schedule file
     * @param alertType type of alert (EMAIL or SMS)
     * @param scheduleFile file used to schedule job execution
     * @return
     */
    public static AlertProperties getAlerts(AlertTypes alertType, String scheduleFile){

        // We haven't found any property yet;
        AlertProperties alerts = new AlertProperties();

        try {

            // Checks scheduled job configuration file
            String base = System.getProperty("karaf.etc");
            Properties props = new Properties(new File(base, "katexec/" + scheduleFile));

            // Per default, get mail properties
            alerts.setActive(props.get("job.alert.mail.active"));
            String recipientsList = props.get("job.alert.mail.list");

            // In case we ask for SMS properties
            if(alertType.equals(AlertTypes.SMS)) {
                alerts.setActive(props.get("job.alert.sms.active"));
                recipientsList = props.get("job.alert.sms.list");
            }

            // Set schedule file back
            alerts.setScheduleFile(scheduleFile);

            // Get list of recepients
            if(recipientsList != null) {
                String recipients[] = recipientsList.split(",");
                for (int i = 0; i < recipients.length; i++)
                    alerts.addRecipients(recipients[i]);
            }


        } catch (IOException e) {
            LOGGER.error("Unable to get alerting properties file for schedule file *** " + scheduleFile + " ***");
        }
        return alerts;
    }

    /**
     * Sets state and distribution list of alerts for given type
     * @param alertType type of alert (EMAIL or SMS)
     * @param alerts JSON object containing schedule file, state of alert and distribution list
     * @return true if setting was OK, false otherwise
     */
    public static boolean setAlerts(AlertTypes alertType, AlertProperties alerts){

        // We' assume everything went OK
        boolean ok = true;

        try {

            // Prepares scheduled job configuration file for writing
            String base = System.getProperty("karaf.etc");
            Properties props = new Properties(new File(base, "katexec/" + alerts.getScheduleFile()));

            // Sets properties name according to alert type
            String isActiveProperty = "job.alert.mail.active";
            String recipientsListProperty = "job.alert.mail.list";
            if(alertType.equals(AlertTypes.SMS)) {
                isActiveProperty = "job.alert.sms.active";
                recipientsListProperty = "job.alert.sms.list";
            }

            // Sets notification state
            props.put(isActiveProperty, Boolean.toString(alerts.isActive()));

            // Retrieves recipients list
            String recipientsList = "";
            Iterator<String> recipients = alerts.getRecipients().iterator();
            while(recipients.hasNext()) {
                if (!recipientsList.equals("")) recipientsList += ",";
                recipientsList += recipients.next();
            }

            // Sets recipients list
            props.put(recipientsListProperty, recipientsList);

            // Save changes to property file
            props.save();

        } catch (IOException e) {
            ok = false;
            LOGGER.error("Unable to set alerting properties file for schedule file *** " + alerts.getScheduleFile() + " ***");
        }
        return ok;
    }

    /**
     * Removes state and distribution list of alerts for given type
     * @param alertType type of alert (EMAIL or SMS)
     * @param scheduleFile file used to schedule job execution
     * @return true if removing was OK, false otherwise
     */
    public static boolean removeAlerts(AlertTypes alertType, String scheduleFile){

        // We' assume everything went OK
        boolean ok = true;

        try {

            // Prepares scheduled job configuration file for writing
            String base = System.getProperty("karaf.etc");
            Properties props = new Properties(new File(base, "katexec/" + scheduleFile));

            // Sets properties name according to alert type
            if(alertType.equals(AlertTypes.EMAIL)) {
                props.remove("job.alert.mail.active");
                props.remove("job.alert.mail.list");
            }
            else if(alertType.equals(AlertTypes.SMS)) {
                props.remove("job.alert.sms.active");
                props.remove("job.alert.sms.list");
            }

            // Save changes to property file
            props.save();

        } catch (IOException e) {
            ok = false;
            LOGGER.error("Unable to remove alerting properties file for schedule file *** " + scheduleFile + " ***");
        }
        return ok;
    }
}