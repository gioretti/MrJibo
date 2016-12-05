package data.dao;

import javax.inject.Inject;

import model.Product;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private final PlayJongo jongo ;

    @Inject
    public ProductDao(PlayJongo playJongo) {
        this.jongo = playJongo;
    }

    public MongoCollection products() {
        return jongo.getCollection("products");
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        for(Product product : products().find().as(Product.class) ) {
            products.add(product);
        }
        return products;
    }

    public void insert(Product product) {
        products().save(product);
    }

    public void remove(Product product){
        products().remove(product.getId());
    }

    public  Product findByName(String name) {
        return products().findOne("{name: #}", name).as(Product.class);
    }

    public  Product findById(String id) {
        return products().findOne("{_id: #}", new ObjectId(id)).as(Product.class);
    }



}
