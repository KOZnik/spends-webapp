import {Component, OnInit} from "angular2/core";
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from "angular2/common";
import {ArraySortPipe} from "../pipes/array-sort.pipe";
import {CategoryService} from "../service/category.service";
import {SpendService} from "../service/spend.service";
import {CategoryNames} from "../entity/category-names";
import {StoreSpendComponent} from "../store-spend/store-spend.component";

@Component({
    selector: 'my-month',
    templateUrl: "app/month/month.component.html",
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES, StoreSpendComponent],
    pipes: [ArraySortPipe]
})
export class MonthComponent implements OnInit {
    date:string = "";
    categoryNames:CategoryNames = new CategoryNames();
    errorMessage;
    year:number;
    month:number;
    availableYears:number[] = [2016, 2015];
    availableMonths:number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

    constructor(private _categoryService:CategoryService, private _spendService:SpendService) {
        let currentDate = new Date();
        this.year = currentDate.getFullYear();
        this.month = currentDate.getMonth() + 1;
    }

    ngOnInit() {
        this.getSpends(this.year, this.month);
    }

    getCategories() {
        this._categoryService.getCategories().subscribe(
            categories => categories,
            error => this.errorMessage = <any>error);
    }

    getSpends(year:number, month:number) {
        this._spendService.getSpends(year, month).subscribe(
            spends => spends,
            error => this.errorMessage = <any>error);
    }

    public yearChanged(year:number):void {
        this.getSpends(year, this.month);
    }

    public monthChanged(month:number):void {
        this.getSpends(this.year, month);
    }
}