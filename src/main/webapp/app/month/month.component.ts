import {Component, OnInit} from "angular2/core";
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from "angular2/common";
import {Calendar} from "primeng/primeng";
import {ArraySortPipe} from "../pipes/array-sort.pipe";
import {CategoryService} from "../service/category.service";
import {SpendService} from "../service/spend.service";
import {CategoryNames} from "../entity/category-names";
import {StoreSpendComponent} from "../store-spend/store-spend.component";

@Component({
    selector: 'my-month',
    templateUrl: "app/month/month.component.html",
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES, StoreSpendComponent, Calendar],
    pipes: [ArraySortPipe]
})
export class MonthComponent implements OnInit {
    date:string = "";
    categoryNames:CategoryNames = new CategoryNames();
    errorMessage;

    constructor(private _categoryService:CategoryService, private _spendService:SpendService) {
    }

    ngOnInit() {
        let date = new Date();
        this.getSpends(date.getFullYear(), date.getMonth() + 1);
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

    public dateChanged(date:string):void {
        let chosenDate = new Date(date);
        this.getSpends(chosenDate.getFullYear(), chosenDate.getMonth() + 1);
    }
}