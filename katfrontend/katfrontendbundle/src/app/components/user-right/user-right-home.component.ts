import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit, ViewChild } from '@angular/core';
import { CurrentPageService } from '../../services/communication/current-page.service';
import { UserService } from '../../services/auth/user.service';
import { Context } from '../../model/context';
import {IamContextGroup} from '../../model/Iam/iamContextGroup';
import {UserRightContextGroupComponent} from "./context-groups/user-right-context-group.component";
import {IamRole} from "../../model/Iam/iamRole";

@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'user-right-home',
    templateUrl: 'user-right-home.component.html',
    styleUrls: [
        'user-right-home.component.css'
    ]
})

export class UserRightHomeComponent implements OnInit {
    contexts: Context[];
    contextGroups: IamContextGroup[];
    roles: IamRole[];
    @ViewChild(UserRightContextGroupComponent) userRightContextGroupComponent: UserRightContextGroupComponent;

    constructor(private currentPageService: CurrentPageService, private userService: UserService, private cdRef: ChangeDetectorRef) {
        currentPageService.changePage('profiles-right');
    }

    ngOnInit() {
        this.userService.getContexts().subscribe(res => {
            this.contexts = res;
            this.cdRef.detectChanges();
        });

        this.userService.getContextGroups().subscribe(res => {
            this.contextGroups = res;
            this.cdRef.detectChanges();
        });

        this.userService.getRoles().subscribe(res => {
            this.roles = res;
        });
    }

    updateContextList(contexts): void {
        this.contexts = contexts;

        this.userService.getContextGroups().subscribe(res => {
            this.contextGroups = res;
            this.cdRef.detectChanges();
        });
        this.userRightContextGroupComponent.ngOnChanges();
    }

    updateContextGroup(contextGroups): void {
        this.contextGroups = contextGroups;
    }
}

