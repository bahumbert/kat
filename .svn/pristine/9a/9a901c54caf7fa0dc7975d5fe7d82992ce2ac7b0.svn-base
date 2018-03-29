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

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Date;

import com.idfor.kat.tools.katexecbundle.beans.JobProperties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.MDC;

/**
 * Class used to intercept generated logs from TALEND jobs and sends them to selected output
 * Output may be log4j, sysout or both
 */
public class LogCatcher implements Runnable{

    private final Logger LOGGER = LoggerFactory.getLogger(LogCatcher.class);

    private final InputStream inputStream;
    private LogType logType;
    private LogLevel log4jLevel;
    private boolean hasOutput = false;

    // Logging types
    public enum LogType {SYSOUT, LOG4J, BOTH};

    // Log4j levels
    public enum LogLevel {INFO, ERROR};

    public LogCatcher(InputStream inputStream, JobProperties job, Date jobStartTime, LogType logType, LogLevel log4jLevel) {
        this.inputStream = inputStream;
        this.logType = logType;
        this.log4jLevel = log4jLevel;
    }

    private BufferedReader getBufferedReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        BufferedReader br = getBufferedReader(inputStream);
        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (this.logType == LogType.SYSOUT || this.logType == LogType.BOTH) {
                    System.out.println(line);
                }
                if (this.logType == LogType.LOG4J || this.logType == LogType.BOTH) {
                    if(this.log4jLevel == LogLevel.INFO) LOGGER.info(line);
                    if(this.log4jLevel == LogLevel.ERROR) LOGGER.error(line);
                }
                hasOutput = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getHasOutput(){
        return hasOutput;
    }
}