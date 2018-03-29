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

import { Component, EventEmitter, Output, QueryList, ViewChildren, Input, ViewChild } from '@angular/core';
import { NotificationsService } from 'angular2-notifications';
import { ClusterService } from '../../../../services/manager/cluster.service';
import { KatCluster } from '../../../../model/KatCluster';
import { KatServer } from '../../../../model/KatServer';
import { BrokerComponent } from '../broker/broker.component';
import { KatBroker } from '../../../../model/KatBroker';
import { ServerService } from '../../../../services/manager/server.service';
import { BrokerService } from '../../../../services/manager/broker.service';
import {ServerListComponent} from "../server/server-list.component";

/**
 * Created by bhumbert on 20/04/2017.
 *
 * Manage clusters
 *
 */

@Component({
  selector: 'clusters',
  templateUrl: 'cluster.component.html',
  styleUrls: [
    'cluster.component.scss'
  ],
})


export class ClusterComponent {

    clusterList: Array<KatCluster>;

    serverList: Array<KatServer> = [];
    isLoading: boolean = true;

    addingIndex: number;

    environmentId: string;

    @ViewChildren (ServerListComponent) serverComponents: QueryList<ServerListComponent>;
    @ViewChildren (BrokerComponent) brokerComponents: QueryList<BrokerComponent>;

    @Output()
    init = new EventEmitter();
    @Output()
    editing = new EventEmitter();
    @Output()
    serverSelected = new EventEmitter();
    @Output()
    clusterSelected = new EventEmitter();
    @Output()
    brokerSelected = new EventEmitter();

    @ViewChild('addExistingComponentModal') addExistingComponentModalElement;
    existingListLoaded: boolean = false;
    existingClusters: Array<KatCluster>;
    existingServers: Array<KatServer>;
    existingBrokers: Array<KatBroker>;
    addExistingTmp;

    @Input()
    set setEnvironmentId(environmentId: string) {
        this.environmentId = environmentId;
        if (this.environmentId){
            this.getClusters();
        }
    }

    removeClusterTemp: KatCluster;

    constructor(private clusterService: ClusterService, private notificationsService: NotificationsService,
                private serverService: ServerService, private brokerService: BrokerService){
    }

    getClusters(){
        this.clusterService.getClusterListByEnvironment(this.environmentId).subscribe(
        clusterList => {
          this.clusterList = clusterList;
          this.isLoading = false;
          this.init.emit();
        });
    }

    addNewCluster(){
        this.isLoading = true;
        let clusterToSave = new KatCluster();

        this.clusterService.add(clusterToSave, this.environmentId).subscribe(
        cluster => {
            this.clusterList.push(cluster);
            this.isLoading = false;
        });
    }

    removeCluster(){
        this.isLoading = true;
        this.clusterService.remove(this.removeClusterTemp, this.environmentId).subscribe(
        ret => {
            let that = this;
            let index = this.clusterList.findIndex(function (element) {
                return element.id === that.removeClusterTemp.id;
            });

            if (index !== -1) {
                this.clusterList.splice(index, 1);
            }
            else {
                this.notificationsService.error('An error occured', 'Please reload the page');
            }
            this.removeClusterTemp = null;
            this.isLoading = false;
        });
    }

    updateCluster(cluster: KatCluster){
        this.isLoading = true;
        this.clusterService.update(cluster).subscribe(
        res => {

            let index = this.clusterList.findIndex(element => {
                return element.id === cluster.id;
            });

            if (index !== -1) {
                this.clusterList[index] = cluster;
            }
            else {
                this.notificationsService.error('An error occured', 'Please reload the page');
            }

            this.isLoading = false;
        });
    }

    clusterChange(object: KatServer|KatBroker, $event, type: string){

        let removeManually: boolean = false;

        let formerParentCluster: KatCluster = null;
        if (object.parentCluster){
            formerParentCluster = this.clusterList.find(cluster => {return cluster.id === object.parentCluster; });
        }

        if (formerParentCluster && formerParentCluster[type][0]){
            formerParentCluster[type].splice(formerParentCluster[type].findIndex( item => {
                return item.id === object.id;
            }), 1);
            this.updateCluster(formerParentCluster);
        }
        else {
            removeManually = true;
        }

        object.parentCluster = null;

        let newParentCluster: KatCluster = null;
        if ($event.newParentCluster){
            newParentCluster = this.clusterList.find(cluster => {return cluster.id === $event.newParentCluster; });
            object.parentCluster = newParentCluster.id;
            if (!newParentCluster[type]){
                newParentCluster[type] = [];
            }
            newParentCluster[type].push(object);
            this.updateCluster(newParentCluster);
        }

        if (!$event.newParentCluster && (!formerParentCluster || !formerParentCluster[type][0])){
            this.isLoading = false;
        }

        return removeManually;
    }

