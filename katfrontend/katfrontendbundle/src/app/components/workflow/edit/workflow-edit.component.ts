import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit, ViewChild } from '@angular/core';
import { KatEnvironment } from '../../model/KatEnvironment';
import { EnvironmentService } from '../../services/environment.service';
import {
    MenuItemSelectedEvent,
    Ng2TreeSettings,
    NodeEvent,
    NodeMenuItem,
    NodeMenuItemAction,
    TreeComponent,
    TreeModel
} from 'ng2-tree';
import { Task } from '../../../model/Task';
import { Artifact } from '../../../model/artifact';
import { ArtifactService } from '../../../services/manager/artifact.service';
import { SELECT_DIRECTIVES } from '../../../ng2-select';
import { SelectComponent } from 'ng2-select';
import { WorkflowService } from '../../../services/manager/workflow.service';
import { Workflow } from '../../../model/Workflow';
import { ActivatedRoute, Router } from '@angular/router';
import {AuthService} from "../../../services/auth/auth.service";
import {IamUser} from "../../../model/Iam/iamUser";

@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'workflow-edit',
    templateUrl: 'workflow-edit.component.html',
    styleUrls: [
        'workflow-edit.component.css'
    ]
})

export class WorkflowEditComponent implements OnInit {
    public user: IamUser;
    public completeTasks: Task[] = [];
    public currentTask: Task;
    public currentId: number;
    public nextId: number;
    public nexusArtifacts: Artifact[] = [];
    public idTree: string;
    selectValues: { text: string, id: string }[] = [];
    value: any = null;
    wkSend: Workflow;
    public edit: boolean = false;
    public icons = {
        value: 'Start',
        id: 1,
        settings: {
            menuItems: [],
            templates: {
                node: '<i class="fa fa-play workflow-play"></i>',
                leaf: '<i class="fa fa-play fa-lg workflow-play"></i>',
                leftMenu: '<i class="fa fa-play fa-lg workflow-play"></i>',
            }
        },
        children: []
    };
    public templateSuccess = {
        node: '<i class="fa fa-check workflow-play"></i>',
        leaf: '<i class="fa fa-check fa-lg workflow-play"></i>',
        leftMenu: '<i class="fa fa-check fa-lg workflow-play"></i>',
    };
    public templateThen = {
        node: '<i class="fa fa-exclamation-triangle workflow-play"></i>',
        leaf: '<i class="fa-exclamation-triangle fa-lg workflow-play"></i>',
        leftMenu: '<i class="fa fa-exclamation-triangle fa-lg workflow-play"></i>',
    };
    public templateError = {
        node: '<i class="fa fa-ban workflow-play"></i>',
        leaf: '<i class="fa fa-check fa-ban workflow-play"></i>',
        leftMenu: '<i class="fa fa-check fa-ban workflow-play"></i>',
    };
    public settings: Ng2TreeSettings = {
        rootIsVisible: false
    };
    menuItemsConfigured = [
        {action: NodeMenuItemAction.Custom, name: 'On Success', cssClass: 'fa fa-check-circle'},
        {action: NodeMenuItemAction.Custom, name: 'On Failure', cssClass: 'fa fa-ban'},
        {action: NodeMenuItemAction.Custom, name: 'Then', cssClass: 'fa fa-exclamation'}
    ];

    @ViewChild('tree') private tree: TreeComponent;
    @ViewChild('selectArtifact') private ngSelect: SelectComponent;


