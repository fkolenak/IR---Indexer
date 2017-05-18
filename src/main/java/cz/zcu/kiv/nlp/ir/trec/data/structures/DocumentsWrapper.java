package cz.zcu.kiv.nlp.ir.trec.data.structures;

import cz.zcu.kiv.nlp.ir.trec.my.Weighting.IWeight;
import cz.zcu.kiv.nlp.ir.trec.my.Weighting.WeightTF_IDF;

import java.util.*;

/**
 * Created by japan on 14-May-17.
 */
public class DocumentsWrapper {
    IWeight weight;
    /**
     * Total tokens across the documents
     */
    private int totalTokens = 0;
    /**
     * Hash map {DocID, term frequency}
     *
     */
    private final HashMap<String, WeightedDocument> documents = new LinkedHashMap<String,  WeightedDocument>();
    //private final List<WeightedDocument> weightedDocuments = new ArrayList<WeightedDocument>();

    public DocumentsWrapper(String docId){
            totalTokens++;
            documents.put(docId,new WeightedDocument());
            weight = new WeightTF_IDF();
    }


    public void addEntry(String documentId) {
        if(documentId == null){
            return;
        }
        if(documents.containsKey(documentId)){
            WeightedDocument doc = documents.get(documentId);
            doc.setTotalOccurence(doc.getTotalOccurence() + 1);
            return;
        }
        documents.put(documentId,new WeightedDocument(1));
        totalTokens++;
    }



    public int getNumberOfDocuments(){
        return documents.size();
    }

    public int getNumberOfTokensInDocument(String docId){
        return documents.get(docId).getTotalOccurence();
    }

    public void calculateWeights(InvertedIndex index){

        float total = 0;
        for(String docId : documents.keySet()) {
            float value = weight.getWeight(this, docId,index);
            total += value * value;
            WeightedDocument w = documents.get(docId);
            w.setWeight(value);
            documents.put(docId,w);
        }
        total = (float) Math.sqrt(total);
        // Normalize
        for(String docId : documents.keySet()) {

            WeightedDocument w = documents.get(docId);
            w.setWeight(w.getWeight()/total);
            documents.put(docId,w);
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DocumentsWrapper wrapper = (DocumentsWrapper) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(totalTokens, wrapper.totalTokens)
                .append(documents, wrapper.documents)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(totalTokens)
                .append(documents)
                .toHashCode();
    }


    public HashMap<String, WeightedDocument> getDocuments() {
        return documents;
    }

    public Collection<WeightedDocument> getDocumentIds() {
        return new ArrayList<>(documents.values());
    }

    public WeightedDocument getDocument(String docId) {
        return documents.get(docId);
    }
}
