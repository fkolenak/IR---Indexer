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
    private final HashMap<String, Integer> documents = new LinkedHashMap<String,  Integer>();
    private final List<WeightedDocument> weightedDocuments = new ArrayList<WeightedDocument>();

    // TODO documents as vectors
    public DocumentsWrapper(String docId){
            totalTokens++;
            documents.put(docId,1);
            weight = new WeightTF_IDF();
    }


    public void addEntry(String documentId, Position position) {
        if(documentId == null || position == null){
            return;
        }
        Integer tf = 1;
        if(documents.containsKey(documentId)){
            tf += documents.get(documentId);
        }
        documents.put(documentId,tf);
        totalTokens++;
    }



    public int getNumberOfDocuments(){
        return documents.size();
    }

    public int getNumberOfTokensInDocument(String docId){
        return documents.get(docId);
    }

    public void calculateWeights(InvertedIndex index){

        for(String docId : documents.keySet()) {
            float value = weight.getWeight(this, docId,index);
            WeightedDocument doc = new WeightedDocument(docId,value);
            weightedDocuments.add(doc);
        }
        weightedDocuments.sort((o1, o2) -> {
            if(o1.getWeight() < o2.getWeight() ){
                return 1;
            }
            if(o1.getWeight() > o2.getWeight()){
                return -1;
            }
            return 0;
        });
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




}
