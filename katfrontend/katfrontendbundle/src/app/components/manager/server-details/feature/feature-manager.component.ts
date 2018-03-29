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
import { KatServer } from '../../../../model/KatServer';
import { NotificationsService } from 'angular2-notifications';
import { FeatureManagerService } from '../../../../services/manager/feature-manager.service';
import { Feature } from '../../../../model/feature';

/**
 * Created by bhumbert on 16/06/2017.
 */


@Component({
    selector: 'feature-manager',
    templateUrl: 'feature-manager.component.html',
    styleUrls: [
        'feature-manager.component.scss'
    ],
})
export class FeatureManagerComponent {
    server: KatServer;

    filterQuery: string;

    @Input()
    set setServer(server: KatServer) {
        this.server = server;
        this.getFeatures();
    }

    data: Array<Feature>;
    isLoading: boolean = true;

    constructor(private featureManagerService: FeatureManagerService, private notificationsService: NotificationsService){}

    getFeatures(){
        this.isLoading = true;
        this.featureManagerService.getFeatures(this.server.id).subscribe(
        features => {
            this.data = features;
            this.isLoading = false;
        });
    }

    uninstall(feature: Feature){
        this.isLoading = true;

        if (feature.installed === false){
            return null;
        }

        this.featureManagerService.uninstall(this.server.id, feature.name + '/' + feature.version).subscribe(
        res => {
            this.getFeatures();
            if (res == null || res.response != null){
                this.notificationsService.error('An error occured', res.response);
            }
        });
    }

    install(feature: Feature){
        this.isLoading = true;

        if (feature.installed === true){
            return null;
        }

        this.featureManagerService.install(this.server.id, feature).subscribe(
            res => {
                this.getFeatures();
                if (res == null || res.response != null){
                    this.notificationsService.error('An error occured', res.response);
                }
            });

    }

}