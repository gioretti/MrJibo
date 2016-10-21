var MongoClient = require('mongodb').MongoClient, assert = require('assert');

// Connection URL
var url = 'mongodb://localhost:27017/jiboshop';

// Use connect method to connect to the server
MongoClient.connect(url, function(err, db) {
  assert.equal(null, err);
  console.log("Connected successfully to server");

  reset(db, function(){
      insertDocuments(db, function(){
          findAllDocuments(db, function() {
              db.close();
          });
      });
  });

});

/* ----------- FUNCTIONS ----------- */

var reset = function(db, callback) {
    db.dropDatabase()
    console.log("Dropped database");
    callback();
}

var insertDocuments = function(db, callback) {
  // Get the documents collection
  var collection = db.collection('documents');
  // Insert some documents
  collection.insertMany([
    {
        // name: "CLASSIC CLEAN SHAMPOO",
        name: "shampoo",
        brand: "Head & Shoulders",
        description: "- 7 Benefits: fights dry scalp, calms itchy scalp*, relieves irritation*, reduces redness*, great scent, controls flaky scalp* and leaves hair looking great (*associated with dandruff) " +
                     "- Formulated with Head & Shoulders’ NEW Fresh Scent Technology for an improved in-shower scent experience " +
                     "- Dandruff shampoo that gives you soft, manageable, great-looking hair that's 100% flake free** (**visible flakes; with regular use)",
        price: "5",
        quantity: "100"
    },
    {
        name: "CITRUS BREEZE SHAMPOO",
        brand: "Head & Shoulders",
        description: "- Contains infused citrus essences" +
                     "- Treats oily hair and oily scalp with a fresh, vibrant fragrance; formulated with Head & Shoulders’ " +
                     "NEW Fresh Scent Technology featuring enhanced fragrance notes for an improved in-shower scent experience " +
                     "- Gentle and pH balanced for everyday use, even for color and chemically treated hair.",
        price: "5",
        quantity: "5"
    },
    {
        //name: "FIXPENCIL MARIO BOTTA WHITE MECHANICAL PENCIL - LIMITED EDITION",
        name: "pencil",
        brand: "Carandache",
        description: "Hexagonal barrel for a unique hold and writing comfort, body in light and resistant aluminum Steel clip Checked motif designed by Mario Botta The signature of the architect Mario Botta B graphite lead Ø 2 mm Supplied with a tube of 4 coloured watercolour leads Ø 2 mm Pack with a checked pattern which reflects the Fixpencil design Swiss Made Created in 1929, the Fixpencil is an exceptional invention based on centuries of expertise of the Maison Caran d’Ache. Its novel silhouette, unrivalled technical qualities, and wide range of leads have impressed generations of architects and designers turning into a unique icon. For the world-renowned Swiss architect Mario Botta, every sketch is completed with a Fixpencil that creates a “direct relationship between the thought and the drawing”. This instrument is a source of inspiration in itself. Therefore, the shared values of Swiss excellence and creativity led Caran d’Ache and the architect to this passionate encounter with the Fixpencil.",
        price: "50",
        quantity: "2"
    },
  ], function(err, result) {
    assert.equal(err, null);
    assert.equal(3, result.result.n);
    assert.equal(3, result.ops.length);
    console.log("Inserted 3 documents into the collection");
    callback(result);
  });
}


var findAllDocuments = function(db, callback) {
  // Get the documents collection
  var collection = db.collection('documents');
  // Find some documents
  collection.find({}).toArray(function(err, docs) {
    assert.equal(err, null);
    console.log("Found the following records");
    console.log(docs)
    callback(docs);
  });
}
