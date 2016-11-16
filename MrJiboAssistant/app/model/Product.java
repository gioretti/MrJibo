package model;

import org.jongo.marshall.jackson.oid.MongoObjectId;
import play.api.Play;
import uk.co.panaxiom.playjongo.PlayJongo;

public class Product {

    @MongoObjectId
    private String _id;
    private String name;
    private String brand;
    private String description;
    private Double price;
    private int quantity;

    private Product(){}

    public Product(String name, String brand, String description, Double price, int quantity) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
