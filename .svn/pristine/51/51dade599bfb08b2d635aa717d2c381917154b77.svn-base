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
import com.idfor.kat.tools.UIBackend.model.KatServer;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.ArrayList;
import java.util.List;

public class BrokerRepository {

    public List<KatBroker> findAll(){

        List<KatBroker> brokerList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        Iterable<Vertex> all = graph.getVerticesOfClass("Broker");
        for (Vertex vert : all) {
            KatBroker e = KatBroker.vertexToBroker((OrientVertex)vert);
            if (e != null)
                brokerList.add(e);
        }
        graph.shutdown();
        return brokerList;
    }

    public List<KatBroker> findAllWithoutOneEnvironment(String envId){

        List<KatBroker> brokerList = new ArrayList<>();
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Iterable<Vertex> brokers = graph.command(new OCommandSQL("SELECT FROM Broker WHERE in('KatEnvironment-contains-Broker') NOT IN [" + envId + "]")).execute();

        for (Vertex vert : brokers) {
            KatBroker b = KatBroker.vertexToBroker((OrientVertex)vert);
            if (b != null)
                brokerList.add(b);
        }

        graph.shutdown();
        return brokerList;
    }


    public List<KatBroker> findLonelyByEnvironment(String environmentId){
        List<KatBroker> brokerList = new ArrayList<>();
        OrientGraph graph = Orientdb.getOrientGraphTx();
        Iterable<Vertex> brokers = graph.command(new OCommandSQL(
                "SELECT FROM " +
                        "(" +
                        "SELECT expand(out('KatEnvironment-contains-Broker')) FROM " + environmentId +
                        ")" +
                        "WHERE " +
                        "in('KatCluster-contains-Broker') NOT IN " +
                        "(" +
                        "SELECT out('KatEnvironment-contains-KatCluster') FROM "+ environmentId + "" +
                        ")")).execute();

        for (Vertex vert : brokers) {
            KatBroker b = KatBroker.vertexToBroker((OrientVertex)vert);
            if (b != null){
                brokerList.add(b);
            }
        }

        graph.shutdown();
        return brokerList;
    }

    public List<KatBroker> findAllByClusterId(String id){
        List<KatBroker> brokerList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex cluster = graph.getVertex(id);

        for(Edge e : cluster.getEdges(Direction.OUT, "KatCluster-contains-Broker")){
            OrientVertex broker = (OrientVertex) e.getVertex(Direction.IN);
            KatBroker b = KatBroker.vertexToBroker(broker);
            if (b != null)
                brokerList.add(b);
        }

        graph.shutdown();
        return brokerList;
    }

    public KatBroker findOneById(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        KatBroker b = KatBroker.vertexToBroker(graph.getVertex(id));

        graph.shutdown();

        return b;
    }

    public List<KatBroker> findAllByEnvironmentId(String environmentId) {

            return this.findLonelyByEnvironment(environmentId);
    }

    public OrientVertex findOneVertexById(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex vertex = graph.getVertex(id);

        graph.shutdown();

        return vertex;
    }

    public KatBroker save(KatBroker b, String envId){
        OrientVertex v = null;

        OrientGraph graph = Orientdb.getOrientGraphTx();

        if (b.getId() != null){     /* Update */

            v = graph.getVertex(b.getId());
            if (!v.getType().toString().equals("Broker")){
                v = null;
            }
        }
        if (v == null){             /* Create */

            v = graph.addVertex("class:Broker");

            OrientVertex environment = graph.getVertex(envId);

            if (environment == null){
                return null;
            }

            graph.addEdge("KatEnvironment-contains-Broker", environment, v, "KatEnvironment-contains-Broker");
        }

        v = b.toVertex(v);
        graph.commit();

        KatBroker res = KatBroker.vertexToBroker(v);

        graph.shutdown();

        return res;
    }

    public void addExistingBrokerToEnvironment(String brokerId, String environmentId){

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex environment = graph.getVertex(environmentId);
        OrientVertex broker = graph.getVertex(brokerId);

        if (broker == null || environment == null){
            return;
        }

        graph.addEdge("KatEnvironment-contains-Broker", environment, broker, "KatEnvironment-contains-Broker");

        graph.commit();
        graph.shutdown();
    }

    public void addExistingBrokerToEnvironmentAndCluster(String brokerId, String environmentId, String clusterId){

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex environment = graph.getVertex(environmentId);
        OrientVertex broker = graph.getVertex(brokerId);
        OrientVertex cluster = graph.getVertex(clusterId);

        if (broker == null || environment == null || cluster == null){
            return;
        }

        graph.addEdge("KatEnvironment-contains-Broker", environment, broker, "KatEnvironment-contains-Broker");
        graph.addEdge("KatCluster-contains-Broker", cluster, broker, "KatCluster-contains-Broker");

        graph.commit();
        graph.shutdown();
    }

    public void delete(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex v = graph.getVertex(id);
        if (v != null) {
            for (Edge e : v.getEdges(Direction.BOTH, "KatCluster-contains-Broker")) {
                graph.removeEdge(e);
            }
            for (Edge e : v.getEdges(Direction.BOTH, "KatEnvironment-contains-Broker")) {
                graph.removeEdge(e);
            }

            graph.removeVertex(v);

            graph.commit();
        }

        graph.shutdown();
    }

    public void remove(String id, String environmentId) {
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex broker = graph.getVertex(id);
        Vertex environment = graph.getVertex(environmentId);

        if (broker == null || environment == null) {
            return;
        }

        boolean existingElsewhere = false;

        for (Edge e : broker.getEdges(Direction.BOTH, "KatEnvironment-contains-Broker")) {
            if (e.getVertex(Direction.OUT).getId().toString().equals(environmentId)){
                graph.removeEdge(e);
            }
            else {
                existingElsewhere = true;
            }
        }

        if (!existingElsewhere){
            graph.removeVertex(broker);
        }

        graph.commit();

        graph.shutdown();
    }
}
