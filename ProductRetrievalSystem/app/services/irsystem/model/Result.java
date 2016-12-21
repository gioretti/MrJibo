package services.irsystem.model;

import java.util.*;

/**
 * query -> (Doc, accuValue)
 */
public class Result extends TreeMap<InformationElement,HashMap<InformationElement,Double>> {

    public Result() {
        super(new KeyComparator());
    }

    /**
     * It will be used to order the queries during insert.
     */
    private static class KeyComparator implements Comparator {

        public int compare(Object o1, Object o2){
            InformationElement e1 = (InformationElement)o1;
            InformationElement e2 = (InformationElement)o2;
            Integer e1Id = Integer.parseInt(e1.getId());
            Integer e2Id = Integer.parseInt(e2.getId());
            return e1Id.compareTo(e2Id);
        }

        public KeyComparator(){}
    }

    public void printResults(int numberOfResults){

        Set<InformationElement> queries = this.keySet();
        for(InformationElement query : queries){

            writeTrecResultOutput(query, this.get(query), numberOfResults);
        }
    }

    /**
     *
     * @param query
     * @param numberOfResults
     * @return a ordered list of the documents that fits better the query. The first element of the list is the
     * Document that should more relevant for the query.
     */
    public List<InformationElement> getBestResults(InformationElement query, int numberOfResults){

        ArrayList<InformationElement> bestResults = new ArrayList<InformationElement>();

        List<Map.Entry<InformationElement, Double>> list = orderAccuHash(query, this.get(query));
        Iterator itaccu = list.iterator();
        int counter = 0;
        while(itaccu.hasNext() && (counter <= numberOfResults)){
            counter++;
            Map.Entry m = (Map.Entry) itaccu.next();
            bestResults.add((InformationElement) m.getKey());
        }
        return bestResults;
    }

    private void writeTrecResultOutput(InformationElement query, HashMap accuHash, int numberOfResults){
        List<Map.Entry<InformationElement, Double>> list = orderAccuHash(query, this.get(query));
        Iterator itaccu = list.iterator();
        int counter = 0;
        while(itaccu.hasNext() && (counter <= numberOfResults)){
            counter++;
            Map.Entry m = (Map.Entry) itaccu.next();
            System.out.println(query.getId() + " Q0 " + m.getKey().toString() + " " + counter + " " + accuHash.get(m.getKey()) + " miniRetrieve");
        }
    }

    private List<Map.Entry<InformationElement, Double>> orderAccuHash(InformationElement query, HashMap<InformationElement, Double> accuHash){
        List<Map.Entry<InformationElement, Double>> list =
                new ArrayList<Map.Entry<InformationElement, Double>>(accuHash.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<InformationElement, Double>>() {
            public int compare(Map.Entry<InformationElement, Double> left,
                               Map.Entry<InformationElement, Double> right) {
                if (left.getValue() < right.getValue()) return +1;
                else if (left.getValue() > right.getValue()) return -1;
                else return 0;
            }
        });

        return list;
    }
}


