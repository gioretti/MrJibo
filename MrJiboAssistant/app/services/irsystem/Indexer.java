package services.irsystem;

import services.irsystem.model.InformationElement;
import services.irsystem.model.Term;
import services.irsystem.model.TermRepertory;
import services.irsystem.model.index.IndexItem;
import services.irsystem.model.index.InvertedIndex;
import services.irsystem.model.index.NonInvertedIndex;

import java.util.Collection;


public class Indexer {

    public static void indexDocuments(Collection<InformationElement> documents,
                                      InvertedIndex invertedIndex,
                                      NonInvertedIndex nonInvertedIndex,
                                      TermRepertory termRepertory) {
        for(InformationElement e : documents) {
            indexInformationElement(e, invertedIndex, nonInvertedIndex, termRepertory);
        }
    }

    public static void indexQueries(Collection<InformationElement> queries,
                                    NonInvertedIndex queryIndex,
                                    TermRepertory termRepertory) {
        for( InformationElement e : queries ){
            indexInformationElement(e, null, queryIndex, termRepertory);
        }
    }

    private static void indexInformationElement(InformationElement element,
                                                InvertedIndex invertedIndex,
                                                NonInvertedIndex nonInvertedIndex,
                                                TermRepertory termRepertory){

        String[] termValues = element.getText().split("\\W");

        for(String termValue : termValues) {

            termValue = termValue.trim().toLowerCase();
            Term term = getTerm(termValue, termRepertory);

            IndexItem item = nonInvertedIndex.getIndexItem(element, term);
            if(item == null){
                item = new IndexItem(term, element);

                if(invertedIndex != null){
                    invertedIndex.put(element,item);
                }
                nonInvertedIndex.put(element, item);
            } else {

                item.setFrequencyInDoc(item.getFrequencyInDoc() + 1);
            }
        }
    }

    private static Term getTerm(String termValue, TermRepertory termRepertory) {
        Term term = termRepertory.get(termValue);
        if ( term == null ) {
            term = new Term(termValue);
            termRepertory.put(termValue, term);
        }
        return term;
    }
}
