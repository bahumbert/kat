/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
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

import com.idfor.kat.tools.UIBackend.model.Workflow;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WorkflowRepository {
    public static final Logger logger = LoggerFactory.getLogger(ServerRepository.class);

    public Workflow findOneById(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Workflow w = Workflow.vertexToWorkflow(this.findOneVertexById(id));
        graph.shutdown();

        return w;
    }

    public List<Workflow> findAll(){

        List<Workflow> workflowList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        Iterable<Vertex> all = graph.getVerticesOfClass("KatWorkflow");
        for (Vertex vert : all) {
            Workflow w = Workflow.vertexToWorkflow((OrientVertex)vert);
            if (w != null)
                workflowList.add(w);
        }
        graph.shutdown();

        return workflowList;
    }

    public Workflow save(Workflow w) {
        OrientVertex v = null;
        OrientGraph graph = Orientdb.getOrientGraphTx();
        v = graph.addVertex("class:KatWorkflow");
        v = w.toVertex(v);
        graph.commit();
        graph.commit();
        Workflow res = Workflow.vertexToWorkflow(v);
        graph.shutdown();

        return res;
    }

    public void addTaskNode(String workflow1id, String taskId) {
        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex w1 = graph.getVertex(workflow1id);
        OrientVertex w2 = graph.getVertex(taskId);

        graph.addEdge("KatWorkflow-contains-Task", w1, w2, "KatWorkflow-contains-Task");

        graph.commit();
        graph.shutdown();
    }

    public OrientVertex findOneVertexById(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex vertex = graph.getVertex(id);

        graph.shutdown();

        return vertex;
    }

    public void delete(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex v = graph.getVertex(id);
        if (v != null) {
            for (Edge e : v.getEdges(Direction.BOTH, "KatWorkflow-contains-Task")) {
                graph.removeEdge(e);
            }

            graph.removeVertex(v);

            graph.commit();
        }
        graph.shutdown();
    }


}