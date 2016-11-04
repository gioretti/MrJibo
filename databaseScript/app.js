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
        name: "CLASSIC CLEAN SHAMPOO",
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
        name: "FIXPENCIL MARIO BOTTA WHITE MECHANICAL PENCIL - LIMITED EDITION",
        brand: "Carandache",
        description: "Hexagonal barrel for a unique hold and writing comfort, body in light and resistant aluminum Steel clip Checked motif designed by Mario Botta The signature of the architect Mario Botta B graphite lead Ø 2 mm Supplied with a tube of 4 coloured watercolour leads Ø 2 mm Pack with a checked pattern which reflects the Fixpencil design Swiss Made Created in 1929, the Fixpencil is an exceptional invention based on centuries of expertise of the Maison Caran d’Ache. Its novel silhouette, unrivalled technical qualities, and wide range of leads have impressed generations of architects and designers turning into a unique icon. For the world-renowned Swiss architect Mario Botta, every sketch is completed with a Fixpencil that creates a “direct relationship between the thought and the drawing”. This instrument is a source of inspiration in itself. Therefore, the shared values of Swiss excellence and creativity led Caran d’Ache and the architect to this passionate encounter with the Fixpencil.",
        price: "50",
        quantity: "2"
    },
	    {
        name: "Dimmable LED Desk Lamp Lofter 5W USB Powered Eye-caring Table Lamps with 24 LEDs, 2 Dimming Levels, 3 Lighting Modes Flexible Clip On Lights for Reading,Studying,Working,Bedroom,Office (White)",
        brand: "Lofter",
		description: "Highest quality Led: Led light brings a soft and extremely stable non flickering LED light which is kindly to your eyes. Led life more than 100,000 hours, no need to change the LED. " +
					 "2 Dimming Levels & 3 Light Modes: Built-in 24 LEDs (12 Cool White LEDs, 12 Warm Color LEDs).High and Low brightness option enables you to easily create the most comfortable and perfect lighting mood. Simple button control of 3 different Modes (Cool white LED, Warm Color LED and Combination of Cool White and Warm Color LED) " +
					 "Flexible and Stability: The light utilizes a highly flexible silicone goose neck which allows you to easily adjust the light to the perfect angle for your convenience. Firm and super strong metal clamp can clip on any hard surfaces stably, such as bedframe, headboard, home office tables, computer desk, countertops etc." +
					 "USB Powered & Last Light Mode Memory: Insert the 1.6m (5.25ft) USB Cable to power adapter, power bank, computer or other USB devices to power the lamp. Can remember the Light Mode of last time use,it is smart and convenient. PLEASE NOTE: The lamp only comes with USB Plug, it should be added a adapter when plugging it into the standard electrical outlet." +
					 "Best After-sale Service: 30-Days Money Back Guarantee, 12-Month Replacement Warranty and Lifetime Support Guarantee. Ready to reply your message within 24 hours,welcome to contact us if you have question.",
        price: "20",
        quantity: "10"
    },
    {
        name: "GoPro HERO Session (8MP, 30p, Black)",
        brand: "GoPro",
        description: "HERO4 Session packs the power of GoPro into our smallest, lightest, most convenient camera yet, featuring a rugged and waterproof design, easy one-button control, 1080p60 video and 8MP photos. " +
					 " 50% smaller and 40% lighter than other HERO4." +
					 "HERO4 Session is the most wearable and mountable GoPro ever." +
					 "Professional video quality." +
					 "HERO4 Session delivers stunning video quality. Capture high-resolution 1440p30 and 1080p60 video that’s sharp and lifelike. High frame rate 720p100 video enables exceptionally smooth slow-motion playback of your best moments." +
			 		 "Powerful photo capture." +
			 		 "Nail the shot with a variety of photo modes. Capture 8MP single photos, Time Lapse photos at set intervals from 0.5 to 60 seconds, and Burst photos at 10 frames per second. When it comes to versatile photo capture, HERO4 Session’s got you covered." +
			 		 "Durable + waterproof." +
			  		 "HERO4 Session is rugged and waterproof to 10m, eliminating the need for a separate housing. It’s convenient, ready-to-go design makes it easy to get out the door quickly to capture and enjoy your session!" +
					 "Easy one-button control." +
					 "A single press of the shutter button powers on the camera and begins capturing video or Time Lapse photos automatically. A second press of the shutter button stops recording and powers off the camera. It’s that." +
					 "Auto Low Light Mode." +
					 "Let the camera do the thinking for you. Auto Low Light mode intelligently changes frame rates based on lighting conditions for optimal low-light performance, enabling you to move between bright and dark environments without having to adjust your camera." +
					 "Protune™ for video." +
					 "Protune unlocks the camera’s full potential, delivering minimally compressed, cinema-caliber video optimized for professional productions." +
					 "HiLight Tag for your best moments." +
					 "HiLight Tag enables you to mark key moments while recording so you can quickly locate your best clips later for convenient playback, editing or sharing. Just press the button on the camera or Smart Remote2 when you capture a moment you’d like to tag. You can also tag your highlights using the GoPro." +
					 "Dual mic system." +
					 "HERO4 Session features two microphones: one on the front of the camera and one on the back. When you’re filming in windy conditions or during high-speed activities like motorsports, the camera automatically switches to the mic that’s best-suited for capturing optimal audio. The result? Reduced wind noise and enhanced audio capture—no matter what the conditions or activity." +
					 "Built-in battery." +
					 "The battery is built directly into the camera and offers up to two hours of recording time on a full charge.3 The included USB cable allows for charging the camera with your computer or the GoPro Auto or Wall Charger." +
					 "Mount upside down, capture right-side up." +
					 "When the camera is mounted upside down, Auto Image Rotation automatically adjusts the capture orientation to right-side up so there’s no need to flip your footage in post-production",
        price: "5",
        quantity: "5"
    },
  ], function(err, result) {
    assert.equal(err, null);
    assert.equal(5, result.result.n);
    assert.equal(5, result.ops.length);
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
