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
import { Feature } from '../../model/feature';
import {StringResponse} from "../../model/string-reponse";
import {VirtualClusterMap} from "../../model/virtual-cluster-map";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

/**
 * Created by bhumbert on 16/06/2017.
 */

@Injectable()
export class FeatureManagerService extends HttpCustomService {

    constructor(
        private urlService: UrlService,
        private httpClient: HttpClient,
        routerS: Router
    ) {
        super(routerS);
    }

    getFeatures(id: string, cellar?: boolean): Observable<Array<Feature>>{
        if (!id){
            return null;
        }

        let url = this.urlService.getFeaturesUrl(id);

        if (cellar != null){
            if (cellar){
                url = this.urlService.getCellarFeaturesUrl(id);
            }
            else {
                url = this.urlService.getVirtualFeaturesUrl(id);
            }
        }

        return this.httpClient
            .get(url, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    uninstall(id: string, featureId: string, cellar?: boolean, toUninstall?: Array<VirtualClusterMap>): Observable<StringResponse>{
        let url = this.urlService.getUninstallFeatureUrl(id, featureId);

        if (cellar != null) {
            if (cellar) {
                url = this.urlService.getUninstallCellarFeatureUrl(id, featureId);
            } else {
                url = this.urlService.getUninstallVirtualFeatureUrl(id, featureId);

                return this.httpClient.post(url, toUninstall, this.getOptions())
                    .catch(this.handleError)
                    .map(this.extractData)
                ;
            }
        }

        return this.httpClient.delete(url, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    install(id: string, feature: Feature, cellar?: boolean, toInstall?: Array<VirtualClusterMap>): Observable<StringResponse>{
        let url = this.urlService.getInstallFeatureUrl(id);

        if (cellar != null) {
            if (cellar){
                url = this.urlService.getInstallCellarFeatureUrl(id);
            } else {
                url = this.urlService.getInstallVirtualFeatureUrl(id);

                return this.httpClient.post(url, toInstall, this.getOptions())
                    .catch(this.handleError)
                    .map(this.extractData)
                ;
            }
        }

        return this.httpClient
            .post(url, feature, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }
}
