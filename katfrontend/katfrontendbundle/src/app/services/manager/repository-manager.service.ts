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
import { Repository } from '../../model/repository';
import {StringResponse} from "app/model/string-reponse";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

/**
 * Created by bhumbert on 27/06/2017.
 */

@Injectable()
export class RepositoryManagerService extends HttpCustomService {

    constructor(
        private urlService: UrlService,
        private httpClient: HttpClient,
        routerS: Router
    ) {
        super(routerS);
    }

    getRepositories(id: string, cellar?: boolean): Observable<Array<Repository>> {
        if (!id) {
            return null;
        }

        let url = this.urlService.getRepositoriesUrl(id)

        if (cellar != null){
            if (cellar){
                url = this.urlService.getCellarRepositoriesUrl(id);
            }
            else {
                url = this.urlService.getVirtualRepositoriesUrl(id);
            }
        }

        return this.httpClient
            .get(url, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    remove(id: string, name: string, cellar?: boolean): Observable<StringResponse>{
        let url = this.urlService.getRemoveRepositoryUrl(id, name);

        if (cellar != null ) {
            let r = new Repository();
            r.uri = name;

            if (cellar) {
                url = this.urlService.getCellarRemoveRepositoryUrl(id);

                return this.httpClient.post(url, r, this.getOptions())
                    .catch(this.handleError)
                    .map(this.extractData)
                ;
            }
            else {
                url = this.urlService.getVirtualRemoveRepositoryUrl(id);

                return this.httpClient.post(url, r, this.getOptions())
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

    add(id: string, repository: Repository, cellar?: boolean): Observable<StringResponse>{
        let url = this.urlService.getAddRepositoryUrl(id);

        if (cellar != null){
            if (cellar){
                url = this.urlService.getCellarAddRepositoryUrl(id);
            }
            else {
                url = this.urlService.getVirtualAddRepositoryUrl(id);
            }
        }

        return this.httpClient.post(url, repository, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }
}
