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
import com.idfor.kat.tools.UIBackend.dao.IamContextGroupRepository;
import com.idfor.kat.tools.UIBackend.dao.ContextRepository;
import com.idfor.kat.tools.UIBackend.model.iam.IamContextGroup;

import java.util.List;

public class IamContextGroupRestImpl implements IamContextGroupRest {
    private IamContextGroupRepository iamContextGroupRepository = new IamContextGroupRepository();


    @Override
    public List<IamContextGroup> getAllContextGroups() {
        return this.iamContextGroupRepository.findAll();
    }

    @Override
    public IamContextGroup postContextGroup(IamContextGroup iamContextGroup) {
        return this.iamContextGroupRepository.save(iamContextGroup);
    }

    @Override
    public IamContextGroup putContextGroup(IamContextGroup iamContextGroup) {
        return this.iamContextGroupRepository.save(iamContextGroup);
    }

    @Override
    public void deleteContextGroup(String idContextGroup) {
        this.iamContextGroupRepository.delete(idContextGroup);
    }
}
