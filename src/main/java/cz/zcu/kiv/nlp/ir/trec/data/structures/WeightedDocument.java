package cz.zcu.kiv.nlp.ir.trec.data.structures;

/**
 * Created by japan on 15-May-17.
 */
public class WeightedDocument {

    private String docId;
    private float weight;

    public WeightedDocument(String docId, float weight) {
        this.docId = docId;
        this.weight = weight;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }


}
