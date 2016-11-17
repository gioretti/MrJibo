package services;


import data.dao.ProductDao;
import model.Product;
import services.irsystem.RetrievalSystem;
import services.irsystem.model.InformationElement;
import services.irsystem.model.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class SuggestionService {

    private final ProductDao productDao;
    private RetrievalSystem rsDescriptions;
    private RetrievalSystem rsNames;
    private HashMap<String, InformationElement> descriptionsMap = new HashMap<>();
    private HashMap<String, InformationElement> namesMap = new HashMap<>();
    private InformationElement currentQuery;

    @Inject
    public SuggestionService(ProductDao productDao, RetrievalSystem rsDescriptions, RetrievalSystem rsNames) {
        this.productDao = productDao;
        this.rsDescriptions = rsDescriptions;
        this.rsNames = rsNames;

        init();
    }

    /**
     * Prepare a Retrieval System for all the names and another for all the descriptions
     */
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

        this.rsDescriptions.setStamming(false);
        this.rsDescriptions.setDocs(productDescriptions);
        this.rsNames.setDocs(productNames);
    }

    public void setQuery(String queryText){
        this.currentQuery = new InformationElement("1", queryText);
    }

    public List<Product> getBestSuggestion(){

        processQuery();

        Result result = getQueryResult();
        if( result == null ) {
            return null;
        }
        result.printResults(100);   // for debugging purpose
        List<InformationElement> resultsRangList = relevanceFilter(result);

        List<Product> suggestedProducts = new ArrayList<>();
        for(InformationElement element : resultsRangList){
            Product p = productDao.findById(element.getId());
            suggestedProducts.add(p);
        }
        return suggestedProducts;
    }

    // filter results that are not enough relevant compared to the best result.
    private List<InformationElement> relevanceFilter(Result result){
        List<InformationElement> rangList = result.getBestResults(this.currentQuery, 100);
        HashMap<InformationElement, Double> resultMap =  result.get(this.currentQuery);
        Double bestValue = resultMap.get(rangList.get(0));

        List<InformationElement> filteredRangList = new ArrayList<>();
        for(InformationElement elem : rangList){
            double ratio = resultMap.get(elem) / bestValue;
            if ( ratio > 0.6 ) {
                filteredRangList.add(elem);
            }
        }

        return filteredRangList;
    }

    private boolean hasNameResults(InformationElement query){
        HashMap<InformationElement, Double> result = rsNames.getResult().get(query);
        return (result != null) && result.size() > 0;
    }

    private boolean hasDescriptionResults(InformationElement query){
        HashMap<InformationElement, Double> result = rsDescriptions.getResult().get(query);
        return (result != null) && result.size() > 0;
    }

    private Result getQueryResult(){
        Result result = null;
        if( hasNameResults(this.currentQuery) && hasDescriptionResults(this.currentQuery)) {
            result = new Result();
            result.put(this.currentQuery, mergeResults(this.currentQuery));
        } else if (hasNameResults(this.currentQuery)) {
            result = rsNames.getResult();
        } else if (hasDescriptionResults(this.currentQuery)){
            result = rsDescriptions.getResult();
        }
        return result;
    }

    private void processQuery(){
        rsDescriptions.setQueries(this.currentQuery);
        rsDescriptions.processQueries();
        rsNames.setQueries(this.currentQuery);
        rsNames.processQueries();
    }

    private HashMap<InformationElement, Double> mergeResults(InformationElement query){

        HashMap<InformationElement, Double> nameResult = rsNames.getResult().get(query);
        HashMap<InformationElement, Double> descriptionResult =  rsDescriptions.getResult().get(query);

        // add merged name + description results or just name result if there is not correspoding description.
        HashMap<InformationElement, Double> newResultMap = new HashMap<>();
        for(InformationElement name : nameResult.keySet()){
            InformationElement description = descriptionsMap.get(name.getId());
            if(descriptionResult.containsKey(description)){
                Double newScore = nameResult.get(name) * 2 + descriptionResult.get(description);
                newResultMap.put(name, newScore);
            } else  {
                newResultMap.put(name, nameResult.get(name));
            }
        }

        // add description that did't match with name
        for(InformationElement desc : descriptionResult.keySet()){
            InformationElement name = namesMap.get(desc.getId());
            if ( !newResultMap.containsKey(name) ) {
                newResultMap.put(name, descriptionResult.get(desc));
            }
        }

        return newResultMap;
    }
}
