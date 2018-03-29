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
import { Repository } from '../../../../model/repository';
import { RepositoryManagerService } from '../../../../services/manager/repository-manager.service';


/**
 * Created by bhumbert on 27/06/2017.
 */

@Component({
    selector: 'repository-manager',
    templateUrl: 'repository-manager.component.html',
    styleUrls: [
        'repository-manager.component.scss'
    ],
})
export class RepositoryManagerComponent {

    server: KatServer;

    filterQuery: string;

    @Input()
    set setServer(server: KatServer) {
        this.server = server;
        this.getRepositories();
    }

    data: Array<Repository>;
    isLoading: boolean = true;

    constructor(private repositoryManagerService: RepositoryManagerService, private notificationsService: NotificationsService){}

    getRepositories(){
        this.isLoading = true;
        this.repositoryManagerService.getRepositories(this.server.id).subscribe(
        repositories => {
            this.data = repositories;
            this.isLoading = false;
        });
    }

    remove(repository: Repository){
        this.isLoading = true;

        this.repositoryManagerService.remove(this.server.id, repository.name).subscribe(
        res => {
            this.getRepositories();
            if (res == null || res.response != null){
                this.notificationsService.error('An error occured', res.response);
            }
        });

    }

    addRepository(uri: string){
        if (!uri){
            return null;
        }

        this.isLoading = true;

        let repository: Repository = new Repository();
        repository.uri = uri.trim();

        this.repositoryManagerService.add(this.server.id, repository).subscribe(
            res => {
                this.getRepositories();
                if (res == null || res.response != null){
                    this.notificationsService.error('An error occured', res.response);
                }
            });
    }
}