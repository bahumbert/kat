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
 * Created by bhumbert on 10/07/2017.
 */

import { Injectable } from '@angular/core';
import { UrlService } from '../url.service';
import { HttpCustomService } from '../http-custom.service';
import { Observable } from 'rxjs';
import { NotificationsService } from 'angular2-notifications';
import { KatBroker } from '../../model/KatBroker';
import {StringResponse} from "app/model/string-reponse";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../auth/auth.service";

@Injectable()
export class BrokerService extends HttpCustomService {

    constructor(
        private urlService: UrlService,
        private httpClient: HttpClient,
        private notificationsService: NotificationsService,
        private authService: AuthService,
        routerS: Router
    ) {
        super(routerS);
    }


    getBrokerList(): Observable<Array<KatBroker>>{
        return this.httpClient
            .get(this.urlService.getBrokerUrl(), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getBrokerListWithoutOneEnvironment(environmentId: string): Observable<Array<KatBroker>>{
        return this.httpClient
            .get(this.urlService.getBrokerWithoutOneEnvironmentUrl(environmentId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }


    getBrokerListByEnvironment(environmentId: string): Observable<Array<KatBroker>>{
        return this.httpClient
            .get(this.urlService.getBrokerByEnvironmentUrl(environmentId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    add(broker: KatBroker, environmentId: string): Observable<KatBroker>{
        return this.httpClient
            .post(this.urlService.getAddBrokerUrl(environmentId), broker, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    update(broker: KatBroker): Observable<KatBroker>{
        return this.httpClient
            .put(this.urlService.getUpdateBrokerUrl(broker.id), broker, this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    remove(broker: KatBroker, environmentId: string): Observable<any>{

        return this.httpClient
            .delete(this.urlService.getDeleteBrokerUrl(broker.id, environmentId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    getBrokerStatus(broker: KatBroker): Observable<String>{

        return this.httpClient
            .get(this.urlService.getBrokerStatusUrl(broker.id), this.getOptions())
            .catch(this.extractData)
            .map(this.extractData)
        ;
    }

    getBrokerStats(broker: KatBroker){
        if (this.authService.getUser()) {
            broker.serverStats = null;

            let stats = this.httpClient
                .get(this.urlService.getBrokerStatsUrl(broker.id), this.getOptions())
                .catch(this.handleError)
                .map(this.extractData)
            ;

            stats
                .expand(() => Observable.timer(30000).concatMap(() => stats))
                .subscribe(
                    brokerStats => {
                        broker.serverStats = brokerStats;
                    },
                    error => {
                        this.notificationsService.error('An error occured', 'Can\'t get "' + broker.name + '"\'s status, please check its cave url');
                    }
                )
            ;
        }
    }

    shutdown(broker: KatBroker): Observable<StringResponse>{
        return this.httpClient
            .get(this.urlService.getBrokerShutdownUrl(broker.id),  this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    restart(broker: KatBroker): Observable<StringResponse>{
        return this.httpClient
            .get(this.urlService.getBrokerRestartUrl(broker.id),  this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    startThroughSsh(broker: KatBroker): Observable<any>{
        return this.httpClient
            .post(this.urlService.getBrokerSshStartUrl(broker.id), broker.sshLogin,  this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }

    addExisting(brokerId: string, environmentId: string, clusterId?: string): Observable<KatBroker>{
        return this.httpClient
            .post(this.urlService.getAddExistingBrokerUrl(brokerId, environmentId, clusterId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }
}
