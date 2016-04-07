import {Component, OnInit} from "angular2/core";
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from "angular2/common";
import {MonthlyStatisticsService} from "../service/month-statistics.service";
import {DataTable} from 'primeng/primeng';
import {Column} from 'primeng/primeng';

@Component({
    selector: 'my-dashboard',
    templateUrl: 'app/dashboard/dashboard.component.html',
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES, DataTable, Column]
})
export class DashboardComponent implements OnInit {

    statistics;
    errorMessage;

    constructor(private _service:MonthlyStatisticsService) {
    }

    ngOnInit() {
        this.statistics = this.getMonthlyStatistics();
    }

    getMonthlyStatistics() {
        this._service.getMonthlyStatistics().subscribe(
            statistics => this.statistics = statistics,
            error => this.errorMessage = <any>error);
    }

}