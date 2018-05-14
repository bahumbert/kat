import { KatCluster } from './KatCluster';
import { KatServer } from './KatServer';
import { KatBroker } from './KatBroker';

/**
 * Created by bhumbert on 07/07/2017.
 */


export class KatJob{
    pid: string;
    nextExecution: string;
    project: string;
    name: string;
    description: string;
    version: string;
    overloadedContext: boolean;
    context: string;
    state: string;
    deployDate: string;
    propertyFile: string;
    scheduling: string;
    running: boolean;
    mailActive: boolean;
    jvmActive: boolean;

/*    notifySms: string[] = [];*/

    static fromObject(obj) {
        const ks = new KatJob();

        for (const key in obj) {
            ks[key] = obj[key];
        }

        return ks;
    }
}
