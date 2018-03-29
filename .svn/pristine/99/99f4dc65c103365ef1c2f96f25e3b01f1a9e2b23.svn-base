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

import { Component, Input, OnInit, EventEmitter, Output, NgZone, TemplateRef, ChangeDetectorRef} from '@angular/core';
import { BundleManagerService } from '../../../services/manager/bundle-manager.service';
import { KatServer } from '../../../../../model/KatServer';
import { UrlService } from '../../../../../services/url.service';
import { ServerService } from '../../../../../services/manager/server.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { KatJob } from '../../../../../model/katJob';
import { MailNotif } from '../../../../../model/mailNotif';
import { OverridedContext } from '../../../../../model/overridedContext';
import { Context } from '../../../../../model/context';
import { IamRight } from '../../../../../model/Iam/iamRight';
import { AuthService } from '../../../../../services/auth/auth.service';
import { ScheduleUpdater } from '../../../../../model/scheduleUpdater';
import { CronOptions } from 'cron-editor/cron-editor';



@Component({
    selector: 'scheduled-cron-manager',
    templateUrl: 'scheduled-cron-manager.component.html',
    styleUrls: [
        'scheduled-cron-manager.component.css'
    ],
})
export class ScheduledCronManagerComponent implements OnInit{
    scheduledCron: KatJob[] = [];
    rowsOnPage = 10;
    filterQuery: string;
    modalRef: BsModalRef;
    server: KatServer;
    intervalSet: boolean = false;
    selectedKatJob: KatJob;
    mailStr: string;
    telStr: string;
    currentMailNotif: MailNotif;
    currentOverridedContext: OverridedContext;
    dataToUpdate: ScheduleUpdater;
    @Input()
    set setServer(server: KatServer) {
        this.server = server;
        this.ngOnInit();
    }

    @Output()
    deleteScheduller = new EventEmitter();
    right: IamRight;
    versionDispos: string[];
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
    ngOnInit() {
        this.serverService.getKatJobs(this.server.id).subscribe(result => {
            if ( result ) {
                this.scheduledCron = [];
                result.forEach(res => {
                    let context = res.context;
                    let adding = false;
                    this.right.contextList.forEach(ct => {
                       if (res.context === ct.label) {
                           adding = true;
                       }
                    });
                    if ( adding || this.right.isAdmin || this.right.isSuperAdmin ) {
                    this.serverService.getEmailAlert( res.propertyFile, this.server.id ).subscribe(emails => {
                        res.mailActive = emails.active;
                    });
                    this.scheduledCron.push(KatJob.fromObject(res));
                    }
                });
            }
        });

        if (this.intervalSet === false) {
            this.intervalSet = true;
            setInterval(() => {
                if ( this.authService.getUser() ) {
                    this.ngOnInit();
                }
               }, 10000);
        }
    }

    update() {
        this.scheduledCron = [];
        this.serverService.getKatJobs(this.server.id).subscribe(result => {
            this.scheduledCron = result;
        });
    }

    constructor(private urlService: UrlService, private serverService: ServerService,
                private modalService: BsModalService, private authService: AuthService){
        this.right = this.authService.getRights();
    }



    changeStatus(pid, status) {
        this.serverService.changeKatJobStatus(this.server.id, pid, status).subscribe(result => {
            this.scheduledCron.forEach(sc => {
                if (sc.pid === pid) {
                    if (status === 'resume') {
                        sc.state = 'active';
                    }
                    if (status === 'pause') {
                        sc.state = 'paused';
                    }

                    if (status === 'start') {
                        sc.running = true;
                    }
                    if (status === 'kill') {
                        sc.running = false;
                    }
                }
            });
        });
    }

    deleteKatJob(katJob: KatJob) {
        let fileName = katJob.propertyFile;
        this.serverService.deleteKatJobs(this.server.id, fileName).subscribe(result => {
           if (result === true) {
               this.scheduledCron.forEach((sc, index, object) => {
                  if (sc === katJob)  {
                      this.deleteScheduller.emit(katJob);
                      object.splice(index, 1);

                  }
               });
           }
        });
    }

    addCron($event: KatJob) {
        let obj = KatJob.fromObject($event);

         this.scheduledCron.push(obj);
    }

/*    openModalSms(template: TemplateRef<any>, katJob) {
        this.selectedKatJob = katJob;
        this.modalRef = this.modalService.show(template);
    }*/

    openModalMail(template: TemplateRef<any>, katJob) {
        this.selectedKatJob = katJob;
         this.serverService.getEmailAlert( katJob.propertyFile, this.server.id ).subscribe(res => {
             this.currentMailNotif = res;
         });
        this.modalRef = this.modalService.show(template);
    }

    openModalCron(template: TemplateRef<any>, katJob) {
        this.selectedKatJob = katJob;
        this.modalRef = this.modalService.show(template, {class: 'modal-lg'});
    }