    brokerClusterChange($event){
        return this.clusterChange($event.broker, $event, 'brokers');
    }

    serverClusterChange($event){
        return this.clusterChange($event.server, $event, 'servers');
    }

    droppedInCluster($event, clusterId, index){
        this.isLoading = true;
        $event = $event.dragData;
        $event.newParentCluster = clusterId;

        if ($event.server){
            if (this.serverClusterChange($event)){
                $event.serverList.splice($event.index, 1);
            }
        }
        else if ($event.broker){
            if (this.brokerClusterChange($event)){
                $event.brokerList.splice($event.index, 1);
            }
        }
    }

    droppedOutsideCluster($event, list){
        this.isLoading = true;
        $event = $event.dragData;

        if ($event.server){
            this.serverClusterChange($event);
            list.push($event.server);
        }
        else if ($event.broker){
            this.brokerClusterChange($event);
            list.push($event.broker);
        }

    }

    ServerUpdatedEvent($event){
        let serverComponentsArray = this.serverComponents.toArray();
        let componentIndex = serverComponentsArray.findIndex(component => {
            return component.id === $event.server.componentId;
        });
        if (componentIndex !== -1){
            serverComponentsArray[componentIndex].updateData($event.server);
        }
    }

    BrokerUpdatedEvent($event){

        let brokerComponentsArray = this.brokerComponents.toArray();
        let componentIndex = brokerComponentsArray.findIndex(component => {
            return component.id === $event.broker.componentId;
        });
        if (componentIndex !== -1){
            brokerComponentsArray[componentIndex].updateData($event.broker);
        }
    }

    editCluster(cluster: KatCluster){
        this.editing.emit({data: cluster, type: 'Cluster'});
    }
    editServer($event){
        this.editing.emit($event);
    }

    serverSelectedEvent($event){
        this.serverSelected.emit($event);
    }

    brokerSelectedEvent($event){
        this.brokerSelected.emit($event);
    }

    clusterSelectedEvent(cluster: KatCluster){
        cluster.selected = true;
        this.clusterSelected.emit({cluster: cluster});
    }

    addNewServer(){
        let serverComponentsArray = this.serverComponents.toArray();
        serverComponentsArray[this.addingIndex].addNewServer();
    }

    addNewBroker(){
        let brokerComponentsArray = this.brokerComponents.toArray();
        brokerComponentsArray[this.addingIndex].addNewBroker();
    }

    addExistingComponentOpenModal(){
        this.addExistingComponentModalElement.show();

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
        if (this.existingBrokers && this.existingServers){
            this.existingListLoaded = true;
        }
    }

    addExistingComponent(){
        let service;

        if (this.addExistingTmp.type === 'server'){
            service = this.serverService;
        }
        else if (this.addExistingTmp.type === 'broker'){
            service = this.brokerService;
        }

        service.addExisting(this.addExistingTmp.value, this.environmentId, this.clusterList[this.addingIndex].id).subscribe(
        res => {

            if (!res){
                this.notificationsService.error('An error occured', 'Please reload the page');
            }

            //console.log('res=', res);
            if (this.addExistingTmp.type === 'server'){
                let serverComponentsArray = this.serverComponents.toArray();
                serverComponentsArray[this.addingIndex].katServerList.push(res);
                //console.log('serverList', serverComponentsArray[this.addingIndex].katServerList);
                this.existingServers.splice(this.existingServers.findIndex(server => {
                    return server.id === res.id;
                }), 1);
            }
            else if (this.addExistingTmp.type === 'broker'){
                let brokerComponentsArray = this.brokerComponents.toArray();
                brokerComponentsArray[this.addingIndex].brokerList.push(res);
                // console.log('brokerList', brokerComponentsArray[this.addingIndex].brokerList);
                this.existingBrokers.splice(this.existingBrokers.findIndex(broker => {
                    return broker.id === res.id;
                }), 1);
            }
        });

    }
}
