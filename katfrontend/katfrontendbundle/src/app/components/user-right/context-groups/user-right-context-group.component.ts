import {
    ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, TemplateRef,
    ViewChild
} from '@angular/core';
import { IamContextGroup } from '../../../model/Iam/iamContextGroup';
import { Context } from '../../../model/context';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { UserService } from '../../../services/auth/user.service';
import {IamRight} from "../../../model/Iam/iamRight";
import {AuthService} from "../../../services/auth/auth.service";


@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'user-right-context-group',
    templateUrl: 'user-right-context-group.component.html',
    styleUrls: [
        'user-right-context-group.component.css'
    ]
})

export class UserRightContextGroupComponent implements  OnChanges {
    @Input()
    contextGroups: IamContextGroup[];
    @Input()
    contexts: Context[];
    contextGroup: IamContextGroup;
    rowsOnPage: number = 10;
    filterQuery: string;
    modalRef: BsModalRef;
    contextPreview: Context[];
    public items: { text: string, id: string}[];
    public value: { text: string, id: string}[];
    right: IamRight;
    @Output() contextGroupUpdater = new EventEmitter();
    constructor(private modalService: BsModalService, private cdRef: ChangeDetectorRef, private userService: UserService,private authService: AuthService) {
        this.right = this.authService.getRights();
    }


    ngOnChanges() {
        this.contextGroup = new IamContextGroup;
        this.items = [];
        if (this.contexts) {
            this.contexts.forEach(ct => {
                this.items.push( { text: ct.label, id: ct.id});
            });
            this.refreshValue(this.items);
        }
    }

    public selected(value: any): void {
       let contextToAdd = this.contexts.filter(ct => ct.id ===  value.id)[0];
       this.contextGroup.contexts.push(contextToAdd);
        this.cdRef.detectChanges();
    }

    public removed(value: any): void {
        let item = this.contextGroup.contexts.filter(ct => ct.id ===  value.id)[0];
        let index = this.contextGroup.contexts.indexOf(item);
        this.contextGroup.contexts.splice(index, 1);
        this.cdRef.detectChanges();
    }


    openModal(template: TemplateRef<any>) {
        this.modalRef = this.modalService.show(template);
    }

    submitContextGroup() {
        if (!this.contextGroup.id) {
            this.userService.postContextGroup(this.contextGroup).subscribe(then => {
                this.contextGroups.push(then);
                this.contextGroupUpdater.emit(this.contextGroups);
            });
        } else {
            this.userService.putContextGroup(this.contextGroup).subscribe(then => {
                this.contextGroups.filter(ctg => ctg.id === then.id)[0] = then;
                this.contextGroupUpdater.emit(this.contextGroups);
            });
        }

        this.modalRef.hide();
        this.contextGroup = new IamContextGroup;
    }
    public refreshValue(value: any): void {
        this.contextGroup.contexts.forEach(ct => {
            this.value.push({ text: ct.label, id: ct.id});
        });
    }

    closeModal() {
        this.modalRef.hide();
    }

    openModalContexts(template: TemplateRef<any>, contexts: Context[]) {
        this.contextPreview = contexts;
        this.modalRef = this.modalService.show(template);
    }

    editContextGroup(contextGrp: IamContextGroup): void {
        this.contextGroup = contextGrp;
        this.value = [];
        this.contextGroup.contexts.forEach(ct => {
           this.value.push({ text: ct.label, id: ct.id});
        });
    }

    deleteContextGroup(contextGrp: IamContextGroup): void{
        this.userService.deleteContextGroup(contextGrp.id).subscribe(then => {
        });
        let index = this.contextGroups.indexOf(contextGrp);
        this.contextGroups.splice(index, 1);
    }
}

