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

import {Injectable} from '@angular/core';
import {ConfigurationService} from './configuration.service';

@Injectable()
export class UrlService {

    constructor(
        private configurationService: ConfigurationService
    ) {}

    undeployWorkflowUrl(idServer: string, workflowFile: string): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getOneWorkflowUrl() + '/server/' + idServer + '/workflow-file/' + workflowFile ;
    }
    deployWorkflowUrl(idServer, idWorkflow): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getOneWorkflowUrl(idWorkflow) + '/server/' + idServer ;
    }

    getJobUpdateUrl(idServer: string) {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/job/update' ;
    }
    getJobVersionUrl(idServer: string, jobName: string) {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/' + jobName + '/versions' ;
    }
    getContextUrl(id = null): string {
        if (id === null) {
            return this.getApiUrl() + '/contexts';
        } else {
            id = id.replace(/#/g, '%23');
            return this.getApiUrl() + '/contexts/' + id;
        }
    }

    getContextGroupUrl(id = null): string {
        if (id === null) {
            return this.getApiUrl() + '/group-contexts';
        } else {
            id = id.replace(/#/g, '%23');
            return this.getApiUrl() + '/group-contexts/' + id;
        }
    }

    getGroupUrl(id: string = null): string {
        if (id === null) {
            return this.getApiUrl() + '/groups';
        } else {
            id = id.replace(/#/g, '%23');
            return this.getApiUrl() + '/groups/' + id;
        }
    }
    getUserUrl(): string {
        return this.getApiUrl() + '/users';
    }
    getRolesUrl(): string {
        return this.getApiUrl() + '/roles';
    }
    getOverridedContext(pidFile: string, idServer: string): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/job/' + pidFile + '/overrided-context' ;
    }
    postOverridedContext( idServer: string ): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/overrided-context' ;
    }
    postEmailAlert(idServer: string): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/alert-mail' ;
    }
    getEmailAlert(pidFile: string, idServer: string): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/job/' + pidFile + '/alert-mail' ;
    }
    getArtifactDeploymentStepUrl(idServer, nameJob, version): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/job/' + nameJob + '/version/' + version;
    }

    getApiUrl() {
        return this.configurationService.getBaseUrl();
    }

    getEnvironmentsUrl() {
        return this.getApiUrl() + '/environments';
    }

    getAddEnvironmentUrl() {
        return this.getEnvironmentsUrl();
    }

    getRemoveEnvironmentUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getEnvironmentsUrl() + '/' + id;
    }

    getUpdateEnvironmentUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getEnvironmentsUrl() + '/' + id;
    }

    getServerArtifactNexusUrl(token: string): string {
        return this.getApiUrl() + '/zip-nexus/token/' + token;
    }

    getKatJobUrl(idServer: string): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/scheduled-job';
    }

    getKatJobDeployedUrl(idServer: string): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/artifacts';

    }

    deleteKatJobUrl(idServer: string, fileName: string): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/scheduled-job/' + fileName;
    }

    changeKatJobStatusUrl(idServer: string, pid: string, status: string): string {
        idServer = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + idServer + '/job/' + pid + '/state-job/' + status;
    }

    artifactDeployUrl(serverId: string) {
        serverId = serverId.replace(/#/g, '%23');
        return this.getApiUrl() + '/zip-nexus/deploy/server/' + serverId;
    }

    artifactUndeployUrl(artifactName: string, version: string, serverId: string) {
        serverId = serverId.replace(/#/g, '%23');
        return this.getApiUrl() + '/zip-nexus/undeploy/' + artifactName + '/server/' + serverId + '/version/' + version;
    }

    artifactDeployCron(serverId: string) {
        serverId = serverId.replace(/#/g, '%23');
        return this.getApiUrl() + '/zip-nexus/deploy-cron/server/' + serverId;
    }


    getEnvironmentByIdUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getEnvironmentsUrl() + '/' + id;
    }

    getStatsUrl() {
        return this.getApiUrl() + '/stats';
    }

    getServerUrl() {
        return this.getApiUrl() + '/servers';
    }

    getServerByEnvironmentUrl(envId: string) {
        envId = envId.replace(/#/g, '%23');
        return this.getEnvironmentsUrl() + '/' + envId + '/servers';
    }

    getServerWithoutOneEnvironmentUrl(environmentId: string) {
        environmentId = environmentId.replace(/#/g, '%23');
        return this.getServerUrl() + '/withoutOneEnv/' + environmentId;
    }

    getBrokerUrl() {
        return this.getApiUrl() + '/brokers';
    }

    getBrokerWithoutOneEnvironmentUrl(environmentId: string) {
        environmentId = environmentId.replace(/#/g, '%23');
        return this.getBrokerUrl() + '/withoutOneEnv/' + environmentId;
    }

    getServerWithoutClusterUrl() {
        return this.getServerUrl();
    }

    getServerByClusterUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getClusterUrl() + '/' + id + '/servers';
    }

    getBrokerWithoutClusterUrl() {
        return this.getBrokerUrl();
    }

    getBrokerByClusterUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getClusterUrl() + '/' + id + '/brokers';
    }

    getServerByEnvironmentAndClusterUrl(envId: string, clusterId: string) {
        envId = envId.replace(/#/g, '%23');
        if (clusterId)
            clusterId = clusterId.replace(/#/g, '%23');

        return this.getEnvironmentsUrl() + '/' + envId + '/clusters/' + clusterId + '/servers';
    }

    getBrokerByEnvironmentUrl(envId: string) {
        envId = envId.replace(/#/g, '%23');
        return this.getEnvironmentsUrl() + '/' + envId + '/brokers';
    }

    getUpdateServerUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + id;
    }

    getUpdateBrokerUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getBrokerUrl() + '/' + id;
    }

    getCellarStateUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + id + '/cellar';
    }

    getAddServerUrl(environmentId: string) {
        environmentId = environmentId.replace(/#/g, '%23');
        return this.getEnvironmentsUrl() + '/' + environmentId + '/servers';
    }

    getAddBrokerUrl(environmentId: string) {
        environmentId = environmentId.replace(/#/g, '%23');
        return this.getBrokerUrl() + '/' + environmentId;
    }

    getServerShutdownUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getServerUrl() + '/shutdown/' + id;
    }

    getServerRestartUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getServerUrl() + '/restart/' + id;
    }

    getServerStatusUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + id + '/status/';
    }

    getServerSshStartUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getServerUrl() + '/start/' + id;
    }

    getDeleteServerUrl(id: string, environmentId: string, clusterId: string) {
        id = id.replace(/#/g, '%23');
        environmentId = environmentId.replace(/#/g, '%23');
        if (clusterId) {
            clusterId = clusterId.replace(/#/g, '%23');
            return this.getServerUrl() + '/' + id + '/' + environmentId + '/' + clusterId;
        }
        return this.getServerUrl() + '/' + id + '/' + environmentId;
    }

    getDeleteBrokerUrl(id: string, environmentId: string) {
        id = id.replace(/#/g, '%23');
        environmentId = environmentId.replace(/#/g, '%23');
        return this.getBrokerUrl() + '/' + id + '/' + environmentId;
    }

    getBrokerStatsUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getStatsUrl() + '/broker/' + id;
    }

    getBrokerStatusUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getBrokerUrl() + '/status/' + id;
    }

    getBrokerShutdownUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getBrokerUrl() + '/shutdown/' + id;
    }

    getBrokerRestartUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getBrokerUrl() + '/restart/' + id;
    }

    getBrokerSshStartUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getBrokerUrl() + '/start/' + id;
    }

    getClusterUrl() {
        return this.getApiUrl() + '/clusters';
    }

    getClusterWithoutOneEnvironmentUrl(environmentId: string) {
        environmentId = environmentId.replace(/#/g, '%23');
        return this.getClusterUrl() + '/withoutOneEnv/' + environmentId;
    }

    getClusterByEnvironmentUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getEnvironmentsUrl() + '/' + id + '/clusters/';
    }

    getUpdateClusterUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getClusterUrl() + '/' + id;
    }

    getAddClusterUrl(environmentId: string) {
        environmentId = environmentId.replace(/#/g, '%23');
        return this.getClusterUrl() + '/' + environmentId;
    }

    getDeleteClusterUrl(id: string, environmentId: string) {
        id = id.replace(/#/g, '%23');
        environmentId = environmentId.replace(/#/g, '%23');
        return this.getClusterUrl() + '/' + id + '/' + environmentId;
    }

    getServerStatsUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getApiUrl() + '/servers/' + id + '/stats';
    }

    getBundlesUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + id + '/bundles';
    }

    getFeaturesUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + id + '/features';
    }

    getUpdateBundleStateUrl(serverId: string, bundleId: number) {
        return this.getBundlesUrl(serverId) + '/state/' + bundleId;
    }

    getUninstallBundleUrl(serverId: string, bundleId: number) {
        return this.getBundlesUrl(serverId) + '/uninstall/' + bundleId;
    }

    getUploadBundleUrl(id) {
        return this.getBundlesUrl(id);
    }

    getRestartBundleUrl(serverId: string, bundleId: number) {
        return this.getBundlesUrl(serverId) + '/restart/' + bundleId;
    }

    getUninstallFeatureUrl(serverId: string, featureId: string) {
        return this.getFeaturesUrl(serverId) + '/' + featureId;
    }

    getInstallFeatureUrl(serverId: string) {
        return this.getFeaturesUrl(serverId);
    }

    getRepositoriesUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + id + '/repositories';
    }

    getRemoveRepositoryUrl(id: string, name: string) {
        return this.getRepositoriesUrl(id) + '/' + name;
    }

    getAddRepositoryUrl(id: string) {
        return this.getRepositoriesUrl(id);
    }

    getCellarUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getClusterUrl() + '/' + id + '/cellar';
    }

    getCheckIsCellarInstalledUrl(id: string) {
        return this.getCellarUrl(id);
    }

    getInstallCellarUrl(id: string) {
        return this.getCellarUrl(id);
    }

    getCellarBundlesUrl(id: string) {
        return this.getCellarUrl(id) + '/bundles';
    }

    getUpdateCellarBundleStateUrl(id: string, bundleId: number) {
        return this.getCellarBundlesUrl(id) + '/state/' + bundleId;
    }

    getRestartCellarBundleUrl(id: string, bundleId: number) {
        return this.getCellarBundlesUrl(id) + '/restart' + bundleId;
    }

    getUninstallCellarBundleUrl(id: string, bundleId: number) {
        return this.getCellarBundlesUrl(id) + '/uninstall/' + bundleId;
    }

    getCellarFeaturesUrl(id: string) {
        return this.getCellarUrl(id) + '/features';
    }

    getUninstallCellarFeatureUrl(id: string, featureId: string) {
        return this.getCellarFeaturesUrl(id) + '/' + featureId;
    }

    getInstallCellarFeatureUrl(id: string) {
        return this.getCellarFeaturesUrl(id);
    }

    getCellarRepositoriesUrl(id: string) {
        return this.getCellarUrl(id) + '/repositories';
    }

    getCellarRemoveRepositoryUrl(id: string) {
        return this.getCellarUrl(id) + '/repositories/remove';
    }

    getCellarAddRepositoryUrl(id: string) {
        return this.getCellarUrl(id) + '/repositories';
    }

    getVirtualUrl(id: string) {
        id = id.replace(/#/g, '%23');
        return this.getClusterUrl() + '/' + id + '/virtual';
    }

    getVirtualBundlesUrl(id: string) {
        return this.getVirtualUrl(id) + '/bundles';
    }

    getUpdateVirtualBundleUrl(id: string, bundleId: number) {
        return this.getVirtualBundlesUrl(id) + '/state/' + bundleId;
    }

    getRestartVirtualBundleUrl(id: string, bundleId: number) {
        return this.getVirtualBundlesUrl(id) + '/restart/' + bundleId;
    }

    getUninstallVirtualBundleUrl(id: string, bundleId: number) {
        return this.getVirtualBundlesUrl(id) + '/uninstall/' + bundleId;
    }

    getVirtualFeaturesUrl(id: string) {
        return this.getVirtualUrl(id) + '/features';
    }

    getUninstallVirtualFeatureUrl(id: string, featureId: string) {
        return this.getVirtualFeaturesUrl(id) + '/' + featureId;
    }

    getInstallVirtualFeatureUrl(id: string) {
        return this.getVirtualFeaturesUrl(id);
    }

    getVirtualRepositoriesUrl(id: string) {
        return this.getVirtualUrl(id) + '/repositories';
    }

    getVirtualRemoveRepositoryUrl(id: string) {
        return this.getVirtualUrl(id) + '/repositories/remove';
    }

    getVirtualAddRepositoryUrl(id: string) {
        return this.getVirtualUrl(id) + '/repositories';
    }

    getAddExistingClusterUrl(clusterId: string, environmentId: string) {
        clusterId = clusterId.replace(/#/g, '%23');
        environmentId = environmentId.replace(/#/g, '%23');

        return this.getClusterUrl() + '/existing/' + clusterId + '/' + environmentId;
    }

    getAddExistingServerUrl(serverId: string, environmentId: string, clusterId: string) {
        serverId = serverId.replace(/#/g, '%23');
        environmentId = environmentId.replace(/#/g, '%23');
        if (clusterId) {
            clusterId = clusterId.replace(/#/g, '%23');
            return this.getServerUrl() + '/existing/' + serverId + '/' + environmentId + '/' + clusterId;
        }
        return this.getServerUrl() + '/existing/' + serverId + '/' + environmentId;
    }

    getAddExistingBrokerUrl(brokerId: string, environmentId: string, clusterId: string) {
        brokerId = brokerId.replace(/#/g, '%23');
        environmentId = environmentId.replace(/#/g, '%23');
        if (clusterId) {
            clusterId = clusterId.replace(/#/g, '%23');
            return this.getBrokerUrl() + '/existing/' + brokerId + '/' + environmentId + '/' + clusterId;
        }

        return this.getBrokerUrl() + '/existing/' + brokerId + '/' + environmentId;
    }

    getOneWorkflowUrl(wkId = null): string {
        if ( wkId !== null ) {
            let id = wkId.replace(/#/g, '%23');
            return this.getApiUrl() + '/workflow/' + id;
        } else {
            return this.getApiUrl() + '/workflow';
        }
    }

    getDeployedWorkflowUrl(idServer): string {
        let id = idServer.replace(/#/g, '%23');
        return this.getServerUrl() + '/' + id + '/workflow-deployed';
    }

    getAuthUrl() {
        return 'http://localhost:8182/cxf/kat/auth';
    }

    getPingUrl() {
        return this.getApiUrl() + '/ping';
    }

}
