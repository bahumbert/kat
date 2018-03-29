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

import {Component, ViewEncapsulation, enableProdMode, OnChanges} from "@angular/core";
import {AuthService} from "./services/auth/auth.service";
import {NavigationEnd, NavigationStart, Router} from "@angular/router";

enableProdMode();

/*
 * App Component
 * Top Level Component
 */
@Component({
    selector: 'app',
    encapsulation: ViewEncapsulation.None,
    styleUrls: [
        './app.component.scss'
    ],
    templateUrl: './app.component.html'
})
export class AppComponent {
    isLogged = false;

    notificationOption = {
        position: ['top', 'right'],
        maxStack: 3,
        timeOut: 10000,
        showProgressBar: true
    };

    constructor(
        private router: Router,
        private authService: AuthService
    ) {
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.isLogged = this.authService.isAuthenticated();
            }
        });
    }
}
