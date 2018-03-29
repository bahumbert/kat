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

import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { KatServer } from '../../../model/KatServer';
import { TabsetComponent } from 'ngx-bootstrap/tabs';
import { KatCluster } from 'app/model/KatCluster';

/**
 * Created by bhumbert on 16/06/2017.
 */


@Component({
    selector: 'cluster-details',
    templateUrl: 'cluster-details.component.html',
    styleUrls: [
        'cluster-details.component.scss'
    ],
})
export class ClusterDetailsComponent implements OnInit{

    cluster: KatCluster;
    tabSelection: number = 0;

    @ViewChild('staticTabs') staticTabs: TabsetComponent;

    @Input()
    set setCluster(cluster: KatCluster) {
        this.cluster = cluster;
        if (!cluster.cellarManager)
            this.staticTabs.tabs[0].active = true;
    }

    constructor(){

    }

    ngOnInit(){
    }
}
