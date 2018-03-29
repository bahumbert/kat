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

import { PhysicalStats } from './physical-stats';
import { SshLogin } from './ssh-login';
import { AuthCredentials } from 'app/model/auth-credentials';

/**
 * Created by bhumbert on 10/07/2017.
 */

export class KatBroker {
    id: string;
    name: string;
    type: string;
    systemName: string;
    jolokiaUrl: string;

    parentCluster: string;
    componentId: number;
    isStarted: boolean;
    selected: boolean;
    connecting: boolean;

    serverStats: PhysicalStats;
    sshLogin: SshLogin;
    authCredentials: AuthCredentials;

    constructor(){
        this.name = 'New broker';
    }
}
