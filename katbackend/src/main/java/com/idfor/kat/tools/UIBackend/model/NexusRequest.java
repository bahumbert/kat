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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idfor.kat.tools.UIBackend.beans.KatBackendProperties;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 */
public class NexusRequest {
    private String groupId;
    private String artifactId;
    private String version;
    private String artifactUrl;
    private String repositoryUrl;

    public NexusRequest() {}

    public NexusRequest(String groupId, String artifactId, String version, String artifactUrl, String repositoryUrl) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repositoryUrl = repositoryUrl;

        this.artifactUrl = artifactUrl
            .replace('\\', '/')
            .replaceAll("\\s", "%20")
        ;
    }

    public NexusRequest(String artifactId, String version, String artifactUrl) {
        this.artifactId = artifactId;
        this.version = version;

        this.artifactUrl = artifactUrl
            .replace('\\', '/')
            .replaceAll("\\s", "%20")
        ;

        this.groupId = KatBackendProperties.getNexusGroupId();
        this.repositoryUrl = "http://"
            + KatBackendProperties.getNexusUserid()
            + ":" + KatBackendProperties.getNexusPwd()
            + "@" + KatBackendProperties.getNexusUrl()
            + "/" + KatBackendProperties.getNexusRepository()
            + "/"
        ;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArtifactUrl() {
        return artifactUrl;
    }

    public void setArtifactUrl(String artifactUrl) {
        this.artifactUrl = artifactUrl;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String toJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json;

        try {
            json = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            json = "{\"error\": \"JSON serialisation error\"}";
        }

        return json;
    }
}
