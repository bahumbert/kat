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


import com.idfor.kat.tools.UIBackend.model.KatCluster;
import com.idfor.kat.tools.UIBackend.model.KatEnvironment;
import com.idfor.kat.tools.UIBackend.model.KatServer;
import com.idfor.kat.tools.UIBackend.model.KatBroker;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;

public class EnvironmentRepository {
    public static final Logger logger = LoggerFactory.getLogger(EnvironmentRepository.class);

    private ClusterRepository clusterRepository;
    private ServerRepository serverRepository;
    private BrokerRepository brokerRepository;

    /**
     *
     * @return
     */
    public List<KatEnvironment> findAll() {

        List<KatEnvironment> katEnvironmentList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();
        Iterable<Vertex> all = graph.getVerticesOfClass("KatEnvironment");
        for (Vertex vert : all) {
            KatEnvironment e = KatEnvironment.vertexToEnvironment((OrientVertex) vert);
            if (e != null)
                katEnvironmentList.add(e);
        }
        graph.shutdown();
        return katEnvironmentList;
    }

    public KatEnvironment findOneById(String id) {

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex vertex = graph.getVertex(id);

        KatEnvironment e = KatEnvironment.vertexToEnvironment(vertex);

        graph.shutdown();

        return e;
    }

    public KatEnvironment save(KatEnvironment en) {
        logger.info("Save environement start");
        OrientVertex v = null;
        OrientGraph graph = Orientdb.getOrientGraphTx();

        if (en.getId() != null) {     /* Update */
            logger.info("Save environment UPDATE");
            v = graph.getVertex(en.getId());
            if (!v.getType().toString().equals("KatEnvironment")) {
                v = null;
            }
        }

        if (v == null) {             /* Create */
            logger.info("Save environment CREATE");
            v = graph.addVertex("class:KatEnvironment");
        }

        for (Edge e : v.getEdges(Direction.BOTH, "KatEnvironment-contains-KatCluster")) {
            graph.removeEdge(e);
        }

        if (en.getKatServers() != null) {
            for (KatCluster c : en.getKatClusters()) {
                Edge e = graph.addEdge("KatEnvironment-contains-KatCluster", v, clusterRepository.findOneVertexById(c.getId()), "KatEnvironment-contains-KatCluster");
            }
        }
        for (Edge e : v.getEdges(Direction.BOTH, "KatEnvironment-contains-KatServer")) {
            graph.removeEdge(e);
        }
        if (en.getKatServers() != null) {
            for (KatServer s : en.getKatServers()) {
                Edge e = graph.addEdge("KatEnvironment-contains-KatServer", v, serverRepository.findOneVertexById(s.getId()), "KatEnvironment-contains-KatServer");
            }
        }
        for (Edge e : v.getEdges(Direction.BOTH, "KatEnvironment-contains-Broker")) {
            graph.removeEdge(e);
        }
        if (en.getKatServers() != null) {
            for (KatBroker b : en.getBrokers()) {
                Edge e = graph.addEdge("KatEnvironment-contains-Broker", v, brokerRepository.findOneVertexById(b.getId()), "KatEnvironment-contains-Broker");
            }
        }

        v = en.toVertex(v);
        graph.commit();

        KatEnvironment res = KatEnvironment.vertexToEnvironment(v);
        logger.info(res.toString());
        graph.shutdown();
        logger.info("Save environment End");

        return res;
    }

    public void delete(String id) {
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex v = graph.getVertex(id);
        if (v != null) {
            for (Edge e : v.getEdges(Direction.BOTH, "KatEnvironment-contains-KatCluster")) {
                graph.removeEdge(e);
            }
            for (Edge e : v.getEdges(Direction.BOTH, "KatEnvironment-contains-KatServer")) {
                graph.removeEdge(e);
            }

            graph.removeVertex(v);

            graph.commit();
        }

        graph.shutdown();
    }


    public Vertex findVertexById(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();
        Vertex vertex = graph.getVertex(id);
        graph.shutdown();

        return vertex;
    }
}
