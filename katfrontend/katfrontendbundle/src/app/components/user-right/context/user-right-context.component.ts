import {
    ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, TemplateRef,
    ViewChild
} from '@angular/core';
import { CurrentPageService } from '../../../services/communication/current-page.service';
import { UserService } from '../../../services/auth/user.service';
import { DataTable } from 'angular2-datatable';
import { Context } from '../../../model/context';
import { IamContext } from '../../../model/Iam/iamContext';
import { BsModalRef, BsModalService} from 'ngx-bootstrap';
import {IamRight} from "../../../model/Iam/iamRight";
import {AuthService} from "../../../services/auth/auth.service";

@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'user-right-context',
    templateUrl: 'user-right-context.component.html',
    styleUrls: [
        'user-right-context.component.css'
    ]
})

export class UserRightContextComponent implements OnInit {
    @Input()
    contexts: Context[];
    rowsOnPage: number = 10;
    filterQuery: string;
    modalRef: BsModalRef;
    context: Context;
    @ViewChild(DataTable) dataTableComponent: DataTable;
    @Output() contextUpdater = new EventEmitter();
    right: IamRight;
    constructor(private currentPageService: CurrentPageService, private userService: UserService, private cdRef: ChangeDetectorRef,
                private modalService: BsModalService, private authService: AuthService) {
        this.right = this.authService.getRights();
    }

    ngOnInit() {
        this.contexts = [];
        this.context = new Context;
    }

    deleteContext(context: Context) {
        this.userService.removeContext(context.id).subscribe(then => {
                let index = this.contexts.indexOf(context);
                this.contexts.splice(index, 1);
                this.contextUpdater.emit(this.contexts);
                this.cdRef.detectChanges();
            }
        );
    }

    openModal(template: TemplateRef<any>) {
            this.modalRef = this.modalService.show(template);
    }

    closeModal() {
        this.modalRef.hide();
    }

    submitContext() {
        this.userService.postContext(this.context).subscribe(res => {
            this.context = new Context();
            this.contexts.push(res);
            this.modalRef.hide();
            this.contextUpdater.emit(this.contexts);
            this.cdRef.detectChanges();
        });
    }
}

