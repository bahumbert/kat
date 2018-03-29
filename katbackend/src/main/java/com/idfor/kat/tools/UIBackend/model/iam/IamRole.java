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

package com.idfor.kat.tools.UIBackend.model.iam;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.HashMap;
import java.util.Map;

/**
 * Modèle représentant un rôle (CRUD) pour la gestion des habilitations.
 */
public class IamRole {

    /**
     * Accès à tous les éléments de l'IHM, lui seul peut créer les GROUPES
     */
    public static String SUPERADMIN = "SUPERADMIN";

    /**
     *  Lui seul peut créer un environnements et lui associer un ou plusieurs serveurs (ou les supprimer)
     */
    public static String ADMIN = "ADMIN";

    /**
     * N'a accès qu'à la zone d'upload d'un job artifact sur le NEXUS
     */
    public static String DEVELOPPER = "DEVELOPPER";

    /**
     * N'a pas accès à l'upload sur le NEXUS. Peut choisir un environnement, déployer un job du NEXUS dans cet
     * environnement, peut planifier une exécution et peut accéder à la liste des jobs planifiés et effectuer des
     * opérations sur ces jobs (démarrer, arrêter la planification, supprimer une planification, ...), avoir accès
     * à la définition des alertes, à la surcharge des contextes, ... (toute action sur les jobs)
     */
    public static String OPERATOR = "OPERATOR";

    /**
     * Peut choisir un environnement et voir la liste des jobs mais ne peut faire aucune action
     */
    public static String VIEWER = "VIEWER";

    /**
     * L'ID du rôle dans OrientDB
     */
    private String id;

    /**
     * Le libellé du rôle
     */
    private String label;

    /**
     * Constructeur simple de l'entité
     */
    public IamRole() {}

    /**
     * Constructeur complet de l'entité
     *
     * @param id    L'ID de l'entité
     * @param label Le libellé de l'entité
     */
    public IamRole(String id, String label) {
        this.id = id;
        this.label = label;
    }

    /**
     * Constructeur complet de l'entité
     *
     * @param id    L'ID de l'entité
     * @param label Le libellé de l'entité
     */
    public IamRole( String label) {
        this.label = label;
    }

    /**
     * Getter de l'ID
     *
     * @return L'ID du rôle
     */
    public String getId() {
        return id;
    }

    /**
     * Setter de l'ID
     *
     * @param id Le nouvel ID
     * @return this
     */
    public IamRole setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Getter du libellé
     *
     * @return Le libellé actuel
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter du libellé
     *
     * @param label Le nouveau libellé
     * @return this
     */
    public IamRole setLabel(String label) {
        this.label = label;
        return this;
    }

    public static IamRole vertexToIamRole(OrientVertex v) {
        if (v != null) {

            String id = v.getId().toString();
            String label = (String) v.getProperty("label");

            return new IamRole(id, label);
        }
        return null;
    }

    /**
     * @param v
     * @return
     */
    public OrientVertex toVertex(OrientVertex v){

        if (v == null){
            return null;
        }

        Map<String,Object> props = new HashMap<String,Object>();

        if (this.getLabel() != null){
            props.put("label", this.getLabel());
        }

        v.setProperties(props);

        return v;
    }
}
