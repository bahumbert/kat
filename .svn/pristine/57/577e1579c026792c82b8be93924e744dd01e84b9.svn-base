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

import com.idfor.kat.tools.UIBackend.model.iam.IamGroup;
import com.idfor.kat.tools.UIBackend.model.iam.IamRole;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Orientdb {

    public static final Logger logger = LoggerFactory.getLogger(Orientdb.class);

    private static String orientDBName = "graph-db";
    private static OrientGraphFactory factory;
    private OServer server;

    public void start(){
        logger.info("KAT Application starting");
        try {
            logger.info("DatabaseServer starting");
            server = OServerMain.create();
            server.startup(Orientdb.class.getClassLoader().getResourceAsStream("db-config.xml"));
            server.activate();
            logger.info("Embedded DB server started.");

            factory = new OrientGraphFactory("plocal:kat_db/" + orientDBName).setupPool(1, 10);
            //factory = new OrientGraphFactory("remote:MyOrientServer/RobotWorld", "admin", "admin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Roles
        ArrayList<IamRole> arrayRoles = new ArrayList<>();

        initDb();
    }

    public void stop(){
        logger.info("End KAT Application");
        factory.close();
        server.shutdown();
    }

    public static OrientGraph getOrientGraphTx(){
        return factory.getTx();
    }

    public static OrientGraphNoTx getOrientGraphNoTx(){
        return factory.getNoTx();
    }

    private static void initDb(){
        IamRoleRepository iamRoleRepository = new IamRoleRepository();
        IamGroupRepository iamGroupRepository = new IamGroupRepository();

        IamRole roleAdmin = null;
        IamRole roleViewer = null;
        IamRole roleSuperAdmin = null;
        IamRole roleDevelopper = null;
        IamRole roleOperator = null;

        OrientGraphNoTx graph = getOrientGraphNoTx();

        if (!graph.getRawGraph().existsCluster("KatServer")){
            graph.createVertexType("KatServer");
        }
        if (!graph.getRawGraph().existsCluster("KatCluster")){
            graph.createVertexType("KatCluster");
        }
        if (!graph.getRawGraph().existsCluster("KatEnvironment")){
            graph.createVertexType("KatEnvironment");
        }
        if (!graph.getRawGraph().existsCluster("KatBroker")){
            graph.createVertexType("KatBroker");
        }
        if (!graph.getRawGraph().existsCluster("KatWorkflow")){
            graph.createVertexType("KatWorkflow");
        }
        if (!graph.getRawGraph().existsCluster("KatTask")){
            graph.createVertexType("KatTask");
        }
        if (!graph.getRawGraph().existsCluster("KatTask-contains-KatTaskSuccess")) {
            graph.createEdgeType("KatTask-contains-KatTaskSuccess");
        }
        if (!graph.getRawGraph().existsCluster("KatTask-contains-KatTaskError")) {
            graph.createEdgeType("KatTask-contains-KatTaskError");
        }
        if (!graph.getRawGraph().existsCluster("KatTask-contains-KatTaskThen")) {
            graph.createEdgeType("KatTask-contains-KatTaskThen");
        }
        if (!graph.getRawGraph().existsCluster("KatCluster-contains-KatServer")) {
            graph.createEdgeType("KatCluster-contains-KatServer");
        }
        if (!graph.getRawGraph().existsCluster("KatCluster-contains-KatBroker")) {
            graph.createEdgeType("KatCluster-contains-KatBroker");
        }
        if (!graph.getRawGraph().existsCluster("KatEnvironment-contains-KatCluster")) {
            graph.createEdgeType("KatEnvironment-contains-KatCluster");
        }
        if (!graph.getRawGraph().existsCluster("KatEnvironment-contains-KatServer")) {
            graph.createEdgeType("KatEnvironment-contains-KatServer");
        }
        if (!graph.getRawGraph().existsCluster("KatEnvironment-contains-KatBroker")) {
            graph.createEdgeType("KatEnvironment-contains-KatBroker");
        }

        if (!graph.getRawGraph().existsCluster("KatWorkflow")){
            graph.createVertexType("KatWorkflow");
        }

        if (!graph.getRawGraph().existsCluster("KatTask")){
            graph.createVertexType("KatTask");
        }
        if (!graph.getRawGraph().existsCluster("KatIamUser")) {
            graph.createVertexType("KatIamUser");
        }
        if (!graph.getRawGraph().existsCluster("KatIamRole")) {

            graph.createVertexType("KatIamRole");

            roleSuperAdmin = iamRoleRepository.save(new IamRole("SUPER-ADMIN"));
            roleViewer = iamRoleRepository.save(new IamRole("VIEWER"));
            roleAdmin = iamRoleRepository.save(new IamRole("ADMIN"));
            roleDevelopper = iamRoleRepository.save(new IamRole("DEVELOPER"));
            roleOperator = iamRoleRepository.save(new IamRole("OPERATOR"));

        }
        if (!graph.getRawGraph().existsCluster("KatIamGroup")) {

            graph.createVertexType("KatIamGroup");

            if(roleSuperAdmin != null) {
                IamGroup iamGroup = new IamGroup();
                iamGroup.setRole(roleSuperAdmin);
                iamGroup.setLabel("k_superadmin");

                IamGroup iamGrp = iamGroupRepository.save(iamGroup);

                logger.info("IAM GROUP SAVED");
                logger.info(iamGroup.getId());
            }

            if(roleViewer != null) {
                IamGroup iamGroup = new IamGroup();
                iamGroup.setRole(roleViewer);
                iamGroup.setLabel("k_viewer");

                iamGroupRepository.save(iamGroup);
            }

            if(roleAdmin != null) {
                IamGroup iamGroup = new IamGroup();
                iamGroup.setRole(roleAdmin);
                iamGroup.setLabel("k_admin");

                iamGroupRepository.save(iamGroup);
            }

            if(roleDevelopper != null) {
                IamGroup iamGroup = new IamGroup();
                iamGroup.setRole(roleDevelopper);
                iamGroup.setLabel("k_developer");

                iamGroupRepository.save(iamGroup);
            }

            if(roleOperator != null) {
                IamGroup iamGroup = new IamGroup();
                iamGroup.setRole(roleOperator);
                iamGroup.setLabel("k_operator");

                iamGroupRepository.save(iamGroup);
            }


        }
        if (!graph.getRawGraph().existsCluster("KatContextGroup")) {
            graph.createVertexType("KatContextGroup");
        }
        if (!graph.getRawGraph().existsCluster("KatContext")) {
            graph.createVertexType("KatContext");
        }
        if (!graph.getRawGraph().existsCluster("KatContextGroup")) {
            graph.createVertexType("KatContextGroup");
        }
        if (!graph.getRawGraph().existsCluster("KatIamEnvironment")) {
            graph.createVertexType("KatIamEnvironment");
        }
        if (!graph.getRawGraph().existsCluster("KatTask-contains-KatTaskSuccess")) {
            graph.createEdgeType("KatTask-contains-KatTaskSuccess");
        }
        if (!graph.getRawGraph().existsCluster("KatTask-contains-KatTaskError")) {
            graph.createEdgeType("KatTask-contains-KatTaskError");
        }
        if (!graph.getRawGraph().existsCluster("KatTask-contains-KatTaskThen")) {
            graph.createEdgeType("KatTask-contains-KatTaskThen");
        }
        if (!graph.getRawGraph().existsCluster("KatCluster-contains-KatServer")) {
            graph.createEdgeType("KatCluster-contains-KatServer");
        }
        if (!graph.getRawGraph().existsCluster("KatCluster-contains-KatBroker")) {
            graph.createEdgeType("KatCluster-contains-KatBroker");
        }
        if (!graph.getRawGraph().existsCluster("KatEnvironment-contains-KatCluster")) {
            graph.createEdgeType("KatEnvironment-contains-KatCluster");
        }
        if (!graph.getRawGraph().existsCluster("KatEnvironment-contains-KatServer")) {
            graph.createEdgeType("KatEnvironment-contains-KatServer");
        }
        if (!graph.getRawGraph().existsCluster("KatEnvironment-contains-KatBroker")) {
            graph.createEdgeType("KatEnvironment-contains-KatBroker");
        }
        if (!graph.getRawGraph().existsCluster("KatIamUser-contains-KatIamGroup")) {
            graph.createEdgeType("KatIamUser-contains-KatIamGroup");
        }
        if (!graph.getRawGraph().existsCluster("KatIamGroup-contains-KatIamEnvironment")) {
            graph.createEdgeType("KatIamGroup-contains-KatIamEnvironment");
        }
        if (!graph.getRawGraph().existsCluster("KatIamGroup-contains-KatIamRoles")) {
            graph.createEdgeType("KatIamGroup-contains-KatIamRoles");
        }

        if (!graph.getRawGraph().existsCluster("KatContextGroup-contains-KatContext")) {
            graph.createEdgeType("KatContextGroup-contains-KatContext");
        }
        if (!graph.getRawGraph().existsCluster("KatIamGroup-contains-KatContextGroup")) {
            graph.createEdgeType("KatIamGroup-contains-KatContextGroup");
        }

        graph.shutdown();
    }
}
