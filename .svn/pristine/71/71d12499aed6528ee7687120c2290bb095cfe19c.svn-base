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

import com.idfor.kat.tools.UIBackend.dao.ClusterRepository;
import com.idfor.kat.tools.UIBackend.dao.EnvironmentRepository;
import com.idfor.kat.tools.UIBackend.dao.ServerRepository;
import com.idfor.kat.tools.UIBackend.model.KatBroker;
import com.idfor.kat.tools.UIBackend.model.KatCluster;
import com.idfor.kat.tools.UIBackend.model.KatEnvironment;
import com.idfor.kat.tools.UIBackend.model.KatServer;

import com.idfor.kat.tools.UIBackend.service.utils.Reflector;
import org.apache.cxf.common.util.StringUtils;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;

public class KatRestEnvironmentImpl implements KatRestEnvironment {
    @Context
    private HttpHeaders headers;

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(KatRestEnvironment.class);

    private EnvironmentRepository environmentRepository = new EnvironmentRepository();
    private ClusterRepository clusterRepository = new ClusterRepository();
    private ServerRepository serverRepository = new ServerRepository();

    @Override
    public List<KatEnvironment> getEnvironments() {
        List<KatEnvironment> katEnvironments = environmentRepository.findAll();
        Reflector ref = Reflector.handle(katEnvironments);
        return katEnvironments;
    }

    @Override
    public KatEnvironment createEnvironment(KatEnvironment katEnvironment) {
        return environmentRepository.save(katEnvironment);
    }


    @Override
    public KatEnvironment updateEnvironment(String environmentId, KatEnvironment katEnvironment) {

        if(StringUtils.isEmpty(katEnvironment.getId()))
            katEnvironment.setId(environmentId);

        return environmentRepository.save(katEnvironment);

    }

    @Override
    public void deleteEnvironment(String id) {
        environmentRepository.delete(id);
    }

    @Override
    public KatEnvironment getEnvironmentById(String environmentId) {
        return environmentRepository.findOneById(environmentId);
    }

    @Override
    public List<KatCluster> getClustersByEnvironment(String environmentId) {

        return clusterRepository.findAllByEnvironmentId(environmentId);
    }

    @Override
    public List<KatServer> getServer(String environmentId) {
        return serverRepository.findAllByEnvironmentId(environmentId);
    }

    @Override
    public KatServer addServer(KatServer server, String environmentId) {
        return serverRepository.save(server,environmentId);
    }

    @Override
    public void deleteServer(String id, String environmentId) {

    }

    @Override
    public List<KatServer> getServersByEnvironmentAndCluster(String environmentId, String clusterId) {
        return null;
    }

    @Override
    public List<KatBroker> getBrokersByEnvironment(String environmentId) {
        return null;
    }




}
