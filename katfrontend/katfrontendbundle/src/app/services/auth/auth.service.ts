import { Injectable } from '@angular/core';
import { tokenNotExpired } from 'angular2-jwt';
import { UrlService } from '../url.service';
import { IamUser } from '../../model/Iam/iamUser';
import {IamRight} from "../../model/Iam/iamRight";

@Injectable()
export class AuthService {
    rights: IamRight;
    user: IamUser;
    constructor(
       private urlService: UrlService,
    ) {}

    public getUser(): IamUser {
        let user = localStorage.getItem('user');
        if (!this.user || this.user.token !== JSON.parse(user).token) {
                this.user = JSON.parse(user);

        }
        return user ? JSON.parse(user) as IamUser : null;
    }

    public getRights() {
        if (this.rights && this.user.id === this.getUser().id) {
            return this.rights;
        } else {
            let user = this.getUser();
            if ( user ) {
                let iamRoles = new IamRight();
                iamRoles.canRead = true;
                if (this.user) {
                    let labelRole = this.user.group.role.label;
                    iamRoles.name = labelRole;
                    if (labelRole === 'ADMIN' || labelRole === 'SUPER-ADMIN') {
                        iamRoles.isAdmin = true;
                        if (labelRole === 'SUPER-ADMIN') {
                            iamRoles.isSuperAdmin = true;
                        }
                    }
                    if (labelRole === 'ADMIN' || labelRole === 'SUPER-ADMIN' || labelRole === 'OPERATOR') {
                        iamRoles.canEdit = true;
                    }
                    if (labelRole === 'DEVELOPER') {
                        iamRoles.canUpload = true;
                    }
                    if (this.user.group.contextGroup) {
                        this.user.group.contextGroup.contexts.forEach(ct => {
                            iamRoles.contextList.push(ct);
                        });
                    }

                    this.user.group.environments.forEach(env => {
                        iamRoles.environmentList.push(env);
                    });
                    this.rights = iamRoles;
                }
                }


            if (this.rights) {
                this.rights.username = this.user.id;
                return this.rights;
            } else {
                return null;
            }
        }
    }

    public isAuthenticated(): boolean {
        const user = this.getUser();

        // Avec JWT : tokenNotExpired(user.token)

        return user !== null;
    }

    logout(): void {
        if (localStorage.getItem('user')) {
            localStorage.removeItem('user');
            this.rights = null;
            this.user = null;
        }
    }
}
