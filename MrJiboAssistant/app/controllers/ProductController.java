package controllers;

import data.dao.ProductDao;
import model.Product;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.SuggestionService;

import javax.inject.Inject;
import java.util.List;

public class ProductController extends Controller {

    private final SuggestionService suggestionService;

    @Inject
    public ProductController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    public Result suggestion(String query){

        Logger.info("/suggestion, received query: " + query);

        List<Product> suggestions = suggestionService.getBestSuggestion(query, 100);

        if(suggestions != null && suggestions.size() > 0){
            return ok(Json.toJson(suggestions));
        } else {
            return notFound();
        }
    }
}
