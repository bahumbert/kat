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


import com.idfor.kat.tools.UIBackend.model.Context;
import com.idfor.kat.tools.UIBackend.model.iam.IamContextGroup;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IamContextGroupRepository {
    public static final Logger logger = LoggerFactory.getLogger(EnvironmentRepository.class);


    public List<IamContextGroup> findAll() {
        // v = s.toVertex(v);
        List<IamContextGroup> iamContextGroupList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();
        Iterable<Vertex> all = graph.getVerticesOfClass("KatContextGroup");
        for (Vertex vert : all) {
            IamContextGroup iamContextGroup = IamContextGroup.vertexToIamContextGroup((OrientVertex) vert);
            if (iamContextGroup != null)
                iamContextGroupList.add(iamContextGroup);
        }
        graph.shutdown();
        return iamContextGroupList;
    }

    public IamContextGroup save(IamContextGroup ctg) {
        logger.info("Save context start");
        OrientVertex v = null;
        OrientGraph graph = Orientdb.getOrientGraphTx();

        if (ctg.getId() != null) {     /* Update */
            logger.info("Save context UPDATE");
            v = graph.getVertex(ctg.getId());
            if (!v.getType().toString().equals("KatContextGroup")) {
                v = null;
            }
        }

        if (v == null) {             /* Create */
            logger.info("Save environment CREATE");
            v = graph.addVertex("class:KatContextGroup");
        }

        for (Edge e : v.getEdges(Direction.BOTH, "KatContextGroup-contains-KatContext")) {
            graph.removeEdge(e);
        }

        if (ctg.getContexts() != null) {
            for (Context c : ctg.getContexts()) {
                OrientVertex context = graph.getVertex(c.getId());
                graph.addEdge("KatContextGroup-contains-KatContext", v, context, "KatContextGroup-contains-KatContext");
            }
        }
        v = ctg.toVertex(v);
        graph.commit();
        IamContextGroup res = IamContextGroup.vertexToIamContextGroup(v);
        graph.shutdown();

        logger.info("Server Save Stop");
        return res;
    }

    public void delete(String idContextGroup) {
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex v = graph.getVertex(idContextGroup);
        if (v != null) {
            for (Edge e : v.getEdges(Direction.BOTH, "KatContextGroup-contains-KatContext")) {
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

        return vertex;
    }
}
