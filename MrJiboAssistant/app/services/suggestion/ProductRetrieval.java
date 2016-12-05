package services.suggestion;

import model.Product;
import services.irsystem.RetrievalSystem;
import services.irsystem.model.InformationElement;
import services.irsystem.model.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductRetrieval {

    private static final double RELEVANCE_FILTER_RATIO = 0.6;

    private final RetrievalSystem rsNames;
    private final HashMap<String, InformationElement> namesMap = new HashMap<>();

    private final RetrievalSystem rsDescriptions;
    private final HashMap<String, InformationElement> descriptionsMap = new HashMap<>();

    private final HashMap<String, Product> indexedProducts = new HashMap<>();

    private InformationElement currentQuery;


    @Inject
    ProductRetrieval(RetrievalSystem rsDescriptions, RetrievalSystem rsNames) {
        this.rsDescriptions = rsDescriptions;
        this.rsNames = rsNames;
    }

    public void importProducts(List<Product> products){

        List<InformationElement> productDescriptions = new ArrayList<>();
        List<InformationElement> productNames = new ArrayList<>();

        for(Product product :  products){
            InformationElement description = new InformationElement(product.getId(), product.getDescription());
            productDescriptions.add(description);
            descriptionsMap.put(product.getId(), description);

            InformationElement name = new InformationElement(product.getId(), product.getName());
            productNames.add(name);
            namesMap.put(product.getId(), name);

            indexedProducts.put(product.getId(), product);
        }

        this.rsDescriptions.setStamming(true);
        this.rsDescriptions.setDocs(productDescriptions);

        this.rsNames.setStamming(false);
        this.rsNames.setDocs(productNames);
    }

    public void processQuery(String queryText){
        InformationElement query = new InformationElement("1", queryText);
        this.currentQuery = query;
        rsDescriptions.setQueries(query);
        rsDescriptions.processQueries();
        rsNames.setQueries(query);
        rsNames.processQueries();
    }

    public List<Product> getBestSuggestion(){

        Result result = getQueryResult();
        if( result == null ) {
            return null;
        }
        result.printResults(100);   // for debugging purpose
        List<InformationElement> resultsRangList = relevanceFilter(result);

        List<Product> suggestedProducts = new ArrayList<>();
        for(InformationElement element : resultsRangList){
            Product p = indexedProducts.get(element.getId());
            suggestedProducts.add(p);
        }
        return suggestedProducts;
    }

    // filter results that are not enough relevant compared to the best result.
    private List<InformationElement> relevanceFilter(Result result){
        List<InformationElement> rangList = result.getBestResults(this.currentQuery, 100);
        HashMap<InformationElement, Double> resultMap =  result.get(this.currentQuery);
        Double bestValue = resultMap.get(rangList.get(0));

        if(rangList.size() == 1){
            return rangList;
        }

        List<InformationElement> filteredRangList = new ArrayList<>();
        for(InformationElement elem : rangList){
            double ratio = resultMap.get(elem) / bestValue;
            if ( ratio > RELEVANCE_FILTER_RATIO ) {
                filteredRangList.add(elem);
            }
        }

        return filteredRangList;
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

    private boolean hasNameResults(InformationElement query){
        HashMap<InformationElement, Double> result = rsNames.getResult().get(query);
        return (result != null) && result.size() > 0;
    }

    private boolean hasDescriptionResults(InformationElement query){
        HashMap<InformationElement, Double> result = rsDescriptions.getResult().get(query);
        return (result != null) && result.size() > 0;
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
