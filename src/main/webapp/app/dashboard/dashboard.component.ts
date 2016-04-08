import {Component, OnInit} from "angular2/core";
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from "angular2/common";
import {MonthlyStatisticsService} from "../service/month-statistics.service";
import {DataTable, Column} from "primeng/primeng";
import {MonthlyStatistics} from "../entity/monthly-statistics";
import {CategoryService} from "../service/category.service";

@Component({
    selector: 'my-dashboard',
    templateUrl: 'app/dashboard/dashboard.component.html',
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES, DataTable, Column]
})
export class DashboardComponent implements OnInit {

    statistics:MonthlyStatistics[] = [];
    errorMessage;

    constructor(private _service:MonthlyStatisticsService, private _categoryService:CategoryService) {
    }

    ngOnInit() {
        this.getMonthlyStatistics();
    }

    getMonthlyStatistics() {
        this._service.getMonthlyStatistics().subscribe(
            statistics => this.fromMapToStatistics(statistics),
            error => this.errorMessage = <any>error);
    }

    private fromMapToStatistics(map) {
        this.statistics = [];
        for (var millisecond in map) {
            let stat = new MonthlyStatistics();
            stat.date = DashboardComponent.formatDate(new Date(parseInt(millisecond)));
            for (var categoryIndex in this._categoryService.categories) {
                let categoryName = this._categoryService.categories[categoryIndex];
                let sum = map[millisecond][categoryName];
                stat[categoryName] = sum ? sum : 0;
            }
            this.statistics.push(stat);
        }
    }

    private static formatDate(date:Date):string {
        let month = date.getMonth() + 1;
        return date.getFullYear() + '-' + (month < 10 ? '0' + month : month);
    }

}