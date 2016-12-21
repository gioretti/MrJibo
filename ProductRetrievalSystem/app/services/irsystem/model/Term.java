package services.irsystem.model;

public class Term {

    private String value;
    private Double idf;

    public Term(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;

        return value.equals(term.value);

    }

    public Double getIdf() {
        return idf;
    }

    public void setIdf(Double idf) {
        this.idf = idf;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
