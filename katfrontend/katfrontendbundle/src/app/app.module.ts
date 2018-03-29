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

import { NgModule, ApplicationRef, LOCALE_ID } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule, Http } from '@angular/http';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { removeNgStyles, createNewHosts, createInputTransfer } from '@angularclass/hmr';
import { ENV_PROVIDERS } from './projectEnvironment';
import { ROUTES, appRoutingProviders } from './app.routes';
import { AppComponent } from './app.component';
import { APP_RESOLVER_PROVIDERS } from './app.resolver';
import { AppState, InternalStateType } from './app.service';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HomeComponent } from './components/home/home.component';
import { ConfigurationService } from './services/configuration.service';
import { UrlService } from './services/url.service';
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { CurrentPageService } from './services/communication/current-page.service';
import { SimpleNotificationsModule } from 'angular2-notifications/components';
import { ServerComponent } from './components/manager/server-list/server/server.component';
import { BrokerComponent } from './components/manager/server-list/broker/broker.component';
import { ServerService } from './services/manager/server.service';
import { BrokerService } from './services/manager/broker.service';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ClusterComponent } from './components/manager/server-list/cluster/cluster.component';
import { ServerDetailsComponent } from './components/manager/server-details/server-details.component';
import { ClusterDetailsComponent } from './components/manager/cluster-details/cluster-details.component';
import { BundleManagerComponent } from './components/manager/server-details/bundle/bundle-manager.component';
import { FeatureManagerComponent } from './components/manager/server-details/feature/feature-manager.component';
import { RepositoryManagerComponent } from './components/manager/server-details/repository/repository-manager.component';
import { ServerManagerComponent } from './components/manager/server-details/server/server-manager.component';
import { ClusterManagerComponent } from './components/manager/cluster-details/cluster/cluster-manager.component';
import { ClusterBundleManagerComponent } from './components/manager/cluster-details/bundle/cluster-bundle-manager.component';
import { ClusterFeatureManagerComponent } from './components/manager/cluster-details/feature/cluster-feature-manager.component';
import { ClusterRepositoryManagerComponent } from './components/manager/cluster-details/repository/cluster-repository-manager.component';
import { ClusterService } from './services/manager/cluster.service';
import { ManagerService } from './services/manager/manager.service';
import { DndModule } from 'ng2-dnd';
import {AccordionModule, BsDropdownModule, TooltipModule} from 'ngx-bootstrap';
import { BundleManagerService } from './services/manager/bundle-manager.service';
import { FeatureManagerService } from './services/manager/feature-manager.service';
import { RepositoryManagerService } from './services/manager/repository-manager.service';
import { DataTableModule } from 'angular2-datatable';
import { DataFilterPipe } from './pipes/data-filter.pipe';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { GaugeModule } from 'angular-gauge';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ResizableModule } from 'angular-resizable-element';
import { SplitPaneModule } from 'ng2-split-pane/lib/ng2-split-pane';
import { EnvironmentService } from './services/environment.service';
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { BrokerManagerComponent } from './components/manager/broker-details/broker/broker-manager.component';
import { BrokerDetailsComponent } from './components/manager/broker-details/broker-details.component';
import { EnvironmentComponent } from './components/manager/server-list/environment.component';
import { ServerListComponent } from './components/manager/server-list/server/server-list.component';
import { ZipManagerComponent } from './components/manager/server-details/server/zip-manager/zip-manager.component';
import { Ng2UploaderModule } from 'ng2-uploader';
import { ToolTipModule } from 'angular2-tooltip';
import { CronManagerComponent } from './components/manager/server-details/server/cron-manager/cron-manager.component';
import { CronEditorModule } from 'cron-editor/cron-editor';
import { ScheduledCronManagerComponent } from './components/manager/server-details/server/cron-manager/scheduled-cron-manager.component';
import { WorkflowEditComponent } from './components/workflow/edit/workflow-edit.component';
import { WorkflowListComponent } from './components/workflow/list/workflow-list.component';
import { TreeModule } from 'ng2-tree';
import { ArtifactService } from './services/manager/artifact.service';
import { SelectModule } from 'ng2-select';
import { WorkflowService } from './services/manager/workflow.service';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RawPipe } from './pipes/raw.pipe';
import { UploadComponent } from './components/repository/upload.component';
import { TokenInterceptor } from './services/auth/token.interceptor';
import { AuthService } from './services/auth/auth.service';
import { LoginComponent } from './components/login/login.component';
import { UserService } from './services/auth/user.service';
import { UserRightHomeComponent } from './components/user-right/user-right-home.component';
import { UserRightGroupComponent } from 'app/components/user-right/groupes/user-right-group.component';
import {UserRightRoleComponent} from "./components/user-right/role/user-right-roles.component";
import {UserRightContextComponent} from "./components/user-right/context/user-right-context.component";
import {UserRightContextGroupComponent} from "./components/user-right/context-groups/user-right-context-group.component";
import {AngularMultiSelectModule} from "angular2-multiselect-dropdown";
import {WorkflowManagerComponent} from "./components/workflow/manager/workflow-manager.component";
/*
 * Platform and Environment providers/directives/pipes
 */
