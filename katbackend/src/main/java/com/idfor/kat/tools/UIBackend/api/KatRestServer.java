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
import com.idfor.kat.tools.UIBackend.model.JvmOptions;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;

@CrossOriginResourceSharing(
    allowAllOrigins = true,
    allowCredentials = true,
    allowHeaders = {"authorization", "cache-control", "x-requested-with", "content-type"}
)
@Path(value = "/servers")
public interface KatRestServer {

    /**
     * Return all servers attributes
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     * @param lonely
     * @return
     */
    @Path(value = "")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    List<KatServer> getServersAttributes(@QueryParam(value = "lonely") Boolean lonely);

    /**@param serverId
     * Return one server attributes identified by serverId
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     *
     * @return List<KatServer>
     */
    @Path(value = "/{serverId}")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    KatServer getServerAttributes(@PathParam(value = "serverId") String serverId);


    /**@param serverId
     * Return one server attributes identified by serverId
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     *
     * @return List<KatServer>
     */
    @Path(value = "/{serverId}/job/update")
    @Consumes("application/json")
    @Produces("application/json")
    @PUT
    String updateScheduledJob(@PathParam(value = "serverId") String serverId,
                           ScheduleUpdater scheduleUpdater,
                           @Context HttpHeaders headers);

    /**@param serverId
     * Return one server attributes identified by serverId
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     *
     * @return List<KatServer>
     */
    @Path(value = "/{serverId}/{jobName}/versions")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    String getServerJobVersion(@PathParam(value = "serverId") String serverId,
                                  @PathParam(value = "jobName") String jobName,
                                       @Context HttpHeaders headers);

    /**@param serverId
     * Return one server attributes identified by serverId
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     *
     * @return List<KatServer>
     */
    @Path(value = "/{serverId}/scheduled-job")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    String getKatScheduledJob(@PathParam(value = "serverId") String serverId, @Context HttpHeaders headers);

    /**@param serverId
     * Return one server attributes identified by serverId
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     *
     * @return List<KatServer>
     */
    @Path(value = "/{serverId}/artifacts")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    String getArtifacts(@PathParam(value = "serverId") String serverId, @Context HttpHeaders headers);

    /**@param serverId
     * Return one server attributes identified by serverId
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     *
     * @return List<KatServer>
     */
    @Path(value = "/{serverId}/scheduled-job/{fileName}")
    @Consumes("application/json")
    @Produces("application/json")
    @DELETE
    Boolean deleteKatJob(@PathParam(value = "serverId") String serverId,
                         @PathParam(value = "fileName") String fileName,
                         @Context HttpHeaders headers);

    /**@param serverId
     * Return one server attributes identified by serverId
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     *
     * @return List<KatServer>
     */
    @Path(value = "/{serverId}/job/{pidJob}/state-job/{status}")
    @Consumes("application/json")
    @Produces("application/json")
    @PUT
    String changeJobStatus(@PathParam(value = "serverId") String serverId,
                        @PathParam(value = "pidJob") String pidJob,
                           @PathParam(value = "status") String status,
                           @Context HttpHeaders headers);

    /**
     */
    @Path(value = "/{serverId}/job/{pidJob}/alert-mail")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    String getJobAlertMail(@PathParam(value = "serverId") String serverId,
                           @PathParam(value = "pidJob") String pidJob,
                           @Context HttpHeaders headers);

    /**
     */
    @Path(value = "/{serverId}/jvm/{pidJob}/params")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    String getJobJvmOptions(@PathParam(value = "serverId") String serverId,
                           @PathParam(value = "pidJob") String pidJob,
                           @Context HttpHeaders headers);

    /**
     */
    @Path(value = "/{serverId}/job/{pidJob}/overrided-context")
    @Consumes("application/json")
    @Produces("application/json")
    @GET
    String getContexts(@PathParam(value = "serverId") String serverId,
                       @PathParam(value = "pidJob") String pidJob,
                       @Context HttpHeaders headers);

    /**
     */
    @Path(value = "/{serverId}/job/{pidJob}/overrided-context")
    @Consumes("application/json")
    @Produces("application/json")
    @DELETE
    String deleteContexts(@PathParam(value = "serverId") String serverId,
                          @PathParam(value = "pidJob") String pidJob,
                          @Context HttpHeaders headers);

    /**
     */
    @Path(value = "/{serverId}/overrided-context")
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    String postContext(@PathParam(value = "serverId") String serverId,
                       OverridedContext oContext, @Context HttpHeaders headers);


