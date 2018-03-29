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

package com.idfor.kat.tools.UIBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KatCluster {

    private String id;
    private String name;
    private String type;
    private boolean autoUpdate;
    private String cellarGroup;
    private String cellarManager;
    private List<KatServer> katServers;
    private List<KatBroker> brokers;

    public KatCluster() {
    }

    public KatCluster(String name, String type, boolean autoUpdate, String cellarGroup, List<KatServer> katServers) {
        this.name = name;
        this.type = type;
        this.autoUpdate = autoUpdate;
        this.cellarGroup = cellarGroup;
        this.katServers = katServers;
    }

    public KatCluster(String id, String name, String type, boolean autoUpdate, String cellarGroup, String cellarManager, List<KatServer> katServers, List<KatBroker> brokers) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.autoUpdate = autoUpdate;
        this.cellarGroup = cellarGroup;
        this.cellarManager = cellarManager;
        this.katServers = katServers;
        this.brokers = brokers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public String getCellarGroup() {
        return cellarGroup;
    }

    public void setCellarGroup(String cellarGroup) {
        this.cellarGroup = cellarGroup;
    }

    public String getCellarManager() {
        return cellarManager;
    }

    public void setCellarManager(String cellarManager) {
        this.cellarManager = cellarManager;
    }

    public List<KatServer> getKatServers() {
        return katServers;
    }

    public void setKatServers(List<KatServer> katServers) {
        this.katServers = katServers;
    }

    public List<KatBroker> getBrokers() {
        return brokers;
    }

    public void setBrokers(List<KatBroker> brokers) {
        this.brokers = brokers;
    }

    public KatServer findServerByName(String name){
        for (KatServer s : this.getKatServers()){
            if(s.getName().equals(name)){
                return s;
            }
        }
        return null;
    }

    public OrientVertex toVertex(OrientVertex v){

        if (v == null){
            return null;
        }

        Map<String,Object> props = new HashMap<String,Object>();

        if (this.getName() != null){
            props.put("name", this.getName());
        }
        if (this.getType() != null){
            props.put("type", this.getType());
        }

        props.put("autoUpdate", this.isAutoUpdate());

        if (this.getCellarGroup() != null){
            props.put("cellarGroup", this.getCellarGroup());
        }

        if (this.getCellarManager() != null){
            props.put("cellarManager", this.getCellarManager());
        }

        v.setProperties(props);

        return v;
    }

    public static KatCluster vertexToCluster(OrientVertex v){
        if (v != null && v.getType() != null && v.getType().toString().equals("KatCluster")){

            List<KatServer> katServers = new ArrayList<>();
            List<KatBroker> brokers = new ArrayList<>();

            for(Edge e : v.getEdges(Direction.OUT, "KatCluster-contains-KatServer")){
                OrientVertex server = (OrientVertex) e.getVertex(Direction.IN);
                katServers.add(KatServer.vertexToServer(server));
            }

            for(Edge e : v.getEdges(Direction.OUT, "KatCluster-contains-Broker")){
                OrientVertex broker = (OrientVertex) e.getVertex(Direction.IN);
                brokers.add(KatBroker.vertexToBroker(broker));
            }

            return new KatCluster(v.getId().toString(), v.getProperty("name"), v.getProperty("type"), v.getProperty("autoUpdate"), v.getProperty("cellarGroup"),  v.getProperty("cellarManager"), katServers, brokers);
        }
        return null;
    }

    public static List<KatCluster> vertexListToClusterList(List<OrientVertex> vertices){

        List<KatCluster> katClusters = new ArrayList<>();

        for (OrientVertex v : vertices){
            katClusters.add(KatCluster.vertexToCluster(v));
        }

        return katClusters;
    }

}