// App is our top level component

// Application wide providers
const APP_PROVIDERS = [
    ...APP_RESOLVER_PROVIDERS,
    AppState
];

type StoreType = {
    state: InternalStateType,
    restoreInputValues: () => void,
    disposeOldHosts: () => void
};

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: Http) {
    return new TranslateHttpLoader(http);
}

/**
 * `AppModule` is the main entry point into Angular2's bootstraping process
 */
@NgModule({
    bootstrap: [AppComponent],
    declarations: [
        AppComponent,
        LoginComponent,
        HomeComponent,
        NavBarComponent,
        EnvironmentComponent,
        ServerComponent,
        ServerListComponent,
        BrokerComponent,
        ClusterComponent,
        ServerDetailsComponent,
        ClusterDetailsComponent,
        BundleManagerComponent,
        FeatureManagerComponent,
        DataFilterPipe,
        RawPipe,
        ServerManagerComponent,
        ClusterManagerComponent,
        RepositoryManagerComponent,
        ClusterBundleManagerComponent,
        ClusterFeatureManagerComponent,
        ClusterRepositoryManagerComponent,
        BrokerManagerComponent,
        BrokerDetailsComponent,
        ZipManagerComponent,
        CronManagerComponent,
        ScheduledCronManagerComponent,
        WorkflowEditComponent,
        WorkflowListComponent,
        UploadComponent,
        UserRightHomeComponent,
        UserRightRoleComponent,
        UserRightGroupComponent,
        UserRightContextComponent,
        UserRightContextGroupComponent,
        WorkflowManagerComponent
    ],

    imports: [ // import Angular's modules
        BrowserModule,
        FormsModule,
        SelectModule,
        HttpModule,
        Ng2UploaderModule,
        SimpleNotificationsModule.forRoot(),
        ModalModule.forRoot(),
        AccordionModule.forRoot(),
        CronEditorModule,
        TreeModule,
        HttpClientModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [Http]
            }
        }),
        RouterModule.forRoot(ROUTES, {useHash: true, preloadingStrategy: PreloadAllModules}),
        DndModule.forRoot(),
        TooltipModule.forRoot(),
        CollapseModule.forRoot(),
        DataTableModule,
        BrowserAnimationsModule,
        GaugeModule.forRoot(),
        TabsModule.forRoot(),
        CollapseModule.forRoot(),
        ResizableModule,
        SplitPaneModule
    ],
    providers: [ // expose our Services and Providers into Angular's dependency injection
        {provide: LOCALE_ID, useValue: 'fr'},
        appRoutingProviders,
        ENV_PROVIDERS,
        APP_PROVIDERS,
        ConfigurationService,
        UrlService,
        CurrentPageService,
        ServerService,
        BrokerService,
        ClusterService,
        ManagerService,
        BundleManagerService,
        FeatureManagerService,
        RepositoryManagerService,
        EnvironmentService,
        ArtifactService,
        WorkflowService,
        UserService,
        AuthService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenInterceptor,
            multi: true
        }
    ]
})
export class AppModule {
    constructor(public appRef: ApplicationRef, public appState: AppState) {
    }

    hmrOnInit(store: StoreType) {
        if (!store || !store.state) return;
        // set state
        this.appState._state = store.state;
        // set input values
        if ('restoreInputValues' in store) {
            let restoreInputValues = store.restoreInputValues;
            setTimeout(restoreInputValues);
        }

        this.appRef.tick();
        delete store.state;
        delete store.restoreInputValues;
    }

    hmrOnDestroy(store: StoreType) {
        const cmpLocation = this.appRef.components.map(cmp => cmp.location.nativeElement);
        // save state
        const state = this.appState._state;
        store.state = state;
        // recreate root elements
        store.disposeOldHosts = createNewHosts(cmpLocation);
        // save input values
        store.restoreInputValues = createInputTransfer();
        // remove styles
        removeNgStyles();
    }

    hmrAfterDestroy(store: StoreType) {
        // display new elements
        store.disposeOldHosts();
        delete store.disposeOldHosts;
    }

}

