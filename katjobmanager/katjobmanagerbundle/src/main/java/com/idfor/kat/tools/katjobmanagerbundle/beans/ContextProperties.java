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

package com.idfor.kat.tools.katjobmanagerbundle.beans;

import java.util.Hashtable;

/**
 * Bean that holds a list of all properties for a given context, for a given job in a particular deployed version
 * Properties values are double: the original value as exported by job and user overloaded ones
 */
public class ContextProperties {

    private boolean isActive;
    private String propertyName;
    private String originalValue;
    private String overloadedValue;

    /**
     * Contructor needed for jackson marshalling
     */
    public ContextProperties(){
    }

    /**
     * Constructor to instantiate a property with its values at creation
     * @param isActive tells if overloaded value is active
     * @param propertyName property key
     * @param originalValue original property value
     * @param overloadedValue overloaded property value
     */
    public ContextProperties(boolean isActive, String propertyName, String originalValue, String overloadedValue){
        this.isActive = isActive;
        this.propertyName = propertyName;
        this.originalValue = originalValue;
        this.overloadedValue = overloadedValue;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public String getOverloadedValue() {
        return overloadedValue;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public void setOverloadedValue(String overloadedValue) {
        this.overloadedValue = overloadedValue;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
