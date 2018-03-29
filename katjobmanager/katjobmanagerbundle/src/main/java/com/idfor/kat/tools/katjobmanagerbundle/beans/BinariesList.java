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

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds a collection of deployed jobs (binaries) on the execution server
 * These jobs may not have been scheduled yet, it is just a list of existing installed TALEND jobs
 * @see BinariesProperties
 */
public class BinariesList {

    // The collection itself
    private static List<BinariesProperties> binariesList = new ArrayList<>();

    /**
     * Adds deployed TALEND job binaries to a collection of installed binaries
     * @param binaries the binaries object to add
     */
    public static void add(BinariesProperties binaries){
        binariesList.add(binaries);
    }

    /**
     * Returns whole list of deployed binaries
     * @return a list of deployed job binaries
     */
    public static List<BinariesProperties> getBinariesList() {
        return binariesList;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}