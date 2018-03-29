import { Injectable } from '@angular/core';
import {tokenNotExpired} from 'angular2-jwt';
import {HttpClient, HttpRequest} from "@angular/common/http";
import {UrlService} from "../url.service";
import {HttpCustomService} from "../http-custom.service";
import {Router} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {IamRole} from "../../model/Iam/iamRole";
import {IamUser} from "../../model/Iam/iamUser";
import {IamGroup} from "../../model/Iam/iamGroup";
import {IamContext} from "../../model/Iam/iamContext";
import {Context} from "../../model/context";
import {IamContextGroup} from "../../model/Iam/iamContextGroup";

@Injectable()
export class UserService extends HttpCustomService {

    constructor(
       private urlService: UrlService,
       private httpClient: HttpClient,
       router: Router
    ) {
        super(router);
    }

    getAuth(username: string, password: string): Observable<Object> {
        let options = this.getOptions();

        options.withCredentials = true;
        options.headers = options.headers.append('Cache-Control', 'no-cache, must-revalidate');
        options.headers = options.headers.append('Authorization', 'Basic ' + btoa(username + ':' + password));

        return this.httpClient
            .post(this.urlService.getAuthUrl(), {username: username, password: password}, options)
            .catch(this.handleError)
            .map(this.extractData)
        ;
    }
    removeContext(id: string) {
        return this.httpClient
            .delete(this.urlService.getContextUrl(id) , this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    putContextGroup(data: IamContextGroup): Observable<IamContextGroup> {
        return this.httpClient
            .put(this.urlService.getContextGroupUrl(), data , this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }

    postContextGroup(data: IamContextGroup): Observable<IamContextGroup> {
        return this.httpClient
            .post(this.urlService.getContextGroupUrl(), data , this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }

    postGroup(data: IamGroup): Observable<IamGroup> {
        return this.httpClient
            .post(this.urlService.getGroupUrl(), data , this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }

    putGroup(data: IamGroup): Observable<IamGroup> {
        return this.httpClient
            .put(this.urlService.getGroupUrl(), data , this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }

    postContext(data: Context): Observable<Context> {
        return this.httpClient
            .post(this.urlService.getContextUrl(), data , this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    deleteContextGroup(idContextGroup): Observable<any> {
        return this.httpClient
            .delete(this.urlService.getContextGroupUrl(idContextGroup), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    getContextGroups(): Observable<IamContextGroup[]> {
        return this.httpClient
            .get(this.urlService.getContextGroupUrl(), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    getContexts(): Observable<Context[]> {
        return this.httpClient
            .get(this.urlService.getContextUrl(), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
    getGroups(): Observable<IamGroup[]> {
        return this.httpClient
            .get(this.urlService.getGroupUrl(), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
     getRoles(): Observable<IamRole[]> {
        return this.httpClient
            .get(this.urlService.getRolesUrl(), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }

    getUsers(): Observable<IamUser[]> {
        return this.httpClient
            .get(this.urlService.getUserUrl(), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }

    deleteGroup(grpId) {
        return this.httpClient
            .delete(this.urlService.getGroupUrl(grpId), this.getOptions())
            .catch(this.handleError)
            .map(this.extractData);
    }
}
