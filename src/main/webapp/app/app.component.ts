import {Component} from "angular2/core";
import {MonthComponent} from "./month/month.component";
import {CategoryService} from "./service/category.service";
import {HTTP_PROVIDERS} from "angular2/http";
import {SpendService} from "./service/spend.service";
import {RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS} from "angular2/router";
import {DashboardComponent} from "./dashboard/dashboard.component";

@Component({
    selector: 'my-app',
    templateUrl: 'app/app.component.html',
    directives: [MonthComponent, DashboardComponent, ROUTER_DIRECTIVES],
    providers: [HTTP_PROVIDERS, ROUTER_PROVIDERS, CategoryService, SpendService]
})
@RouteConfig([
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: DashboardComponent
    },
    {
        path: '/month',
        name: 'Month',
        component: MonthComponent,
        useAsDefault: true
    }
])
export class AppComponent {
    title:string = "Wydatki";
}
