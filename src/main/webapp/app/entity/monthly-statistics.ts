export class MonthlyStatistics {
    public date:string;
    public FOOD:number;
    public TAXES:number;
    public MY:number;
    public WIFE:number;
    public DIFFERENT:number;
    public SON:number;
    public CAR:number;

    constructor() {
    }

    public get summary() {
        return this.FOOD + this.TAXES + this.MY + this.WIFE + this.DIFFERENT + this.SON + this.CAR;
    }
}