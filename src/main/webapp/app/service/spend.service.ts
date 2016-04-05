import {Injectable} from "angular2/core";
import {Http, Response, URLSearchParams, Headers, RequestOptions} from "angular2/http";
import {Observable} from "rxjs/Observable";
import "rxjs/Rx";
import {Spend} from "../entity/spend";

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
            .catch(SpendService.handleError);
    }

    storeSpend(spend: Spend) : Observable<Spend> {
        let body = JSON.stringify(spend);
        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });

        return this.http.post(this._spendsUrl, body, options)
            .map(res =>  <Spend> res.json())
            .catch(SpendService.handleError)
    }

    private static handleError(error:Response) {
        console.error(error);
        return Observable.throw(error.json() || 'Server error');
    }
}