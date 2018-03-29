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

import {Injectable} from '@angular/core';
import {UrlService} from '../url.service';
import {HttpCustomService} from '../http-custom.service';
import {Observable} from 'rxjs';
import {Task} from "../../model/Task";
import {Workflow} from "../../model/Workflow";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class WorkflowService extends HttpCustomService {

    constructor(
        private urlService: UrlService,
        private httpClient: HttpClient,
        routerS: Router
    ) {
        super(routerS);
    }

    getDeployedWorkflow(idServer): Observable<Workflow[]> {
        return this.httpClient
            .get(this.urlService.getDeployedWorkflowUrl(idServer), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
            ;
    }

    deployWorkflow(idServer, workflow): Observable<Workflow> {
        return this.httpClient
            .post(this.urlService.deployWorkflowUrl(idServer, workflow.id), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
            ;
    }

    undeployWorkflow(idServer, workflow): Observable<Workflow> {
        return this.httpClient
            .delete(this.urlService.undeployWorkflowUrl(idServer, workflow.workflowFile), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
            ;
    }

    deleteWorkflow(idWk): Observable<Workflow[]> {
        return this.httpClient
            .delete(this.urlService.getOneWorkflowUrl(idWk), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getWorkflows(): Observable<Workflow[]> {
        return this.httpClient
            .get(this.urlService.getApiUrl() + '/workflow', this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getWorkflowById(wkId: string): Observable<Workflow> {
        return this.httpClient
            .get(this.urlService.getOneWorkflowUrl(wkId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    postWorkflow(data): Observable<Task> {
        return this.httpClient
            .post(this.urlService.getApiUrl() + '/workflow', data, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    putWorkflow(wkId, data): Observable<Task> {
        return this.httpClient
            .put(this.urlService.getOneWorkflowUrl(wkId), data, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }
}
