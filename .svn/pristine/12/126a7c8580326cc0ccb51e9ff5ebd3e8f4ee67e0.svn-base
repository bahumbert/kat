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

import {Routes} from '@angular/router';
import {AuthGuard} from './auth-guard.service';
import {HomeComponent} from './components/home/home.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {EnvironmentComponent} from './components/manager/server-list/environment.component';
import {WorkflowListComponent} from "./components/workflow/list/workflow-list.component";
import {WorkflowEditComponent} from "./components/workflow/edit/workflow-edit.component";
import {UploadComponent} from "./components/repository/upload.component";
import {LoginComponent} from "./components/login/login.component";
import {UserRightHomeComponent} from "./components/user-right/user-right-home.component";

export const ROUTES: Routes = [
    {
        path: '',
        redirectTo: '/home',
        pathMatch: 'full'
    },
    {
        path: 'home', component: HomeComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'login', component: LoginComponent
    },

    {
        path: 'workflows',
        canActivate: [AuthGuard],
        children: [
            { path: '', component: WorkflowListComponent },
            { path: ':id', component: WorkflowEditComponent },
            { path: 'new', component: WorkflowEditComponent }
        ]
    },

    {
        path: 'profiles-right',
        canActivate: [AuthGuard],
        children: [
            { path: '', component: UserRightHomeComponent },
        ]
    },
    {
        path: 'uploads',
        canActivate: [AuthGuard],
        component: UploadComponent
    },
    {
        path: 'manager',
        canActivate: [AuthGuard],
        children: [
            { path: '', component: EnvironmentComponent },
            { path: ':id', component: EnvironmentComponent }
        ]
    }
];

export const authProviders = [
    AuthGuard,
];

export const appRoutingProviders: any[] = [
    authProviders
];
