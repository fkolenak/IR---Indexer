package cz.zcu.kiv.nlp.ir.trec.data.structures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Created by japan on 13-May-17.
 */
public class InvertedIndex {
    /**
     * Index for key=tokens and and value {@link DocumentsWrapper}
     */
    private final HashMap<String,DocumentsWrapper> index = new LinkedHashMap<String,DocumentsWrapper>();
    /**
     * Stores documents(docId) and number of tokens in them.
     */
    private final  HashMap<String,Integer> documents = new LinkedHashMap<String,Integer>();


    public void addEntry(String token, String documentId){
        if(token == null || documentId == null ){
            return;
        }
        // Add number of tokens in document
        if (!documents.containsKey(documentId)){
            documents.put(documentId,1);
        } else {
            int numOfDocuments = documents.get(documentId) + 1;
            documents.put(documentId,numOfDocuments);
        }

        // Index the token
        DocumentsWrapper wrapper = index.get(token);
        if(wrapper == null){
            wrapper = new DocumentsWrapper(documentId);
        } else {
            wrapper.addEntry(documentId);
        }
        index.put(token,wrapper);

    }

    public void calculateWeights() {
        for (String token: index.keySet()) {
            DocumentsWrapper doc = index.get(token);
            doc.calculateWeights(this);
        }
    }

    public DocumentsWrapper getDocumentWrapper(String token){
        return index.get(token);
    }


    public int getTotalNumberOfDocuments(){
        return documents.size();
    }

    public int getTokensInDocument(String docId){
        return documents.get(docId);
    }



}
