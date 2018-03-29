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
import com.idfor.kat.tools.UIBackend.dao.IamGroupRepository;
import com.idfor.kat.tools.UIBackend.model.*;
import com.idfor.kat.tools.UIBackend.model.iam.IamGroup;
import com.idfor.kat.tools.UIBackend.model.iam.IamUser;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AuthRestImpl implements AuthRest
{
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(KatRestEnvironment.class);
    private IamGroupRepository iamGroupRepository = new IamGroupRepository();
    private BundleContext bundleContext;

    @Context
    SecurityContext securityContext;

    /**
     * Web service that checks if a user has a defined role
     * @param auth user information
     * @return user name, token and group name
     */
    @Override
    public IamUser postAuth(UserAuthentication auth)
    {

        // Prepares structures for WS response
        IamUser user = new IamUser();
        IamGroup group = new IamGroup();

        // Finds out which is group of logged in user and get it
        List<String> allGroups = getAllDefinedGroups();
        String userGroup = null;
        Iterator<String> a = allGroups.iterator();
        while(a.hasNext() && userGroup == null) {
            userGroup = a.next();
            if (!securityContext.isUserInRole(userGroup)) userGroup = null;
        }

        IamGroup iamGrp = iamGroupRepository.findOneByLabel(userGroup);

        // Final assignments before sending response
        user.setId(securityContext.getUserPrincipal().getName());
        user.setFirstname("BasicAuth");
        user.setLastname("User");
        user.setToken(auth.getToken());
        user.setGroup(iamGrp);

        // Sends response
        return user;
    }

    @Override
    public Ping ping()
    {
        return new Ping();
    }

    /**
     * Concateanates all existing groups for login check
     * @return exhaustive list of defined groups
     */
    private List<String> getAllDefinedGroups(){
        List<String> allGroups = new ArrayList<>();
        if(KatBackendProperties.getViewerGroups() != null) allGroups.addAll(KatBackendProperties.getViewerGroups());
        if(KatBackendProperties.getSuperadminGroups() != null) allGroups.addAll(KatBackendProperties.getSuperadminGroups());
        if(KatBackendProperties.getAdminGroups() != null) allGroups.addAll(KatBackendProperties.getAdminGroups());
        if(KatBackendProperties.getDeveloperGroups() != null) allGroups.addAll(KatBackendProperties.getDeveloperGroups());
        if(KatBackendProperties.getOperatorGroups() != null) allGroups.addAll(KatBackendProperties.getOperatorGroups());
        return allGroups;
    }
}