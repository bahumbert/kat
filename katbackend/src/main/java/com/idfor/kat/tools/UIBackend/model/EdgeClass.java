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

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;

public class EdgeClass {
    private String id;
    private String inVertexId;
    private String outVertexId;

    public EdgeClass(String id, String inVertexId, String outVertexId) {
        this.id = id;
        this.inVertexId = inVertexId;
        this.outVertexId = outVertexId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInVertexId() {
        return inVertexId;
    }

    public void setInVertexId(String inVertexId) {
        this.inVertexId = inVertexId;
    }

    public String getOutVertexId() {
        return outVertexId;
    }

    public void setOutVertexId(String outVertexId) {
        this.outVertexId = outVertexId;
    }

    public Edge toEdge(EdgeClass ec){

        return null;
    }

    public static EdgeClass edgeToEdgeClass(Edge e){
        if (e != null) {
            return new EdgeClass(e.getId().toString(), e.getVertex(Direction.IN).getId().toString(), e.getVertex(Direction.OUT).getId().toString());
        }
        return null;
    }
}
