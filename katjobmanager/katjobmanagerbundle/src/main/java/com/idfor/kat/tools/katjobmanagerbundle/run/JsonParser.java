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

package com.idfor.kat.tools.katjobmanagerbundle.run;

import org.codehaus.jackson.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class used to parse JSON structure describing a workflow
 */
public class JsonParser {

    /**
     * Gets first job to execute in a workflow
     * @param jsonWorkflow JSON representation of the workflow
     * @return artifact URL of first job
     */
    public List<String> getWorkflowArtifacts(JsonNode jsonWorkflow){

        List<String> firstJob = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> nodes = jsonWorkflow.getFields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            if(entry.getValue().isArray()){
                Iterator<JsonNode> it = entry.getValue().getElements();
                while(it.hasNext()){
                    JsonNode j = it.next();
                    firstJob.addAll(getWorkflowArtifacts(j));
                }
            }
            if("artifactUrl".equals(entry.getKey())){
                firstJob.add(entry.getValue().toString());
            }

        }

        // JsonNode name = jsonWorkflow.get("task").get("artifactUrl");
        // if(name != null) firstJob = name.getTextValue();
        return firstJob;
    }
}