    /**
     */
    @Path(value = "/{serverId}/alert-mail")
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    String postJobAlertMail(@PathParam(value = "serverId") String serverId,
                            MailNotif data,
                            @Context HttpHeaders headers);

    /**
     */
    @Path(value = "/{serverId}/jvm/params")
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    String postJobJVMOptions(@PathParam(value = "serverId") String serverId,
                             JvmOptions data,
                             @Context HttpHeaders headers);

    /**@param serverId
     * Update server attributes identified by serverId
     * If you want uniquely the servers who are not in clusters,  use parameter lonely
     *
     * @return List<KatServer>
     */
    @Path(value = "/{serverId}")
    @Consumes("application/json")
    @Produces("application/json")
    @PUT
    KatServer updateServerAttributes(@PathParam(value = "serverId") String serverId, KatServer server);

    /**
     *
     * @param server
     * @return
     */
    @Path(value = "")
    @POST
    @Consumes("application/json")
    public KatServer add(KatServer server);

    /**
     * Suppression d'un serveur sur un environnement sans supprimer le serveur en lui même
     *
     * @param serverId
     */
    @Path(value = "/{serverId}/{environnementId}")
    @DELETE
    public void delete(@PathParam(value = "serverId") String serverId,
                       @PathParam(value = "environnementId") String environnementId);

    /**
     * liste des bundles d'un serveur
     * @param serverId
     * @param type
     * @return
     */
    @Path(value = "/{serverId}/bundles")
    @GET
    @Produces("application/json")
    public List<KatBundle> getBundles(@PathParam(value = "serverId") String serverId, @QueryParam("type") String type);

    @Path(value="/{serverId}/bundle/{idBundle}")
    @GET
    public String getBundle(@PathParam(value = "serverId") String serverId,
                                @PathParam(value = "idBundle") String bundleId,
                                @QueryParam(value = "cellarGroupName") String cellarGroupName);

    @Path(value="/{serverId}/bundle/{idBundle}")
    @POST
    public String installBundle(@PathParam(value = "serverId") String serverId,
                                @PathParam(value = "idBundle") String bundleId,
                                @QueryParam(value = "cellarGroupName") String cellarGroupName);

    @Path(value="/{serverId}/bundle/{idBundle}")
    @DELETE
    public String uninstallBundle(@PathParam(value = "serverId") String serverId,
                                  @PathParam(value = "idBundle") String bundleId,
                                  @QueryParam(value = "cellarGroupName") String cellarGroupName);

    /**
     *
     * @param serverId
     * @param bundleId
     * @param cellarGroupName
     * @return
     */
    @POST
    @Path("/{serverId}/bundle/{idBundle}/start")
    @Produces("application/json")
    public String startBundle(@PathParam(value = "serverId") String serverId,
                              @PathParam(value = "idBundle") String bundleId,
                              @QueryParam(value = "cellarGroupName") String cellarGroupName);

    /**
     *
     * @param serverId
     * @param bundleId
     * @param cellarGroupName
     * @return
     */
    @POST
    @Path("/{serverId}/bundle/{idBundle}/stop")
    @Produces("application/json")
    public String stopBundle(@PathParam(value = "serverId") String serverId,
                              @PathParam(value = "idBundle") String bundleId,
                              @QueryParam(value = "cellarGroupName") String cellarGroupName);

    @Path(value="/{serverId}/bundle/{idBundle}/restart")
    @POST
    public String restartBundle(@PathParam(value = "serverId") String serverId,
                                @PathParam(value = "idBundle") String bundleId);

    @Path(value ="/{serverId}/features")
    @GET
    public List<KatFeature> getFeatures (@PathParam(value = "serverId") String serverId);

    @GET
    @Path(value ="/{serverId}/status")
    @Produces("application/json")
    public KatServer getStatus (@PathParam(value = "serverId") String serverId, @Context HttpHeaders headers);

    @GET
    @Path(value ="/{serverId}/stats")
    @Produces("application/json")
    public PhysicalStats getStats (@PathParam(value = "serverId") String serverId);

    @GET
    @Path(value ="/{serverId}/job/{jobName}/version/{version}")
    @Produces("application/json")
    public String getIsDeployedArtifact (@PathParam(value = "serverId") String serverId,
                                                @PathParam(value = "jobName") String jobName,
                                                @PathParam(value = "version") String version,
                                         @Context HttpHeaders headers);
    @GET
    @Path(value ="/{serverId}/workflow-deployed")
    @Produces("application/json")
    public String getDeployedWorkflow (@PathParam(value = "serverId") String serverId,
                                         @Context HttpHeaders headers);



}