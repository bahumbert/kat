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

import { Component, Input, OnInit } from '@angular/core';
import { NotificationsService } from 'angular2-notifications';
import { KatCluster } from 'app/model/KatCluster';
import { ClusterService } from 'app/services/manager/cluster.service';
import { KatServer } from '../../../../model/KatServer';
import { ServerService } from 'app/services/manager/server.service';

/**
 * Created by bhumbert on 29/06/2017.
 */


@Component({
    selector: 'cluster-manager',
    templateUrl: 'cluster-manager.component.html',
    styleUrls: [
        'cluster-manager.component.scss'
    ],
})
export class ClusterManagerComponent implements OnInit{

    cluster: KatCluster;
    cellarNotInstalled: boolean = false;
    isInstallingCellar: boolean = false;
    refreshed: number = 0;

    @Input()
    set setCluster(cluster: KatCluster) {
        this.cluster = cluster;
        if (this.cluster.type === 'cellar'){
            this.checkIsCellarInstalled();
        }
        else {
            this.cellarNotInstalled = false;
        }
    }

    constructor(private clusterService: ClusterService, private notificationsService: NotificationsService, private serverService: ServerService){}

    ngOnInit(){ }

    update(){

        this.clusterService.update(this.cluster).subscribe(
        newData => {
            this.cluster = newData;
            if (this.cluster.type === 'cellar'){
                this.checkIsCellarInstalled();
            }
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });
    }

    checkIsCellarInstalled(){

        this.isInstallingCellar = false;

        for (let server of this.cluster.servers){
//            if (server.hasCellar === false){
//                return this.cellarNotInstalled = true;
//            }
        }
        return this.cellarNotInstalled = false;

    }

    installCellar(){
        this.isInstallingCellar = true;

        this.clusterService.installCellar(this.cluster).subscribe(
        res => {

            if (res != null && res.response != null){
                this.notificationsService.error('An error occured when installing Cellar', res.response);
            }
            else {
                setTimeout( () => {
                    for (let server of this.cluster.servers){
                        this.refreshCellarState(server);
                    }
                }, 30000);
            }
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });
    }

    refreshCellarState(server: KatServer){
        this.serverService.getCellarState(server).subscribe(
        state => {
            //server.hasCellar = state;
            this.refreshed++;
            if (this.refreshed >= this.cluster.servers.length - 1){
                this.refreshed = 0;
                return this.checkIsCellarInstalled();
            }
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });
    }
}
