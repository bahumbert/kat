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

import { Injectable } from '@angular/core';
import { UrlService } from 'app/services/url.service';
import { HttpCustomService } from './http-custom.service';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';
import { KatEnvironment } from '../model/KatEnvironment';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

/**
 * Created by bhumbert on 07/07/2017.
 */

@Injectable()
export class EnvironmentService extends HttpCustomService {

    constructor(
        private urlService: UrlService,
        private httpClient: HttpClient,
        routerS: Router
    ) {
        super(routerS);
    }

    getEnvironments(): Observable<Array<KatEnvironment>>{
        return this.httpClient
            .get(this.urlService.getEnvironmentsUrl(), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    addEnvironment(environment: KatEnvironment): Observable<KatEnvironment>{
        return this.httpClient
            .post(this.urlService.getAddEnvironmentUrl(), environment, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    removeEnvironment(id: string){
        return this.httpClient
            .delete(this.urlService.getRemoveEnvironmentUrl(id), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    updateEnvironment(environment: KatEnvironment): Observable<KatEnvironment>{
        return this.httpClient
            .put(this.urlService.getUpdateEnvironmentUrl(environment.id), environment, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getEnvironmentById(id: string): Observable<KatEnvironment>{
        return this.httpClient
            .get(this.urlService.getEnvironmentByIdUrl(id), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }
}