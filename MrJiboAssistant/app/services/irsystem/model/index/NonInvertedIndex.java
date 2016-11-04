package services.irsystem.model.index;

import services.irsystem.model.Term;
import services.irsystem.model.InformationElement;

import java.util.HashMap;

public class NonInvertedIndex extends HashMap<InformationElement, HashMap<Term, IndexItem>> {

    public boolean isInformationElementInIndex(InformationElement informationElement){
        return this.get(informationElement) != null;
    }

    public IndexItem getIndexItem(InformationElement informationElement, Term term) {
        if( !isInformationElementInIndex(informationElement) ) {
            return null;
        } else {
            return this.get(informationElement).get(term);
        }
    }

    public void put(InformationElement informationElement, IndexItem indexItem) {

        Term term = indexItem.getTerm();
        HashMap<Term, IndexItem> items;
        if( !isInformationElementInIndex(informationElement) ){
            items = new HashMap<Term, IndexItem>();
            this.put(informationElement, items);
        } else {
            items = this.get(informationElement);
        }

        items.put(term, indexItem);
    }
}
