import {Component, OnInit} from "angular2/core";
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from "angular2/common";
import {Calendar} from "primeng/primeng";
import {Spend} from "../entity/spend";
import {ArraySortPipe} from "../pipes/array-sort.pipe";
import {CategoryService} from "../service/category.service";
import {SpendService} from "../service/spend.service";
import {CategoryNames} from "../entity/category-names";

@Component({
    selector: 'my-month',
    templateUrl: "app/month/month.component.html",
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES, Calendar],
    pipes: [ArraySortPipe]
})
export class MonthComponent implements OnInit {
    date:string = "";
    categories:string[];
    categoryNames:CategoryNames = new CategoryNames();
    spends:{ [key:string]:Spend; };
    errorMessage;

    constructor(private _categoryService:CategoryService, private _spendService:SpendService) {
    }

    ngOnInit() {
        this.getCategories();
        let date = new Date();
        this.getSpends(date.getFullYear(), date.getMonth() + 1);
    }

    getCategories() {
        this._categoryService.getCategories().subscribe(
            categories => this.categories = categories,
            error => this.errorMessage = <any>error);
    }

    getSpends(year:number, month:number) {
        this._spendService.getSpends(year, month).subscribe(
            spends => this.spends = spends,
            error => this.errorMessage = <any>error);
    }

    public dateChanged(date:string):void {
        let chosenDate = new Date(date);
        this.getSpends(chosenDate.getFullYear(), chosenDate.getMonth() + 1);
    }
}