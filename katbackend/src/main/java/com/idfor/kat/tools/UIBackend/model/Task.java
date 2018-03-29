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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Task {
    private String id;
    private String name;
    private String artifactUrl;
    private Task[] success;
    private Task[] error;
    private Task[] then;


    public static Task vertexToTask(OrientVertex v) {
        if (v != null) {

            String id = v.getId().toString();
            String name = (String) v.getProperty("name");
            String artifactUrl = (String) v.getProperty("artifactUrl");


            return new Task(name, id, artifactUrl);
        }

        return null;
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


    public Task[] getSuccess() {
        return success;
    }

    public void setSuccess(Task[] success) {
        this.success = success;
    }

    public Task[] getError() {
        return error;
    }

    public void setError(Task[] error) {
        this.error = error;
    }

    public Task[] getThen() {
        return then;
    }

    public void setThen(Task[] then) {
        this.then = then;
    }

    public String getArtifactUrl() {
        return artifactUrl;
    }

    public void setArtifactUrl(String artifactUrl) {
        this.artifactUrl = artifactUrl;
    }


    public OrientVertex toVertex(OrientVertex v){

        if (v == null){
            return null;
        }

        Map<String,Object> props = new HashMap<String,Object>();

        if (this.getName() != null){
            props.put("name", this.getName());
        }

        if (this.getArtifactUrl() != null){
            props.put("artifactUrl", this.getArtifactUrl());
        }


        v.setProperties(props);

        return v;
    }



    public Task() {
       super();
    }

    public Task(String name,String id,String artifactUrl) {
        this.name = name;
        this.id = id;
        this.artifactUrl = artifactUrl;
    }

    public void razSuccessChildrenFromList(ArrayList<Task> list) {
        this.success = list.toArray(new Task[ list.size() ]);
    }

    public void razErrorChildrenFromList(ArrayList<Task> list) {
        this.error = list.toArray(new Task[ list.size() ]);
    }

    public void razThenChildrenFromList(ArrayList<Task> list) {
        this.then = list.toArray(new Task[ list.size() ]);
    }

    public Task(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Task(String id, String name, Task[] success, Task[] error, Task[] then, String creator) {
        this.id = id;
        this.name = name;
        this.success = success;
        this.error = error;
        this.then = then;
    }


}
