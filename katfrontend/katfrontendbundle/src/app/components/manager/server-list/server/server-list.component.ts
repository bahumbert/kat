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

import {Component, Directive, EventEmitter, Input, Output, QueryList, ViewChild, ViewChildren} from '@angular/core';
import { NotificationsService } from 'angular2-notifications';
import { ServerService } from '../../../../services/manager/server.service';
import { KatServer } from '../../../../model/KatServer';
import { KatCluster } from '../../../../model/KatCluster';
import {ServerComponent} from "./server.component";

/**
 * Created by bhumbert on 18/04/2017.
 *
 * Manage available servers
 *
 */

@Component({
  selector: 'server-list',
  templateUrl: 'server-list.component.html',
  styleUrls: ['server-list.component.scss' ]

})

export class ServerListComponent {

    katServerList: Array<KatServer> = [];

    environmentId: string;

    @Input()
    cluster: KatCluster;

    @Input()
    id: number;

    @ViewChildren(ServerComponent) serverComponent: QueryList<ServerComponent>;

    @Input()
    set setEnvironmentId(environmentId: string) {
        this.environmentId = environmentId;
        if (this.environmentId){
            this.getServers();
        }
    }

    @Output()
    serverClusterChange = new EventEmitter();

    @Output()
    serverSelected = new EventEmitter();

    server: KatServer;

    isLoading: boolean = false;

    removeServerTemp: KatServer;

    constructor(private serverService: ServerService, private notificationsService: NotificationsService){
    }

    ServerUpdatedEvent($event) {
        let componentFiltered = this.serverComponent.toArray().filter(cmp => cmp.server.id === $event.server.id);
        if(componentFiltered) {
            componentFiltered.forEach(cmp => {
            cmp.getServerStatus();
        });
        }
    }

    getServers(){
        this.isLoading = true;
        let clusterId = null;

        if (this.cluster) {
            clusterId = this.cluster.id;
        }

        this.serverService.getServerListByEnvironment(this.environmentId).subscribe(
        serverList => {
            //console.log(serverList);
            this.katServerList = serverList;
            this.isLoading = false;
        },
        error => {
            this.notificationsService.error('An error occured', `Unable to load server from environment ${this.environmentId}`);
        });
    }

    addNewServer(){
        //console.log('Add servers');
        this.isLoading = true;
        let serverToSave = new KatServer();
        if (this.cluster){
            serverToSave.parentCluster = this.cluster.id;
        }
//        serverToSave.componentId = this.id;
        // serverToSave.hasCellar = null;

        this.serverService.add(serverToSave, this.environmentId).subscribe(
        server => {
            //console.log(server);
            serverToSave.id = server.id;

            if (this.cluster){
                this.serverClusterChange.emit({server: serverToSave, newParentCluster: this.cluster.id});
            }
            else {
                this.katServerList.push(server);
            }

            this.isLoading = false;
        });
    }

    removeServerEvent($event) {
        this.removeServerTemp = $event;
        this.removeServer();
    }

    removeServer() {
        this.isLoading = true;
        this.serverService.remove(this.removeServerTemp, this.environmentId, this.cluster).subscribe(
        ret => {

            if (!this.cluster){
                let index = this.katServerList.findIndex(server => {
                    return server.id === this.removeServerTemp.id;
                });
                if (index !== -1){
                    this.katServerList.splice(index, 1);
                }
            }
            else {
                this.serverClusterChange.emit({server: this.removeServerTemp, newParentCluster: null});
            }

            this.removeServerTemp = null;
            this.isLoading = false;
        });
    }

    getServerStatus(server: KatServer){
//        server.hasCellar = null;
        server.isStarted = false;
        //console.log('====>');
        this.serverService.getServerStatus(server).subscribe(
        res => {
            //console.log(res);
            if (res.status === 'STARTED'){
                //console.log('je passe');
                server.isStarted = true;
                this.getServerData(server);

                if (this.cluster.type === 'cellar'){
                    this.serverService.getCellarState(server).subscribe(
                    state => {
//                        server.hasCellar = state;
                    });
                }
            }
            server.connecting = false;
        });
    }

    getServerData(server: KatServer){
        this.serverService.getServerStats(server);
    }

    selectServer(server: KatServer){

        server.selected = true;
        this.serverSelected.emit({server: server});
    }

    shutdown(server: KatServer){
        this.serverService.shutdown(server).subscribe(
        res => {
            server.serverStats = null;
            setTimeout(() => { this.getServerStatus(server); }, 2000); // wait for server to shutdown

        });
    }

    restart(server: KatServer){
        this.serverService.restart(server).subscribe(
        res => {
            server.serverStats = null;
            setTimeout(() => { this.getServerStatus(server); }, 10000); // wait for server to restart

        });
    }



    updateData(server){
        this.getServerStatus(server);
    }
}
