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

/**
 * Modèle représentant un contexte pour la gestion des habilitations.
 */
public class IamContext {

    /**
     * L'ID du contexte dans OrientDB
     */
    private String id;

    /**
     * Le libellé du contexte
     */
    private String label;

    /**
     * Constructeur simple de l'entité
     */
    public IamContext() {}

    /**
     * Constructeur complet de l'entité
     *
     * @param id    L'ID de l'entité
     * @param label Le libellé de l'entité
     */
    public IamContext(String id, String label) {
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
    public IamContext setId(String id) {
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
    public IamContext setLabel(String label) {
        this.label = label;
        return this;
    }
}
