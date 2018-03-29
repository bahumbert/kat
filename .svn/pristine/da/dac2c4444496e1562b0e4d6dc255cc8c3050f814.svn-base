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

package com.idfor.kat.tools.UIBackend.dao;


import com.idfor.kat.tools.UIBackend.model.KatBroker;
import com.idfor.kat.tools.UIBackend.model.KatCluster;
import com.idfor.kat.tools.UIBackend.model.KatServer;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.ArrayList;
import java.util.List;

public class ClusterRepository {

    private ServerRepository serverRepository;

    private BrokerRepository brokerRepository;

    public List<KatCluster> findAll(){

        List<KatCluster> katClusterList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        Iterable<Vertex> all = graph.getVerticesOfClass("KatCluster");
        for (Vertex vert : all) {
            KatCluster c = KatCluster.vertexToCluster((OrientVertex)vert);
            if (c != null)
                katClusterList.add(c);
        }
        graph.shutdown();
        return katClusterList;
    }

    public List<KatCluster> findAllWithoutOneEnvironment(String envId){

        List<KatCluster> katClusterList = new ArrayList<>();
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Iterable<Vertex> clusters = graph.command(new OCommandSQL("SELECT FROM `KatCluster` WHERE in('KatEnvironment-contains-KatCluster') NOT IN [" + envId + "]")).execute();

        for (Vertex vert : clusters) {
            KatCluster c = KatCluster.vertexToCluster((OrientVertex)vert);
            if (c != null)
                katClusterList.add(c);
        }

        graph.shutdown();
        return katClusterList;
    }

    public OrientVertex findOneVertexById(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex vertex = graph.getVertex(id);

        graph.shutdown();

        return vertex;
    }

    public KatCluster findOneById(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        KatCluster c = KatCluster.vertexToCluster(this.findOneVertexById(id));

        graph.shutdown();

        return c;
    }

    public List<KatCluster> findAllByEnvironmentId(String environmentId){
        List<KatCluster> katClusterList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex environment = graph.getVertex(environmentId);

        for(Edge e : environment.getEdges(Direction.OUT, "KatEnvironment-contains-KatCluster")){
            OrientVertex cluster = (OrientVertex) e.getVertex(Direction.IN);
            KatCluster c = KatCluster.vertexToCluster(cluster);
            if (c != null)
                katClusterList.add(c);
        }

        graph.shutdown();
        return katClusterList;
    }

    public KatCluster save(KatCluster c, String environmentId){
        OrientVertex v = null;

        OrientGraph graph = Orientdb.getOrientGraphTx();



        if (c.getId() != null){     /* Update */
            v = graph.getVertex(c.getId());
            if (!v.getType().toString().equals("KatCluster")){
                v = null;
            }
        }
        if (v == null){             /* Create */

            OrientVertex environment = graph.getVertex(environmentId);

            if (environment == null){
                return null;
            }

            v = graph.addVertex("class:KatCluster");

            graph.addEdge("KatEnvironment-contains-KatCluster", environment, v, "KatEnvironment-contains-KatCluster");
        }

        v = c.toVertex(v);

        for (Edge e : v.getEdges(Direction.BOTH, "KatCluster-contains-KatServer")){
            graph.removeEdge(e);
        }
        if (c.getKatServers() != null){
            for (KatServer s : c.getKatServers()){
                Edge e = graph.addEdge("KatCluster-contains-KatServer", v, serverRepository.findOneVertexById(s.getId()), "KatCluster-contains-KatServer");
            }
        }

        for (Edge e : v.getEdges(Direction.BOTH, "KatCluster-contains-Broker")){
            graph.removeEdge(e);
        }
        if (c.getBrokers() != null){
            for (KatBroker b : c.getBrokers()){
                Edge e = graph.addEdge("KatCluster-contains-Broker", v, brokerRepository.findOneVertexById(b.getId()), "KatCluster-contains-Broker");
            }
        }

        graph.commit();
        KatCluster ret = KatCluster.vertexToCluster(v);

        graph.shutdown();


        return ret;
    }

    public void addExistingClusterToEnvironment(String clusterId, String environmentId){

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex environment = graph.getVertex(environmentId);
        OrientVertex cluster = graph.getVertex(clusterId);

        if (cluster == null || environment == null){
            return;
        }

        graph.addEdge("KatEnvironment-contains-KatCluster", environment, cluster, "KatEnvironment-contains-KatCluster");

        graph.commit();
        graph.shutdown();
    }

    public void delete(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex v = graph.getVertex(id);
        for (Edge e : v.getEdges(Direction.BOTH, "KatCluster-contains-KatServer")){
            graph.removeEdge(e);
        }
        for (Edge e : v.getEdges(Direction.BOTH, "KatCluster-contains-Broker")){
            graph.removeEdge(e);
        }
        for (Edge e : v.getEdges(Direction.BOTH, "KatEnvironment-contains-KatCluster")){
            graph.removeEdge(e);
        }

        graph.removeVertex(v);

        graph.commit();
        graph.shutdown();

    }

    public void remove(String id, String environmentId){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex cluster = graph.getVertex(id);
        Vertex environment = graph.getVertex(environmentId);

        if (cluster == null || environment == null){
            return;
        }

        boolean existingElsewhere = false;

        for (Edge e : cluster.getEdges(Direction.BOTH, "KatCluster-contains-KatServer")){
            graph.removeEdge(e);
        }
        for (Edge e : cluster.getEdges(Direction.BOTH, "KatCluster-contains-Broker")){
            graph.removeEdge(e);
        }
        for (Edge e : cluster.getEdges(Direction.BOTH, "KatEnvironment-contains-KatCluster")){
            if (e.getVertex(Direction.OUT).getId().toString().equals(environmentId)){
                graph.removeEdge(e);
            }
            else {
                existingElsewhere = true;
            }
        }

        if (!existingElsewhere){
            graph.removeVertex(cluster);
        }

        graph.commit();
        graph.shutdown();

    }
}
