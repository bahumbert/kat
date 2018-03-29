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

import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NotificationsService } from 'angular2-notifications';
import { KatBroker } from 'app/model/KatBroker';
import { BrokerService } from '../../../../services/manager/broker.service';
import {SshLogin} from "../../../../model/ssh-login";

/**
 * Created by bhumbert on 10/07/2017.
 *
 */

@Component({
  selector: 'brokers',
  templateUrl: 'broker.component.html',
  styleUrls: [
    'broker.component.scss'
  ],
})

export class BrokerComponent {

    brokerList: Array<KatBroker>;

    environmentId: string;

    @Input()
    id: number;

    @Input()
    set setEnvironmentId(environmentId: string) {
        this.environmentId = environmentId;
        if (this.environmentId){
            this.getBrokers();
        }
    }

    @Output()
    brokerSelected = new EventEmitter();

    sshBroker: KatBroker;

    isLoading: boolean = false;

    removeBrokerTemp: KatBroker;

    constructor(private brokerService: BrokerService, private notificationsService: NotificationsService){
    }

    getBrokers(){

        this.brokerService.getBrokerListByEnvironment(this.environmentId).subscribe(
        serverList => {

           this.brokerList = serverList;
            this.init();
        });
    }

    init(){
        for (let broker of this.brokerList){

            if (!broker.sshLogin){
                broker.sshLogin = new SshLogin();
                broker.sshLogin.isValid = false;
            }
            else {
                this.isSSHValid(broker.sshLogin);
            }


            broker.componentId = this.id;

            if (broker.jolokiaUrl){
                this.getBrokerStatus(broker);
            }
        }
        this.isLoading = false;
    }

    addNewBroker(){
        this.isLoading = true;
        let brokerToSave = new KatBroker();

        brokerToSave.componentId = this.id;

        this.brokerService.add(brokerToSave, this.environmentId).subscribe(
        broker => {

            brokerToSave.id = broker.id;
            this.brokerList.push(brokerToSave);
            this.isLoading = false;
        });
    }

    removeBroker() {
        this.isLoading = true;
        this.brokerService.remove(this.removeBrokerTemp, this.environmentId).subscribe(
        ret => {
                let index = this.brokerList.findIndex(broker => {
                    return broker.id === this.removeBrokerTemp.id;
                });
                if (index !== -1){
                    this.brokerList.splice(index, 1);
                }
            this.removeBrokerTemp = null;
            this.isLoading = false;
        });
    }

    selectBroker(broker: KatBroker){
        broker.selected = true;
        this.brokerSelected.emit({broker: broker});
    }

    getBrokerData(broker: KatBroker){
        this.brokerService.getBrokerStats(broker);
    }


    getBrokerStatus(broker: KatBroker){
        broker.isStarted = false;
        this.brokerService.getBrokerStatus(broker).subscribe(
        res => {
            if (res === 'STARTED'){
                broker.isStarted = true;
                this.getBrokerData(broker);
            }
            broker.connecting = false;
        });
    }

    shutdown(broker: KatBroker){
        this.brokerService.shutdown(broker).subscribe(
            res => {

                if (res == null || res.response != null){
                    this.notificationsService.error('An error occured', 'Please reload the page');
                }

                broker.serverStats = null;
                setTimeout(() => { this.getBrokerStatus(broker); }, 2000); // wait for server to shutdown

            });
    }

    restart(broker: KatBroker){
        this.brokerService.restart(broker).subscribe(
            res => {

                if (res == null || res.response != null){
                    this.notificationsService.error('An error occured', 'Please reload the page');
                }

                broker.serverStats = null;
                setTimeout(() => { this.getBrokerStatus(broker); }, 15000); // wait for server to restart

            });
    }

    startThroughSsh(){
        if (!this.isSSHValid(this.sshBroker.sshLogin)){
            return null;
        }

        this.sshBroker.connecting = true;
        this.brokerService.startThroughSsh(this.sshBroker).subscribe(
            res => {
                if (res !== -1){
                    this.notificationsService.info('SSH', 'Command executed');
                    setTimeout(() => {this.getBrokerStatus(this.sshBroker); }, 8000);
                }
                else {
                    this.notificationsService.error('An error occured', 'Please reload the page');
                }
            });
    }

    isSSHValid(sshLogin: SshLogin): boolean{
        if (!sshLogin){
            sshLogin.isValid = false;
            return false;
        }
        if (sshLogin.user && sshLogin.password && sshLogin.host && sshLogin.port && sshLogin.path){
            sshLogin.isValid = true;
            return true;
        }
        sshLogin.isValid = false;
        return false;
    }

    updateData(broker){
        this.isSSHValid(broker.sshLogin);
        this.getBrokerStatus(broker);
    }

}
