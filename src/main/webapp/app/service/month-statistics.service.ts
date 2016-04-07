import {Injectable} from "angular2/core";
import {Http, Response} from "angular2/http";
import {Observable} from "rxjs/Observable";
import "rxjs/Rx";

@Injectable()
export class MonthlyStatisticsService {

    constructor(private http:Http) {
    }

    private _Url:string = 'resources/statistics/monthly';

    getMonthlyStatistics() {
        return this.http.get(this._Url)
            .map(res => res.json())
            .do(statistics => {
                console.log(statistics);
            })
            .catch(MonthlyStatisticsService.handleError);
    }

    private static handleError(error:Response) {
        console.error(error);
        return Observable.throw(error.json() || 'Server error');
    }
}