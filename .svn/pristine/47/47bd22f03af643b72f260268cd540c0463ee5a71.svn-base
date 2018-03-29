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

import com.idfor.kat.tools.UIBackend.model.Context;
import com.idfor.kat.tools.UIBackend.model.KatEnvironment;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Modèle représentant une liste d'environnements pour la gestion des habilitations.
 */
public class IamGroup {

    /**
     * L'ID de l'entité dans OrientDb
     */
    private String id;


    private String label;

    /**
     * La liste des environnements concernés
     */
    private ArrayList<KatEnvironment> environments;

    /**
     * La liste des environnements concernés
     */
    private IamRole role;


    public void setContextGroup(IamContextGroup contextGroup) {
        this.contextGroup = contextGroup;
    }

    /**
     * Le groupe de contextes TALEND
     */
    private IamContextGroup contextGroup;

    /**
     * Constructeur simple de l'entité
     */
    public IamGroup() {
        this.environments = new ArrayList<>();
        this.contextGroup = new IamContextGroup();
        this.role = new IamRole();
    }

    /**
     * Constructeur simple de l'entité
     */
    public IamGroup(String label, IamRole role) {
        this.label = label;
        this.role = role;
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
    public IamGroup setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Getter de la liste des environnements
     *
     * @return La liste des environnements
     */
    public ArrayList<KatEnvironment> getEnvironments() {
        return environments;
    }

    /**
     * Setter de la liste des environnements
     *
     * @param environments La nouvelle liste des environnements
     * @return this
     */
    public IamGroup setEnvironments(ArrayList<KatEnvironment> environments) {
        this.environments.clear();

        for(KatEnvironment env: environments) {
            this.addEnvironment(env);
        }

        return this;
    }

    /**
     * Ajoute un environnement à la liste si celui-ci n'est pas déjà dedans.
     *
     * @param env L'environnement à ajouter
     * @return this
     */
    public IamGroup addEnvironment(KatEnvironment env) {
        if(!this.environments.contains(env)) {
            this.environments.add(env);
        }

        return this;
    }

    /**
     * Supprime un environnement de la liste si celui-ci est dedans.
     *
     * @param env L'environnement à supprimer
     * @return this
     */
    public IamGroup removeEnvironment(KatEnvironment env) {
        if(this.environments.contains(env)) {
            this.environments.remove(env);
        }

        return this;
    }

    /**
     * Permet de savoir si un environnement est ou n'est pas dans la liste.
     *
     * @param env L'environnement à tester
     * @return Vrai si l'environnement donné est dans la liste, sinon faux
     */
    public boolean hasEnvironment(KatEnvironment env) {
        return this.environments.contains(env);
    }

    /**
     * Permet de savoir si oui ou non l'environnement demandé via son nom fait parti du groupe d'accès.
     * Insensible à la casse.
     *
     * @param envName Le nom de l'environnement à rechercher
     * @return Vrai si l'environnement fait parti du groupe, sinon faux
     */
    public boolean hasEnvironmentByName(String envName) {
        boolean found = false;

        envName = envName.toLowerCase();

        for(KatEnvironment env : this.environments) {
            if(env.getName().toLowerCase().equals(envName)) {
                found = true;
                break;
            }
        }

        return found;
    }

    /**
     * Getter de la liste des rôles
     *
     * @return La liste des rôles
     */
    public IamRole getRole() {
        return role;
    }

    /**
     * Setter de la liste des rôles
     *
     * @param role La nouvelle liste de rôles
     * @return this
     */
    public IamGroup setRole(IamRole role) {

        this.role = role;

        return this;
    }



    /**
     * Getter du groupe de contextes
     *
     * @return Le groupe de contexte
     */
    public IamContextGroup getContextGroup() {
        return contextGroup;
    }



    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Constructeur complet de l'entité
     *
     * @param id    L'ID de l'entité
     * @param label Le libellé de l'entité
     */
    public IamGroup(String id, String label) {
        this.id = id;
        this.label = label;
    }


    public static IamGroup vertexToIamGroup(OrientVertex v) {

        IamRole rol = null;
        IamContextGroup iamCtGroup = null;
        ArrayList<KatEnvironment> katEnvironments = new ArrayList<>();
        if (v != null) {

            String id = v.getId().toString();
            String label = (String) v.getProperty("label");
            for(Edge e : v.getEdges(Direction.OUT, "KatIamGroup-contains-KatIamRoles")) {
                OrientVertex role = (OrientVertex) e.getVertex(Direction.IN);
                rol = IamRole.vertexToIamRole(role);
            }

            for(Edge e : v.getEdges(Direction.OUT, "KatIamGroup-contains-KatContextGroup")) {
                OrientVertex ct = (OrientVertex) e.getVertex(Direction.IN);
                iamCtGroup = IamContextGroup.vertexToIamContextGroup(ct);
            }

            for(Edge e : v.getEdges(Direction.OUT, "KatIamGroup-contains-KatEnvironment")) {
                OrientVertex env = (OrientVertex) e.getVertex(Direction.IN);

                katEnvironments.add(KatEnvironment.vertexToEnvironment(env));
            }



            return new IamGroup(id, label, rol, iamCtGroup, katEnvironments);
        }
        return null;
    }

    /**
     * Constructeur complet de l'entité
     *
     * @param id    L'ID de l'entité
     * @param label Le libellé de l'entité
     */
    public IamGroup(String id, String label, IamRole role, IamContextGroup iamContextGroup, ArrayList<KatEnvironment> katEnvironments) {
        this.id = id;
        this.label = label;
        this.role = role;
        this.contextGroup = iamContextGroup;
        this.environments = katEnvironments;
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
