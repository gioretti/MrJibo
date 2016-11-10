mongo jiboshop --eval "db.dropDatabase()"
mongoimport -d jiboshop -c products --type csv --file .\products\Others.csv --headerline
mongoimport -d jiboshop -c products --type csv --file .\products\HeadAndShoulders.csv --headerline
mongoimport -d jiboshop -c products --type csv --file .\products\AmazonSoftware.csv --headerline