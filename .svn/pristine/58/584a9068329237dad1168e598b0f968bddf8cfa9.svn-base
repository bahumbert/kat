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

import { HttpCustomService } from '../http-custom.service';
import { Injectable } from '@angular/core';
import { UrlService } from '../url.service';
import { Observable } from 'rxjs';
import { KatBundle } from '../../model/KatBundle';
import {StringResponse} from "../../model/string-reponse";
import {VirtualClusterMap} from "../../model/virtual-cluster-map";
import {Router} from "@angular/router";
import {HttpClient, HttpParams} from "@angular/common/http";

/**
 * Created by bhumbert on 16/06/2017.
 */

@Injectable()
export class BundleManagerService extends HttpCustomService {

    constructor(
        private urlService: UrlService,
        private httpClient: HttpClient,
        routerS: Router
    ) {
        super(routerS);
    }

    getBundles(id: string, cellar?: boolean): Observable<Array<KatBundle>>{

        if (!id){
            return null;
        }

        let url = this.urlService.getBundlesUrl(id);

        if (cellar != null){
            if (cellar){
                url = this.urlService.getCellarBundlesUrl(id);
            }
            else {
                url = this.urlService.getVirtualBundlesUrl(id);
            }
        }

        return this.httpClient.get(url, this.getOptions())
            .map(this.extractData)
            .catch(this.handleError);

    }

    startOrStop(action: boolean, id: string, bundleId: number, cellar?: boolean,
                toChangeState?: Array<VirtualClusterMap>): Observable<StringResponse>{

        let params: HttpParams = new HttpParams();
        params.set('state', action.toString());

        let options = this.getOptions( params);
        let url = this.urlService.getUpdateBundleStateUrl(id, bundleId);

        if (cellar != null) {
            if (cellar) {
                url = this.urlService.getUpdateCellarBundleStateUrl(id, bundleId);
            } else {
                url = this.urlService.getUpdateVirtualBundleUrl(id, bundleId);

                return this.httpClient.post(url, toChangeState, options)
                    .catch(this.handleError)
                    .map(this.extractData)
                ;
            }
        }

        return this.httpClient.get(url, options)
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    restart(id: string, bundleId: number, cellar?: boolean, toRestart?: Array<VirtualClusterMap>): Observable<StringResponse>{

        let url = this.urlService.getRestartBundleUrl(id, bundleId);

        if (cellar != null) {
            if (cellar) {
                url = this.urlService.getRestartCellarBundleUrl(id, bundleId);
            } else {
                url = this.urlService.getRestartVirtualBundleUrl(id, bundleId);

                return this.httpClient.post(url, toRestart, this.getOptions())
                    .catch(this.handleError)
                    .map(this.extractData)
                ;
            }
        }

        return this.httpClient.get(url, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    uninstall(id: string, bundleId: number, cellar?: boolean, toUninstall?: Array<VirtualClusterMap>): Observable<StringResponse>{
        let url = this.urlService.getUninstallBundleUrl(id, bundleId);

        if (cellar != null) {
            if (cellar) {
                url = this.urlService.getUninstallCellarBundleUrl(id, bundleId);
            } else {
                url = this.urlService.getUninstallVirtualBundleUrl(id, bundleId);

                return this.httpClient.post(url, toUninstall, this.getOptions())
                    .catch(this.handleError)
                    .map(this.extractData)
                ;
            }
        }

        return this.httpClient.get(url, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    postBundleFile(file: File, extension: string, serverId: string): Observable<string>{
        let params: HttpParams = new HttpParams();

        if (file.name != null && extension != null) {
            params.set('name', file.name);
            params.set('extension', extension);
        }

        let formData = new FormData();
        formData.append('file', file);

        return this.httpClient.post(this.urlService.getUploadBundleUrl(serverId), formData, this.getOptions(params))
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }
}
