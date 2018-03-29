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
import { AuthCredentials } from 'app/model/auth-credentials';

/**
 * Created by bhumbert on 19/04/2017.
 */

export class KatServer{
  id: string;
  name: string;
  type: string;
  jolokiaUrl: string;
  systemName: string;
  hasCellar: boolean;
  componentId: number;
  parentCluster: string;
  isStarted: boolean;
  connecting: boolean;
  serverStats: PhysicalStats;
  authCredentials: AuthCredentials;
  selected: boolean;
  correctlyConfigured: boolean;
    status: string;

  constructor(){
    this.name = 'New server';
  }

  toSendingJson() {
    return {
        "id": this.id,
        "name": this.name,
        "type": this.type,
        "jolokiaUrl": this.jolokiaUrl,
        "systemName": this.systemName,
        "correctlyConfigured": this.correctlyConfigured
    }
  }

  static fromObject(obj) {
      const ks = new KatServer();

      for(const key in obj) {
          ks[key] = obj[key];
      }

      return ks;
  }
}

