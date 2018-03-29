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

import {ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, TemplateRef} from '@angular/core';
import { NotificationsService } from 'angular2-notifications';
import { ServerService } from '../../../../services/manager/server.service';
import { KatServer } from '../../../../model/KatServer';
import { KatCluster } from '../../../../model/KatCluster';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import {IamRight} from "../../../../model/Iam/iamRight";
import {AuthService} from "../../../../services/auth/auth.service";

/**
 * Created by bhumbert on 18/04/2017.
 *
 * Manage available servers
 *
 */

@Component({
  selector: 'server',
  templateUrl: 'server.component.html',
  styleUrls: [ 'server-list.component.scss' ]

})

export class ServerComponent implements OnInit, OnChanges{
    @Input()
    server: KatServer;
    @Input()
    environmentId: string;
    @Input()
    cluster: KatCluster;
    @Input()
    id: number;
    @Input()
    index: number;
    @Input()
    update: boolean;
    @Output()
    removeServer: EventEmitter<KatServer>  = new EventEmitter();

    isLoading: boolean = false;

    removeServerTemp: KatServer;

    modalRef: BsModalRef;
    right: IamRight;


    // TODO REFRESH HERE !!
     constructor(private serverService: ServerService, private notificationsService: NotificationsService,
                 private modalService: BsModalService, private ref: ChangeDetectorRef, private authService: AuthService){
         this.right = authService.getRights();
    }

    ngOnInit(){

        if (this.cluster){
            this.server.parentCluster = this.cluster.id;
        }

        // this.server.componentId = this.id;
//        this.server.hasCellar = null;

        if (this.server.jolokiaUrl){
            this.getServerStatus();
        }
        this.isLoading = false;
    }


    ngOnChanges() {
       this.getServerStatus();
    }

    getServerStatus(){
 //       this.server.hasCellar = null;
        this.server.isStarted = false;
        this.serverService.getServerStatus(this.server).subscribe(
            res => {
                if (res.status === 'STARTED'){
                    this.server.isStarted = true;
                    this.getServerData();

                    if (this.cluster && this.cluster.type === 'cellar'){
                        this.serverService.getCellarState(this.server).subscribe(
                            state => {
                                // this.server.hasCellar = state;
                            });
                    }
                }
                this.server.connecting = false;
            });
    }

    getServerData(){
        this.serverService.getServerStats(this.server);
    }

    shutdown(){
        this.serverService.shutdown(this.server).subscribe(
        res => {
            this.server.serverStats = null;
            setTimeout(() => {
                if(this.authService.getUser()) {
                    if(this.authService.getUser()) {
                        this.getServerStatus();
                    }
                }

                }, 2000); // wait for server to shutdown

        });
    }

    restart(){
        this.serverService.restart(this.server).subscribe(
        res => {
            this.server.serverStats = null;
            setTimeout(() => {
                if (this.authService.getUser()) {
                    this.getServerStatus();
                }
                }, 10000); // wait for server to restart

        });
    }

    updateData(server){

        this.getServerStatus();
    }

    openModal(template: TemplateRef<any>) {
        this.modalRef = this.modalService.show(template);
    }

    closeModal() {
        this.modalRef.hide();
        this.modalRef = null;
    }

    deleteServer() {
        this.removeServer.emit(this.removeServerTemp);
        this.closeModal();
    }
}
