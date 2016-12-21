package services.irsystem.model.index;

import services.irsystem.model.Term;
import services.irsystem.model.InformationElement;

import java.util.HashMap;

public class InvertedIndex extends HashMap<Term, HashMap<InformationElement, IndexItem>>{

    public boolean isTermInIndex(Term term){
        return this.get(term) != null;
    }

    public int getDocumentFrequency(Term term){
        if(!isTermInIndex(term)){
            return 0;
        } else {
            return this.get(term).size();
        }
    }

    public IndexItem getIndexItem(InformationElement informationElement, Term term) {
        if( !isTermInIndex(term) ){
            return null;
        } else {
            return this.get(term).get(informationElement);
        }
    }

    public void put(InformationElement informationElement, IndexItem indexItem) {

        Term term = indexItem.getTerm();
        HashMap<InformationElement, IndexItem> items;
        if( !isTermInIndex(indexItem.getTerm()) ){
            items = new HashMap<InformationElement, IndexItem>();
            this.put(term, items);
        } else {
            items = this.get(term);
        }
        items.put(informationElement, indexItem);
    }
}
