"use strict";

let jibo = require('jibo');
let Status = jibo.bt.Status;
let Behavior = jibo.bt.Behavior;
let BehaviorTree = jibo.bt.BehaviorTree;
let ProductManager = require('../utilities/ProductManager');

class OrderEnquiring extends Behavior {

    constructor(options) {
        super(options);
        this.status = Status.INVALID;
        this.pm = new ProductManager(options.databaseURL);
        this.keywords = options.getKeywords() ;
        this.onResult = options.onResult;
    }

    start() {
        this.status = Status.IN_PROGRESS;
        var _this = this;

        this.pm.findProduct(this.keywords, function(result) {
            _this.onResult(result);
            _this.status = Status.SUCCEEDED;
        });

        return true;
    }

    update() {
        return this.status;
    }
}

// register(name, namespace, class)
jibo.bt.register("OrderEnquiring", "project", OrderEnquiring);

module.exports = OrderEnquiring;
