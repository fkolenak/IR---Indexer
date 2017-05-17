package cz.zcu.kiv.nlp.ir.trec.data.structures;

/**
 * Created by japan on 15-May-17.
 */
public class WeightedDocument {

    private float weight;
    private int totalOccurence = 1;

    public WeightedDocument(){}

    public WeightedDocument(int totalOccurence) {
        this.totalOccurence = totalOccurence;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getTotalOccurence() {
        return totalOccurence;
    }

    public void setTotalOccurence(int totalOccurence) {
        this.totalOccurence = totalOccurence;
    }
}
