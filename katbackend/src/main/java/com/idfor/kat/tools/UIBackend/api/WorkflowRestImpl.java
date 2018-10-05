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

package com.idfor.kat.tools.UIBackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idfor.kat.tools.UIBackend.dao.ServerRepository;
import com.idfor.kat.tools.UIBackend.dao.TaskRepository;
import com.idfor.kat.tools.UIBackend.dao.WorkflowRepository;
import com.idfor.kat.tools.UIBackend.model.KatServer;
import com.idfor.kat.tools.UIBackend.model.Task;
import com.idfor.kat.tools.UIBackend.model.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.HttpHeaders;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WorkflowRestImpl implements WorkflowRest {
    public static final Logger logger = LoggerFactory.getLogger(KatRestServer.class);
    private WorkflowRepository workflowRepository = new WorkflowRepository();
    private TaskRepository taskRepository = new TaskRepository();
    private ServerRepository serverRepository = new ServerRepository();
    private ArrayList<String> arrayIdDelete;

    @Override
    public String unDeployWorkflow(String wkFile, String idServer, HttpHeaders headers){
        KatServer s = serverRepository.findOneById(idServer);
        StringBuffer response = new StringBuffer();
        try {
            String url = s.getCommunicationUrl()+"/cxf/katjobmanager/workflows/"+wkFile;
            logger.info(url);
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestProperty("Charset", "utf-8");
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type","application/json");

            // String monchamp=  new String(data,"UTF-8");

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            wr.flush();
            wr.close();
        } catch (Exception e){
            logger.info("error");
            logger.info(e.getMessage());
        }

        return response.toString();
    }

    @Override
    public String deployWorkflow(String idWorkflow, String idServer, HttpHeaders headers){
        KatServer s = serverRepository.findOneById(idServer);
        Workflow wk = this.getWorkflow(idWorkflow);
        StringBuffer response = new StringBuffer();
        try {
            String url = s.getCommunicationUrl()+"/cxf/katjobmanager/workflows";
            logger.info(url);
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestProperty("Charset", "utf-8");
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");

            ObjectMapper mapper = new ObjectMapper();
            final byte[] data = mapper.writeValueAsBytes(wk);

            // String monchamp=  new String(data,"UTF-8");

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(data);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            wr.flush();
            wr.close();

        } catch (Exception e){
            logger.info("error");
            logger.info(e.getMessage());
        }

        return response.toString();
    }


    @Override
    public  List<Workflow> getWorkflows() {
        return workflowRepository.findAll();
    }

    public List<Workflow> deleteWorkflow(String idWorkflow) {
        this.arrayIdDelete = new ArrayList<>();
        Workflow wkInit =  this.workflowRepository.findOneById(idWorkflow);



        if(wkInit != null && wkInit.getId() != null) {
            List<Task> task = this.taskRepository.findByWorkflowId(wkInit.getId());
            wkInit.setTask(this.runOverThatWorkflowDeleting(task.get(0), wkInit));
        }

        if(wkInit!= null && wkInit.getTask() != null) {
            arrayIdDelete.add(wkInit.getTask().getId());
        }

        for (String temp : this.arrayIdDelete) {
          taskRepository.delete(temp);
        }

        this.workflowRepository.delete(idWorkflow);
        return workflowRepository.findAll();
    }

    public Workflow getWorkflow(String id) {
        Workflow wkInit =  this.workflowRepository.findOneById(id);
        if(wkInit != null && wkInit.getId() != null) {
            List<Task> task = this.taskRepository.findByWorkflowId(wkInit.getId());
            wkInit.setTask(this.runOverThatWorkflowGetting(task.get(0), wkInit));
        }


        return wkInit;
    }

    public Task addWorkflow(Workflow workflow) {
        Workflow wkInit =  this.workflowRepository.save(workflow);

        return this.runOverThatWorkflowAdding(workflow.getTask(), true, wkInit);
    }

    @Override
    public Task updateWorkflow(String idWorkflow, Workflow workflow){
        this.arrayIdDelete = new ArrayList<>();
        Workflow wkInit =  this.workflowRepository.findOneById(idWorkflow);

        if(wkInit != null && wkInit.getId() != null) {
            List<Task> task = this.taskRepository.findByWorkflowId(wkInit.getId());
            wkInit.setTask(this.runOverThatWorkflowDeleting(task.get(0), wkInit));
        }

        if(wkInit!= null && wkInit.getTask() != null) {
            arrayIdDelete.add(wkInit.getTask().getId());
        }

        for (String temp : this.arrayIdDelete) {
            taskRepository.delete(temp);
        }

        this.workflowRepository.delete(idWorkflow);

        wkInit =  this.workflowRepository.save(workflow);

        return this.runOverThatWorkflowAdding(workflow.getTask(), true, wkInit);
    }

    private Task runOverThatWorkflowDeleting(Task task, Workflow wk) {
        Task syncedParent;
        Task wf;

        syncedParent = task;


        List<Task> arrSuccess = this.taskRepository.findByTaskStatusId(task.getId(), "success");
        List<Task> arrError = this.taskRepository.findByTaskStatusId(task.getId(), "error");
        List<Task> arrThen = this.taskRepository.findByTaskStatusId(task.getId(), "then");

        ArrayList<Task> syncedSuccess = new ArrayList<>();
        ArrayList<Task> syncedErrors = new ArrayList<>();
        ArrayList<Task> syncedThens = new ArrayList<>();

        for(Task w : arrSuccess) {

            Task syncedChild = w;

            syncedChild.setSuccess(w.getSuccess());
            syncedChild.setError(w.getError());
            syncedChild.setThen(w.getThen());

            if(w != null && w.getId() != null) {
                this.arrayIdDelete.add(w.getId());
            }

            syncedSuccess.add(this.runOverThatWorkflowDeleting( syncedChild, null ));
        }

        for(Task w : arrError) {
            Task syncedChild = w;

            syncedChild.setSuccess(w.getSuccess());
            syncedChild.setError(w.getError());
            syncedChild.setThen(w.getThen());
            if(w.getId() != null) {
            this.arrayIdDelete.add(w.getId());
            }
            syncedErrors.add(this.runOverThatWorkflowDeleting( syncedChild, null ));
        }

        for(Task w : arrThen) {
            Task syncedChild = w;

            syncedChild.setSuccess(w.getSuccess());
            syncedChild.setError(w.getError());
            syncedChild.setThen(w.getThen());
            if(w.getId() != null) {
                this.arrayIdDelete.add(w.getId());
            }
            syncedThens.add(this.runOverThatWorkflowDeleting( syncedChild, null ));
        }

        // On RAZ les enfants avant de les remettre
        syncedParent.razSuccessChildrenFromList( syncedSuccess );
        syncedParent.razErrorChildrenFromList( syncedErrors );
        syncedParent.razThenChildrenFromList( syncedThens );

        return syncedParent;
    }
    private Task runOverThatWorkflowGetting(Task task, Workflow wk) {
        Task syncedParent;
        Task wf;

        syncedParent = task;


        List<Task> arrSuccess = this.taskRepository.findByTaskStatusId(task.getId(), "success");
        List<Task> arrError = this.taskRepository.findByTaskStatusId(task.getId(), "error");
        List<Task> arrThen = this.taskRepository.findByTaskStatusId(task.getId(), "then");

        ArrayList<Task> syncedSuccess = new ArrayList<>();
        ArrayList<Task> syncedErrors = new ArrayList<>();
        ArrayList<Task> syncedThens = new ArrayList<>();

        for(Task w : arrSuccess) {
            Task syncedChild = w;

            syncedChild.setSuccess(w.getSuccess());
            syncedChild.setError(w.getError());
            syncedChild.setThen(w.getThen());

            syncedSuccess.add(this.runOverThatWorkflowGetting( syncedChild, null ));
        }

        for(Task w : arrError) {
            Task syncedChild = w;

            syncedChild.setSuccess(w.getSuccess());
            syncedChild.setError(w.getError());
            syncedChild.setThen(w.getThen());
            syncedErrors.add(this.runOverThatWorkflowGetting( syncedChild, null ));
        }

        for(Task w : arrThen) {
            Task syncedChild = w;

            syncedChild.setSuccess(w.getSuccess());
            syncedChild.setError(w.getError());
            syncedChild.setThen(w.getThen());
            syncedThens.add(this.runOverThatWorkflowGetting( syncedChild, null ));
        }

        // On RAZ les enfants avant de les remettre
        syncedParent.razSuccessChildrenFromList( syncedSuccess );
        syncedParent.razErrorChildrenFromList( syncedErrors );
        syncedParent.razThenChildrenFromList( syncedThens );

        return syncedParent;
    }


    private Task runOverThatWorkflowAdding(Task task, boolean needSave, Workflow wk) {
        Task syncedParent;
        Task wf;
        if(needSave) {
            wf = this.taskRepository.save(task);
            if(wk.getTask() != null &&wk.getTask().getId() != null && wf.getId() != null) {
                this.taskRepository.addTaskSuccessNode(wk.getTask().getId(), wf.getId());
            }
            syncedParent = wf;
            if(wk != null) {
                this.workflowRepository.addTaskNode(wk.getId(), wf.getId());
            }
        } else {
            syncedParent = task;
        }

        Task[] arrSuccess = task.getSuccess() != null ? task.getSuccess() : new Task[0];
        Task[] arrError = task.getError() != null ? task.getError() : new Task[0];
        Task[] arrThen = task.getThen() != null ? task.getThen() : new Task[0];

        ArrayList<Task> syncedSuccess = new ArrayList<>();
        ArrayList<Task> syncedErrors = new ArrayList<>();
        ArrayList<Task> syncedThens = new ArrayList<>();

        for(Task w : arrSuccess) {
            Task syncedChild = this.taskRepository.save(w);
            this.taskRepository.addTaskSuccessNode(syncedParent.getId(), syncedChild.getId());

            syncedChild.setSuccess(w.getSuccess());
            syncedChild.setError(w.getError());
            syncedChild.setThen(w.getThen());

            syncedSuccess.add(this.runOverThatWorkflowAdding( syncedChild, false, null ));
        }

        for(Task w : arrError) {
            Task syncedChild = this.taskRepository.save(w);
            this.taskRepository.addTaskErrorNode(syncedParent.getId(), syncedChild.getId());

            syncedChild.setSuccess(w.getSuccess());
            syncedChild.setError(w.getError());
            syncedChild.setThen(w.getThen());

            syncedErrors.add(this.runOverThatWorkflowAdding( syncedChild, false, null ));
        }

        for(Task w : arrThen) {
            Task syncedChild = this.taskRepository.save(w);
            this.taskRepository.addTaskThenNode(syncedParent.getId(), syncedChild.getId());

            syncedChild.setSuccess(w.getSuccess());
            syncedChild.setError(w.getError());
            syncedChild.setThen(w.getThen());


            syncedThens.add(this.runOverThatWorkflowAdding( syncedChild, false, null ));
        }

        // On RAZ les enfants avant de les remettre
        syncedParent.razSuccessChildrenFromList( syncedSuccess );
        syncedParent.razErrorChildrenFromList( syncedErrors );
        syncedParent.razThenChildrenFromList( syncedThens );

        return syncedParent;
    }
}