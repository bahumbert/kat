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

import com.idfor.kat.tools.UIBackend.model.EdgeClass;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import java.util.ArrayList;
import java.util.List;

public class EdgeRepository {

    public List<EdgeClass> findAllEdges(){
        List<EdgeClass> edgeList = new ArrayList<>();
        OrientGraphNoTx graph = Orientdb.getOrientGraphNoTx();

        for (Edge e : graph.getEdges()){
            edgeList.add(EdgeClass.edgeToEdgeClass(e));
        }

        graph.shutdown();

        return edgeList;

    }
}
