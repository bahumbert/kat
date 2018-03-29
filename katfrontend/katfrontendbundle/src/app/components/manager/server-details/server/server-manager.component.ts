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

import { Component, Input, OnInit, EventEmitter, Output } from '@angular/core';
import { KatServer } from '../../../../model/KatServer';
import { NotificationsService } from 'angular2-notifications';
import { BundleManagerService } from '../../../services/manager/bundle-manager.service';
// import { Bundle } from '../../../model/bundle';
import { ServerService } from '../../../../services/manager/server.service';
import {SshLogin} from "app/model/ssh-login";
import {IamRight} from "../../../../model/Iam/iamRight";
import {AuthService} from "../../../../services/auth/auth.service";

/**
 * Created by bhumbert on 22/06/2017.
 */


@Component({
    selector: 'server-manager',
    templateUrl: 'server-manager.component.html',
    styleUrls: [
        'server-manager.component.scss'
    ],
})
export class ServerManagerComponent implements OnInit{

    server: KatServer;
    canUpdate: boolean = false;
    @Output()
    serverUpdated = new EventEmitter();

    @Input()
    set setServer(server: KatServer) {
        this.server = server;
        this.canUpdate = true;
    }
    right: IamRight;

    constructor(private serverService: ServerService, private notificationsService: NotificationsService, private authService: AuthService){
        this.right = this.authService.getRights();
    }

    ngOnInit(){

    }

    update(pathUpdate?: boolean){

        let reg = /^[a-zA-Z0-9\/\.\-_]*$/ig;

        if (this.canUpdate === true) {
        this.serverService.update(KatServer.fromObject(this.server).toSendingJson()).subscribe(
        newData => {

            this.server.systemName = newData.systemName;
            this.server.correctlyConfigured = newData.correctlyConfigured;

            this.serverUpdated.emit({server: this.server});

        });
        }
    }
}
