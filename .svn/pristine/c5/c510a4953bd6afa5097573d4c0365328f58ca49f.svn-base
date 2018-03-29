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
import com.idfor.kat.tools.UIBackend.model.KatBroker;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Modèle représentant les groupes de contextes TALEND pour la gestion des habilitations.
 */
public class IamContextGroup {

    /**
     * L'ID de l'entité dans OrientDb
     */
    private String id;


    private String label;
    /**
     * La liste des contextes concernés
     */
    private ArrayList<Context> contexts;

    /**
     * Constructeur simple de l'entité
     */
    public IamContextGroup() {
        this.contexts = new ArrayList<>();
    }

    /**
     * Constructeur complet de l'entité avec liste
     *
     * @param id        L'ID de l'entité
     * @param contexts  La liste des contextes
     */
    public IamContextGroup(String id, String label, ArrayList<Context> contexts) {
        this.id = id;
        this.label = label;
        this.contexts = new ArrayList<>();

        for(Context ctx: contexts) {
            this.addContext(ctx);
        }
    }

    /**
     * Constructeur complet de l'entité avec liste
     *
     * @param id        L'ID de l'entité

     */
    public IamContextGroup(String id, String label) {
        this.id = id;
        this.contexts = new ArrayList<>();
        this.label = label;
        for(Context ctx: contexts) {
            this.addContext(ctx);
        }
    }

    /**
     * Constructeur complet de l'entité avec tableau
     *
     * @param id        L'ID de l'entité
     * @param contexts  La liste des contextes
     */
    public IamContextGroup(String id,String label, Context[] contexts) {
        this.id = id;
        this.contexts = new ArrayList<>();
        this.label = label;
        for(Context ctx: contexts) {
            this.addContext(ctx);
        }
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
    public IamContextGroup setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Getter de la liste des contextes
     *
     * @return La liste des contextes
     */
    public ArrayList<Context> getContexts() {
        return contexts;
    }

    /**
     * Setter de la liste des contextes
     *
     * @param contexts La nouvelle liste des contextes
     * @return this
     */
    public IamContextGroup setContexts(ArrayList<Context> contexts) {
        this.contexts.clear();

        for(Context ctx: contexts) {
            this.addContext(ctx);
        }

        return this;
    }

    /**
     * Ajoute un contexte à la liste si celui-ci n'est pas déjà dedans.
     *
     * @param ctx Le contexte à ajouter
     * @return this
     */
    public IamContextGroup addContext(Context ctx) {
        if(!this.contexts.contains(ctx)) {
            this.contexts.add(ctx);
        }

        return this;
    }

    /**
     * Supprime un contexte de la liste si celui-ci est dedans.
     *
     * @param ctx Le contexte à supprimer
     * @return this
     */
    public IamContextGroup removeContext(Context ctx) {
        if(this.contexts.contains(ctx)) {
            this.contexts.remove(ctx);
        }

        return this;
    }

    /**
     * Permet de savoir si un contexte est ou n'est pas dans la liste.
     *
     * @param ctx Le contexte à tester
     * @return Vrai si le contexte donné est dans la liste, sinon faux
     */
    public boolean hasContext(Context ctx) {
        return this.contexts.contains(ctx);
    }

    public static IamContextGroup vertexToIamContextGroup(OrientVertex v) {
        if (v != null) {
            String id = v.getId().toString();
            String label = v.getProperty("label");
            ArrayList<Context> contexts = new ArrayList<>();
            for(Edge e : v.getEdges(Direction.OUT, "KatContextGroup-contains-KatContext")){
                OrientVertex katContext = (OrientVertex) e.getVertex(Direction.IN);
                contexts.add(Context.vertexToContext(katContext));
            }
            return new IamContextGroup(id, label, contexts);
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


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
