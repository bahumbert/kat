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

import { Component, Input, OnInit, EventEmitter, Output, NgZone, TemplateRef } from '@angular/core';
import { BundleManagerService } from '../../../services/manager/bundle-manager.service';
import { KatServer } from '../../../../../model/KatServer';
import { UrlService } from '../../../../../services/url.service';
import { ServerService } from '../../../../../services/manager/server.service';
import { CronEditorModule, CronOptions } from 'cron-editor/cron-editor';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { DeploymentCron } from '../../../../../model/deployment-cron';
import { Artifact } from '../../../../../model/artifact';
import { NotificationsService } from 'angular2-notifications';
import { Context } from '../../../../../model/context';
import { UserService } from '../../../../../services/auth/user.service';
import { AuthService } from '../../../../../services/auth/auth.service';
import { IamRight } from '../../../../../model/Iam/iamRight';
/**
 * Created by bhumbert on 22/06/2017.
 */


@Component({
    selector: 'cron-manager',
    templateUrl: 'cron-manager.component.html',
    styleUrls: [
        'cron-manager.component.css'
    ],
})
export class CronManagerComponent implements OnInit{
    public options = {
        position: ['bottom', 'left'],
        timeOut: 700,
    };
    public cronOptions: CronOptions = {
        formInputClass: 'form-control cron-editor-input',
        formSelectClass: 'form-control cron-editor-select',
        formRadioClass: 'cron-editor-radio',
        formCheckboxClass: 'cron-editor-checkbox',
        defaultTime: '10:00:00',
        hideMinutesTab: false,
        hideHourlyTab: false,
        hideDailyTab: false,
        hideWeeklyTab: false,
        hideMonthlyTab: false,
        hideYearlyTab: false,
        hideAdvancedTab: true,
        use24HourTime: true,
        hideSeconds: false
    };
    contexts: Context[];
    @Output() cronUpdated = new EventEmitter();
    @Input()
    artifacts: Artifact[];
    selectedValue: Artifact;
    toStr = JSON.stringify;
    cronDeployment: DeploymentCron;
    cronExpression: string = '0 0/1 * 1/1 * ? *';
    modalRef: BsModalRef;
    server: KatServer;
    right: IamRight;
    @Input()
    set setServer(server: KatServer) {
        this.server = server;
        this.ngOnInit();
    }
    ngOnInit() {

        this.userService.getContexts().subscribe(res => {
            this.contexts = res;
        });

        this.cronDeployment = new DeploymentCron();

        if (this.artifacts && this.artifacts[0]){
            this.cronDeployment.artifactName = this.artifacts[0].name;
            this.cronDeployment.version = this.artifacts[0].version;

        }
        this.userService.getContexts().subscribe(then => {
            if (!(this.right.isAdmin || this.right.isSuperAdmin)){
                this.contexts.forEach(ctx => {
                    then.forEach(ct => {
                        if (ct.id === ct.id) {
                            this.contexts.push(ct);
                        }
                    });
                });
            } else {
                this.contexts = then;
            }
        });

    }

    constructor(private urlService: UrlService, private serverService: ServerService, private modalService: BsModalService,
                private notifService: NotificationsService, private userService: UserService, private authService: AuthService){
        this.right = this.authService.getRights();
    }
    openModal(template: TemplateRef<any>) {
        this.modalRef = this.modalService.show(template);
    }

    closeModal() {
        this.modalRef.hide();
        this.modalRef = null;
    }

    getCron() {
        this.cronDeployment.cron = this.cronExpression;
        this.modalRef.hide();
    }

    submitCronDeployment() {
        this.serverService.deployCronArtifact(this.cronDeployment, this.server.id).subscribe(res => {
            if (res === true) {
                this.cronUpdated.emit(this.cronDeployment);
                this.cronDeployment = new DeploymentCron();
                this.notifService.success('Success', 'Cron deployment is done');
            }
        });
    }
    updateContext(artifact, context) {
        this.cronDeployment.context = null;
        artifact = JSON.parse(artifact);
        this.cronDeployment.version = artifact.version;
        this.cronDeployment.artifactName = artifact.name;
        let arrayContexts: Context[] = [];
        this.artifacts.forEach(art => {
            if (art.name === artifact.name && art.version === artifact.version) {
                art.contextList.forEach(fn => {
                    let ctx = this.contexts.filter(ct => ct.label === fn);
                    if (!(this.right.isAdmin || this.right.isSuperAdmin)) {
                    if (ctx[0]) {
                            this.right.contextList.forEach(ctl => {
                                if (ctl.id === ctx[0].id) {
                                    arrayContexts.push(ctx[0]);
                                }
                            });
                        }
                    }else{
                        let context = new Context;
                        context.label = fn;
                        arrayContexts.push(context);
                    }
                });
                this.contexts = [];
                this.contexts = arrayContexts;
            }
        });

    }

    updateContextVal(value) {
        this.cronDeployment.context = value;
    }
}
