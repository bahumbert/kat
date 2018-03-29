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
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)

public class KatEnvironment {

    public static final Logger logger = LoggerFactory.getLogger(KatEnvironment.class);
    private String id;
    private String name;


    private boolean editing;

    private List<KatCluster> katClusters;
    private List<KatServer> katServers;



    private List<KatBroker> katBrokers;



    public KatEnvironment() {
    }

    public KatEnvironment(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public KatEnvironment(String id, String name,boolean editing, List<KatCluster> katClusters, List<KatServer> katServers, List<KatBroker> katBrokers) {
        this.id = id;
        this.name = name;
        this.editing = editing;
        this.katClusters = katClusters;
        this.katServers = katServers;
        this.katBrokers = katBrokers;
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

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }


    public List<KatCluster> getKatClusters() {
        return katClusters;
    }

    public void setKatClusters(List<KatCluster> katClusters) {
        this.katClusters = katClusters;
    }

    public List<KatServer> getKatServers() {
        return katServers;
    }

    public void setKatServers(List<KatServer> katServers) {
        this.katServers = katServers;
    }

    public List<KatBroker> getBrokers() {
        return katBrokers;
    }

    public void setBrokers(List<KatBroker> brokers) {
        this.katBrokers = brokers;
    }

    /**
     * @param v
     * @return
     */
    public OrientVertex toVertex(OrientVertex v){

        if (v == null){
            return null;
        }

        Map<String,Object> props = new HashMap<String,Object>();

        if (this.getName() != null){
            props.put("name", this.getName());
        }

        v.setProperties(props);

        return v;
    }

    /**
     * Create an KatEnvironment from OrientDB Vertex
     * @param v
     * @return
     */
    public static KatEnvironment vertexToEnvironment(OrientVertex v) {
        if (v != null && v.getType() != null && v.getType().toString().equals("KatEnvironment")) {

            return new KatEnvironment(v.getId().toString(), (String) v.getProperty("name"));
        }
        logger.error("Vertex not valid in conversion vertex to KatEnvironment");
        return null;
    }
}
