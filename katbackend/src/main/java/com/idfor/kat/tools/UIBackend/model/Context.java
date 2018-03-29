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

package com.idfor.kat.tools.UIBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private String propertyName;
    private String originalValue;
    private String overloadedValue;
    private String active;
    @JsonIgnore
    private String id;
    @JsonIgnore
    private String label;

    /**
     * Constructeur simple de l'entité
     */
    public Context() {}

    /**
     * Constructeur complet de l'entité
     *
     * @param id    L'ID de l'entité
     * @param label Le libellé de l'entité
     */
    public Context(String id, String label) {
        this.id = id;
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
    public Context setId(String id) {
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
    public Context setLabel(String label) {
        this.label = label;
        return this;
    }

    public static Context vertexToContext(OrientVertex v) {
        if (v != null) {
            String id = v.getId().toString();
            String label = (String) v.getProperty("label");

            return new Context(id, label);
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

        if (this.getPropertyName() != null){
            props.put("propertyName", this.getPropertyName());
        }

        if (this.getOriginalValue() != null){
            props.put("originalValue", this.getOriginalValue());
        }

        if (this.getOriginalValue() != null){
            props.put("overloadedValue", this.getOverloadedValue());
        }

        if (this.getActive() != null){
            props.put("active", this.getActive());
        }

        if (this.getId() != null){
            props.put("id", this.getId());
        }

        if (this.getLabel() != null){
            props.put("label", this.getLabel());
        }

        v.setProperties(props);

        return v;
    }


    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getOverloadedValue() {
        return overloadedValue;
    }

    public void setOverloadedValue(String overloadedValue) {
        this.overloadedValue = overloadedValue;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
