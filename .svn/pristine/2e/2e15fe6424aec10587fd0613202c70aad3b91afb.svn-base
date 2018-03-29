import { Observable } from 'rxjs/Rx';
import { Router } from '@angular/router';
import { HttpHeaders, HttpParams } from "@angular/common/http";

interface HttpOptions {
    headers?: HttpHeaders;
    observe?: 'body';
    params?: HttpParams;
    reportProgress?: boolean;
    responseType?: 'json';
    withCredentials?: boolean;
}

export class HttpCustomService {

    constructor(
        private router: Router
    ) {}

    protected getOptions(params ?: HttpParams): HttpOptions {
        const headers = new HttpHeaders();


        headers.set('Application-Name', 'KAT');
        headers.set('X-Requested-With', 'XMLHttpRequest');
        headers.set('Charset', 'utf-8');
        // L'authentification se fait via le TokenInterceptor
        // headers.set('Authorization', 'Basic ' + btoa('karaf:karaf'));

        return {
            headers: headers,
            observe: 'body',
            params: params || null,
            reportProgress: true,
            responseType: 'json',
            withCredentials: true
        };
    }

    protected extractData(result: any) {
        return result;
    }

    handleError(error: any) {
        let errMsg = (error.message) ? error.message :
            error.status ? `${error.status} - ${error.statusText}` : 'Server error';

        if (error.status != null && error.status == 401) {

        }


        return Observable.throw(errMsg);
    }
}
