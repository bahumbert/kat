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

package com.idfor.kat.tools.katjobmanagerbundle.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.idfor.kat.tools.katjobmanagerbundle.beans.*;
import com.idfor.kat.tools.katjobmanagerbundle.run.AlertUtils;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import java.util.List;

import static com.idfor.kat.tools.katjobmanagerbundle.run.AlertUtils.AlertTypes.EMAIL;

/**
 * This interface defines all web services that will be exposed by KAT Executor
 */
// General WS address is on /services/katexec/ (defined in blueprint
// WS produces JSON responses
@Path("/")
@Produces({MediaType.APPLICATION_JSON, "text/json"})
@CrossOriginResourceSharing(
        allowAllOrigins = true,
        allowCredentials = true
)
public interface JobManagerWebServices {

    /**
     * WS at address /cxf/katjobmanager/binaries - Lists all deployed TALEND jobs it in the binaries directory
     * @return Jobs list
     */
    @GET
    @Path("/binaries")
    List<BinariesProperties> binariesList();

    /**
     * WS at address /cxf/katjobmanager/contexts - Lists all contexts available for a deployed TALEND job
     * @return context list
     */
    @GET
    @Path("/contexts/{job}/{version}")
    List<String> contextList(@PathParam("job") String job, @PathParam("version") String version);

    /**
     * WS at address /cxf/katjobmanager/contexts/properties - Lists all properties of a given context for a given job and version
     * @return properties list for the given context
     */
    @GET
    @Path("/contexts/properties/{scheduleFile}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    ContextFile listContextProperties(@PathParam("scheduleFile") String scheduleFile);

    /**
     * WS at address /cxf/katjobmanager/contexts/properties - Overloads properties passed as a JSON object for a given context for a given job and version
     * @return context list
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/contexts/properties")
    boolean setContextProperties(ContextFile request);

    /**
     * WS at address /cxf/katjobmanager/contexts/properties - Deletes overloading for properties of a given context for a given job and version and resets original file
     * @return context list
     */
    @DELETE
    @Path("/contexts/properties/{scheduleFile}")
    boolean deleteContextProperties(@PathParam("scheduleFile") String scheduleFile);

    /**
     * WS at address /cxf/katjobmanager/binaries - Downloads a zipped archive containing a TALEND job to deploy
     * and extracts it in the binaries directory
     * @return True if the job could be installed, False otherwise
     */
    @POST
    @Path("/binaries/{artifactUrl}")
    boolean install(@PathParam("artifactUrl") String artifactUrl);

    /**
     * WS at address /cxf/katjobmanager/binaries - Deletes the binaries of a specific job
     * @return True if the job could be removed, False otherwise
     */
    @DELETE
    @Path("/binaries/{job}/{version}")
    boolean uninstall(@PathParam("job") String job, @PathParam("version") String version);

    /**
     * WS at address /cxf/katjobmanager/schedule - Schedules a deployed job by installing a schedule file in KAT Executor config directory
     * @return True if the job could be launched, False otherwise
     */
    @POST
    @Consumes ({MediaType.APPLICATION_JSON})
    @Path("/schedule")
    boolean schedule(ScheduleParameters request);

    /**
     * WS at address /cxf/katjobmanager/schedule - Un-schedules a deployed job by removing a schedule file from KAT Executor config directory
     * @return a JSON view of all scheduled jobs in KAT Executor, null if an error occurred
     */
    @DELETE
    @Path("/schedule/{scheduleFile}")
    boolean unschedule(@PathParam("scheduleFile") String scheduleFile);

    /**
     * WS at address /cxf/katjobmanager/schedule/property - Change a property in a scheduled file
     * @return true if successful, false otherwise
     */
    @PUT
    @Consumes ({MediaType.APPLICATION_JSON, "text/json"})
    @Path("/schedule/property")
    boolean changeScheduleProperty(ScheduleProperty request);

    /**
     * WS at address /cxf/katjobmanager/versions - Gets list of all deployed versions for passed job
     * @return JSON list of available versions
     */
    @GET
    @Path("/versions/{job}")
    List<String> getVersions(@PathParam("job") String job);

    /**
     * WS at address /cxf/katjobmanager/alerts/mail - Gets list of mail alerts in case job deployed by this file pid has execution errors
     * @return JSON representation of mail alerts
     */
    @GET
    @Path("/alerts/mail/{scheduleFile}")
    AlertProperties getMailAlert(@PathParam("scheduleFile") String scheduleFile);

    /**
     * WS at address /cxf/katjobmanager/alerts/mail - Sets mail alerts in case job deployed by this file pid has execution errors
     * @return True if mail alerting could be set, false otherwise
     */
    @POST
    @Consumes ({MediaType.APPLICATION_JSON, "text/json"})
    @Path("/alerts/mail")
    boolean setMailAlert(AlertProperties request);

    /**
     * WS at address /cxf/katjobmanager/alerts/mail - Suppress all mail alerts in case job deployed by this file pid has execution errors
     * @return True if mail alerting could be set, false otherwise
     */
    @DELETE
    @Path("/alerts/mail/{scheduleFile}")
    boolean removeMailAlert(@PathParam("scheduleFile") String scheduleFile);

    /**
     * WS at address /cxf/katjobmanager/alerts/sms - Gets list of sms alerts in case job deployed by this file pid has execution errors
     * @return JSON representation of mail alerts
     */
    @GET
    @Path("/alerts/sms/{scheduleFile}")
    AlertProperties getSmsAlert(@PathParam("scheduleFile") String scheduleFile);

    /**
     * WS at address /cxf/katjobmanager/alerts/sms - Sets sms alerts in case job deployed by this file pid has execution errors
     * @return True if SMS alerting could be set, false otherwise
     */
    @POST
    @Consumes ({MediaType.APPLICATION_JSON, "text/json"})
    @Path("/alerts/sms")
    boolean setSmsAlert(AlertProperties request);

    /**
     * WS at address /cxf/katjobmanager/alerts/sms - Suppress all sms alerts in case job deployed by this file pid has execution errors
     * @return True if SMS alerting could be set, false otherwise
     */
    @DELETE
    @Path("/alerts/sms/{scheduleFile}")
    boolean removeSmsAlert(@PathParam("scheduleFile") String scheduleFile);

    /**
     * WS at address /cxf/katjobmanager/workflows - Lists all deployed workflows on execution server
     * @return Jobs list
     */
    @GET
    @Path("/workflows")
    List<WorkflowProperties> workflowList();

    /**
     * WS at address /cxf/katjobmanager/workflows - Deploys a created workflow on execution server
     * and extracts it in the binaries directory
     * @return True if the workflow could be installed, False otherwise
     */
    @POST
    @Consumes ({MediaType.APPLICATION_JSON, "text/json"})
    @Path("/workflows")
    boolean deployWorkflow(String workflow);

    /**
     * WS at address /cxf/katjobmanager/workflows - Removes a deployed workflow from an execution server
     * @return True if the workflow could be removed, False otherwise
     */
    @DELETE
    @Path("/workflows/{workflowFile}")
    boolean removeWorkflow(@PathParam("workflowFile") String workflowFile);
}