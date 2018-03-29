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

import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { KatServer } from '../../../model/KatServer';
import { TabsetComponent } from 'ngx-bootstrap/tabs';
import { KatBroker } from 'app/model/KatBroker';

/**
 * Created by , Brokerbhumbert on 10/07/2017.
 */


@Component({
    selector: 'broker-details',
    templateUrl: 'broker-details.component.html',
    styleUrls: [
        'broker-details.component.scss'
    ],
})
export class BrokerDetailsComponent implements OnInit{

    broker: KatBroker;
    tabSelection: number = 0;

    @Output()
    brokerUpdated = new EventEmitter();

    @ViewChild('staticTabs') staticTabs: TabsetComponent;

    @Input()
    set setBroker(broker: KatBroker) {
        this.broker = broker;
    }

    constructor(){

    }

    ngOnInit(){
    }

    brokerUpdatedEvent($event){
        this.brokerUpdated.emit($event);
    }
}
