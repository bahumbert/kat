import { Component, Input, Output, EventEmitter, OnChanges, OnInit } from '@angular/core';
import { NotificationsService } from 'angular2-notifications';
import { KatServer } from '../../model/KatServer';
import { Artifact } from '../../model/artifact';
import { ArtifactService } from '../../services/manager/artifact.service';
import { UrlService } from '../../services/url.service';
import { ServerService } from '../../services/manager/server.service';
import {CurrentPageService} from "../../services/communication/current-page.service";
import {AuthService} from "../../services/auth/auth.service";
import {IamRole} from "../../model/Iam/iamRole";
import {IamRight} from "../../model/Iam/iamRight";



@Component({
    selector: 'upload',
    templateUrl: 'upload.component.html',
    styleUrls: [
        'upload.component.scss'
    ]
})

export class UploadComponent implements OnInit {
    public options = {
        position: ['bottom', 'left'],
        timeOut: 700,
    };

    rowsOnPage: number = 10;
    server: KatServer;
    artifacts: Artifact[] = [];
    filterQuery: string;
    uploadFile: any;
    hasBaseDropZoneOver: boolean = false;
    sizeLimit = 10000 * 1000 * 2; // Mo

    uploaderOver = false;
    uploaderError = false;
    uploaderLoad = false;
    uploaderLabel = 'Drop your file here';
    right: IamRight;

    constructor(
        private artifactService: ArtifactService,
        private urlService: UrlService,
        private serverService: ServerService,
        private notifService: NotificationsService,
        private currentPageService: CurrentPageService,
        private authService: AuthService
    ) {
        this.right = this.authService.getRights();
        currentPageService.changePage('repository');
        this.uploaderLabel = '<p>';
        this.uploaderLabel += '<i class="fa fa-hand-rock-o"></i>';
        this.uploaderLabel += 'Drop your file here';
        this.uploaderLabel += '</p>';
    }

    ngOnInit() {
        this.artifacts = [];
        this.artifactService.resetArtifacts();
        this.artifactService
            .getArtifactsAndDeploymentInfos(null, null, null, false)
            .then(artifacts => {
                this.artifacts = artifacts;
            })
        ;
    }

    onDropFile(event): void {
        event.preventDefault();

        this.uploaderLoad = true;
        this.uploaderOver = false;
        this.uploaderError = false;

        const fileList = <File[]> Array.from(event.target.files || event.dataTransfer.files);

        const uploadNext = () => {
            this.uploaderError = false;
            this.uploaderLoad = false;

            if (fileList.length === 0) return;

            const reader = new FileReader(),
                file: File = fileList.shift();

            if (file.size > this.sizeLimit) {
                this.setFileError(file.name, '<b>' + file.name + '</b> hasn\'t been loaded', 'File is too large')
                    .then(() => {
                        uploadNext();
                    })
                ;

                return;
            }

            reader.onprogress = event => {
                const percent = Math.round(100 * event.loaded / event.total);

                this.uploaderLabel = '<div class="loading-label">';
                this.uploaderLabel += '<i class="fa fa-cog fa-spin"></i>';
                this.uploaderLabel += '<p>Reading <b>' + file.name + '</b>...</p>';
                this.uploaderLabel += '<p>' + percent + '%</p>';
                this.uploaderLabel += '</div>';
            };

            reader.onerror = event => {
                this.uploaderLoad = false;

                this.setFileError(file.name, '<b>' + file.name + '</b> hasn\'t been read', event.message)
                    .then(() => {
                        uploadNext();
                    })
                ;
            };

            reader.onload = event => {
                const b64 = reader.result.split(',')[1];

                this.uploaderLabel = '<div class="loading-label">';
                this.uploaderLabel += '<i class="fa fa-cog fa-spin"></i>';
                this.uploaderLabel += '<p>Uploading <b>' + file.name + '</b>...</p>';
                this.uploaderLabel += '<p>0%</p>';
                this.uploaderLabel += '</div>';

                this.serverService
                    .postFile({'file': b64, 'name': file.name}, (event, percent) => {
                        this.uploaderLabel = '<div class="loading-label">';
                        this.uploaderLabel += '<i class="fa fa-cog fa-spin"></i>';
                        this.uploaderLabel += '<p>Uploading <b>' + file.name + '</b>...</p>';
                        this.uploaderLabel += '<p>' + percent + '%</p>';
                        this.uploaderLabel += '</div>';
                    })
                    .then(result => {
                        this.ngOnInit();

                        if ( result.success) {
                            this.uploaderLabel = '<div class="loading-label">';
                            this.uploaderLabel += '<i class="fa fa-check"></i>';
                            this.uploaderLabel += '<p><b>' + file.name + '</b> has been loaded with success</p>';
                            this.uploaderLabel += '<p>' + result.message + '</p>';
                            this.uploaderLabel += '</div>';

                            this.notifService.success('Success', result.message, this.options);

                            setTimeout(() => {
                                this.uploaderLabel = '<p>';
                                this.uploaderLabel += '<i class="fa fa-hand-rock-o"></i>';
                                this.uploaderLabel += 'Drop your file here';
                                this.uploaderLabel += '</p>';

                                this.uploaderError = false;

                                uploadNext();
                            }, 5000);
                        } else {
                            this.setFileError(file.name, '<b>' + file.name + '</b> hasn\'t been loaded', result.message)
                                .then(() => {
                                    uploadNext();
                                })
                            ;
                        }

                        this.uploaderLoad = false;
                    })
                ;
            };

            reader.readAsDataURL(file);
        };

        uploadNext();
    }

    onDragOverFile(event): void {
        event.stopPropagation();
        event.preventDefault();
    }

    onDragEnter(event): void {
        this.uploaderOver = true;

        this.uploaderLabel = '<p>';
        this.uploaderLabel += '<i class="fa fa-hand-paper-o"></i>';
        this.uploaderLabel += 'Drop your file here';
        this.uploaderLabel += '</p>';
    }

    onDragLeave(event: DragEvent): void {
        this.uploaderOver = false;

        this.uploaderLabel = '<p>';
        this.uploaderLabel += '<i class="fa fa-hand-rock-o"></i>';
        this.uploaderLabel += 'Drop your file here';
        this.uploaderLabel += '</p>';
    }

    notAllowed() {
        this.notifService.error('Success', 'Action impossible (job scheduled)', this.options);
    }

    setFileError(filename, errorMsgA, errorMsgB): Promise<any> {
        this.uploaderLabel = '<div class="loading-label">';
        this.uploaderLabel += '<i class="fa fa-times"></i>';
        this.uploaderLabel += '<p>' + errorMsgA + '</p>';
        this.uploaderLabel += '<p>' + errorMsgB + '</p>';
        this.uploaderLabel += '</div>';

        this.uploaderError = true;

        this.notifService.error('Error', errorMsgA + ' - ' + errorMsgB, this.options);

        return new Promise<any>((resolve, reject) => {
            setTimeout(() => {
                this.uploaderLabel = '<p>';
                this.uploaderLabel += '<i class="fa fa-hand-rock-o"></i>';
                this.uploaderLabel += 'Drop your file here';
                this.uploaderLabel += '</p>';

                this.uploaderError = false;

                resolve(true);
            }, 5000);
        });
    }

}

