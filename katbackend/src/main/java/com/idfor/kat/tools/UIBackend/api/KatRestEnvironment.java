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

import com.idfor.kat.tools.UIBackend.model.KatCluster;
import com.idfor.kat.tools.UIBackend.model.KatServer;
import com.idfor.kat.tools.UIBackend.model.KatBroker;
import com.idfor.kat.tools.UIBackend.model.KatEnvironment;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import javax.ws.rs.*;

import java.util.List;

@CrossOriginResourceSharing(
    allowAllOrigins = true,
    allowCredentials = true,
    allowHeaders = {"authorization", "cache-control", "x-requested-with", "content-type"}
)
@Path(value = "/environments")
public interface KatRestEnvironment {

    /**
     * List of environments
     * @return
     */
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    public List<KatEnvironment> getEnvironments() ;

    /**
     * Créer un environement
     * @param katEnvironment
     * @return
     */
    @Path(value="/")
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    public KatEnvironment createEnvironment(KatEnvironment katEnvironment);

    /**
     * Détail d'un environement
     * @param environmentId
     * @return
     */
    @Path(value="/{environmentId}")
    @GET
    @Produces("application/json")
    public KatEnvironment getEnvironmentById(@PathParam(value = "environmentId") String environmentId);

    /**
     * Modification d'un environement
     * @param environmentId
     * @param katEnvironment
     * @return
     */
    @Path(value="/{environmentId}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public KatEnvironment updateEnvironment(@PathParam(value = "environmentId") String environmentId, KatEnvironment katEnvironment);

    /**
     * Supression d'un environnement
     * @param id
     */
    @Path(value="/{id}")
    @DELETE
    public void deleteEnvironment(@PathParam(value = "id") String id);

    /**
     * liste des cluster d'un environement
     * @param environmentId
     * @return
     */
    @Path(value="/{environmentId}/clusters")
    @GET
    public List<KatCluster> getClustersByEnvironment(@PathParam(value="environmentId") String environmentId);

    /**
     *Ajout d'un serveur dans un environement
     * @param environmentId
     * @return
     */
    @Path(value = "/{environmentId}/servers")
    @GET
    @Consumes("application/json")
    public List<KatServer> getServer(@PathParam(value = "environmentId") String environmentId);


    /**
     *Ajout d'un serveur dans un environement
     * @param server
     * @param environmentId
     * @return
     */
    @Path(value = "/{environmentId}/servers")
    @POST
    @Consumes("application/json")
    public KatServer addServer(KatServer server,@PathParam(value = "environmentId") String environmentId);


    /**
     * Suppression d'un serveur sur un environnement sans supprimer le serveur en lui même
     *
     * @param id
     * @param environmentId
     */
    @Path(value = "/{environmentId}/servers/{serverId}")
    @DELETE
    public void deleteServer(@PathParam(value = "serverId") String id,
                       @PathParam(value = "environmentId") String environmentId);


    /**
     *
     * @param environmentId
     * @param clusterId
     * @return
     */
    @Path(value="/{environmentId}/clusters/{clusterId}/servers")
    @GET
    public List<KatServer> getServersByEnvironmentAndCluster(@PathParam("environmentId") String environmentId,
                                                             @PathParam(value ="clusterId") String clusterId);

    /**
     * Liste des brokers d'un environement
     * @param environmentId
     * @return
     */
    @Path(value = "/{environmentId}/brokers")
    @GET
    public List<KatBroker> getBrokersByEnvironment(@PathParam(value = "environmentId") String environmentId);
}
