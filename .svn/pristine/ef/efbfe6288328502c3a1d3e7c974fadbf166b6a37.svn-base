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
import { KatServer } from '../../../../model/KatServer';
import { NotificationsService } from 'angular2-notifications';
import { BundleManagerService } from '../../../../services/manager/bundle-manager.service';
import { KatBundle } from '../../../../model/KatBundle';

/**
 * Created by bhumbert on 16/06/2017.
 */


@Component({
    selector: 'bundle-manager',
    templateUrl: 'bundle-manager.component.html',
    styleUrls: [
        'bundle-manager.component.scss'
    ],
})
export class BundleManagerComponent {
    server: KatServer;

    filterQuery: string;
    uploading: boolean = false;

    @ViewChild('inputFile') upload: ElementRef;

    allowedExtensions: Array<string> = ['jar'];


    @Input()
    set setServer(server: KatServer) {
        this.server = server;
        this.getBundles();
    }

    data: Array<KatBundle>;
    isLoading: boolean = true;

    constructor(private bundleManagerService: BundleManagerService, private notificationsService: NotificationsService){}

    getBundles(){
        this.isLoading = true;
        this.bundleManagerService.getBundles(this.server.id).subscribe(
        bundles => {
            this.data = bundles;
            this.isLoading = false;
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });
    }

    startOrStop(bundle: KatBundle){
        this.isLoading = true;
        let action: boolean = true;
        if (bundle.state === 'Active'){
            action = false;
        }

        this.bundleManagerService.startOrStop(action, this.server.id, bundle.id).subscribe(
        res => {
            this.getBundles();
            if (res == null || res.response != null) {
                this.notificationsService.error('An error occured', res.response);
            }
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });

    }

    restart(bundle: KatBundle){
        this.isLoading = true;
        let action: boolean = true;

        this.bundleManagerService.restart(this.server.id, bundle.id).subscribe(
        res => {
            this.getBundles();
            if (res == null || res.response != null) {
                this.notificationsService.error('An error occured', res.response);
            }
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });
    }

    uninstall(bundle: KatBundle){
        this.isLoading = true;
        let action: boolean = true;

        this.bundleManagerService.uninstall(this.server.id, bundle.id).subscribe(
        res => {
            this.getBundles();
            if (res == null || res.response != null) {
                this.notificationsService.error('An error occured', res.response);
            }
        },
        error => {
            this.notificationsService.error('An error occured', 'Please reload the page');
        });

    }

    uploadFile(){

        if (this.uploading || !this.upload || !this.upload.nativeElement.files || this.upload.nativeElement.files.length === 0){
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

        this.bundleManagerService.postBundleFile(this.upload.nativeElement.files[0], extension, this.server.id).subscribe(
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