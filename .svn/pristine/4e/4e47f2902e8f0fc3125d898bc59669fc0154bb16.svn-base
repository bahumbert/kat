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

import com.idfor.kat.tools.UIBackend.beans.KatBackendProperties;
import com.idfor.kat.tools.UIBackend.dao.ServerRepository;
import com.idfor.kat.tools.UIBackend.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZipNexusUploaderEnvironmentImpl implements ZipNexusUploaderEnvironment {

    private static final Pattern FIND_VERSION = Pattern.compile("[\\-_]([0-9]+\\.[0-9]+)\\.zip$");
    private static final String UPLOAD_FOLDER =  System.getProperty("karaf.data") + "/tmp/zipper";
    public static final Logger logger = LoggerFactory.getLogger(KatRestServer.class);
    private ServerRepository serverRepository = new ServerRepository();

    @Override
    public String undeployArtifact(String artifactName, String serverId, String version, HttpHeaders headers) {
        KatServer server = serverRepository.findOneById(serverId);
        String urlPost = "";
        StringBuffer response = new StringBuffer();

        try {
            String urlDelete = server.getJolokiaUrl() + "/cxf/katjobmanager/binaries/"+ artifactName + '/'+ version;

            URL obj = new URL(urlDelete);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);
            // Setting basic post request
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;

            while ((output = in.readLine()) != null) {
                response.append(output);
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    @Override
    public String deployArtifact(ZipArtifact zipArtifact, String serverId, HttpHeaders headers) {
        KatServer server = serverRepository.findOneById(serverId);
        String urlPost = "";
        StringBuffer response = new StringBuffer();
        try {
            URL url = new URL(zipArtifact.getUrl());
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            String urlEncoded = URLEncoder.encode(uri.toASCIIString().replace("http://",""));

            urlPost = server.getJolokiaUrl() + "/cxf/katjobmanager/binaries/http%3A%2F%2F" + KatBackendProperties.getNexusUserid() + "%3A" + KatBackendProperties.getNexusPwd() + "%40" + urlEncoded;

            URL obj = new URL(urlPost);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);
            // Setting basic post request
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");


            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String output;


            while ((output = in.readLine()) != null) {
                response.append(output);
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    @Override
    public String deployCronArtifact(DeployCron deployCron, String serverId, HttpHeaders headers){
        KatServer server = serverRepository.findOneById(serverId);
        StringBuffer response = new StringBuffer();
        try {

            String url = server.getJolokiaUrl() + "/cxf/katjobmanager/schedule";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            String basicAuth = headers.getHeaderString("Authorization");
            con.setRequestProperty ("Authorization", basicAuth);
            // Setting basic post request
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Charset", "UTF-8");


            String postJsonData = "{" +
                    "\"name\" : \"" + deployCron.getArtifactName() + "\"," +
                    "\"version\" : \"" + deployCron.getVersion() + "\"," +
                    "\"context\" : \"" + deployCron.getContext() + "\"," +
                    "\"state\" : \"paused\"," +
                    "\"label\" : \""+deployCron.getLabel()+"\"," +
                    "\"description\" : \""+deployCron.getDescription()+"\"," +
                    "\"scheduling\" : \""+deployCron.getCron()+"\"" +
                    "}";

            logger.info(postJsonData);

            // Send post request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(postJsonData);
            writer.close();
            wr.close();


            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(),  "UTF-8"));
            String output;


            while ((output = in.readLine()) != null) {
                response.append(output);
            }

            in.close();
        } catch (Exception e){
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return response.toString();
    }

    @Override
    public Response uploadZipOnNexus(FileExtractor fileExtractor, HttpHeaders headers) {
        String file = fileExtractor.getFile();
        byte[] data = Base64.getDecoder().decode(file);

        Response response;

        try {
            createFolderIfNotExists(UPLOAD_FOLDER);

            try (OutputStream stream = new FileOutputStream(UPLOAD_FOLDER + "/job.zip")) {
                stream.write(data);
            }

            response = this.uploadNexus( fileExtractor.getName(),  headers );
        } catch (SecurityException se) {
            response = Response.serverError().entity("Impossible de créer le dossier").build();
        } catch (Exception ex) {
            response = Response.serverError().entity( ex ).build();
        }

        return response;
    }

    public String getZipOnNexus(String token) {
        StringBuffer response = new StringBuffer();
        List<ZipArtifact> results = new ArrayList<>();

        try {
            String url = KatBackendProperties.getNexusBrowse() + KatBackendProperties.getNexusRepository();

            if (! (token.equals("null"))) {
               url =  url.concat("&continuationToken="+token);
            }

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return response.toString();
    }

    private Response uploadNexus(String fileName, HttpHeaders headers) throws Exception {
        URL obj = new URL(KatBackendProperties.getCaveAddress() + "/cxf/cave/deployer/artifact/upload");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        Matcher matcher = FIND_VERSION.matcher(fileName);
        String version = null;
        Response response;

        String basicAuth = headers.getHeaderString("Authorization");

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty ("Authorization", basicAuth);
        con.setDoOutput(true);

        // Finds version from filename
        while(matcher.find()) {
            version = matcher.group(1);
            fileName = fileName.substring(0, fileName.lastIndexOf(version + ".zip") - 1);
        }

        if(version != null) {
            NexusRequest req = new NexusRequest(
                fileName,
                version,
                "file:///" + UPLOAD_FOLDER + "/job.zip"
            );

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(req.toJSON());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            boolean isSuccess = responseCode >= 200 && responseCode < 400;
            String output;

            InputStream is = isSuccess ? con.getInputStream() : con.getErrorStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuilder buffer = new StringBuilder();

            while ((output = in.readLine()) != null) {
                buffer.append(output);
            }

            in.close();

            response = Response.status( responseCode )
                .entity( isSuccess ? "Artifact successfully uploaded in Nexus" : this.extractNexusError( buffer.toString() ) )
                .build()
            ;
        } else {
            response = Response.serverError().entity("The artifact has no version number").build();
        }

        return response;
    }

    /**
     * Creates a folder to desired location if it not already exists
     *
     * @param dirName
     *            - full path to the folder
     * @throws SecurityException
     *             - in case you don't have permission to create the folder
     */
    private void createFolderIfNotExists(String dirName) throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }

    /**
     * Parse HTML errors from Karaf HTTP responses. Always returns the highest stack message
     *
     * @param responseHTML The HTML response
     * @return The parsed error message, or the default error message
     */
    private String extractNexusError(String responseHTML) {
        String error = "An unknown error occured during upload in Nexus repository";
        Matcher matcher = Pattern.compile("h3><pre>[^:]+:([^\\t]+)").matcher(responseHTML);

        while(matcher.find()) {
            error = matcher.group(1).trim();
        }

        return error;
    }
}