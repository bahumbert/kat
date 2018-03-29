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

package com.idfor.kat.tools.katexecbundle.ws;

import com.idfor.kat.tools.katexecbundle.beans.JobAction;
import com.idfor.kat.tools.katexecbundle.beans.JobProperties;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * This interface defines all web services that will be exposed by KAT Executor
 */
// General WS address is on /cxf/katexec/ (defined in blueprint)
// WS produces JSON responses


@Path("/")
@Produces({MediaType.APPLICATION_JSON, "text/json"})
@CrossOriginResourceSharing(
        allowAllOrigins = true,
        allowCredentials = true
)
public interface KatExecWebServices {

    /**
     * WS at address /cxf/katexec/jobs - Returns a complete list of scheduled jobs in KAT Executor
     * @return a JSON view of all scheduled jobs in KAT Executor, null if an error occured
     */
    @GET
    @Path("/jobs")
    List<JobProperties> getJobs();

    /**
     * WS at address /cxf/katexec/jobs/pid - Returns specific scheduled job with with given pid in KAT Executor
     * @return a JSON view of job with given pid in KAT Executor, null if not found
     */
    @GET
    @Path("/jobs/{pid}")
    JobProperties getJob(@PathParam("pid") String pid);

    /**
     * WS at address /cxf/katexec/jobs/pid - Verifies if a job has at least one active scheduling
     * @return a JSON view of job with given pid in KAT Executor, null if not found
     */
    @GET
    @Path("/jobs/scheduled/{job}/{version}")
    boolean isScheduled(@PathParam("job") String job, @PathParam("version") String version);

    /**
     * WS at address /cxf/katexec/jobs - Allows to start, pause or resume a scheduled job
     * The action and pid are passed as JSON object - valid actions are start, pause, resume
     * @return True if the job could be launched, False otherwise
     */
    @PUT
    @Consumes ({MediaType.APPLICATION_JSON, "text/json"})
    @Path("/jobs")
    boolean jobAction(JobAction request);
}