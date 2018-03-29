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
import { Repository } from '../../../../model/repository';
import { RepositoryManagerService } from '../../../../services/manager/repository-manager.service';
import { KatCluster } from '../../../../model/KatCluster';
import {VirtualClusterMap} from "../../../../model/virtual-cluster-map";


/**
 * Created by bhumbert on 05/07/2017.
 */

@Component({
    selector: 'cluster-repository-manager',
    templateUrl: 'cluster-repository-manager.component.html',
    styleUrls: [
        'cluster-repository-manager.component.scss'
    ],
})
export class ClusterRepositoryManagerComponent {

    cluster: KatCluster;

    filterQuery: string;
    hasCellar: boolean;

    @Input()
    set setCluster(cluster: KatCluster) {
        this.cluster = cluster;
        this.hasCellar = this.cluster.type === 'cellar';
        this.getRepositories();
    }

    data: Array<Repository>;
    isLoading: boolean = true;

    constructor(private repositoryManagerService: RepositoryManagerService, private notificationsService: NotificationsService){}

    getRepositories(){
        this.isLoading = true;
        this.repositoryManagerService.getRepositories(this.cluster.id, this.hasCellar).subscribe(
        repositories => {
            this.data = repositories;
            this.isLoading = false;
        });
    }

    remove(repository: Repository){
        this.isLoading = true;

        this.repositoryManagerService.remove(this.cluster.id, repository.uri, this.hasCellar).subscribe(
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

        this.repositoryManagerService.add(this.cluster.id, repository, this.hasCellar).subscribe(
        res => {
            this.getRepositories();
            if (res == null || res.response != null){
                this.notificationsService.error('An error occured', res.response);
            }
        });
    }

    fillVirtualClusterMap(virtualClusterMap: Array<VirtualClusterMap>, repository: Repository){
        for (let data of this.data){
            if (data.uri === repository.uri){
                virtualClusterMap.push(new VirtualClusterMap(data.located, repository.uri));
            }
        }
    }
}