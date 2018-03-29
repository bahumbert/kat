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

/**
 * Created by bhumbert on 20/04/2017.
 */

import com.idfor.kat.tools.UIBackend.model.KatServer;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.List;

public class ServerRepository {
    public static final Logger logger = LoggerFactory.getLogger(ServerRepository.class);

    public List<KatServer> findAll(){

        List<KatServer> katServerList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        Iterable<Vertex> all = graph.getVerticesOfClass("KatServer");
        for (Vertex vert : all) {
            KatServer s = KatServer.vertexToServer((OrientVertex)vert);
            if (s != null)
                katServerList.add(s);
        }
        graph.shutdown();
        return katServerList;
    }

    public List<KatServer> findAllLonely(){

        List<KatServer> katServerList = new ArrayList<>();
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Iterable<Vertex> servers = graph.command(new OCommandSQL("SELECT FROM KatServer WHERE IN('KatCluster-contains-KatServer').SIZE() = 0")).execute();


        for (Vertex vert : servers) {
            KatServer s = KatServer.vertexToServer((OrientVertex)vert);
            if (s != null)
                katServerList.add(s);
        }

        graph.shutdown();
        return katServerList;
    }

    public List<KatServer> findAllWithoutOneEnvironment(String envId){

        List<KatServer> katServerList = new ArrayList<>();
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Iterable<Vertex> servers = graph.command(new OCommandSQL("SELECT FROM KatServer WHERE in('KatEnvironment-contains-KatServer') NOT IN [" + envId + "]")).execute();

        for (Vertex vert : servers) {
            KatServer s = KatServer.vertexToServer((OrientVertex)vert);
            if (s != null)
                katServerList.add(s);
        }

        graph.shutdown();
        return katServerList;
    }

    public List<KatServer> findLonelyByEnvironment(String environmentId){
        List<KatServer> katServerList = new ArrayList<>();
        OrientGraph graph = Orientdb.getOrientGraphTx();
        Iterable<Vertex> servers = graph.command(new OCommandSQL(
                "SELECT FROM " +
                        "(" +
                            "SELECT expand(out('KatEnvironment-contains-KatServer')) FROM " + environmentId +
                        ")" +
                      "WHERE " +
                        "in('KatCluster-contains-KatServer') NOT IN " +
                        "(" +
                            "SELECT out('KatEnvironment-contains-KatCluster') FROM "+ environmentId + "" +
                        ")")).execute();

        for (Vertex vert : servers) {
            KatServer s = KatServer.vertexToServer((OrientVertex)vert);
            if (s != null){
                katServerList.add(s);
            }
        }

        graph.shutdown();
        return katServerList;
    }

    public List<KatServer> findAllByClusterId(String id){
        List<KatServer> katServerList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex cluster = graph.getVertex(id);

        for(Edge e : cluster.getEdges(Direction.OUT, "KatCluster-contains-KatServer")){
            OrientVertex server = (OrientVertex) e.getVertex(Direction.IN);
            KatServer s = KatServer.vertexToServer(server);
            if (s != null)
                katServerList.add(s);
        }

        graph.shutdown();
        return katServerList;
    }

    public List<KatServer> findAllByEnvironmentIdAndClusterId(String environmentId, String clusterId){
        List<KatServer> katServerList = new ArrayList<>();
        List<KatServer> tmp = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();
        Vertex cluster = graph.getVertex(clusterId);

        if (cluster == null){
            graph.shutdown();
           return this.findLonelyByEnvironment(environmentId);
        }

        Iterable<Vertex> servers = graph.command(new OCommandSQL(
                "SELECT FROM " +
                        "(" +
                        "SELECT expand(out('KatEnvironment-contains-KatServer')) FROM " + environmentId +
                        ")" +
                        "WHERE " +
                        "in('KatCluster-contains-KatServer') IN " +
                        "[" + clusterId + "]")).execute();

        for (Vertex vert : servers) {
            KatServer s = KatServer.vertexToServer((OrientVertex)vert);
            if (s != null){
                katServerList.add(s);
            }
        }

        graph.shutdown();
        return katServerList;
    }

