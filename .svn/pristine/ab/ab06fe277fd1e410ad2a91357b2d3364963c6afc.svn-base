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

import { Injectable } from '@angular/core';
import { ServerService } from './server.service';
import { Artifact } from '../../model/artifact';

/**
 * Created by bhumbert on 16/06/2017.
 */

@Injectable()
export class ArtifactService {
    public artifacts: Artifact [] = [];

    constructor(
        private serverService: ServerService
    ) {}

    resetArtifacts(): void {
        this.artifacts = [];
    }

    getArtifactsAndDeploymentInfos(currentTokenContinuation, nextTokenContinuation, serverId = null, withDeployInfos = false): Promise<any> {

        return new Promise((resolve, reject) => {
            this.serverService.getArtifactOnNexusServer(currentTokenContinuation).subscribe(result => {
                if (result) {
                    nextTokenContinuation = result.continuationToken;

                    if (result) {
                        result.items.forEach(res => {
                            let arti = new Artifact();
                            arti.isDeployed = false;
                            arti.name = res.name;
                            arti.version = res.version;
                            arti.url = res.assets[0].downloadUrl;
                            if (serverId !== null) {
                            this.serverService.getIsScheduledArtifact(serverId, res.name, res.version).subscribe(isSchedulled => {
                                if (isSchedulled === true) {
                                    arti.isScheduled = true;
                                } else {
                                    arti.isScheduled = false;
                                }
                                if (withDeployInfos) {
                                    this.serverService.getDeployedArtifact(serverId).subscribe(then => {
                                        if (then && then !== null) {
                                            then.forEach(artt => {
                                                if (res.name === artt.name && res.version === artt.version) {
                                                    arti.isDeployed = true;
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                            }
                                let lengthArt: any = this.artifacts
                                    .filter(art => art.name === arti.name && art.version === arti.version).length;
                                if (lengthArt === 0) {
                                    this.artifacts.push(arti);
                                }

                        });
                    }

                    if (result.continuationToken === null) {
                        resolve(this.artifacts);
                    } else {
                        currentTokenContinuation = result.continuationToken;
                        this.getArtifactsAndDeploymentInfos(currentTokenContinuation, nextTokenContinuation, serverId).then(res => {
                                resolve(this.artifacts);
                        });
                    }
                } else {
                    resolve([]);
                }
            });
        });
    }
}
