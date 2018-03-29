import { Task } from  './Task';

export class Workflow{
    id: string;
    name: string;
    workflowFile: string;
    isDeployed: boolean;
    task: Task;
}
