import { Context } from '../context';
import { KatEnvironment } from '../KatEnvironment';

export class IamRight {
    username: string;
    name: string;
    canEdit: boolean = false;
    canUpload: boolean = false;
    canRead: boolean = false;
    isAdmin: boolean = false;
    isSuperAdmin: boolean = false;
    contextList: Context[] = [];
    environmentList: KatEnvironment[] = [];

}
