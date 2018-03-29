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

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.HashMap;
import java.util.Map;

public class Workflow {

    String id;
    String name;
    Task task;

    public Workflow(String id, String name, Task task) {
        this.id = id;
        this.name = name;
        this.task = task;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public static Workflow vertexToWorkflow(OrientVertex v) {
        if (v != null) {

            String id = v.getId().toString();
            String name = (String) v.getProperty("name");

            return new Workflow(name, id);
        }

        return null;
    }

    public Workflow(String name, String id) {
        this.name = name;
        this.id = id;
    }

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

    public Workflow() {
        super();
    }


}
