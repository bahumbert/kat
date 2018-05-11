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

/**
 * Created by bhumbert on 19/04/2017.
 */

import { Injectable } from '@angular/core';
import { UrlService } from '../url.service';
import { HttpCustomService } from '../http-custom.service';
import { Observable } from 'rxjs';
import { KatServer } from '../../model/KatServer';
import { KatCluster } from '../../model/KatCluster';
import { NotificationsService } from 'angular2-notifications';
import { DeploymentCron } from '../../model/deployment-cron';
import { KatJob } from '../../model/katJob';
import { Artifact } from '../../model/artifact';
import { Router } from '@angular/router';

import {
    HttpClient, HttpEventType, HttpRequest, HttpResponse, HttpEvent,
    HttpErrorResponse, HttpParams
} from '@angular/common/http';
import { MailNotif } from '../../model/mailNotif';
import { OverridedContext } from '../../model/overridedContext';
import { ScheduleUpdater } from '../../model/scheduleUpdater';
import {AuthService} from "../auth/auth.service";
import {ParamJvm} from "../../model/paramJvm";

@Injectable()
export class ServerService extends HttpCustomService {

    constructor (
        private urlService: UrlService,
        private httpClient: HttpClient,
        private notificationsService: NotificationsService,
        private authService: AuthService,
        routerS: Router
    ) {
        super(routerS);
    }
    updateJob(model: ScheduleUpdater, idServer: string) {
        return this.httpClient
            .put(this.urlService.getJobUpdateUrl(idServer), model, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    getJobsVersion(jobName: string, idServer: string) {
        return this.httpClient
            .get(this.urlService.getJobVersionUrl(idServer, jobName), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    deleteOveridedContext( propertyFile: string, idServer: string ): Observable<OverridedContext> {
        return this.httpClient
            .delete(this.urlService.getOverridedContext(propertyFile, idServer), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    postOverridedContext( idServer: string, data: OverridedContext ) {
        return this.httpClient
            .post(this.urlService.postOverridedContext(idServer), data , this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    getOveridedContext( propertyFile: string, idServer: string ): Observable<OverridedContext> {
        return this.httpClient
            .get(this.urlService.getOverridedContext(propertyFile, idServer), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    postEmailAlert( idServer: string, data: MailNotif): Observable<Boolean> {
        return this.httpClient
            .post(this.urlService.postEmailAlert(idServer), data , this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }

    getEmailAlert(propertyFile: string, idServer: string): Observable<MailNotif> {
        return this.httpClient
            .get(this.urlService.getEmailAlert(propertyFile, idServer), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    postParamJvm( idServer: string, paramJvm: ParamJvm): Observable<Boolean> {
        console.log("param JVM");
        console.log(paramJvm);
        return this.httpClient
            .post(this.urlService.postJvmParams(idServer), paramJvm,  this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }

    getParam(propertyFile: string, idServer: string): Observable<any> {
        return this.httpClient
            .get(this.urlService.getParams(propertyFile, idServer), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }


    getPing(): Observable<Object> {
        return this.httpClient
            .get(this.urlService.getPingUrl(), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }
    getDeployedArtifact(idServer: string): Observable<Artifact[]> {
        return this.httpClient
            .get(this.urlService.getKatJobDeployedUrl(idServer), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getIsScheduledArtifact(idServer, nameJob, version): Observable<boolean> {
        return this.httpClient
            .get(this.urlService.getArtifactDeploymentStepUrl(idServer, nameJob, version), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }

    getArtifactOnKat(id): Observable<Artifact[]> {
        return this.httpClient
            .get(this.urlService.getKatJobDeployedUrl(id), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    postFile(data, onProgress: (event: HttpEvent<any>, percent: number) => void): Promise<{success: boolean, message: string}> {
        const req = new HttpRequest('POST', this.urlService.getApiUrl() + '/zip-nexus', data, {
            headers: this.getOptions().headers,
            reportProgress: true,
            responseType: 'text'
        });

        return new Promise<{success: boolean, message: string}>((resolve, reject) => {
            this.httpClient.request(req)
                .catch((err: HttpErrorResponse) => {
                    resolve({
                        success: false,
                        message: err.error
                    });
                    return this.handleError(err);
                })
                .subscribe(event => {
                    if (event.type === HttpEventType.UploadProgress) {
                        onProgress(event, Math.round(100 * event.loaded / event.total));
                    } else if (event instanceof HttpResponse) {
                        onProgress(event, 100);

                        resolve({
                            success: event.status >= 200 && event.status <= 300,
                            message: event.body ? event.body : "Artifact successfully uploaded in Nexus"
                        });
                    }
                })
            ;
        });
    }

    getArtifactOnNexusServer(tokenContinuation): Observable<any> {
        return this.httpClient
            .get(this.urlService.getServerArtifactNexusUrl(tokenContinuation), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getServerList(): Observable<Array<KatServer>> {
        return this.httpClient
            .get(this.urlService.getServerUrl(), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getServerListByEnvironment(environmentId: string): Observable<Array<KatServer>> {
        return this.httpClient
            .get(this.urlService.getServerByEnvironmentUrl(environmentId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getServerListWithoutOneEnvironment(environmentId: string): Observable<Array<KatServer>> {
        return this.httpClient
            .get(this.urlService.getServerWithoutOneEnvironmentUrl(environmentId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getServerListByCluster(cluster: KatCluster): Observable<Array<KatServer>> {
        if (cluster) {
            return this.httpClient
                .get(this.urlService.getServerByClusterUrl(cluster.id), this.getOptions())
                .catch(this.handleError)
                .map(this.extractData)
            ;
        }

        let params: HttpParams = new HttpParams();
        params.set('lonely', 'true');

        return this.httpClient
            .get(this.urlService.getServerWithoutClusterUrl(), this.getOptions(params))
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getServerListByEnvironmentAndCluster(environmentId: string, clusterId: string): Observable<Array<KatServer>> {
        return this.httpClient
            .get(this.urlService.getServerByEnvironmentAndClusterUrl(environmentId, clusterId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getCellarState(server: KatServer): Observable<boolean> {
        return this.httpClient
            .get(this.urlService.getCellarStateUrl(server.id), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    add(server: KatServer, environmentId: string): Observable<KatServer> {
        return this.httpClient
            .post(this.urlService.getAddServerUrl(environmentId), server, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    update(server: any): Observable<KatServer> {
        return this.httpClient
            .put(this.urlService.getUpdateServerUrl(server.id), server, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    remove(server: KatServer, environmentId: string, cluster: KatCluster): Observable<any> {
        let clusterId = cluster ? cluster.id : null;

        return this.httpClient
            .delete(this.urlService.getDeleteServerUrl(server.id, environmentId, clusterId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getServerStatus(server: KatServer): Observable<KatServer> {
        return this.httpClient
            .get(this.urlService.getServerStatusUrl(server.id), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getServerStats(server: KatServer) {
        if (this.authService.getUser()) {

        server.serverStats = null;

        let stats = this.httpClient
            .get(this.urlService.getServerStatsUrl(server.id), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;

        stats
            .expand(() => Observable.timer(30000).concatMap(() => stats))
            .subscribe(
                serverStats => {
                    server.serverStats = serverStats;
                },
                error => {
                    if (this.authService.getUser()) {
                        this.notificationsService.error('An error occured', 'Can\'t get "' + server.name + '"\'s status, please check its cave url');
                    }
                }
            )
        ;
        }
    }

    shutdown(server: KatServer): Observable<any> {
        return this.httpClient
            .get(this.urlService.getServerShutdownUrl(server.id), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    restart(server: KatServer): Observable<any> {
        return this.httpClient
            .get(this.urlService.getServerRestartUrl(server.id), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    addExisting(serverId: string, environmentId: string, clusterId?: string): Observable<KatServer> {
        return this.httpClient
            .post(this.urlService.getAddExistingServerUrl(serverId, environmentId, clusterId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    deployArtifact(urlArtifact: string, serverId: string) {
        return this.httpClient
            .post(this.urlService.artifactDeployUrl(serverId),{'url': urlArtifact}, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    undeployArtifact(artifactName: string, artifactVersion: string, idServer: string) {
        return this.httpClient
            .delete(this.urlService.artifactUndeployUrl(artifactName, artifactVersion, idServer), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    deployCronArtifact(deploymentCron: DeploymentCron, serverId: string) {
        return this.httpClient
            .post(this.urlService.artifactDeployCron(serverId), deploymentCron, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getKatJobs(idServer: string): Observable<KatJob[]> {
        return this.httpClient
            .get(this.urlService.getKatJobUrl(idServer), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    deleteKatJobs(idServer: string, fileName: string): Observable<boolean> {
        return this.httpClient
            .delete(this.urlService.deleteKatJobUrl(idServer, fileName), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    changeKatJobStatus(idServer: string, pid, status): Observable<KatJob[]> {
        return this.httpClient
            .put(this.urlService.changeKatJobStatusUrl(idServer, pid, status), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }
}
