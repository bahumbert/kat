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

import org.osgi.service.cm.ManagedService;

import java.util.Dictionary;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Bean used to monitor and hold KAT Executor main configuration. It implements a ManagedService
 * to continuously track changes made to main configuration file and immediately apply these changes
 * to KAT Executor.
 */
public class KatExecProperties implements ManagedService {

    private final static Logger LOGGER = LoggerFactory.getLogger(KatExecProperties.class);

    private static String defaultContext;
    private static String defaultBinariesPath;
    private static String defaultInitialJavaHeapSpace;
    private static String defaultMaximumJavaHeapSpace;
    private static String mailHostname;
    private static int mailPort;
    private static String mailFrom;
    private static String mailPassword;
    private static boolean mailDebug;
    private static boolean mailSsl;
    private static boolean mailStarttls;
    private static boolean mailAuth;
    private static String mailSubject;
    private static String mailBody;

    public static void setDefaultContext(String context) {
        defaultContext = context;
    }

    public static void setDefaultBinariesPath(String path) {
        defaultBinariesPath = path;
    }

    public static void setDefaultInitialJavaHeapSpace(String defaultInitialJavaHeapSpace) {
        KatExecProperties.defaultInitialJavaHeapSpace = defaultInitialJavaHeapSpace;
    }

    public static void setDefaultMaximumJavaHeapSpace(String defaultMaximumJavaHeapSpace) {
        KatExecProperties.defaultMaximumJavaHeapSpace = defaultMaximumJavaHeapSpace;
    }

    public static void setMailHostname(String mailHostname) {
        KatExecProperties.mailHostname = mailHostname;
    }

    public static void setMailPort(String mailPort) {
        try {
            KatExecProperties.mailPort = Integer.parseInt(mailPort);
        }
        catch (NumberFormatException e){
            KatExecProperties.mailPort = 587;
            LOGGER.warn("Property katexec.alert.mail.port is not a valid port number - setting to default SMTP SSL port 587");
        }
    }

    public static void setMailFrom(String mailFrom) {
        KatExecProperties.mailFrom = mailFrom;
    }

    public static void setMailPassword(String mailPassword) {
        KatExecProperties.mailPassword = mailPassword;
    }

    public static void setMailDebug(String mailDebug) {
        KatExecProperties.mailDebug = Boolean.parseBoolean(mailDebug);
    }

    public static void setMailSsl(String mailSsl) {
        KatExecProperties.mailSsl = Boolean.parseBoolean(mailSsl);
    }

    public static void setMailStarttls(String mailStarttls) {
        KatExecProperties.mailStarttls = Boolean.parseBoolean(mailStarttls);
    }

    public static void setMailAuth(String mailAuth) {
        KatExecProperties.mailAuth = Boolean.parseBoolean(mailAuth);
    }

    public static void setMailSubject(String mailSubject) {
        KatExecProperties.mailSubject = mailSubject;
    }

    public static void setMailBody(String mailBody) {
        KatExecProperties.mailBody = mailBody;
    }

    public static String getDefaultMaximumJavaHeapSpace() {
        return defaultMaximumJavaHeapSpace;
    }

    public static String getDefaultContext() {
        return defaultContext;
    }

    public static String getDefaultBinariesPath() {
        return defaultBinariesPath;
    }

    public static String getDefaultInitialJavaHeapSpace() {
        return defaultInitialJavaHeapSpace;
    }

    public static String getMailHostname() {
        return mailHostname;
    }

    public static int getMailPort() {
        return mailPort;
    }

    public static String getMailFrom() {
        return mailFrom;
    }

    public static String getMailPassword() {
        return mailPassword;
    }

    public static boolean isMailDebug() {
        return mailDebug;
    }

    public static boolean isMailSsl() {
        return mailSsl;
    }

    public static boolean isMailStarttls() {
        return mailStarttls;
    }

    public static boolean isMailAuth() {
        return mailAuth;
    }

    public static String getMailSubject() {
        return mailSubject;
    }

    public static String getMailBody() {
        return mailBody;
    }

    /**
     * Method used to update the monitored properties
     * @param properties a set of properties to update
     */
    public static void update(Dictionary properties){

        String propertyName;

        // The configuration file exists present
        if (properties != null) {

            // DEFAULT CONTEXT
            propertyName = "katexec.default.context";
            if (properties.get(propertyName) != null) setDefaultContext((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // BINARIES PATH
            propertyName = "katexec.default.binaries.path";
            if (properties.get(propertyName) != null) setDefaultBinariesPath((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // INITIAL JAVA HEAP SPACE
            propertyName = "katexec.default.initial.java.heap.space";
            if (properties.get(propertyName) != null)
                setDefaultInitialJavaHeapSpace((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAXIMUM JAVA HEAP SPACE
            propertyName = "katexec.default.maximum.java.heap.space";
            if (properties.get(propertyName) != null)
                setDefaultMaximumJavaHeapSpace((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAIL HOSTNAME
            propertyName = "katexec.alert.mail.hostname";
            if (properties.get(propertyName) != null)
                setMailHostname((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAIL PORT
            propertyName = "katexec.alert.mail.port";
            if (properties.get(propertyName) != null)
                setMailPort((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAIL FROM
            propertyName = "katexec.alert.mail.from";
            if (properties.get(propertyName) != null)
                setMailFrom((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAIL PASSWORD
            propertyName = "katexec.alert.mail.password";
            if (properties.get(propertyName) != null)
                setMailPassword((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAIL DEBUG
            propertyName = "katexec.alert.mail.debug";
            if (properties.get(propertyName) != null)
                setMailDebug((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAIL SSL
            propertyName = "katexec.alert.mail.ssl";
            if (properties.get(propertyName) != null)
                setMailSsl((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAIL STARTTLS
            propertyName = "katexec.alert.mail.starttls";
            if (properties.get(propertyName) != null)
                setMailStarttls((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAIL AUTH
            propertyName = "katexec.alert.mail.auth";
            if (properties.get(propertyName) != null)
                setMailAuth((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAIL SUBJECT
            propertyName = "katexec.alert.mail.subject";
            if (properties.get(propertyName) != null)
                setMailSubject((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // MAIL BODY
            propertyName = "katexec.alert.mail.body";
            if (properties.get(propertyName) != null)
                setMailBody((String) properties.get(propertyName));
            else LOGGER.warn("Property " + propertyName + " has not been found in the configuration file");

            // Final info message
            LOGGER.info("Configuration of IDFOR KAT Executor has been updated");
        }

        // There is no valid configuration so issue an error message to log file
        else
            LOGGER.error("The configuration file com.idfor.kat.tools.katexec.cfg is not present or has been deleted !");

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
}