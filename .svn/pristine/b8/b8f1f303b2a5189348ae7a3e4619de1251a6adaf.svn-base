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

import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { KatCluster } from 'app/model/KatCluster';
import { KatBundle } from 'app/model/KatBundle';
import { NotificationsService } from 'angular2-notifications';
import { BundleManagerService } from '../../../../services/manager/bundle-manager.service';
import { VirtualClusterMap } from 'app/model/virtual-cluster-map';

/**
 * Created by bhumbert on 03/07/2017.
 */


@Component({
    selector: 'cluster-bundle-manager',
    templateUrl: 'cluster-bundle-manager.component.html',
    styleUrls: [
        'cluster-bundle-manager.component.scss'
    ],
})
export class ClusterBundleManagerComponent {
    cluster: KatCluster;

    filterQuery: string;
    uploading: boolean = false;
    hasCellar: boolean;

    @ViewChild('inputFile') upload: ElementRef;

    allowedExtensions: Array<string> = ['jar'];


    @Input()
    set setCluster(cluster: KatCluster) {
        this.cluster = cluster;
        this.hasCellar = this.cluster.type === 'cellar';
        this.getBundles();
    }

    data: Array<KatBundle>;
    isLoading: boolean = true;

    constructor(private bundleManagerService: BundleManagerService, private notificationsService: NotificationsService){}

    getBundles(){
        this.isLoading = true;
        this.bundleManagerService.getBundles(this.cluster.id, this.hasCellar).subscribe(
            bundles => {
                this.data = bundles;
                this.isLoading = false;
            });
    }

    startOrStop(bundle: KatBundle){
        this.isLoading = true;
        let action: boolean = true;

        //console.log(bundle);

        if (bundle.state === 'Active'){
            action = false;
        }

        let toChangeState: Array<VirtualClusterMap> = [];
        this.fillVirtualClusterMap(toChangeState, bundle);

        this.bundleManagerService.startOrStop(action, this.cluster.id, bundle.id, this.hasCellar, toChangeState).subscribe(
            res => {
                this.getBundles();
                if (res == null || res.response != null) {
                    this.notificationsService.error('An error occured', res.response);
                }
            });

    }

    uninstall(bundle: KatBundle){
        this.isLoading = true;
        let action: boolean = true;

        let toUninstall: Array<VirtualClusterMap> = [];
        this.fillVirtualClusterMap(toUninstall, bundle);

        this.bundleManagerService.uninstall(this.cluster.id, bundle.id, this.hasCellar, toUninstall).subscribe(
        res => {
            this.getBundles();
            if (res == null || res.response != null) {
                this.notificationsService.error('An error occured', res.response);
            }
        });
    }

    restart(bundle: KatBundle){
        this.isLoading = true;
        let action: boolean = true;

        let toRestart: Array<VirtualClusterMap> = [];
        this.fillVirtualClusterMap(toRestart, bundle);

        this.bundleManagerService.restart(this.cluster.id, bundle.id, this.hasCellar, toRestart).subscribe(
        res => {
            this.getBundles();
            if (res == null || res.response != null) {
                this.notificationsService.error('An error occured', res.response);
            }
        });
    }

    fillVirtualClusterMap(virtualClusterMap: Array<VirtualClusterMap>, bundle: KatBundle){
        for (let data of this.data){
            if (data.name === bundle.name){
                virtualClusterMap.push(new VirtualClusterMap(data.located, String(data.id)));
            }
        }
    }

    uploadFile(){

        if (this.uploading || !this.upload || !this.upload.nativeElement.files || this.upload.nativeElement.files.length == 0){
            return;
        }

        let extension = this.upload.nativeElement.files[0].name.split('.');
        extension = extension[extension.length - 1].toLowerCase();

        let index = this.allowedExtensions.findIndex(allowed => {
            return allowed === extension;
        });
        if (index === -1){
            this.notificationsService.error('Error', 'This file type is not allowed');
            return;
        }

        this.uploading = true;

        this.bundleManagerService.postBundleFile(this.upload.nativeElement.files[0], extension, this.cluster.cellarManager).subscribe(
            res => {
                setTimeout(() => { this.getBundles(); }, 2000);
                if (res == null) {
                    this.notificationsService.error('An error occured', 'The bundle was not uploaded correctly');
                }
                this.uploading = null;
            },
            error => {
                this.notificationsService.error('An error occured', 'Please reload the page');
                this.uploading = null;
            });
    }
}