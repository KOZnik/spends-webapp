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
    errorMessages;
    success:boolean = false;
    spend:Spend = new Spend("", 0, "", 0);
    categoryNames:CategoryNames = new CategoryNames();

    constructor(private _categoryService:CategoryService, private _spendService:SpendService) {
    }

    ngOnInit() {
        this.getCategories();
    }

    getCategories() {
        this._categoryService.getCategories().subscribe(
            categories => categories,
            error => this.errorMessages = <any>error);
    }

    onSubmit() {
        this._spendService.storeSpend(this.spend)
            .subscribe(
                spend  => this.showSuccessMessage(5),
                error =>  this.errorMessages = <any>error);
    }

    private showSuccessMessage(seconds: number) {
        this.success=true;
        this.errorMessages = "";
        setTimeout(() => this.success=false, seconds * 1000);
    }

}