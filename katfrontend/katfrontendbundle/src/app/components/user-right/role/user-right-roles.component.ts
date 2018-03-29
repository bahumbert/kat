import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit, ViewChild } from '@angular/core';
import { CurrentPageService } from '../../../services/communication/current-page.service';
import { UserService } from '../../../services/auth/user.service';
import { IamRole } from '../../../model/Iam/iamRole';
import { DataTable } from 'angular2-datatable';

@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'user-right-role',
    templateUrl: 'user-right-role.component.html',
    styleUrls: [
        'user-right-role.component.css'
    ]
})

export class UserRightRoleComponent implements OnInit {
    roles: IamRole[];
    rowsOnPage: number = 10;
    filterQuery: string;
    @ViewChild(DataTable) dataTableComponent: DataTable;

    constructor(private currentPageService: CurrentPageService, private userService: UserService, private cdRef: ChangeDetectorRef) {
    }

    ngOnInit() {
        this.userService.getRoles().subscribe(res => {
            this.roles = res;
            this.dataTableComponent.setPage(1, 10);
            this.cdRef.detectChanges();
        });
    }



}

