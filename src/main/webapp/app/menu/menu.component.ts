import {Component} from "angular2/core";
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from "angular2/common";
import {Calendar} from "primeng/primeng";

@Component({
    selector: 'my-menu',
    template: `
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-body"
                            aria-expanded="false"></button>
                    <a class="navbar-brand" href="#">{{title}}</a>
                </div>
                <div class="collapse navbar-collapse" id="navbar-body">
                    <form class="navbar-form navbar-left">
                        <button type="submit" class="btn btn-primary">{{buttonAddText}}</button>
                    </form>
                </div>
            </div>
        </nav>
        `,
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES, Calendar]
})
export class MenuComponent {
    title:string = "Wydatki";
    buttonAddText:string = "Dodaj";
}
