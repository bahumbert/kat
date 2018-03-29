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

import com.idfor.kat.tools.UIBackend.model.Context;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;

import javax.ws.rs.*;
import java.util.List;

@CrossOriginResourceSharing(
        allowAllOrigins = true,
        allowCredentials = true,
        allowHeaders = {"authorization", "cache-control", "x-requested-with", "content-type"}
)
@Path(value = "/contexts")
public interface ContextRest {

    /**
     * Return all servers attributes
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     */
    @Path(value = "")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    List<com.idfor.kat.tools.UIBackend.model.Context> getAllContexts();

    /**
     * Return all servers attributes
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     */
    @Path(value = "")
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    Context postContext(Context context);



    /**
     * Return all servers attributes
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     */
    @Path(value = "/{contextId}")
    @Consumes("application/json")
    @Produces("application/json")
    @DELETE
    void deleteContext(@PathParam(value = "contextId") String contextId);

}