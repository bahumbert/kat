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

import {Component} from "@angular/core";
import {CurrentPageService} from "../../services/communication/current-page.service";
import {AuthService} from "../../services/auth/auth.service";
import {IamRight} from "../../model/Iam/iamRight";

@Component({
    selector: 'home',
    templateUrl: 'home.component.html',
    styleUrls: [
        'home.component.scss'
    ]
})
export class HomeComponent {

    rights: IamRight;
  constructor(private currentPageService: CurrentPageService, private authService: AuthService) {
    this.rights = this.authService.getRights();
  }

}
