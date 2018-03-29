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


import { KatServer } from './KatServer';
import {CellarState} from "./cellar-state";
import {KatBroker} from "./KatBroker";
/**
 * Created by bhumbert on 20/04/2017.
 */

export class KatCluster{
  id: string;
  name: string;
  type: string;
  autoUpdate: boolean;
  cellarGroup: string;
  servers: Array<KatServer>;
  brokers: Array<KatBroker>;
  cellarManager: string;

  selected: boolean;

  constructor() {
    this.name = 'New cluster';
  }
}
