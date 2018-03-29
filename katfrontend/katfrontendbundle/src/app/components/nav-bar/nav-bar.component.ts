import { Component, OnInit } from '@angular/core';
import { CurrentPageService } from '../../services/communication/current-page.service';
import { KatEnvironment } from '../../model/KatEnvironment';
import { EnvironmentService } from '../../services/environment.service';
import { NotificationsService } from 'angular2-notifications';
import { Router } from '@angular/router';
import {UserService} from "../../services/auth/user.service";
import {AuthService} from "../../services/auth/auth.service";
import {IamRight} from "../../model/Iam/iamRight";

@Component({
  selector: 'nav-bar',
  templateUrl: 'nav-bar.component.html',
  styleUrls: [
    'nav-bar.component.scss'
  ]
})

export class NavBarComponent implements OnInit{

  activePage: string;
  katEnvironments: Array<KatEnvironment>;
  isCollapsedConfig: boolean = true;
  isCollapsedEnv: boolean = true;
  right: IamRight;
  constructor(
      private currentPageService: CurrentPageService,
      private environmentService: EnvironmentService,
      private notificationsService: NotificationsService,
      private authService: AuthService,
      private router: Router
  ) {
    this.right = authService.getRights();
    currentPageService.pageChange$.subscribe(page => this.activePage = page);
  }

  ngOnInit(){
    this.environmentService.getEnvironments().subscribe(
    environments => {
      if ( !(this.right.isSuperAdmin || this.right.isAdmin) ) {
        this.katEnvironments = [];
        this.right.environmentList.forEach(env => {
          environments.forEach(en => {
            if (en.id === env.id) {
              this.katEnvironments.push(en);
            }
          });
        });
      } else {
        this.katEnvironments = environments;
      }
    });
  }

  changeRoute(route){
    this.router.navigate([route]);
  }

  addEnvironment(){

    let newEnvironment = new KatEnvironment();
    newEnvironment.name = 'New environment';

    this.environmentService.addEnvironment(newEnvironment).subscribe(
    environment => {
      this.katEnvironments.push(environment);
    });
  }

  removeEnvironment(environment: KatEnvironment){
    this.environmentService.removeEnvironment(environment.id).subscribe(
    res => {
      this.katEnvironments.splice(this.katEnvironments.findIndex(env => {
        return env.id === environment.id;
      }), 1);

      if (this.katEnvironments.length === 0){
        this.router.navigate(['/manager']);
      }

    });
  }

  editEnvironment(environment: KatEnvironment){
    environment.editing = !environment.editing;
  }

/*
Update environment with
*/
  updateEnvironment(environment: KatEnvironment){
    this.environmentService.updateEnvironment(environment).subscribe(
    env => {
      let index = this.katEnvironments.findIndex(e => {
        return e.id === environment.id;
      });

      environment.editing = false;

      if (index !== -1){
        this.katEnvironments[index] = env;
      }
      else {
        this.notificationsService.error('An error occured', 'Please reload the page');
      }
    });
  }

  logout(event: Event) {
    event.preventDefault();
    event.stopPropagation();
    event.stopImmediatePropagation();

    this.authService.logout();

      this.router.navigate(['/login']);

    return false;
  }
}

