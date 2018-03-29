import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor, HttpResponse, HttpErrorResponse
} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { AuthService } from './auth.service';
import { Component, Input, OnInit, EventEmitter, Output } from '@angular/core';
import { NotificationsService } from 'angular2-notifications';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(
        public auth: AuthService,
        protected notificationService: NotificationsService
    ) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        // Si on est sur la requête d'authentification, on passe
        if (/\/auth$/.test( request.url )) {
            return next.handle(request).do((event: HttpEvent<any>) => {
                if (event instanceof HttpResponse) {
                    // do stuff with response if you want
                }
            }, (err: any) => {
                if (err instanceof HttpErrorResponse) {
                    if (err.status === 401) {
                        this.notificationService.error('Forbidden', 'Access Restricted');
                    }
                }
            });
        }

        request = request.clone({
            withCredentials: true,
            setHeaders: {
                Authorization: 'Basic ' + this.auth.getUser().token
            }
        });

        return next.handle(request);
    }
}