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

package com.idfor.kat.tools.UIBackend.api;

import com.idfor.kat.tools.UIBackend.model.*;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;


@CrossOriginResourceSharing(
        allowAllOrigins = true,
        allowCredentials = true,
        allowHeaders = {"authorization", "cache-control", "x-requested-with", "content-type"}
)
@Path(value = "/workflow")
public interface WorkflowRest {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public List<Workflow> getWorkflows();

    @DELETE
    @Path(value = "/{idWorkflow}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public List<Workflow> deleteWorkflow(@PathParam(value = "idWorkflow") String idWorkflow);

    @Path(value = "/{idWorkflow}/server/{idServer}")
    @POST
    @Consumes("application/json")
    public String deployWorkflow(@PathParam(value = "idWorkflow") String idWorkflow,
                                 @PathParam(value = "idServer") String idServer,
                                 @Context HttpHeaders headers);

    @Path(value = "/server/{idServer}/workflow-file/{workflowFile}")
    @DELETE
    @Consumes("application/json")
    public String unDeployWorkflow(@PathParam(value = "workflowFile") String idWorkflow,
                                 @PathParam(value = "idServer") String idServer,
                                 @Context HttpHeaders headers);

    @Path(value = "")
    @POST
    @Consumes("application/json")
    public Task addWorkflow(Workflow workflow);

    @Path(value = "/{idWorkflow}")
    @PUT
    @Consumes("application/json")
    public Task updateWorkflow(@PathParam(value = "idWorkflow") String idWorkflow, Workflow workflow);


    @Path(value = "/{workflowId}")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    public Workflow getWorkflow(@PathParam(value = "workflowId") String workflowId);
}
