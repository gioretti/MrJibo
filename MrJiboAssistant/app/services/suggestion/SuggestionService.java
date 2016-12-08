package services.suggestion;


import data.dao.ProductDao;
import model.Product;
import play.api.Play;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Singleton
public class SuggestionService {

    private final ProductDao productDao;
    private final ProductRetrieval mainProductRetrieval;
    private final HashMap<UUID, List<Product>> openSuggestions = new HashMap<>();

    @Inject
    public SuggestionService(ProductDao productDao, ProductRetrieval pr) {
        this.productDao = productDao;
        this.mainProductRetrieval = pr;

        pr.importProducts(productDao.findAll());
    }

    public UUID newSuggestion(String queryText){
        mainProductRetrieval.processQuery(queryText);
        List<Product> bestSuggestion = mainProductRetrieval.getBestSuggestion();
        UUID suggestionID = UUID.randomUUID();

        openSuggestions.put(suggestionID, bestSuggestion);

        return suggestionID;
    }

    public List<Product> filterSuggestion(UUID suggestionID, String query){
        ProductRetrieval pr = Play.current().injector().instanceOf(ProductRetrieval.class);
        pr.importProducts(openSuggestions.get(suggestionID));
        pr.processQuery(query);
        List<Product> bestSuggestion = pr.getBestSuggestion();

        if(bestSuggestion != null && bestSuggestion.size() > 0){
            openSuggestions.put(suggestionID, bestSuggestion);
        }

        return bestSuggestion;
    }

    public Product getProductById(String id){
        return productDao.findById(id);
    }

    public List<Product> getSuggestion(UUID suggestionID){
        return openSuggestions.get(suggestionID);
    }

}
