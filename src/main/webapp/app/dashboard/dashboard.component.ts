import {Component, OnInit} from "angular2/core";
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from "angular2/common";
import {MonthlyStatisticsService} from "../service/month-statistics.service";
import {DataTable} from 'primeng/primeng';
import {Column} from 'primeng/primeng';
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
            stat.date = this.formatDate(new Date(parseInt(millisecond)));
            for (var summStat in map[millisecond]) {
                for (var categoryIndex in this._categoryService.categories) {
                    let categoryName = this._categoryService.categories[categoryIndex];
                    let sum = map[millisecond][categoryName];
                    stat[categoryName] = sum ? sum : 0;
                }
            }
            stat.calculateSummary();
            this.statistics.push(stat);
        }
    }

    private formatDate(date:Date):string {
        let month = date.getMonth() + 1;
        return date.getFullYear() + '-' + (month < 10 ? '0' + month : month);
    }

}