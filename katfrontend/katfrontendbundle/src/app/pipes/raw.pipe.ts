import {Pipe, PipeTransform} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';

@Pipe({ name: 'raw', pure: false })
export class RawPipe implements PipeTransform {

    constructor(
        private sanitizer: DomSanitizer
    ) {}

    transform(content) {
        return this.sanitizer.bypassSecurityTrustHtml(content);
    }
}
