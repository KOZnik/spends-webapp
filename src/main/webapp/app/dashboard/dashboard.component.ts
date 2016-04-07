import {Component} from "angular2/core";
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from "angular2/common";

@Component({
    selector: 'my-dashboard',
    template: `
        dashboard
    `,
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES]
})
export class DashboardComponent {
    
}