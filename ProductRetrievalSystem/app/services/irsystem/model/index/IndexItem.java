package services.irsystem.model.index;

import services.irsystem.model.InformationElement;
import services.irsystem.model.Term;

public class IndexItem {

    private Term term;
    private InformationElement informationElement;
    private int frequency;

    public IndexItem(Term term, InformationElement element) {
        this.term = term;
        this.informationElement = element;
        this.frequency = 1;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public InformationElement getInformationElement() {
        return informationElement;
    }

    public void setInformationElement(InformationElement informationElement) {
        this.informationElement = informationElement;
    }

    public int getFrequencyInDoc() {
        return frequency;
    }

    public void setFrequencyInDoc(int frequencyInDoc) {
        this.frequency = frequencyInDoc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexItem indexItem = (IndexItem) o;

        if (!term.equals(indexItem.term)) return false;
        return informationElement.equals(indexItem.informationElement);

    }

    @Override
    public int hashCode() {
        int result = term.hashCode();
        result = 31 * result + informationElement.hashCode();
        return result;
    }
}
