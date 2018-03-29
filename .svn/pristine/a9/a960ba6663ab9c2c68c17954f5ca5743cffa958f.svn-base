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

import com.idfor.kat.tools.UIBackend.model.KatServer;
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

import javax.management.relation.Role;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.List;

public class IamRoleRepository {
    public static final Logger logger = LoggerFactory.getLogger(ServerRepository.class);

    public List<IamRole> findAll(){
        List<IamRole> rolesList = new ArrayList<>();

        OrientGraph graph = Orientdb.getOrientGraphTx();

        Iterable<Vertex> all = graph.getVerticesOfClass("KatIamRole");
        for (Vertex vert : all) {

            IamRole iamRole = IamRole.vertexToIamRole((OrientVertex)vert);
            if (iamRole != null)
                rolesList.add(iamRole);
        }
        graph.shutdown();
        return rolesList;
    }

    public IamRole findOneByLabel(String label) {
        OrientGraphNoTx graph = Orientdb.getOrientGraphNoTx();
        OrientVertex v = graph.command(new OCommandSQL("select * from `KatIamRole` WHERE label = '"+ label +"'")).execute();
        return IamRole.vertexToIamRole(v);
    }

    public IamRole save(IamRole iamRole) {
        OrientVertex v = null;
        OrientGraph graph = Orientdb.getOrientGraphTx();

        if (iamRole.getId() != null) {     /* Update */
            v = graph.getVertex(iamRole.getId());
            if (!v.getType().toString().equals("KatIamRole")) {
                v = null;
            }
        }

        if (v == null) {             /* Create */
            v = graph.addVertex("class:KatIamRole");
        }

        v = iamRole.toVertex(v);
        graph.commit();
        IamRole res = IamRole.vertexToIamRole(v);
        graph.shutdown();

        return res;
    }

    public Vertex findVertexById(String id){
        OrientGraph graph = Orientdb.getOrientGraphTx();
        Vertex vertex = graph.getVertex(id);

        return vertex;
    }
}