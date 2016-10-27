"use strict";

let MongoClient = require('mongodb').MongoClient, assert = require('assert');

class ProductManager {

    constructor(dbURL) {
        this.dbURL = dbURL;
    }

    findProduct(keywords, callback) {
        var _this = this;
        this.openConnection(function(db) {
            _this.operationfindProduct(db, keywords, function(result){
                console.log("Found the following records");
                console.log(result);
                _this.closeConnection(db);

                callback(result);
            });
        });
    }

    openConnection(callback) {
        var _this = this;
        MongoClient.connect(this.dbURL, function(err, db) {
          assert.equal(null, err);
          console.log("Connected successfully to server");

          callback(db);
        });
    }

    closeConnection(db) {
        db.close();
        console.log("database connection closed");
    }

    operationfindProduct(db, keywords, callback) {
        // Get the documents collection
        var collection = db.collection('documents');
        // Find some documents
        console.log("looking for: " + keywords);
        collection.find({'name': keywords}).toArray(function(err, docs) {
            assert.equal(err, null);
            callback(docs);
        });
    }
}

module.exports = ProductManager;
