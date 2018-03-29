import { Component, Input, OnInit } from '@angular/core';
import { KatServer } from '../../../model/KatServer';
import {Workflow} from "../../../model/Workflow";
import {WorkflowService} from "../../../services/manager/workflow.service";
import {AuthService} from "../../../services/auth/auth.service";
import {IamRight} from "../../../model/Iam/iamRight";

@Component({
    selector: 'workflow-manager',
    templateUrl: 'workflow-manager.component.html',
    styleUrls: [
        'workflow-manager.component.css'
    ]
})

export class WorkflowManagerComponent implements OnInit{
    server: KatServer;
    rowsOnPage = 10;
    filterQuery: string;
    workflows: Workflow[];
    right: IamRight;
    constructor( private workflowService: WorkflowService, private authService: AuthService) {
        this.right = this.authService.getRights();
    }
    @Input()
    set setServer(server: KatServer) {
        this.server = server;
    }

    ngOnInit(){
      this.workflowService.getWorkflows().subscribe(then => {
          this.workflows = then;
          this.workflowService.getDeployedWorkflow(this.server.id).subscribe(deployed => {
              deployed.forEach(dp => {
                  this.workflows.forEach(wk => {
                        if ( dp.name === wk.name) {
                            wk.isDeployed = true;
                            wk.workflowFile = dp.workflowFile;
                        }
                  });
              });
              // this.workflows = then;
          });
      });
    }

    deployWorkflow(workflow) {
        this.workflowService.deployWorkflow(this.server.id, workflow).subscribe(then => {
          this.ngOnInit();
        });
    }
    undeployWorkflow(workflow) {
        this.workflowService.undeployWorkflow(this.server.id, workflow).subscribe(then => {
            this.ngOnInit();
        });
    }
}
