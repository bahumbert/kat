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

import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import { KatServer } from '../../../model/KatServer';
import { NotificationsService } from 'angular2-notifications';
import { BundleManagerService } from '../../../services/manager/bundle-manager.service';
import { KatBundle } from '../../../model/KatBundle';
import { TabsetComponent } from 'ngx-bootstrap/tabs';
import {ServerService} from "../../../services/manager/server.service";
import {Artifact} from "../../../model/artifact";
import {ScheduledCronManagerComponent} from "./server/cron-manager/scheduled-cron-manager.component";
import {ZipManagerComponent} from "./server/zip-manager/zip-manager.component";
import {IamRight} from "../../../model/Iam/iamRight";
import {AuthService} from "../../../services/auth/auth.service";

/**
 * Created by bhumbert on 29/06/2017.
 */


@Component({
    selector: 'server-details',
    templateUrl: 'server-details.component.html',
    styleUrls: [
        'server-details.component.scss'
    ],
})
export class ServerDetailsComponent implements OnInit{
    server: KatServer;
    tabSelection: number = 0;
    @Output() serverUpdated = new EventEmitter();
    @ViewChild('staticTabs') staticTabs: TabsetComponent;
    @ViewChild(ScheduledCronManagerComponent) scheduledCronManagerComponent: ScheduledCronManagerComponent;
    @ViewChild(ZipManagerComponent) zipManagerComponent: ZipManagerComponent;
    activeartifacts: Artifact[] = [];
    right: IamRight;


    @Input()
    set setServer(server: KatServer) {
        this.server = server;

            this.staticTabs.tabs[0].active = true;

        this.ngOnInit();
    }

    constructor(protected serverService: ServerService,protected authService: AuthService){
        this.right = this.authService.getRights();
    }

    ngOnInit(){
        if (this.server.id) {
            this.activeartifacts = [];
        this.serverService.getArtifactOnKat(this.server.id).subscribe(result => {
            this.activeartifacts = result;
        });
        }

    }

    serverUpdatedEvent($event){
        this.serverUpdated.emit($event);
    }

    undeployArtifact($event) {
        this.ngOnInit();
    }

    handleNewArtifact($event) {
        this.ngOnInit();
    }

    handleCronUpdated($event) {

        // this.ngOnInit();
        if (this.scheduledCronManagerComponent) {
            this.scheduledCronManagerComponent.addCron($event);
        }
        if (this.zipManagerComponent){
            let artifact = new Artifact();
            artifact.name = $event.artifactName;
            artifact.version = $event.version;
            this.zipManagerComponent.updateArtifactStatus(artifact, 1);

        }
    }

    updateZipManager($event) {
            if (this.zipManagerComponent) {
            this.zipManagerComponent.updateArtifactStatus($event, 0);
            }
    }
}
