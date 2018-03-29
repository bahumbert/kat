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

import { Component, OnInit, ViewChild } from '@angular/core';
import { CurrentPageService } from '../../../services/communication/current-page.service';
import { NotificationsService } from 'angular2-notifications';
import { ClusterComponent } from './cluster/cluster.component';
import { BrokerComponent } from './broker/broker.component';
import { KatServer } from '../../../model/KatServer';
import { KatCluster } from 'app/model/KatCluster';
import { ActivatedRoute } from '@angular/router';
import { KatBroker } from 'app/model/KatBroker';
import { ServerService } from '../../../services/manager/server.service';
import { BrokerService } from '../../../services/manager/broker.service';
import { ClusterService } from '../../../services/manager/cluster.service';
import { ServerListComponent } from './server/server-list.component';
import {AuthService} from "../../../services/auth/auth.service";
import {IamRight} from "../../../model/Iam/iamRight";


/**
 * Created by bhumbert on 09/06/2017.
 */

@Component({
    selector: 'environment',
    templateUrl: 'environment.component.html',
    styleUrls: [ 'environment.component.scss' ]
})
export class EnvironmentComponent implements OnInit{

    isLoading: boolean;
    isServerLoading: boolean = false;
    selectedServer: KatServer;
    selectedCluster: KatCluster;
    selectedBroker: KatBroker;

    totalHeight: number = 200;
    listBlocHeight: number = 100;
    detailsBlocHeight: number = 100;

    environmentId: string;

    existingListLoaded: boolean = false;
    existingClusters: Array<KatCluster>;
    existingServers: Array<KatServer>;
    existingBrokers: Array<KatBroker>;
    addExistingTmp;
    right: IamRight;
    @ViewChild('addExistingComponentMainModal') addExistingComponentMainModalElement;

    @ViewChild(ClusterComponent) clusterComponent: ClusterComponent;
    @ViewChild(ServerListComponent) serverComponent: ServerListComponent;
    @ViewChild(BrokerComponent) brokerComponent: BrokerComponent;

    constructor(private currentPageService: CurrentPageService, private notificationsService: NotificationsService,
                private activatedRoute: ActivatedRoute, private serverService: ServerService,
                private clusterService: ClusterService, private brokerService: BrokerService,
                private authService: AuthService){
        this.right = this.authService.getRights();
        this.activatedRoute.params.subscribe(
        (param: any) => {
            this.environmentId = param['id'];
            currentPageService.changePage('manager' + this.environmentId);
            this.unselect(null);
        });
    }

    ngOnInit(){
        this.totalHeight = document.getElementById('page-wrapper').clientHeight - 60;
        this.listBlocHeight = Math.floor(this.totalHeight / 2);
        this.detailsBlocHeight = Math.floor(this.totalHeight / 2);
    }

    isInit(){
        this.isLoading = false;
    }

    dropped($event){
        let list = null;
        if ($event.dragData.server){
           list = this.serverComponent.katServerList;
        }
        else if ($event.dragData.broker){
            list = this.brokerComponent.brokerList;
        }
        this.clusterComponent.droppedOutsideCluster($event, list);
    }

    serverSelected($event){
        this.unselect($event.server);

        this.selectedServer = $event.server;


    }

    brokerSelected($event){
        this.unselect($event.broker);

        this.selectedBroker = $event.broker;
    }

    clusterSelected($event){
        this.unselect($event.cluster);

        this.selectedCluster = $event.cluster;
    }

    unselect(newSelection: KatServer|KatBroker|KatCluster){
        if (this.selectedServer && (!newSelection || this.selectedServer.id !== newSelection.id)){
            this.selectedServer.selected = false;
            this.selectedServer = null;
        }
        /*
        if (this.selectedBroker && (!newSelection || this.selectedBroker.id !== newSelection.id)){
            this.selectedBroker.selected = false;
            this.selectedBroker = null;
        }
        if (this.selectedCluster && (!newSelection || this.selectedCluster.id !== newSelection.id)){
            this.selectedCluster.selected = false;
            this.selectedCluster = null;
        }*/
    }

    addNewCluster(){
        this.clusterComponent.addNewCluster();
    }

    addNewServer(){
        this.serverComponent.addNewServer();
    }

    addNewBroker(){
        this.brokerComponent.addNewBroker();
    }

    onSplitChange($event){
       // console.log($event);
    }

    serverUpdatedEvent($event){

        if(this.serverComponent) {

            this.serverComponent.ServerUpdatedEvent($event);
        }
    }

    brokerUpdatedEvent($event){
        this.clusterComponent.BrokerUpdatedEvent($event);
    }

    addExistingComponentOpenModal(){
        this.addExistingComponentMainModalElement.show();

        this.clusterService.getClusterListWithoutOneEnvironment(this.environmentId).subscribe(
        clusters => {
            this.existingClusters = clusters;
            this.checkExistingListsLoaded();
        });

        this.serverService.getServerListWithoutOneEnvironment(this.environmentId).subscribe(
        servers => {
            this.existingServers = servers;
            this.checkExistingListsLoaded();
        });

        this.brokerService.getBrokerListWithoutOneEnvironment(this.environmentId).subscribe(
        brokers => {
            this.existingBrokers = brokers;
            this.checkExistingListsLoaded();
        });


    }

    checkExistingListsLoaded(){
        if (this.existingBrokers && this.existingServers && this.existingClusters){
            this.existingListLoaded = true;
        }
    }

    addExistingComponent(){
        let service;

        if (this.addExistingTmp.type === 'cluster'){
            service = this.clusterService;
        }
        else if (this.addExistingTmp.type === 'server'){
            service = this.serverService;
        }
        else if (this.addExistingTmp.type === 'broker'){
            service = this.brokerService;
        }

        service.addExisting(this.addExistingTmp.value, this.environmentId).subscribe(
        res => {

            if (!res){
                this.notificationsService.error('An error occured', 'Please reload the page');
            }

            if (this.addExistingTmp.type === 'cluster'){
                this.clusterComponent.clusterList.push(res);
                this.existingClusters.splice(this.existingClusters.findIndex(cluster => {
                    return cluster.id === res.id;
                }), 1);
            }
            else if (this.addExistingTmp.type === 'server'){
                this.serverComponent.katServerList.push(res);
                this.existingServers.splice(this.existingServers.findIndex(server => {
                    return server.id === res.id;
                }), 1);
            }
            else if (this.addExistingTmp.type === 'broker'){
                this.brokerComponent.brokerList.push(res);
                this.existingBrokers.splice(this.existingBrokers.findIndex(broker => {
                    return broker.id === res.id;
                }), 1);
            }
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });

    }
}
