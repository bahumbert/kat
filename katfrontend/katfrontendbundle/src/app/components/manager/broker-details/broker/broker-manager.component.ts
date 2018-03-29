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
import { NotificationsService } from 'angular2-notifications';
import { BundleManagerService } from '../../../services/manager/bundle-manager.service';
//import { Bundle } from '../../../model/bundle';
import { KatBroker } from 'app/model/KatBroker';
import { BrokerService } from 'app/services/manager/broker.service';
import {SshLogin} from "../../../../model/ssh-login";

/**
 * Created by bhumbert on 10/07/2017.
 */


@Component({
    selector: 'broker-manager',
    templateUrl: 'broker-manager.component.html',
    styleUrls: [
        'broker-manager.component.scss'
    ],
})
export class BrokerManagerComponent implements OnInit{

    broker: KatBroker;
    jolokiaUrlChanged: boolean = false;

    @Output()
    brokerUpdated = new EventEmitter();

    @Input()
    set setBroker(broker: KatBroker) {
        this.broker = broker;
    }

    constructor(private brokerService: BrokerService, private notificationsService: NotificationsService){}

    ngOnInit(){
        if (!this.broker.sshLogin){
            this.broker.sshLogin = new SshLogin();
        }
    }

    update(pathUpdate?: boolean){

        let reg = /^[a-zA-Z0-9\/\.\-_]*$/ig;

        if (!this.broker.sshLogin){
            this.broker.sshLogin = new SshLogin();
        }

        if (pathUpdate && this.broker.sshLogin.path && reg.test(this.broker.sshLogin.path) === false){
            return null;
        }
/*
        this.brokerService.update(this.broker).subscribe(
        newData => {

            this.broker.systemName = newData.systemName;

            this.brokerUpdated.emit({broker: this.broker});
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });
        */
    }
}
