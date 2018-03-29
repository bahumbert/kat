export class Task{
    id: string;
    name: string;
    type: string;
    artifactUrl: string;
    createDate: number;
    creator: string;
    success: Task[] = [];
    error: Task[] = [];
    then: Task[] = [];
}
