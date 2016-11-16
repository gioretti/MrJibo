"use strict";

var Text2Number = function(numberText) {

    let Small = [
        ['zero', 0],
        ['one', 1],
        ['two', 2],
        ['three', 3],
        ['four', 4],
        ['five', 5],
        ['six', 6],
        ['seven', 7],
        ['eight', 8],
        ['nine', 9],
        ['ten', 10],
        ['eleven', 11],
        ['twelve', 12],
        ['thirteen', 13],
        ['fourteen', 14],
        ['fifteen', 15],
        ['sixteen', 16],
        ['seventeen', 17],
        ['eighteen', 18],
        ['nineteen', 19],
        ['twenty', 20],
        ['thirty', 30],
        ['forty', 40],
        ['fifty', 50],
        ['sixty', 60],
        ['seventy', 70],
        ['eighty', 80],
        ['ninety', 90]
    ];
    Small = new Map(Small);

    let Big = [
        ['thousand',     1000],
        ['million',      1000000],
        ['billion',      1000000000],
        ['trillion',     1000000000000],
        ['quadrillion',  1000000000000000],
        ['quintillion',  1000000000000000000],
        ['sextillion',   1000000000000000000000],
        ['septillion',   1000000000000000000000000],
        ['octillion',    1000000000000000000000000000],
        ['nonillion',    1000000000000000000000000000000],
        ['decillion',    1000000000000000000000000000000000],
    ]
    Big = new Map(Big);

    let tokens = numberText.split(" ");
    let smallDigit = 0;
    let bigDigit = 0;

    tokens.forEach( function( numberTXT, index ){
        let x = Small.get(numberTXT);
        if( x != undefined ) {
            smallDigit += x;

        } else if ( numberTXT == "hundred" && smallDigit != 0 ) {              // value is ...
            smallDigit *= 100;

        } else {
            x  = Big.get(numberTXT);
            if( x != undefined ) {
                bigDigit += smallDigit * x;
                smallDigit = 0;
            }
        }
    });

    return smallDigit + bigDigit;
}

module.exports = Text2Number;
