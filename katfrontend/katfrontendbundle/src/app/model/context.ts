import { KatCluster } from './KatCluster';
import { KatServer } from './KatServer';
import { KatBroker } from './KatBroker';

/**
 * Created by bhumbert on 07/07/2017.
 */


export class Context{
    id: string;
    label: string;
    propertyName: string;
    originalValue: string;
    overloadedValue: string;
    active: boolean;
}
