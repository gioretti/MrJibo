package controllers;

import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.JsonNode;
import model.Product;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import services.Publisher;
import services.suggestion.SuggestionService;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;


public class ProductController extends Controller {

    private final SuggestionService suggestionService;
    private final Publisher<JsonNode> publisher = new Publisher<>(256);

    @Inject
    public ProductController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    public Result suggestion(String query){

        Logger.info("/suggestion, received query: " + query);

        UUID suggestionID = suggestionService.newSuggestion(query);
        List<Product> suggestions = suggestionService.getSuggestion(suggestionID);

        Logger.info("suggestion id is " + suggestionID);

        if(suggestions != null && suggestions.size() > 0){
            this.publisher.broadcast(Json.toJson(suggestions));
            return ok(Json.toJson(suggestions));
        } else {
            return notFound();
        }
    }

    public Result getSuggestion(String suggestionID){
        UUID id = UUID.fromString(suggestionID);
        List<Product> suggestions = suggestionService.getSuggestion(id);

        if(suggestions != null && suggestions.size() > 0){
            this.publisher.broadcast(Json.toJson(suggestions));
            return ok(Json.toJson(suggestions));
        } else {
            return notFound();
        }
    }

    public Result filterSuggestion(String suggestionID, String query){
        UUID id = UUID.fromString(suggestionID);
        List<Product> suggestions = suggestionService.filterSuggestion(id, query);

        if(suggestions != null && suggestions.size() > 0){
            this.publisher.broadcast(Json.toJson(suggestions));
            return ok(Json.toJson(suggestions));
        } else {
            return notFound();
        }
    }

    public Result getProduct(String productID){
        Product p = suggestionService.getProductById(productID);

        if(p != null){
            return ok(Json.toJson(p));
        } else {
            return notFound();
        }
    }

    public WebSocket suggestionWs() {
        return WebSocket.Json.accept(requestHeader -> {
            Sink<JsonNode, ?> sink = Sink.ignore();
            Source<JsonNode, ?> source = this.publisher.register();
            Flow<JsonNode, JsonNode, ?> flow = Flow.fromSinkAndSource(sink, source);
            return flow;
        });
    }

    public Result showSuggestion() {
        return ok(views.html.showSuggestion.render());
    }
}
