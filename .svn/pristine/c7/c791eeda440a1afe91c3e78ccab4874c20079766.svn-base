/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Created by bhumbert on 16/06/2017.
 */


import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'dataFilter'
})
export class DataFilterPipe implements PipeTransform {

    transform(array: any[], query: string): any {
        if (query) {

            return array.filter(
            row => {
                for (let field of Object.keys(row)){
                    if (field !== 'id' && row[field] != null && String(row[field]).toLowerCase().indexOf(query.toLowerCase()) !== -1){
                        return row;
                    }
                }
                return null;
            });
        }
        return array;
    }
}
