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

package com.idfor.kat.tools.katexecbundle.run;

import com.idfor.kat.tools.katexecbundle.beans.KatExecProperties;
import com.sun.mail.smtp.SMTPTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;

/**
 * Static methods used to send mails
 */
public class SendMail {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMail.class);

    /**
     * Send a mail message using defined parameters in config file
     * @param mailTo recipient's list, seperated by commas
     * @param mailSubject mail subject
     * @param mailBody mail message
     */
    public static void SendTextMessage(String mailTo, String mailSubject, String mailBody) {

        // Gets account information from config file
        String hostname = KatExecProperties.getMailHostname();
        int port = KatExecProperties.getMailPort();
        String from = KatExecProperties.getMailFrom();
        String password = KatExecProperties.getMailPassword();

        try {

            // Set mail properties
            Properties props = System.getProperties();
            props.put("mail.debug", KatExecProperties.isMailDebug());
            props.put("mail.smtp.debug", KatExecProperties.isMailDebug());
            props.put("mail.smtp.host", hostname);
            props.put("mail.smtp.ssl.enable", KatExecProperties.isMailSsl());
            props.put("mail.smtp.starttls.enable", KatExecProperties.isMailStarttls());
            props.put("mail.smtp.ssl.trust", "*");
            props.put("mail.smtp.auth", KatExecProperties.isMailAuth());
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.socketFactory.port", port);
            props.put("mail.smtp.socketFactory.fallback", "true");
            props.put("mail.smtp.connectiontimeout", "5000");
            props.put("mail.smtp.timeout", "5000");
            Session session = Session.getInstance(props);

            // If authorization is required, prepares one
            if(KatExecProperties.isMailAuth()) {
                Authenticator authenticator = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                };

                // Overrides already prepared session without authorization
                session = Session.getInstance(props, authenticator);
            }

            // Sets debug options
            session.setDebug(KatExecProperties.isMailDebug());

            // Prepares mail message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo, false));
            msg.setSentDate(new Date());
            msg.setSubject(mailSubject);
            msg.setText(mailBody);

            // Prepares SMTP transport
            SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
            msg.saveChanges();

            // Connects and sends mail
            try {
                transport.connect(hostname, from, password);
                transport.sendMessage(msg, msg.getAllRecipients());
            }
            finally {
                transport.close();
            }

        // Processing exceptions
        } catch(NoSuchProviderException e) {
            LOGGER.error("A Security Provider is requested but is not available for selected environment: " + e.getMessage());
        } catch(AddressException e) {
            LOGGER.error("At least one mail address is invalid: " + e.getMessage());
        } catch(MessagingException e) {
            LOGGER.error("An error occurred while preparing message: " + e.getMessage());
        }
    }
}