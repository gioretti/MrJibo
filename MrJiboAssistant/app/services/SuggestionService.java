package services;


import db.dao.ProductDao;
import model.Product;
import services.irsystem.RetrievalSystem;
import services.irsystem.model.InformationElement;
import services.irsystem.model.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class SuggestionService {

    private final ProductDao productDao;
    private RetrievalSystem rsDescriptions;
    private RetrievalSystem rsNames;
    private HashMap<String, InformationElement> descriptionsMap = new HashMap<>();
    private HashMap<String, InformationElement> namesMap = new HashMap<>();

    @Inject
    public SuggestionService(ProductDao productDao) {
        this.productDao = productDao;
        init();
    }

    private void init(){

        ArrayList<InformationElement> productDescriptions = new ArrayList<>();
        ArrayList<InformationElement> productNames = new ArrayList<>();

        for(Product product : productDao.findAll()) {
            InformationElement description = new InformationElement(product.getId(), product.getDescription());
            productDescriptions.add(description);
            descriptionsMap.put(product.getId(), description);

            InformationElement name = new InformationElement(product.getId(), product.getName());
            productNames.add(name);
            namesMap.put(product.getId(), name);
        }

        this.rsDescriptions = new RetrievalSystem(productDescriptions);
        this.rsNames = new RetrievalSystem(productNames);
    }

    public Product getBestSuggestion(String queryText){

        InformationElement query = new InformationElement("1", queryText);

        rsDescriptions.setQueries(query);
        rsDescriptions.processQueries();

        rsNames.setQueries(query);
        rsNames.processQueries();

        HashMap<InformationElement, Double> mergedResults;
        mergedResults = mergeResults(rsNames.getResult().get(query), rsDescriptions.getResult().get(query));

        List<InformationElement> bestResults;
        if(mergedResults != null){
            Result result = new Result();
            result.put(query, mergedResults);
            bestResults = result.getBestResults(query, 1);
        } else {
            bestResults = rsDescriptions.getResult().getBestResults(query, 1);
        }
        List<InformationElement> namesBest = rsNames.getResult().getBestResults(query, 1);
        List<InformationElement> descBest = rsDescriptions.getResult().getBestResults(query, 1);
        if ( namesBest!= null && namesBest.size() > 0 ){
            bestResults = namesBest;
        } else if ( descBest != null && descBest.size() > 0 ){
            bestResults = descBest;
        } else{
            return null;
        }
        return productDao.findById(bestResults.get(0).getId());
    }

    private HashMap<InformationElement, Double> mergeResults(HashMap<InformationElement, Double> nameResult,
                                                            HashMap<InformationElement, Double> descriptionResult){

        if(nameResult == null || nameResult.size() == 0){
            return null;
        }

        HashMap<InformationElement, Double> newScoreMap = new HashMap<>();
        for(InformationElement name : nameResult.keySet()){
            InformationElement description = descriptionsMap.get(name.getId());
            if(descriptionResult.containsKey(description)){
                Double newScore = nameResult.get(name) * 2 + descriptionResult.get(description);
                newScoreMap.put(name, newScore);
            }
        }

        return newScoreMap;
    }
}
