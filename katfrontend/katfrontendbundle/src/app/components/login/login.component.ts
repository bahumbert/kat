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

import {Component, Input, OnInit, EventEmitter, Output, ViewChild} from '@angular/core';
import { CurrentPageService } from '../../services/communication/current-page.service';
import { AuthService } from '../../services/auth/auth.service';
import { UserService } from '../../services/auth/user.service';
import { Router } from '@angular/router';
import { NotificationsService } from 'angular2-notifications';

@Component({
    selector: 'login',
    templateUrl: 'login.component.html',
    styleUrls: [
        'login.component.scss'
    ]
})
export class LoginComponent {

    username: string;
    password:string;
    inputPwdFocused = false;
    inputUsrFocused = false;
    isLoading = false;

    constructor(
        private router: Router,
        private userService: UserService,
        private notificationService: NotificationsService
    ) {}

    public sendLogin(event: Event, username: string, password: string) {
        event.stopImmediatePropagation();
        event.stopPropagation();
        event.preventDefault();

        if (this.isLoading) {
            return false;
        }

        this.isLoading = true;

        this.userService.getAuth(username, password).subscribe(
            result => {
                localStorage.setItem('user', JSON.stringify(result));

                this.isLoading = false;
                this.router.navigate(['/home']);
            },
            error => {

                this.notificationService.error('Erreur', 'Invalid login');
                this.isLoading = false;
            }
        );

        return false;
    }

    changePassword($event){
        this.password = $event.target.value;
    }

    changeUsername($event){
        this.username = $event.target.value;
    }

    enterType(event:any){
        if(event.keyCode == 13){
            this.userService.getAuth(this.username, this.password).subscribe(
                result => {
                    localStorage.setItem('user', JSON.stringify(result));

                    this.isLoading = false;
                    this.router.navigate(['/home']);
                },
                error => {

                    this.notificationService.error('Erreur', 'Invalid login');
                    this.isLoading = false;
                }
            );
        }
    }
}
