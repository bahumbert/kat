import {
    ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit, TemplateRef,
    ViewChild
} from '@angular/core';
import { CurrentPageService } from '../../../services/communication/current-page.service';
import { UserService } from '../../../services/auth/user.service';
import { IamRole } from '../../../model/Iam/iamRole';
import { DataTable } from 'angular2-datatable';
import { IamGroup } from '../../../model/Iam/iamGroup';
import { IamContextGroup } from '../../../model/Iam/iamContextGroup';
import { KatEnvironment } from '../../../model/KatEnvironment';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { EnvironmentService } from '../../../services/environment.service';
import { Environment } from '../../../configuration/environment';
import { IamRight } from '../../../model/Iam/iamRight';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'user-right-group',
    templateUrl: 'user-right-group.component.html',
    styleUrls: [
        'user-right-group.component.css'
    ]
})

export class UserRightGroupComponent implements OnInit {
    @Input()
    contextGroups: IamContextGroup[];
    group: IamGroup;
    groups: IamGroup[];
    rowsOnPage: number = 10;
    filterQuery: string;
    roles: IamRole[];
    environments: KatEnvironment[];
    environmentSelect: { text: string, id: string}[];
    valueEnvironment: { text: string, id: string}[];
    envDisplay: KatEnvironment[];
    modalRef: BsModalRef;
    firstRole: IamRole;
    firstCg: IamContextGroup;
    @ViewChild(DataTable) dataTableComponent: DataTable;
    right: IamRight;
    constructor(private currentPageService: CurrentPageService, private userService: UserService, private authService: AuthService,
                private cdRef: ChangeDetectorRef, private modalService: BsModalService, private environmentService: EnvironmentService) {
        this.right = this.authService.getRights();
    }

    ngOnInit() {
        this.environmentSelect = [];
        this.valueEnvironment = [];
        this.group = new IamGroup;
        this.userService.getRoles().subscribe(res => {
            this.roles = res;
        });

        this.userService.getGroups().subscribe(res => {
            this.groups = res;
            this.cdRef.detectChanges();
        });

        this.environmentService.getEnvironments().subscribe(
            environments => {
                this.environments = environments;
                this.environments.forEach(env => {
                    this.environmentSelect.push( { text: env.name, id: env.id});
                });
            });
    }

    changeRole(value: String) {
       let id = value.split(' ')[1];
        if (id !== 'Object') {
            this.group.role = this.roles.filter(env => env.id === id)[0];
        } else {
            this.group.role = this.firstRole;
        }
    }

    changeCg(value: String) {

        let id = value.split(' ')[1];
        if (id !== 'Object') {
            this.group.contextGroup =  this.contextGroups.filter(cg =>  cg.id === id )[0];
        } else {
            this.group.contextGroup = this.firstCg;
        }
    }
    setEnvDisplay(envs: KatEnvironment[]) {
      this.envDisplay = envs;
    }

    refreshValueEnv(value: any): void {
        this.valueEnvironment = [];
        if (this.group.environments) {
            this.group.environments.forEach(env => {
                this.valueEnvironment.push({ text: env.name, id: env.id});
            });
        }

    }

    public removedEnv(value: any): void {
        let item = this.group.environments.filter(env => env.id ===  value.id)[0];
        let index = this.group.environments.indexOf(item);
        this.group.environments.splice(index, 1);
        let val = this.valueEnvironment.filter(value2 => value2.id === value2.id)[0];
        let indexVal = this.valueEnvironment.indexOf(val);
        this.valueEnvironment.splice(indexVal, 1);
        this.cdRef.detectChanges();
    }

    selectedEnv(value: any): void {
        let envToAdd = this.environments.filter(env => env.id ===  value.id)[0];
        this.group.environments.push(envToAdd);
        this.valueEnvironment.push({ text: envToAdd.name, id: envToAdd.id});
        this.cdRef.detectChanges();
    }




    openModal(template: TemplateRef<any>) {
        this.modalRef = this.modalService.show(template);
        this.firstRole = this.group.role;
        this.firstCg = this.group.contextGroup;

    }

    openModalCreateGroup(template: TemplateRef<any>) {
        this.group = new IamGroup;
        this.valueEnvironment = [];
        this.modalRef = this.modalService.show(template);
    }

    closeModal() {
        this.modalRef.hide();
    }

    submitGroup() {
        if (!this.group.id) {
            this.userService.postGroup(this.group).subscribe(then => {
                this.ngOnInit();
                this.cdRef.detectChanges();
            });
        } else {
            this.userService.putGroup(this.group).subscribe(then => {
                this.groups.filter(gp => gp.id === then.id)[0] = then;
            });
        }
        this.modalRef.hide();
        this.group = new IamGroup;
    }

    editRole(grp: IamGroup): void {
        this.group = grp;
        this.valueEnvironment = [];
        this.group.environments.forEach(env => {
            this.valueEnvironment.push({ text: env.name, id: env.id});
        });
    }

    deleteGroup(grp: IamGroup): void{
        this.userService.deleteGroup(grp.id).subscribe(then => {
        });
        let index = this.groups.indexOf(grp);
        this.groups.splice(index, 1);
    }

}

