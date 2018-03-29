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
 * Created by bhumbert on 20/04/2017.
 */

import {Injectable} from '@angular/core';
import {UrlService} from '../url.service'
import {HttpCustomService} from '../http-custom.service';
import {Observable} from 'rxjs';
import {KatCluster} from '../../model/KatCluster';
import {StringResponse} from '../../model/string-reponse';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class ClusterService extends HttpCustomService {

    constructor(
        private urlService: UrlService,
        private httpClient: HttpClient,
        routerS: Router
    ) {
        super(routerS);
    }

    getClusterList(): Observable<Array<KatCluster>> {
        return this.httpClient
            .get(this.urlService.getClusterUrl(), this.getOptions())
            .map(this.extractData)
            .catch(this.handleError);
    }

    getClusterListWithoutOneEnvironment(environmentId: string): Observable<Array<KatCluster>> {
        return this.httpClient
            .get(this.urlService.getClusterWithoutOneEnvironmentUrl(environmentId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getClusterListByEnvironment(envId: string): Observable<Array<KatCluster>> {
        return this.httpClient
            .get(this.urlService.getClusterByEnvironmentUrl(envId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    add(cluster: KatCluster, environmentId): Observable<KatCluster> {
        return this.httpClient
            .post(this.urlService.getAddClusterUrl(environmentId), cluster, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    update(cluster: KatCluster): Observable<KatCluster> {
        return this.httpClient
            .put(this.urlService.getUpdateClusterUrl(cluster.id), cluster, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    remove(cluster: KatCluster, environmentId: string): Observable<any> {
        return this.httpClient
            .delete(this.urlService.getDeleteClusterUrl(cluster.id, environmentId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    checkIsCellarInstalled(cluster: KatCluster): Observable<StringResponse> {
        return this.httpClient
            .get(this.urlService.getCheckIsCellarInstalledUrl(cluster.id), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    installCellar(cluster: KatCluster): Observable<StringResponse> {
        return this.httpClient
            .post(this.urlService.getInstallCellarUrl(cluster.id), null, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    addExisting(clusterId: string, environmentId: string): Observable<KatCluster> {
        return this.httpClient
            .post(this.urlService.getAddExistingClusterUrl(clusterId, environmentId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }
}
