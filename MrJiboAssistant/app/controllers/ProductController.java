package controllers;

import data.dao.ProductDao;
import model.Product;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.SuggestionService;

import javax.inject.Inject;
import java.util.ArrayList;

public class ProductController extends Controller {

    private final ProductDao productDao;
    private final SuggestionService suggestionService;

    @Inject
    public ProductController(ProductDao productDao, SuggestionService suggestionService) {
        this.productDao = productDao;
        this.suggestionService = suggestionService;
    }

    public Result suggestion(String query){

        Logger.info("Product suggestion, received query: " + query);

        ArrayList<Product> suggestions = new ArrayList<>();
        Product product = suggestionService.getBestSuggestion(query);
        suggestions.add(product);

        if(product != null){
            return ok(Json.toJson(suggestions));
        } else {
            return notFound();
        }
    }
}
