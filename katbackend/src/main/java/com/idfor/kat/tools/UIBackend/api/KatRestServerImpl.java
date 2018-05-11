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
import com.idfor.kat.tools.UIBackend.dao.ClusterRepository;
import com.idfor.kat.tools.UIBackend.dao.EnvironmentRepository;
import com.idfor.kat.tools.UIBackend.dao.ServerRepository;
import com.idfor.kat.tools.UIBackend.model.*;

import com.idfor.kat.tools.UIBackend.model.JvmOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.ws.rs.core.HttpHeaders;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public  class KatRestServerImpl implements KatRestServer {

    public static final Logger logger = LoggerFactory.getLogger(KatRestServer.class);

    private EnvironmentRepository environmentRepository = new EnvironmentRepository();
    private ClusterRepository clusterRepository = new ClusterRepository();
    private ServerRepository serverRepository = new ServerRepository();
    private final String type = "org.apache.karaf:type=bundle,name=";

    @Override
    public String updateScheduledJob(String serverId, ScheduleUpdater scheduleUpdater, HttpHeaders headers) {
        KatServer s = serverRepository.findOneById(serverId);
        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katjobmanager/schedule/property";
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestProperty("Charset", "utf-8");
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type","application/json");

            ObjectMapper mapper = new ObjectMapper();
            final byte[] data = mapper.writeValueAsBytes(scheduleUpdater);

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
            logger.info(e.getMessage());
        }

        return response.toString();
    }

    @Override
    public String getIsDeployedArtifact (String serverId, String jobName, String version, HttpHeaders headers) {
        KatServer s = serverRepository.findOneById(serverId);
        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katexec/jobs/scheduled/"+jobName+"/"+version;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setDoOutput(true);

            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);// optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Charset", "UTF-8");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e){
            logger.info(e.getMessage());
        }
        return response.toString();
    }

    @Override
    public String getDeployedWorkflow(String serverId, HttpHeaders headers) {
        KatServer s = serverRepository.findOneById(serverId);
        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katjobmanager/workflows";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setDoOutput(true);

            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);// optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Charset", "UTF-8");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e){
            logger.info(e.getMessage());
        }
        return response.toString();
    }

    @Override
    public String postContext(String serverId, OverridedContext oContext, HttpHeaders headers){
        KatServer s = serverRepository.findOneById(serverId);
        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katjobmanager/contexts/properties";
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestProperty("Charset", "utf-8");
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");

            ObjectMapper mapper = new ObjectMapper();
            final byte[] data = mapper.writeValueAsBytes(oContext);

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
            logger.info(e.getMessage());
        }

        return response.toString();
    }


    @Override
    public String postJobAlertMail(String serverId, MailNotif data, HttpHeaders headers){
        KatServer s = serverRepository.findOneById(serverId);
        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katjobmanager/alerts/mail";
            logger.info(url);
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");

            String arrayEmails;
            String putJsonData;

            if(data.getRecipients().length != 0) {
                arrayEmails = "";
                for (int i = 0; i < data.getRecipients().length; i++) {
                    arrayEmails = arrayEmails.concat("\"" + data.getRecipients()[i] + "\",");
                }
            } else {
                arrayEmails = null;
            }

            if(arrayEmails != null) {
                arrayEmails = arrayEmails.substring(0, arrayEmails.length() - 1);
                 putJsonData = "{\n" +
                        "    \"scheduleFile\": \""+data.getScheduleFile()+"\",\n" +
                        "    \"recipients\": [\n" +
                        arrayEmails
                        + "    ],\n" +
                        "    \"active\": "+data.getActive()+"\n" +
                        "}";
            } else {
                 putJsonData = "{\n" +
                        "    \"scheduleFile\": \""+data.getScheduleFile()+"\",\n" +
                        "    \"recipients\": [],\n" +
                        "    \"active\": "+data.getActive()+"\n" +
                        "}";
            }
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(putJsonData);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            wr.flush();
            wr.close();

        } catch (Exception e){
            e.printStackTrace();
        }

        return response.toString();
    }

    @Override
    public String postJobJVMOptions(String serverId, JvmOptions data, HttpHeaders headers){
        KatServer s = serverRepository.findOneById(serverId);
        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katjobmanager/jvmoptions";
            logger.info(url);
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json");

            String putJsonData;

            putJsonData = "{\n" +
                    "    \"scheduleFile\": \""+data.getScheduleFile()+"\",\n" +
                    "    \"options\": \""+data.getOptions()+"\",\n" +
                    "    \"active\": "+data.getActive()+"\n" +
                    "}";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(putJsonData);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            wr.flush();
            wr.close();

        } catch (Exception e){
            e.printStackTrace();
        }

        return response.toString();
    }

    @Override
    public List<KatServer> getServersAttributes( Boolean lonely) {
        if (lonely == null || !lonely) {
            return serverRepository.findAll();
        }
        return serverRepository.findAllLonely();
    }

    @Override
    public String getKatScheduledJob(String serverId, HttpHeaders httpHeaders) {
        KatServer s = serverRepository.findOneById(serverId);
        return getKatJobs(s, httpHeaders);
    }

    public String getServerJobVersion(String serverId, String jobName, HttpHeaders headers) {
        KatServer s = serverRepository.findOneById(serverId);
        StringBuffer response = new StringBuffer();

        try {
            String url = s.getJolokiaUrl()+"/cxf/katjobmanager/versions/" + jobName;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);

            // optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Charset", "UTF-8");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e){
            logger.info(e.getMessage());
        }
        return response.toString();
    }


    //RESUME
    @Override
    public Boolean deleteKatJob(String serverId, String fileName, HttpHeaders headers){
        boolean retour = false;
        KatServer s = serverRepository.findOneById(serverId);
        List<ZipArtifact> results = new ArrayList<ZipArtifact>();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katjobmanager/schedule/"+fileName;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);
            // optional default is GET
            con.setRequestMethod("DELETE");

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            in.close();
             retour = true;
        } catch (Exception e){
            retour = false;
            e.printStackTrace();
        }

        return retour;
    }

    @Override
    public String getArtifacts(String serverId, HttpHeaders headers) {
        KatServer s = serverRepository.findOneById(serverId);
        StringBuffer response = new StringBuffer();
        List<ZipArtifact> results = new ArrayList<ZipArtifact>();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katjobmanager/binaries";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);

            // optional default is GET
            con.setRequestMethod("GET");

            con.setRequestProperty("Charset", "UTF-8");


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e){
            logger.info(e.getMessage());
        }
        return response.toString();
    }

    @Override
    public String changeJobStatus(String serverId, String pidJob,  String status, HttpHeaders headers){
        KatServer s = serverRepository.findOneById(serverId);
        List<ZipArtifact> results = new ArrayList<ZipArtifact>();
        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katexec/jobs";
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);

            // optional default is GET
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type","application/json");


            String putJsonData = "{" +
                    "\"action\" : \""+status+"\",\"pid\" : \""+pidJob+"\"}";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(putJsonData);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            wr.flush();
            wr.close();

        } catch (Exception e){
           e.printStackTrace();
        }

        return response.toString();
    }

    @Override
    public String getJobAlertMail(String serverId, String pidJob, HttpHeaders headers) {
        KatServer s = serverRepository.findOneById(serverId);

        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katjobmanager/alerts/mail/"+ pidJob;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            String basicAuth = headers.getHeaderString("Authorization");

            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestMethod("GET");
            con.setRequestProperty("Charset", "UTF-8");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e){
            logger.info(e.getMessage());
        }
        return response.toString();
    }

    @Override
    public String getJobJvmOptions(String serverId, String pidJob, HttpHeaders headers) {
        KatServer s = serverRepository.findOneById(serverId);

        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katjobmanager/jvmoptions/"+ pidJob;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            String basicAuth = headers.getHeaderString("Authorization");

            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestMethod("GET");
            con.setRequestProperty("Charset", "UTF-8");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e){
            logger.info(e.getMessage());
        }
        return response.toString();
    }

    @Override
    public String getContexts(String serverId, String pidJob, HttpHeaders headers) {
        KatServer s = serverRepository.findOneById(serverId);

        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl() + "/cxf/katjobmanager/contexts/properties/" + pidJob;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return response.toString();
    }

    @Override
    public String deleteContexts(String serverId, String pidJob, HttpHeaders headers) {
        KatServer s = serverRepository.findOneById(serverId);

        StringBuffer response = new StringBuffer();
        try {
            String url = s.getJolokiaUrl() + "/cxf/katjobmanager/contexts/properties/" + pidJob;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestMethod("DELETE");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return response.toString();
    }

    public KatServer getServer(String serverId) {
        KatServer s = serverRepository.findOneById(serverId);
        return s;
    }

    public KatServer getServerAttributes(String serverId) {
        return serverRepository.findOneById(serverId);
    }

    @Override
    public KatServer updateServerAttributes(String serverId, KatServer server) {
        return serverRepository.save(server);
    }

    @Override
    public KatServer add(KatServer server) {
        return serverRepository.save(server);
    }

    @Override
    public void delete(String serverId, String environnementId) {
        this.deleteServer(serverId,environnementId);
    }

    public void deleteServer(String id, String environmentId) {
        serverRepository.remove(id, environmentId, null);
    }


    public KatServer add(KatServer server,String environmentId) {
            logger.info(environmentId +"===" + server.toString());
            return serverRepository.save(server, environmentId);
    }

    @Override
    public List<KatBundle> getBundles(String serverId, String type) {
        List<KatBundle> bundles = new ArrayList<KatBundle>();
        // Retrieve server

        KatServer s = serverRepository.findOneById(serverId);
        if (s == null || !s.isCorrectlyConfigured()){
            return null;
        }


        ObjectMapper mapper = new ObjectMapper();

        if (type == null){
            type = this.type;
        }
        if (s.getSystemName()== null || s.getSystemName().isEmpty()){
            logger.warn("System name is not valid");
            return null;
        }

        BasicRequest request = new BasicRequest();
        request.setJmxUrl("service:jmx:rmi:///jndi/rmi://localhost:1099/karaf-root");
        request.setKarafName("root");
        request.setUser("karaf");
        request.setPassword("karaf");

       /* String jsonRequest = null;
        try {
            jsonRequest = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Client client = ClientBuilder.newClient();
        Response response= client.target("http://localhost:8181/cxf/cave/deployer/bundles").request().post(Entity.json(jsonRequest));


        TypeReference<List<KatBundle>> mapType = new TypeReference<List<KatBundle>>() {};
        try {
            bundles = mapper.readValue(response.readEntity(String.class),mapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.close();
        client.close();*/

        return bundles;
    }

    @Override
    public KatServer getStatus(String serverId, HttpHeaders headers) {
        KatServer server = serverRepository.findOneById(serverId);
        String result = "";
        try {
            URL siteURL = new URL(server.getJolokiaUrl());
            HttpURLConnection connection = (HttpURLConnection) siteURL
                    .openConnection();
            String basicAuth = headers.getHeaderString("Authorization");
            connection.setRequestProperty ("Authorization", basicAuth);connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            if (code == 200 || code == 404) {
             server.setStatus("STARTED");
            }
        } catch (Exception e) {
            server.setStatus("STOP");
        }
        return server;
    }

    @Override
    public PhysicalStats getStats(String serverId) {
        KatServer s = serverRepository.findOneById(serverId);
        if (s == null || s.getJolokiaUrl() == null) {
            return null;
        }

        PhysicalStats physicalStats = new PhysicalStats();
      try {
            JMXConnector jmxConnector = getJmxConnector();


            MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
            Integer bundleNumber = getJmxCountBundle(mBeanServerConnection);

          // On reccupere le taux de mémoire utilisé
          Object o = mBeanServerConnection.getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage");
          CompositeData cd = (CompositeData) o;

          String memoryUsageUsed =  cd.get("used").toString();
          String memoryUsageMax = cd.get("max").toString();
          Integer memoryPerCent =  (Integer.parseInt(memoryUsageMax) / Integer.parseInt(memoryUsageUsed)) ;

          physicalStats.setProcessMemUsage(memoryPerCent.doubleValue());
          physicalStats.setBundlesNumber(bundleNumber);

          Object o2 = mBeanServerConnection.getAttribute(new ObjectName("java.lang:type=OperatingSystem"), "SystemCpuLoad");
          String cpuPerCent = o2.toString();
          physicalStats.setProcessCpuLoad(  Double.parseDouble(cpuPerCent) * 100);

          /* WHEN USING CAVE WS  **/
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        physicalStats.setFeaturesNumber(4);

        return physicalStats;
    }



    @Override
    public String getBundle(String serverId, String bundleId, String cellarGroupName) {
        return null;
    }

    @Override
    public String startBundle(String serverId, String bundleId, String cellarGroupName) {
        return null;
    }

    @Override
    public String stopBundle(String serverId, String bundleId, String cellarGroupName) {
        return null;
    }

    @Override
    public String uninstallBundle(String serverId, String bundleId, String cellarGroupName) {
        return null;
    }

    @Override
    public String restartBundle(String serverId, String bundleId) {
        return null;
    }

    @Override
    public String installBundle(String serverId, String bundleId, String cellarGroupName) {
        return null;
    }

    @Override
    public List<KatFeature> getFeatures(String serverId) {
        return null;
    }

    private JMXConnector connect(String jmxUrl, String karafName, String user, String password) throws Exception {
        JMXServiceURL jmxServiceURL = new JMXServiceURL(jmxUrl);
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        String[] credentials = new String[]{ user, password };
        env.put("jmx.remote.credentials", credentials);
        return JMXConnectorFactory.connect(jmxServiceURL, env);
    }

    private String getKatJobs(KatServer s, HttpHeaders headers) {
        StringBuffer response = new StringBuffer();
        List<ZipArtifact> results = new ArrayList<ZipArtifact>();
        try {
            String url = s.getJolokiaUrl()+"/cxf/katexec/jobs";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);

            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);// optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Charset", "UTF-8");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e){
            logger.info(e.getMessage());
        }
        logger.info(response.toString());
        return response.toString();
    }

    private JMXConnector getJmxConnector() throws Exception {
        JMXConnector jmxConnector = connect("service:jmx:rmi://localhost:44445/jndi/rmi://localhost:1100/karaf-katadmin", "katadmin", "karaf", "karaf");
        return jmxConnector;
    }

    private Integer getJmxCountBundle(MBeanServerConnection mBeanServerConnection) throws Exception {
        ObjectName name = new ObjectName("org.apache.karaf:type=bundle,name=" + "katadmin");
        TabularData tabularData = (TabularData) mBeanServerConnection.getAttribute(name, "Bundles");
        List<KatBundle> result = new ArrayList<KatBundle>();
        // On list tous les bundles
        Integer bundleNumber = 0;
        for (Object value : tabularData.values()) {
            CompositeData compositeData = (CompositeData) value;

            Long bundleId = (Long) compositeData.get("ID");
            String bundleName = (String) compositeData.get("Name");
            String bundleVersion = (String) compositeData.get("Version");
            String bundleState = (String) compositeData.get("State");
            Integer bundleStartLevel = (Integer) compositeData.get("Start Level");
            KatBundle bundle = new KatBundle();
            bundleNumber++;
        }
        return bundleNumber;
    }
}
