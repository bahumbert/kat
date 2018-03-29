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

import com.idfor.kat.tools.UIBackend.model.KatServer;
import com.idfor.kat.tools.UIBackend.model.Task;
import com.idfor.kat.tools.UIBackend.model.Workflow;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    public static final Logger logger = LoggerFactory.getLogger(ServerRepository.class);



    public Task save(Task w) {
        OrientVertex v = null;
        OrientGraph graph = Orientdb.getOrientGraphTx();
        v = graph.addVertex("class:KatTask");
        v = w.toVertex(v);
        graph.commit();
        graph.commit();
        Task res = Task.vertexToTask(v);
        graph.shutdown();

        return res;
    }

    public void addTaskSuccessNode(String workflow1id, String workflow2id) {
        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex w1 = graph.getVertex(workflow1id);
        OrientVertex w2 = graph.getVertex(workflow2id);

        graph.addEdge("KatTask-contains-KatTaskSuccess", w1, w2, "KatTask-contains-KatTaskSuccess");

        graph.commit();
        graph.shutdown();
    }

    public void addTaskErrorNode(String workflow1id, String workflow2id) {
        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex w1 = graph.getVertex(workflow1id);
        OrientVertex w2 = graph.getVertex(workflow2id);

        graph.addEdge("KatTask-contains-KatTaskError", w1, w2, "KatTask-contains-KatTaskError");

        graph.commit();
        graph.shutdown();
    }

    public void addTaskThenNode(String workflow1id, String workflow2id) {
        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex w1 = graph.getVertex(workflow1id);
        OrientVertex w2 = graph.getVertex(workflow2id);

        graph.addEdge("KatTask-contains-KatTaskThen", w1, w2, "KatTask-contains-KatTaskThen");

        graph.commit();
        graph.shutdown();
    }

    public List<Task> findByWorkflowId(String workflowId) {
        List<Task> taskList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex cluster = graph.getVertex(workflowId);

        for(Edge e : cluster.getEdges(Direction.OUT, "KatWorkflow-contains-Task")){
            OrientVertex task = (OrientVertex) e.getVertex(Direction.IN);
            Task s = Task.vertexToTask(task);
            if (s != null)
                taskList.add(s);
        }

        graph.shutdown();
        return taskList;
    }

    public List<Task> findByTaskStatusId(String taskStatusId,String status) {
        List<Task> taskList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        OrientVertex cluster = graph.getVertex(taskStatusId);

        if(cluster != null) {
            for (Edge e : cluster.getEdges(Direction.OUT, "KatTask-contains-KatTask" + status)) {
                OrientVertex task = (OrientVertex) e.getVertex(Direction.IN);
                Task s = Task.vertexToTask(task);
                if (s != null)
                    taskList.add(s);
            }
        }

        graph.shutdown();
        return taskList;
    }

    public void delete(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex v = graph.getVertex(id);
        if (v != null) {
            for (Edge e : v.getEdges(Direction.BOTH, "KatTask-contains-KatTaskSuccess")) {
                graph.removeEdge(e);
            }

            for (Edge e : v.getEdges(Direction.BOTH, "KatTask-contains-KatTaskError")) {
                graph.removeEdge(e);
            }


            for (Edge e : v.getEdges(Direction.BOTH, "KatTask-contains-KatTaskThen")) {
                graph.removeEdge(e);
            }


            graph.removeVertex(v);

            graph.commit();
        }
        graph.shutdown();
    }



}