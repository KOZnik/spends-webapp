import {Component} from "angular2/core";
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from "angular2/common";
import {Calendar} from "primeng/primeng";

@Component({
    selector: 'my-menu',
    templateUrl: 'app/menu/menu.component.html',
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES, Calendar]
})
export class MenuComponent {
    title:string = "Wydatki";
}
