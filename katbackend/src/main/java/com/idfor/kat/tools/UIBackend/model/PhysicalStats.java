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

public class PhysicalStats {

    private Double processCpuLoad;
    private Double systemCpuLoad;
    private Double systemMemUsage;
    private Double processMemUsage;
    private Integer bundlesNumber;
    private Integer featuresNumber;

    public PhysicalStats() {
    }

    public PhysicalStats(Double processCpuLoad, Double systemCpuLoad, Double systemMemUsage, Double processMemUsage) {
        this.processCpuLoad = processCpuLoad;
        this.systemCpuLoad = systemCpuLoad;
        this.systemMemUsage = systemMemUsage;
        this.processMemUsage = processMemUsage;
    }

    public PhysicalStats(Double processCpuLoad, Double systemCpuLoad, Double systemMemUsage, Double processMemUsage, Integer bundlesNumber, Integer featuresNumber) {
        this.processCpuLoad = processCpuLoad;
        this.systemCpuLoad = systemCpuLoad;
        this.systemMemUsage = systemMemUsage;
        this.processMemUsage = processMemUsage;
        this.bundlesNumber = bundlesNumber;
        this.featuresNumber = featuresNumber;
    }

    public Double getProcessCpuLoad() {
        return processCpuLoad;
    }

    public void setProcessCpuLoad(Double processCpuLoad) {
        this.processCpuLoad = processCpuLoad;
    }

    public Double getSystemCpuLoad() {
        return systemCpuLoad;
    }

    public void setSystemCpuLoad(Double systemCpuLoad) {
        this.systemCpuLoad = systemCpuLoad;
    }

    public Double getSystemMemUsage() {
        return systemMemUsage;
    }

    public void setSystemMemUsage(Double systemMemUsage) {
        this.systemMemUsage = systemMemUsage;
    }

    public Double getProcessMemUsage() {
        return processMemUsage;
    }

    public void setProcessMemUsage(Double processMemUsage) {
        this.processMemUsage = processMemUsage;
    }

    public Integer getBundlesNumber() {
        return bundlesNumber;
    }

    public void setBundlesNumber(Integer bundlesNumber) {
        this.bundlesNumber = bundlesNumber;
    }

    public Integer getFeaturesNumber() {
        return featuresNumber;
    }

    public void setFeaturesNumber(Integer featuresNumber) {
        this.featuresNumber = featuresNumber;
    }
}
