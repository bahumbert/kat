import { Component, OnInit } from '@angular/core';
import { KatEnvironment } from '../../model/KatEnvironment';
import { EnvironmentService } from '../../services/environment.service';
import { Router } from '@angular/router';
import { WorkflowService } from '../../../services/manager/workflow.service';
import { Workflow } from '../../../model/Workflow';
import { CurrentPageService } from '../../../services/communication/current-page.service';

@Component({
    selector: 'workflow-list',
    templateUrl: 'workflow-list.component.html',
    styleUrls: [
        'workflow-list.component.css'
    ]
})

export class WorkflowListComponent implements OnInit{
    workflows: Workflow[];
    rowsOnPage: number = 10;
    filterQuery: string;
    public idWorkflowEdit: string = null;
    constructor(private workflowService: WorkflowService, private router: Router, private currentPageService: CurrentPageService) {
        currentPageService.changePage('workflow');
    }

    ngOnInit(){
        this.workflowService.getWorkflows().subscribe(res => {
            this.workflows = res;
        });
    }

    deleteWorkflow(idWk) {
        this.workflowService.deleteWorkflow(idWk).subscribe(res => {
            this.workflows = res;
        });
    }
}
