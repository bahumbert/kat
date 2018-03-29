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

/**
 * Modèle représentant un utilisateur pour la gestion des habilitations.
 */
public class IamUser {

    /**
     * L'ID de l'utilisateur
     */
    private String id;

    /**
     * Le token de l'utilisateur
     */
    private String token;

    /**
     * Le nom de l'utilisateur
     */
    private String lastname;

    /**
     * Le prénom de l'utilisateur
     */
    private String firstname;


    private String email;

    /**
     * La liste des attributs de l'utilisateur
     */
    private HashMap<String, String> attributes;

    /**
     * Le groupe d'accès auquel appartient l'utilisateur
     */
    private IamGroup group;

    /**
     * Constructeur simple de l'entité
     */
    public IamUser() {}

    /**
     * Constructeur complet de l'entité
     *
     * @param id         L'ID de l'entité
     * @param firstname  Le prénom de l'utilisateur
     * @param lastname   Le nom de l'utilisateur
     * @param attributes La liste des attributs de l'utilisateur
     */
    public IamUser(String id, String firstname, String lastname, HashMap<String, String> attributes, IamGroup group) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.group = group;
        this.attributes = new HashMap<>();
    }

    /**
     * Constructeur complet de l'entité
     *
     * @param id         L'ID de l'entité
     * @param firstname  Le prénom de l'utilisateur
     * @param lastname   Le nom de l'utilisateur
     * @param email   L'adresse email de l'utilisateur
     */
    public IamUser(String id, String firstname, String lastname,String email) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
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
    public IamUser setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Getter du nom de l'utilisateur
     *
     * @return Le nom de l'utilisateur
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Setter du nom de l'utilisateur
     *
     * @param lastname Le nouveau nom de l'utilisateur
     * @return this
     */
    public IamUser setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    /**
     * Getter du prénom de l'utilisateur
     *
     * @return Le prénom de l'utilisateur
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Setter du prénom de l'utilsateur
     *
     * @param firstname le prénom de l'utilisateur
     * @return this
     */
    public IamUser setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    /**
     * Getter des attributs de l'utilisateur
     *
     * @return Les attributs de l'utilisateur
     */
    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Setter des attributs de l'utilisateur.
     *
     * @param attributes La nouvelle liste des attributs de l'utilisateur
     * @return this
     */
    public IamUser setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    /**
     * Permet de définir un attribut de l'utilisateur.
     *
     * @param attr  Le nom de l'attribut à alimenter
     * @param value La valeur de l'attribut
     * @return this
     */
    public IamUser addAttribute(String attr, String value) {
        this.attributes.put(attr, value);
        return this;
    }

    /**
     * Permet de supprimer un attribut de l'utilisateur.
     *
     * @param attr Le nom de l'attribut à supprimer
     * @return this
     */
    public IamUser removeAttribute(String attr) {
        this.attributes.remove(attr);
        return this;
    }

    /**
     * Permet de savoir si l'utilisateur possède un attribut.
     *
     * @param attr Le nom de l'attribut à rechercher
     * @return Vrai si l'attribut est retrouvé, sinon faux
     */
    public boolean hasAttribute(String attr) {
        return this.attributes.containsKey(attr);
    }

    /**
     * Getter du groupe d'accès
     *
     * @return Le groupe d'accès
     */
    public IamGroup getGroup() {
        return group;
    }

    /**
     * Setter du groupe d'accès
     *
     * @param group Le nouveau groupe d'accès
     * @return this
     */
    public IamUser setGroup(IamGroup group) {
        this.group = group;
        return this;
    }

    /**
     * Getter du token
     *
     * @return Le token
     */
    public String getToken() {
        return token;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter du token
     *
     * @param token Le nouveau token
     */
    public void setToken(String token) {
        this.token = token;
    }

    public static IamUser vertexToIamUser(OrientVertex v) {
        if (v != null) {

            String id = v.getId().toString();
            String firstname = (String) v.getProperty("firstname");
            String lastname = (String) v.getProperty("lastname");
            String email = (String) v.getProperty("email");

            return new IamUser(id, firstname, lastname, email);
        }
        return null;
    }

    // this.id = id;
    //        this.firstname = firstname;
    //        this.lastname = lastname;
    //        this.group = group;
}