    constructor(private artifactService: ArtifactService,
                private workflowService: WorkflowService, private router: Router, private route: ActivatedRoute,private authService: AuthService) {

    }
    ngOnInit() {
        this.user = this.authService.getUser();

        this.wkSend = new Workflow;
        this.idTree = this.route.snapshot.params['id'];
        this.artifactService.resetArtifacts();
        this.nexusArtifacts = [];
        this.selectValues = [];
        this.artifactService.getArtifactsAndDeploymentInfos(null, null, null)
            .then(artifacts => {
                this.nexusArtifacts = artifacts;
                this.nexusArtifacts.forEach(art => {
                    this.selectValues.push({text: art.name + ' - ' + art.version, id: art.url});
                });
            });

        this.currentId = 1;
        this.nextId = 1;


        if (this.idTree !== 'new') {
            this.edit = true;
            this.workflowService.getWorkflowById(this.idTree).subscribe(workflow => {
                this.wkSend = workflow;

                let treeController = this.tree.getControllerByNodeId(1);
                treeController.rename(this.wkSend.task.name);
                treeController.changeNodeId(this.wkSend.task.id);
                treeController.toTreeModel().settings.menuItems = this.menuItemsConfigured;

                let task = new Task();
                task.id = this.wkSend.task.id;
                task.name = this.wkSend.task.name;
                task.artifactUrl = this.wkSend.task.artifactUrl;
                task.type = this.wkSend.task.type;
                this.completeTasks.push(task);


                const buildTreeModel = (taskModel: Task, callback: (type: string, child: Task, parent: Task) => void) => {
                    const templates = {
                        'success' : this.templateSuccess,
                        'error': this.templateError,
                        'then': this.templateThen
                    };

                    const node = {
                        value: taskModel.name,
                        id: taskModel.id,
                        settings: {
                            menuItems: this.menuItemsConfigured,
                            templates: this.templateSuccess
                        },
                        children: []
                    };

                    Object.keys(templates).forEach(type => {
                        taskModel[type].forEach(t => {
                            callback(type, t, taskModel);

                            const subNode = buildTreeModel(t, callback);
                            subNode.settings.templates = templates[type];

                            node.children.push( subNode );
                        });
                    });

                    return node;
                };

                const node = buildTreeModel(this.wkSend.task, (type, child, parent) => {
                    let task = new Task();
                    task.id = child.id.toString();
                    task.name = child.name;
                    task.artifactUrl = child.artifactUrl;
                    task.type = type;

                    this.completeTasks.push(task);
                });

                node.children.forEach((node: TreeModel) => {
                    treeController.addChild(node);
                });
            });
        } else {
            let task = new Task();
            task.id = this.nextId.toString();
            task.name = 'Start';
            this.completeTasks.push(task);
        }
    }

    onNodeSelected($event) {
        if (this.ngSelect) {
            this.ngSelect.active = [];
        }
        this.selectValues = [];
        this.nexusArtifacts.forEach(art => {
            this.selectValues.push({text: art.name + ' - ' + art.version, id: art.url});
        });

        this.currentId = $event.node.id;
        this.currentTask = this.completeTasks.filter(task => task.id == $event.node.node.id)[0];
        console.log(this.currentTask);
    }


    // OK  !!
    submitWorkflow() {
        // remake here
        let treeController;

             treeController = this.tree.getControllerByNodeId(this.currentId);

        if (this.value) {
            let taskName = this.currentTask.name;
            if (this.edit) {
                taskName = taskName.split(' (')[0];
            }

            treeController.rename(taskName + ' (' + this.value.text + ')');
            treeController.toTreeModel().settings.menuItems = this.menuItemsConfigured;
            let artiFind = this.nexusArtifacts.filter(art => art.name === this.value.text.split(' - ')[0]
                && art.version === this.value.text.split(' - ')[1])[0];
            this.completeTasks.filter(workflow => workflow.id === this.currentId.toString())[0].artifactUrl = artiFind.url;

        } else {
            alert('Veuillez choisir un job');
        }
    }

