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

import { Component, Input } from '@angular/core';
import { NotificationsService } from 'angular2-notifications';
import { FeatureManagerService } from '../../../../services/manager/feature-manager.service';
import { Feature } from 'app/model/feature';
import { KatCluster } from 'app/model/KatCluster';
import {VirtualClusterMap} from "app/model/virtual-cluster-map";


/**
 * Created by bhumbert on 05/07/2017.
 */


@Component({
    selector: 'cluster-feature-manager',
    templateUrl: 'cluster-feature-manager.component.html',
    styleUrls: [
        'cluster-feature-manager.component.scss'
    ],
})
export class ClusterFeatureManagerComponent {
    cluster: KatCluster;

    filterQuery: string;
    hasCellar: boolean;

    @Input()
    set setCluster(cluster: KatCluster) {
        this.cluster = cluster;
        this.hasCellar = this.cluster.type === 'cellar';
        this.getFeatures();
    }

    data: Array<Feature>;
    isLoading: boolean = true;

    constructor(private featureManagerService: FeatureManagerService, private notificationsService: NotificationsService){}

    getFeatures(){
        this.isLoading = true;
        this.featureManagerService.getFeatures(this.cluster.id, this.hasCellar).subscribe(
            features => {
                this.data = features;
                this.isLoading = false;
            },
            error => {
                this.notificationsService.error('An error occured', 'Please reload the page');
            });
    }

    uninstall(feature: Feature){
        this.isLoading = true;

        if (feature.installed === false){
            return null;
        }

        let toUninstall: Array<VirtualClusterMap> = [];
        this.fillVirtualClusterMap(toUninstall, feature);

        this.featureManagerService.uninstall(this.cluster.id, feature.name + '/' + feature.version, this.hasCellar, toUninstall).subscribe(
        res => {
            setTimeout( () => {this.getFeatures(); }, 4000);
            if (res == null || res.response != null){
                this.notificationsService.error('An error occured', res.response);
            }
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });

    }

    install(feature: Feature){
        this.isLoading = true;

        if (feature.installed === true){
            return null;
        }

        let toInstall: Array<VirtualClusterMap> = [];
        this.fillVirtualClusterMap(toInstall, feature);

        this.featureManagerService.install(this.cluster.id, feature, this.hasCellar, toInstall).subscribe(
        res => {
            this.getFeatures();
            if (res == null || res.response != null){
                this.notificationsService.error('An error occured', res.response);
            }
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });
    }

    fillVirtualClusterMap(virtualClusterMap: Array<VirtualClusterMap>, feature: Feature){
        for (let data of this.data){
            if (data.name === feature.name){
                virtualClusterMap.push(new VirtualClusterMap(data.located, feature.name + '/' + feature.version));
            }
        }
    }

}