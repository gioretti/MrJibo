package services.irsystem.model;

public class InformationElement {

    private String id;
    private String text;
    private double norm;

    public InformationElement(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public double getNorm() {
        return norm;
    }

    public void setNorm(double norm) {
        this.norm = norm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InformationElement that = (InformationElement) o;

        if (!id.equals(that.id)) return false;
        return text.equals(that.text);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