    public onMenuItemSelected(e: MenuItemSelectedEvent) {
        this.nextId += 1;

        let treeController = this.tree.getControllerByNodeId(this.currentId);
        let name: string = '';
        let type = '';
        if (e.selectedItem === 'On Success') {
            name = 'On Success';
            treeController.addChild({
                value: name,
                id: this.nextId,
                settings: {
                    menuItems: [],
                    templates: {
                        node: '<i class="fa fa-check workflow-play"></i>',
                        leaf: '<i class="fa fa-check fa-lg workflow-play"></i>',
                        leftMenu: '<i class="fa fa-check fa-lg workflow-play"></i>',
                    }
                },
                children: []
            });
            type = 'success';
        }

        if (e.selectedItem === 'On Failure') {
            name = 'On Failure';
            treeController.addChild({
                value: name,
                id: this.nextId,
                settings: {
                    menuItems: [],
                    templates: {
                        node: '<i class="fa fa-ban workflow-play"></i>',
                        leaf: '<i class="fa fa-ban fa-lg workflow-play"></i>',
                        leftMenu: '<i class="fa fa-ban fa-lg workflow-play"></i>',
                    }
                },
                children: []
            });
            type = 'error';
        }

        if (e.selectedItem === 'Then') {
            name = 'Then';

            treeController.addChild({
                value: name,
                id: this.nextId,
                settings: {
                    menuItems: [],
                    templates: {
                        node: '<i class="fa fa-exclamation-triangle workflow-play"></i>',
                        leaf: '<i class="fa fa-exclamation-triangle fa-lg workflow-play"></i>',
                        leftMenu: '<i class="fa fa-exclamation-triangle fa-lg workflow-play"></i>',
                    }
                },
                children: []
            });
            type = 'then';
        }
        let workflow = new Task();
        workflow.name = name;
        workflow.id = this.nextId.toString();
        workflow.type = type;

        this.ngSelect.active = [];
        this.completeTasks.push(workflow);
    }

    updateWkName(name) {
        this.wkSend.name = name;
    }

    private selected(value) {

        this.value = value;
    }

    private removed(value: any) {
        // console.log('Removed value is: ', value);
    }

    private typed(value: any) {
        // console.log('New search input: ', value);
    }

    private onNodeCreated(value: any) {
        // console.log('New search input: ', value);
    }

    private refreshValue(value: Artifact) {
        this.value = value.name;
    }

    private validateWorkflow() {

        let tree = this.tree.getController().toTreeModel();
        let completeTaskBack = this.completeTasks;

        const buildWorkFlow = (treeObj: TreeModel, callback: Function, parentTreeModel: TreeModel = null, deep: number = 1) => {

            let task: Task = new Task();
            task.name = treeObj.value.toString();
            task.id = treeObj.id.toString();

            let workflowFind = completeTaskBack.filter(workflow => workflow.id == treeObj.id)[0];

            task.artifactUrl = workflowFind.artifactUrl;

            treeObj.children.forEach(elem => {

                 workflowFind = completeTaskBack.filter(workflow => workflow.id == elem.id)[0];

                 console.log(workflowFind);

                // task.artifactUrl = workflowFind.artifactUrl;

                if (callback(elem, task, workflowFind.type, deep, workflowFind.artifactUrl)) {
                    const subWF = buildWorkFlow(elem, callback, elem, deep + 1);
                    console.log("subWf");
                    console.log(subWF);
                    switch (workflowFind.type) {
                        case 'success':
                            task.success.push(subWF);
                        break;
                        case 'then':
                            task.then.push(subWF);
                        break;
                        case 'error':
                            task.error.push(subWF);
                        break;
                        default:
                        break;
                    }
                }
            });

            return task;
        };

        let tasks = buildWorkFlow(tree, (model: TreeModel, workFlow: Task, type: string, deep: number, urlArtifact) => {
            return true;
        });



        this.wkSend.task = tasks;

        if (this.edit === false) {
            this.workflowService.postWorkflow(this.wkSend).subscribe(then => {
                this.router.navigate(['/workflows']);
            });
        } else {
            this.workflowService.putWorkflow(this.idTree, this.wkSend).subscribe(then => {
                this.router.navigate(['/workflows']);
            });
        }
    }
}

