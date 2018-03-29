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
import javax.ws.rs.core.Response;

@CrossOriginResourceSharing(
    allowAllOrigins = true,
    allowCredentials = true,
    allowHeaders = {"authorization", "cache-control", "x-requested-with", "content-type"}
)
@Path(value = "/zip-nexus")
public interface ZipNexusUploaderEnvironment {

    // DONE
    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadZipOnNexus (FileExtractor fileExtractor, @Context HttpHeaders headers);


    @Path("/token/{token}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String getZipOnNexus(@PathParam(value = "token") String token);


    // done
    @Path(value="/deploy/server/{serverId}")
    @POST
    public String deployArtifact(
            ZipArtifact zipArtifact,
            @PathParam(value = "serverId") String serverId,
            @Context HttpHeaders headers);

    // done
    @Path(value="/deploy-cron/server/{serverId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String deployCronArtifact(DeployCron deployCron,
                                     @PathParam(value = "serverId") String serverId,
                                     @Context HttpHeaders headers);
//DONE
    @Path(value="/undeploy/{artifactName}/server/{serverId}/version/{version}")
    @DELETE
    public String undeployArtifact(@PathParam(value = "artifactName") String artifactName,
                                   @PathParam(value = "serverId") String serverId,
                                   @PathParam(value = "version") String version,
                                   @Context HttpHeaders headers);
}
