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

import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.Map;

public class KatBroker {
    private String id;
    private String name;
    private String type;
    private String systemName;
    private String jolokiaUrl;
    private PhysicalStats PhysicalStats;
    private SshLogin sshLogin;
    private AuthCredentials authCredentials;


    public KatBroker() {
    }

    public KatBroker(String id, String name,  String type, String systemName, String jolokiaUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.systemName = systemName;
        this.jolokiaUrl = jolokiaUrl;
    }

    public KatBroker(String id, String name,  String type, String systemName, String jolokiaUrl, PhysicalStats PhysicalStats) {
        this(id, name, type, systemName, jolokiaUrl);
        this.PhysicalStats = PhysicalStats;
    }

    public KatBroker(String id, String name,  String type, String systemName, String jolokiaUrl, AuthCredentials authCredentials) {
        this(id, name, type, systemName, jolokiaUrl);
        this.authCredentials = authCredentials;
    }

    public KatBroker(String id, String name,  String type, String systemName, String jolokiaUrl, SshLogin sshLogin, AuthCredentials authCredentials) {
        this(id, name, type, systemName, jolokiaUrl, authCredentials);
        this.sshLogin = sshLogin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getJolokiaUrl() {
        return jolokiaUrl;
    }

    public void setJolokiaUrl(String jolokiaUrl) {
        this.jolokiaUrl = jolokiaUrl;
    }

    public PhysicalStats getPhysicalStats() {
        return PhysicalStats;
    }

    public void setPhysicalStats(PhysicalStats PhysicalStats) {
        this.PhysicalStats = PhysicalStats;
    }

    public SshLogin getSshLogin() {
        return sshLogin;
    }

    public void setSshLogin(SshLogin sshLogin) {
        this.sshLogin = sshLogin;
    }

    public AuthCredentials getAuthCredentials() {
        return authCredentials;
    }

    public void setAuthCredentials(AuthCredentials authCredentials) {
        this.authCredentials = authCredentials;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OrientVertex toVertex(OrientVertex v){

        if (v == null){
            return null;
        }

        Map<String,Object> props = new HashMap<String,Object>();

        if (this.getName() != null){
            props.put("name", this.getName());
        }
        if (this.getType() != null){
            props.put("type", this.getType());
        }
        if (this.getSystemName() != null){
            props.put("systemName", this.getSystemName());
        }
        if (this.getJolokiaUrl() != null){
            props.put("jolokiaUrl", this.getJolokiaUrl());
        }

        if (this.getSshLogin() != null){
            SshLogin sshLogin = this.getSshLogin();
            if (sshLogin.getUser() != null){
                props.put("sshUser", sshLogin.getUser());
            }
            if (sshLogin.getPassword() != null){
                props.put("sshPassword", Base64.encodeBase64(sshLogin.getPassword().getBytes())); // TODO
            }
            if (sshLogin.getHost() != null){
                props.put("sshHost", sshLogin.getHost());
            }
            if (sshLogin.getPort() != null){
                props.put("sshPort", sshLogin.getPort());
            }
            if (sshLogin.getPath() != null){
                props.put("sshPath", sshLogin.getPath());
            }
        }

        if (this.getAuthCredentials() != null){
            AuthCredentials authCredentials = this.getAuthCredentials();
            if (authCredentials.getUser() != null){
                props.put("authCredsUser", authCredentials.getUser());
            }
            if (authCredentials.getPassword() != null){
                props.put("authCredsPassword", Base64.encodeBase64(authCredentials.getPassword().getBytes())); // TODO
            }
        }

        v.setProperties(props);

        return v;
    }

    public static KatBroker vertexToBroker(OrientVertex v) {
        if (v != null && v.getType() != null && v.getType().toString().equals("Broker")) {

            byte[] passwdTmp = (byte[]) v.getProperty("sshPassword");
            String sshPassword = null;
            if (passwdTmp != null) {
                sshPassword = new String(Base64.decodeBase64(passwdTmp));
            }
            String authPassword = null;
            passwdTmp = (byte[]) v.getProperty("authCredsPassword");
            if (passwdTmp != null) {
                authPassword = new String(Base64.decodeBase64(passwdTmp));
            }

            Integer port = (Integer) v.getProperty("sshPort");

            SshLogin sshLogin = new SshLogin(v.getProperty("sshUser"), v.getProperty("sshHost"), port, sshPassword, v.getProperty("sshPath"));
            AuthCredentials authCredentials = new AuthCredentials(v.getProperty("authCredsUser"), authPassword);


            return new KatBroker(v.getId().toString(), v.getProperty("name"), v.getProperty("type"), v.getProperty("systemName"), v.getProperty("jolokiaUrl"), sshLogin, authCredentials);
        }

        return null;
    }
}