    openModalOverride(template: TemplateRef<any>, katJob) {
        this.selectedKatJob = katJob;
        this.serverService.getOveridedContext( katJob.propertyFile, this.server.id ).subscribe(res => {
            this.currentOverridedContext = res;
        });

        this.modalRef = this.modalService.show(template, {class: 'modal-lg'});
    }
    changeVersion(template: TemplateRef<any>, katJob: KatJob) {
        this.selectedKatJob = katJob;
        this.serverService.getJobsVersion(katJob.name, this.server.id).subscribe(then => {
           this.versionDispos = then;
        });
        this.modalRef = this.modalService.show(template, {class: 'modal-lg'});
    }

    closeModal() {
        this.modalRef.hide();
        this.modalRef = null;
    }

    validNotifMail(needActive: boolean) {
        if (needActive === true) {
            this.currentMailNotif.active = true;
            this.scheduledCron.filter(x => x.propertyFile === this.currentMailNotif.scheduleFile)[0].mailActive = true;
        } else {
            this.currentMailNotif.active = false;
            this.scheduledCron.filter(x => x.propertyFile === this.currentMailNotif.scheduleFile)[0].mailActive = false;
        }
        this.serverService.postEmailAlert(this.server.id, this.currentMailNotif).subscribe();
    }

/*
    removeOneTel(katJobTel) {
        let katJob = this.scheduledCron.filter(x => x.pid === this.selectedKatJob.pid)[0];
        const index: number = katJob.notifySms.indexOf(katJobTel);
        if (index !== -1) {
            katJob.notifySms.splice(index, 1);
        }
    }
*/

/*    addTel() {
        let katJob = this.scheduledCron.filter(x => x.pid === this.selectedKatJob.pid)[0];
        if (katJob.notifySms.filter(y => y === this.telStr).length === 0) {
            katJob.notifySms.push(this.telStr);
            this.telStr = '';
        }
    }*/

    addEmail() {
        if (this.currentMailNotif.recipients.filter(y => y === this.mailStr).length === 0) {
            this.currentMailNotif.recipients.push(this.mailStr);
            this.serverService.postEmailAlert(this.server.id, this.currentMailNotif).subscribe();
            this.mailStr = '';
        }
    }

    removeOneMail(katJobMail: string) {
        const index: number = this.currentMailNotif.recipients.indexOf(katJobMail);
        if (index !== -1) {
            this.currentMailNotif.recipients.splice(index, 1);
        }
        this.serverService.postEmailAlert(this.server.id, this.currentMailNotif).subscribe();
    }

    overrideContext(katJob: KatJob, value: boolean) {
        katJob.overloadedContext = value;
    }

    changeContextOverrideStatus(context: Context){
        if (context.active === true) {
            context.active = false;
            this.serverService.postOverridedContext(this.server.id, this.currentOverridedContext).subscribe();
        }else {
            context.active = true;
            this.serverService.postOverridedContext(this.server.id, this.currentOverridedContext).subscribe();
        }

        this.scheduledCron.filter(x => x.propertyFile === this.currentMailNotif.scheduleFile)[0].overloadedContext = true;
    }

    updateOverloadedValue() {
        this.serverService.postOverridedContext(this.server.id, this.currentOverridedContext).subscribe();
        this.scheduledCron.filter(x => x.propertyFile === this.currentMailNotif.scheduleFile)[0].overloadedContext = true;
    }

    removeContextOverloaded(katJob) {
        this.serverService.deleteOveridedContext( katJob.propertyFile, this.server.id).subscribe();
        katJob.overloadedContext = null;
    }

    updateVersionValue(value) {
        let cron = this.scheduledCron.filter(sc => sc.pid === this.selectedKatJob.pid)[0];
        this.dataToUpdate = new ScheduleUpdater;
        this.dataToUpdate.scheduleFile = cron.propertyFile;
        this.dataToUpdate.propertyName = 'job.version';
        this.dataToUpdate.propertyValue = value;
    }

    chhangeUpdateValue() {
        let cron = this.scheduledCron.filter(sc => sc.pid === this.selectedKatJob.pid)[0];
        this.serverService.updateJob(this.dataToUpdate, this.server.id).subscribe(
            then => {
                cron.version = this.dataToUpdate.propertyValue;
            }
        );
        this.modalRef.hide();
    }

    updateCron() {
        let cron = this.scheduledCron.filter(sc => sc.pid === this.selectedKatJob.pid)[0];
        let dataToUpdate = new ScheduleUpdater();
        dataToUpdate.scheduleFile = cron.propertyFile;
        dataToUpdate.propertyName = 'job.scheduling';
        dataToUpdate.propertyValue = this.selectedKatJob.scheduling;

        this.serverService.updateJob(dataToUpdate, this.server.id).subscribe(
            then => {
                cron.scheduling = this.selectedKatJob.scheduling;
                this.modalRef.hide();
            }
        );
    }

    updateCronManager(value) {
        this.selectedKatJob.scheduling = value;
    }
}
