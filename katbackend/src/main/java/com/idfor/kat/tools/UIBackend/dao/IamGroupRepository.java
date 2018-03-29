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

/**
 * Created by bhumbert on 20/04/2017.
 */

import com.idfor.kat.tools.UIBackend.model.KatEnvironment;
import com.idfor.kat.tools.UIBackend.model.iam.IamGroup;
import com.idfor.kat.tools.UIBackend.model.iam.IamRole;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class IamGroupRepository {
    public static final Logger logger = LoggerFactory.getLogger(ServerRepository.class);
    IamRoleRepository iamRoleRepository = new IamRoleRepository();
    IamContextGroupRepository iamContextGroupRepository = new IamContextGroupRepository();
    EnvironmentRepository environmentRepository = new EnvironmentRepository();

    public List<IamGroup> findAll() {
        List<IamGroup> iamGroups = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        Iterable<Vertex> all = graph.getVerticesOfClass("KatIamGroup");
        for (Vertex vert : all) {
            IamGroup iamGroup = IamGroup.vertexToIamGroup((OrientVertex) vert);

            for (Edge e : vert.getEdges(Direction.OUT, "KatIamGroup-contains-KatIamRoles")) {
                OrientVertex role = (OrientVertex) e.getVertex(Direction.IN);
                IamRole rol = IamRole.vertexToIamRole(role);
                if (rol != null)
                    iamGroup.setRole(rol);
            }

            if (iamGroup != null)
                iamGroups.add(iamGroup);
        }
        graph.shutdown();
        return iamGroups;
    }

    public IamGroup findOneByLabel(String label) {
        OrientGraphNoTx graph = Orientdb.getOrientGraphNoTx();
        Iterable<Vertex> iv = graph.command(new OCommandSQL("select * from `KatIamGroup` WHERE label = '"+ label +"'")).execute();
        Vertex v = null;
        for (Vertex vertex : iv) {
            v = vertex;
        }

        IamGroup iamGroup = IamGroup.vertexToIamGroup((OrientVertex) v);

        return iamGroup;
    }

    public IamGroup save(IamGroup grp) {

        OrientVertex v = null;
        OrientGraph graph = Orientdb.getOrientGraphTx();

        if (grp.getId() != null) {     /* Update */

            v = graph.getVertex(grp.getId());
            if (!v.getType().toString().equals("KatIamGroup")) {
                v = null;
            }
        }

        if (v == null) {             /* Create */
            v = graph.addVertex("class:KatIamGroup");
        }

        for (Edge e : v.getEdges(Direction.BOTH, "KatIamGroup-contains-KatIamRoles")) {
            graph.removeEdge(e);
        }

        if (grp.getRole() != null) {
            if (grp.getRole().getId() != null) {
                Edge e = graph.addEdge("KatIamGroup-contains-KatIamRoles", v, iamRoleRepository.findVertexById(grp.getRole().getId()), "KatIamGroup-contains-KatIamRoles");
            }
        }

        for (Edge e : v.getEdges(Direction.BOTH, "KatIamGroup-contains-KatContextGroup")) {
            graph.removeEdge(e);
        }

        if (grp.getContextGroup() != null) {
            if (grp.getContextGroup().getId() != null) {
                Edge e = graph.addEdge("KatIamGroup-contains-KatContextGroup", v, iamContextGroupRepository.findVertexById(grp.getContextGroup().getId()), "KatIamGroup-contains-KatContextGroup");
            }
        }

        for (Edge e : v.getEdges(Direction.BOTH, "KatIamGroup-contains-KatEnvironment")) {
            graph.removeEdge(e);
        }


        for (KatEnvironment env : grp.getEnvironments()) {
            if (env.getId() != null) {
                Edge e = graph.addEdge("KatIamGroup-contains-KatEnvironment", v, environmentRepository.findVertexById(env.getId()), "KatIamGroup-contains-KatEnvironment");

            }
        }

        v = grp.toVertex(v);
        IamGroup res = grp.vertexToIamGroup(v);
        graph.commit();

        graph.shutdown();

        return res;
    }

    public void delete(String idContextGroup) {
        OrientGraph graph = Orientdb.getOrientGraphTx();

        Vertex v = graph.getVertex(idContextGroup);
        if (v != null) {
            for (Edge e : v.getEdges(Direction.BOTH, "KatIamGroup-contains-KatIamRoles")) {
                graph.removeEdge(e);
            }


            for (Edge e : v.getEdges(Direction.BOTH, "KatIamGroup-contains-KatContextGroup")) {
                graph.removeEdge(e);
            }


            for (Edge e : v.getEdges(Direction.BOTH, "KatIamGroup-contains-KatEnvironment")) {
                graph.removeEdge(e);
            }

            graph.removeVertex(v);

            graph.commit();
        }

        graph.shutdown();
    }

}