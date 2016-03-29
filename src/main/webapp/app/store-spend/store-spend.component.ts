import {Component, OnInit} from "angular2/core";
import {CORE_DIRECTIVES, FORM_DIRECTIVES} from "angular2/common";
import {CategoryService} from "../service/category.service";
import {SpendService} from "../service/spend.service";
import {Spend} from "../entity/spend";
import {CategoryNames} from "../entity/category-names";
import {Calendar} from "primeng/primeng";

@Component({
    selector: 'my-store-spend',
    templateUrl: "app/store-spend/store-spend.component.html",
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES, Calendar]
})
export class StoreSpendComponent implements OnInit {
    categories:string[];
    errorMessage;
    spend:Spend = new Spend("", "", "", 0);
    categoryNames:CategoryNames = new CategoryNames();

    constructor(private _categoryService:CategoryService, private _spendService:SpendService) {
    }

    ngOnInit() {
        this.getCategories();
    }

    getCategories() {
        this._categoryService.getCategories().subscribe(
            categories => this.categories = categories,
            error => this.errorMessage = <any>error);
    }

    onSubmit() {
        console.log("submit");
    }

}