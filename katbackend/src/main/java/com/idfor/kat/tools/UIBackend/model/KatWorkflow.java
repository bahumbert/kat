/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idfor.kat.tools.UIBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bhumbert on 20/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KatWorkflow {

    private String id;
    private String name;
    private List<KatWorkflow> katWorkflows;

    public KatWorkflow() {
    }

    public KatWorkflow(String name) {
        this.name = name;
        this.katWorkflows = katWorkflows;
    }

    public KatWorkflow(String id, String name) {
        this.id = id;
        this.name = name;

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


    public List<KatWorkflow> getKatWorkflow() {
        return katWorkflows;
    }

    public void setKatWorkflows(List<KatWorkflow> katWorkflows) {
        this.katWorkflows = katWorkflows;
    }



    public KatWorkflow findKatWorkflowByName(String name){
        for (KatWorkflow s : this.getKatWorkflow()){
            if(s.getName().equals(name)){
                return s;
            }
        }
        return null;
    }

    public static KatWorkflow vertexToWorkflow(OrientVertex v) {
        if (v != null && v.getType() != null && v.getType().toString().equals("KatWorkflow")) {

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
            return new KatWorkflow(v.getId().toString(), (String) v.getProperty("name"));
        }

        return null;
    }

    public OrientVertex toVertex(OrientVertex v){

        if (v == null){
            return null;
        }

        Map<String,Object> props = new HashMap<String,Object>();

        if (this.getName() != null){
            props.put("name", this.getName());
        }
        v.setProperties(props);

        return v;
    }

    

}
