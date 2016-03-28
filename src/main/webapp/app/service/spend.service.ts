import {Injectable} from "angular2/core";
import {Http, Response, URLSearchParams} from "angular2/http";
import {Observable} from "rxjs/Observable";
import "rxjs/Rx";

@Injectable()
export class SpendService {
    constructor(private http:Http) {
    }

    private _spendsUrl:string = 'resources/spends';

    getSpends(year:number, month:number) {
        var params = new URLSearchParams();
        params.set('forYear', year.toString());
        params.set('forMonth', month.toString());
        return this.http.get(this._spendsUrl, {search: params})
            .map(res => res.json())
            .do(data => console.log(data))
            .catch(this.handleError);
    }

    private handleError(error:Response) {
        console.error(error);
        return Observable.throw(error.json() || 'Server error');
    }
}