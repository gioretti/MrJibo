package services.irsystem;

import services.irsystem.model.InformationElement;
import services.irsystem.model.Result;
import services.irsystem.model.Term;
import services.irsystem.model.TermRepertory;
import services.irsystem.model.index.InvertedIndex;
import services.irsystem.model.index.NonInvertedIndex;

import java.util.*;

public class RetrievalSystem {

    private Collection<InformationElement> queries;
    private Collection<InformationElement> docs;

    private InvertedIndex invertedIndex = new InvertedIndex();
    private NonInvertedIndex nonInvertedIndex = new NonInvertedIndex();
    private NonInvertedIndex queryIndex = new NonInvertedIndex();
    private TermRepertory termRepertory = new TermRepertory();
    private Result result = new Result();

    public RetrievalSystem(Collection<InformationElement> docs){
        this.docs = docs;
        Indexer.indexDocuments(docs, invertedIndex, nonInvertedIndex, termRepertory);
        calculateIdfAndNorms();
    }

    public RetrievalSystem(Collection<InformationElement> queries, Collection<InformationElement> docs) {
        this(docs);
        this.setQueries(queries);
    }

    public void setQueries(Collection<InformationElement> queries) {
        this.queries = queries;
        Indexer.indexQueries(queries, queryIndex, termRepertory);
    }

    public void setQueries(InformationElement query) {
        Collection<InformationElement> col = new ArrayList<InformationElement>();
        col.add(query);
        setQueries(col);
    }

    public Result getResult(){
        return this.result;
    }

    public void processQueries() {

        int totalDocuments = nonInvertedIndex.size();

        for(InformationElement query : queries) {

            HashMap<InformationElement, Double> accu = new HashMap<InformationElement, Double>();
            double queryNorm = 0.0;

            Set<Term> queryTerms = queryIndex.get(query).keySet();
            for(Term term : queryTerms){

                if(term.getIdf() == null){
                    term.setIdf(Math.log(1 + totalDocuments));
                }

                int termFrequencyInDoc = queryIndex.getIndexItem(query, term).getFrequencyInDoc();
                double b = termFrequencyInDoc * term.getIdf();
                queryNorm += (b * b);

                if( invertedIndex.getDocumentFrequency( term ) > 0 ) {

                    Set<InformationElement> docs = invertedIndex.get(term).keySet();
                    for(InformationElement doc : docs) {

                        double a = invertedIndex.getIndexItem(doc, term).getFrequencyInDoc() * term.getIdf();

                        Double accuVal = accu.get(doc);
                        if(accuVal == null) {
                            accuVal = 0.0;
                        }
                        accuVal += a * b;

                        accu.put(doc, accuVal);
                    }
                }
            }

            queryNorm = Math.sqrt(queryNorm);
            normalizeVectors(accu, queryNorm);

            result.put(query, accu);
        }
    }

    public void calculateIdfAndNorms(){

        int totalDocuments = nonInvertedIndex.size();
        Set<InformationElement> elements = nonInvertedIndex.keySet();

        for(InformationElement element : elements) {
            double norm = 0;

            Set<Term> terms = nonInvertedIndex.get(element).keySet();
            for(Term term : terms) {

                int documentFrequency = invertedIndex.getDocumentFrequency(term);
                double idfValue = Math.log( (1 + totalDocuments) / (1 + documentFrequency) );
                term.setIdf(idfValue);

                int termFrequencyInDoc = nonInvertedIndex.getIndexItem(element, term).getFrequencyInDoc();
                double a  = termFrequencyInDoc * idfValue;
                norm += a * a;
            }

            element.setNorm(Math.sqrt(norm));
        }
    }

    /**
     * Normilize length of vectors
     */
    private void normalizeVectors(HashMap<InformationElement, Double> accu, Double queryNorm){
        for(InformationElement document :  accu.keySet()) {
            Double accuVal = accu.get(document);
            accuVal = (accuVal * 1000) / ( document.getNorm() * queryNorm);
            accu.put(document, accuVal);
        }
    }

    public void printResults(int numberOfResults){
        result.printResults(numberOfResults);
    }
}
