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


import com.idfor.kat.tools.UIBackend.model.*;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;

public class ContextRepository {
    public static final Logger logger = LoggerFactory.getLogger(EnvironmentRepository.class);


    public List<Context> findAll() {

        List<Context> iamContextList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();
        Iterable<Vertex> all = graph.getVerticesOfClass("KatContext");
        for (Vertex vert : all) {
            Context context = Context.vertexToContext((OrientVertex) vert);
            if (context != null)
                iamContextList.add(context);
        }
        graph.shutdown();
        return iamContextList;
    }

    public Context save(Context ct) {
        logger.info("Save context start");
        OrientVertex v = null;
        OrientGraph graph = Orientdb.getOrientGraphTx();

        if (ct.getId() != null) {     /* Update */
            logger.info("Save context UPDATE");
            v = graph.getVertex(ct.getId());
            if (!v.getType().toString().equals("KatContext")) {
                v = null;
            }
        }

        if (v == null) {             /* Create */
            logger.info("Save environment CREATE");
            v = graph.addVertex("class:KatContext");
        }

        v = ct.toVertex(v);
        graph.commit();
        Context res = Context.vertexToContext(v);
        graph.shutdown();

        logger.info("Server Save Stop");
        return res;
    }

    public void delete(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex v = graph.getVertex(id);
        if (v != null) {
     /*       for (Edge e : v.getEdges(Direction.BOTH, "KatCluster-contains-KatServer")) {
                graph.removeEdge(e);
            }
            for (Edge e : v.getEdges(Direction.BOTH, "KatEnvironment-contains-KatServer")) {
                graph.removeEdge(e);
            }*/

            graph.removeVertex(v);

            graph.commit();
        }
        graph.shutdown();
    }

}
