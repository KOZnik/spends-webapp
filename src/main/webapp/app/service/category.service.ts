import {Injectable} from "angular2/core";
import {Http, Response} from "angular2/http";
import {Observable} from "rxjs/Observable";
import "rxjs/Rx";

@Injectable()
export class CategoryService {

    categories:string[];

    constructor(private http:Http) {
    }

    private _categoriesUrl:string = 'resources/spends/categories';

    getCategories() {
        return this.http.get(this._categoriesUrl)
            .map(res => res.json())
            .do(categories => {
                console.log(categories);
                this.categories = categories;
            })
            .catch(CategoryService.handleError);
    }

    private static handleError(error:Response) {
        console.error(error);
        return Observable.throw(error.json() || 'Server error');
    }
}