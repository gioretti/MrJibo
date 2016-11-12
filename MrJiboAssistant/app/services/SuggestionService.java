package services;


import data.dao.ProductDao;
import model.Product;
import services.irsystem.RetrievalSystem;
import services.irsystem.model.InformationElement;
import services.irsystem.model.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.midi.MidiDevice;
import java.util.*;

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

        this.rsDescriptions = new RetrievalSystem(productDescriptions);
        this.rsNames = new RetrievalSystem(productNames);
    }

    public Product getBestSuggestion(String queryText){
        return getBestSuggestion(queryText, 1).get(0);
    }

    public List<Product> getBestSuggestion(String queryText, int maxResults){
        InformationElement query = new InformationElement("1", queryText);
        processQuery(query);
        Result result = getQueryResult(query);
        if( result == null ) {
            return null;
        }
        result.printResults(100);   // for debugging purpose
        List<InformationElement> resultsRangList = relevanceFilter(result,
                                                                    result.getBestResults(query, maxResults),
                                                                    query );
        List<Product> suggestedProducts = new ArrayList<>();
        for(InformationElement element : resultsRangList){
            Product p = productDao.findById(element.getId());
            suggestedProducts.add(p);
        }
        return suggestedProducts;
    }

    // filter results that are not enough relevant compared to the best result.
    private List<InformationElement> relevanceFilter(Result result, List<InformationElement> rangList, InformationElement query){

        HashMap<InformationElement, Double> resultMap =  result.get(query);
        Double bestValue = resultMap.get(rangList.get(0));

        List<InformationElement> filteredRangList = new ArrayList<>();
        for(InformationElement elem : rangList){
            double ratio = resultMap.get(elem) / bestValue;
            if ( ratio > 0.9 ) {
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

    private Result getQueryResult(InformationElement query){
        Result result = null;
        if( hasNameResults(query) && hasDescriptionResults(query)) {
            result = new Result();
            result.put(query, mergeResults(query));
        } else if (hasNameResults(query)) {
            result = rsNames.getResult();
        } else if (hasDescriptionResults(query)){
            result = rsNames.getResult();
        }
        return result;
    }

    private void processQuery(InformationElement query){
        rsDescriptions.setQueries(query);
        rsDescriptions.processQueries();
        rsNames.setQueries(query);
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