    public List<KatServer> findAllByEnvironmentId(String environmentId){
        List<KatServer> katServerList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();
        OCommandSQL sql;
        sql = new OCommandSQL("SELECT expand(in) FROM `KatEnvironment-contains-KatServer` WHERE  out= " + environmentId );

        Iterable<Vertex> servers = graph.command(sql).execute();

        for (Vertex vert : servers) {
            KatServer s = KatServer.vertexToServer((OrientVertex)vert);
            if (s != null){
                katServerList.add(s);
            }
        }

        graph.shutdown();
        return katServerList;
    }

    public OrientVertex findOneVertexById(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex vertex = graph.getVertex(id);

        graph.shutdown();

        return vertex;
    }

    public KatServer findOneById(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        KatServer s = KatServer.vertexToServer(this.findOneVertexById(id));

        graph.shutdown();

        return s;
    }

    public KatServer save(KatServer s) {
        return this.save(s, null);
    }

    public KatServer save(KatServer s, String environmentId){
        logger.info("Server Save Start");
        OrientVertex v = null;
        OrientGraph graph = Orientdb.getOrientGraphTx();

        if (s.getId() != null){     /* Update */
            logger.info("Server UPDATE");
            v = graph.getVertex(s.getId());
            if (!v.getType().toString().equals("KatServer")){
                v = null;
            }
        }

        if (v == null){             /* Create */
            logger.info("Server CREATE");

            OrientVertex environment = graph.getVertex(environmentId);
            if (environment == null){
                logger.warn("Server CREATE : envionment inconnu");
                return null;
            }
            v = graph.addVertex("class:KatServer");
            graph.addEdge("KatEnvironment-contains-KatServer", environment, v, "KatEnvironment-contains-KatServer");
        }

        v = s.toVertex(v);
        graph.commit();
        KatServer res = KatServer.vertexToServer(v);
        graph.shutdown();

        logger.info("Server Save Stop");
        return res;
    }

    public void addExistingServerToEnvironment(String serverId, String environmentId){

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex environment = graph.getVertex(environmentId);
        OrientVertex server = graph.getVertex(serverId);

        if (server == null || environment == null){
            return;
        }


        graph.addEdge("KatEnvironment-contains-KatServer", environment, server, "KatEnvironment-contains-KatServer");

        graph.commit();
        graph.shutdown();
    }

    public void addExistingServerToEnvironmentAndCluster(String serverId, String environmentId, String clusterId){

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex environment = graph.getVertex(environmentId);
        OrientVertex server = graph.getVertex(serverId);
        OrientVertex cluster = graph.getVertex(clusterId);

        if (server == null || environment == null || cluster == null){
            return;
        }

        graph.addEdge("KatEnvironment-contains-KatServer", environment, server, "KatEnvironment-contains-KatServer");
        graph.addEdge("KatCluster-contains-KatServer", cluster, server, "KatCluster-contains-KatServer");

        graph.commit();
        graph.shutdown();
    }

    public void delete(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex v = graph.getVertex(id);
        if (v != null) {
            for (Edge e : v.getEdges(Direction.BOTH, "KatCluster-contains-KatServer")) {
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

    public void remove(String id, String environmentId, String clusterId){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex server = graph.getVertex(id);
        Vertex environment = graph.getVertex(environmentId);
        Vertex cluster = null;
        if (clusterId != null)
            cluster = graph.getVertex(clusterId);

        if (server == null || environment == null) {
            return;
        }

        boolean existingElsewhere = false;

        if (cluster != null){
            for (Edge e : server.getEdges(Direction.BOTH, "KatCluster-contains-KatServer")) {
                if (e.getVertex(Direction.OUT).getId().toString().equals(clusterId)){
                    graph.removeEdge(e);
                }
                else {
                    existingElsewhere = true;
                }
            }
        }

        for (Edge e : server.getEdges(Direction.BOTH, "KatEnvironment-contains-KatServer")) {
            if (e.getVertex(Direction.OUT).getId().toString().equals(environmentId)){
                graph.removeEdge(e);
            }
            else {
                existingElsewhere = true;
            }
        }

        if (!existingElsewhere){
            graph.removeVertex(server);
        }

        graph.commit();

        graph.shutdown();
    }
}