"use strict";

let Axios = require('axios');

class ProductManager {

    constructor(assistantURL) {
        this.assistantURL = assistantURL;
        console.log("ProductManager: registered to " + assistantURL);
    }

    findProduct(keywords, callback) {
        Axios.get(this.assistantURL + "/suggestion",
                {params : {
                        query : keywords
                    }
                })
                .then( response => {
                    console.log("ProductManager: - received a response: ");
                    console.log(response);
                    callback(response.data);
                })
                .catch( error => {
                    console.log(error);
                    callback();
                });
    }
}

module.exports = ProductManager;
