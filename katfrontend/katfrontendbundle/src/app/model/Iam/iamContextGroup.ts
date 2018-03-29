import { Context } from '../context';

export class IamContextGroup {
    id: string;
    label: string;
    contexts: Context[] = [];
}
