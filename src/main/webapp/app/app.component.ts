import {Component} from "angular2/core";
import {MenuComponent} from "./menu/menu.component";
import {MonthComponent} from "./month/month.component";
import {CategoryService} from "./service/category.service";
import {HTTP_PROVIDERS} from "angular2/http";
import {SpendService} from "./service/spend.service";
import {StoreSpendComponent} from "./store-spend/store-spend.component";

@Component({
    selector: 'my-app',
    template: `
            <div class="container-fluid">
                <my-menu></my-menu>
                <my-store-spend></my-store-spend>
                <my-month></my-month>
            </div>
        `,
    directives: [MenuComponent, MonthComponent, StoreSpendComponent],
    providers: [HTTP_PROVIDERS, CategoryService, SpendService]
})
export class AppComponent {
}
