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

import { Component, Input, Output, EventEmitter, OnChanges} from '@angular/core';
import { NotificationsService } from 'angular2-notifications';
import { BundleManagerService } from '../../../services/manager/bundle-manager.service';
import { KatServer } from '../../../../../model/KatServer';
import { UrlService } from '../../../../../services/url.service';
import { ServerService } from '../../../../../services/manager/server.service';
import { Artifact } from '../../../../../model/artifact';
import { ArtifactService } from '../../../../../services/manager/artifact.service';
import {IamRight} from "../../../../../model/Iam/iamRight";
import {UserService} from "../../../../../services/auth/user.service";
import {AuthService} from "../../../../../services/auth/auth.service";

/**
 * Created by bhumbert on 22/06/2017.
 */

@Component({
    selector: 'zip-manager',
    templateUrl: 'zip-manager.component.html',
    styleUrls: [
        './zip-manager.scss'
    ],
})

export class ZipManagerComponent implements OnChanges {
    public options = {
        position: ['bottom', 'left'],
        timeOut: 700,
    };

    rowsOnPage: number = 10;
    server: KatServer;
    artifacts: Artifact[] = [];
    filterQuery: string;
    right: IamRight;

    @Input()
    set setServer(server: KatServer) {
        this.server = server;
        this.init();
    }

    @Output() addArtifact = new EventEmitter();
    @Output() removeArtifact = new EventEmitter();

    constructor(
        private artifactService: ArtifactService,
        private urlService: UrlService,
        private serverService: ServerService,
        private notifService: NotificationsService,
        private authService: AuthService
    ) {
        this.right = this.authService.getRights();
    }

    ngOnChanges(changes) {
        this.init();
    }

    init() {
        this.artifacts = [];
        this.artifactService.resetArtifacts();
        this.artifactService
            .getArtifactsAndDeploymentInfos(null, null, this.server.id, true)
            .then(artifacts => {
                this.artifacts = artifacts;
            })
        ;
    }

    updateArtifactStatus(artifact, add) {
        let count = 0;

        this.artifacts.forEach(art => {
            if (art.name === artifact.name && art.version === art.version) {
                count = count + 1;
            }
        });

        if (count === this.artifacts.filter(artt => artt.name === artifact.name).length && add === 0) {
            this.artifacts.filter(art => art.name === artifact.name && art.version === artifact.version).forEach(art => {
                art.isScheduled = false;
            });
        }

        if (count === this.artifacts.filter(artt => artt.name === artifact.name).length && add === 1) {
            this.artifacts.filter(art => art.name === artifact.name && art.version === artifact.version).forEach(art => {
                art.isScheduled = true;
            });
        }
    }

    deployArtifact(artifact: Artifact): void {
        let urlEncoded = encodeURI(artifact.url);
        this.serverService.deployArtifact(urlEncoded, this.server.id).subscribe(result => {
            if (result === true) {
                artifact.isDeployed = true;
                this.notifService.success('Success', 'Deploy successfull', this.options);
                this.addArtifact.emit(artifact);

            } else {
                this.notifService.error('Oups', 'An error occured', this.options);
            }
        });
    }

    undeployArtifact(artifact: Artifact): void {

        this.serverService.undeployArtifact(artifact.name, artifact.version, this.server.id).subscribe(result => {
            if (result == true) {
                artifact.isDeployed = false;
                this.notifService.success('Success', 'Undeploy successfull', this.options);
                this.removeArtifact.emit(artifact);
            } else {
                this.notifService.error('Error', 'This artifact is already not deployed', this.options);
            }
        });
    }

    notAllowed() {
        this.notifService.error('Success', 'Action impossible (job scheduled)', this.options);
    }
}
