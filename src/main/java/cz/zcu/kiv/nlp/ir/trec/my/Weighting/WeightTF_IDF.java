package cz.zcu.kiv.nlp.ir.trec.my.Weighting;

import cz.zcu.kiv.nlp.ir.trec.data.structures.DocumentsWrapper;
import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Class that implements weight interface.
 * Used for getting weights based on tf-idf model.
 * Created by japan on 14-May-17.
 */
public class WeightTF_IDF implements IWeight{

    /**
     * Gets TF-IDF weight
     * @param token token that will be weighted
     * @param docId document containing token
     * @param index index itself
     * @return tf-idf weight
     */
    public float getWeight(String token, String docId, InvertedIndex index){
        DocumentsWrapper documentsWrapper = index.getDocumentWrapper(token);

        double idf = getIdf(documentsWrapper, index);
        double tf = getTf(documentsWrapper,index,docId);

        return  (float) (tf * idf);
    }


    /**
     * Gets TF-IDF weight
     * @param documentsWrapper doc wrapper
     * @param docId document containing token
     * @param index index itself
     * @return tf-idf weight
     */
    public float getWeight(DocumentsWrapper documentsWrapper, String docId, InvertedIndex index){

        double idf = getIdf(documentsWrapper, index);
        double tf = getTf(documentsWrapper,index,docId);

        return  (float) (tf * idf);
    }

    /**
     * Gets TF-IDF weight
     * @param documentsWrapper doc wrapper
     * @param docId document containing token
     * @param index index itself
     * @return tf-idf weight
     */
    public float getTitleWeight(DocumentsWrapper documentsWrapper, String docId, InvertedIndex index){

        double idf = getTitleIdf(documentsWrapper, index);
        double tf = getTitleTf(documentsWrapper,index,docId);

        return  (float) (tf * idf);
    }

    /**
     * Gets Inverse Document Frequency part of the equation
     * @param documentsWrapper document wrapper
     * @param index index itself
     * @return idf equation part
     */
    private double getTitleIdf(DocumentsWrapper documentsWrapper, InvertedIndex index){
        if(documentsWrapper == null){
            return 1.0;
        }
        int numberOfDocuments = index.getTotalNumberOfTitleDocuments();
        int numberOfDocumentsThatContainToken = documentsWrapper.getNumberOfDocuments();
        return Math.log(((double)numberOfDocuments) / numberOfDocumentsThatContainToken);
    }

    /**
     * Gets the Term Frequency part of the equation
     * @param documentsWrapper document wrapper
     * @param index index itself
     * @param documentId document id
     * @return tf part of the equation
     */
    private double getTitleTf(DocumentsWrapper documentsWrapper, InvertedIndex index, String documentId){
        int tokenOccurrenceInDocument = documentsWrapper.getNumberOfTokensInDocument(documentId);
        int tokensInDocument = index.getTokensInTitleDocument(documentId);
        return ((double)tokenOccurrenceInDocument)/tokensInDocument;
    }

    /**
     * Gets Inverse Document Frequency part of the equation
     * @param documentsWrapper document wrapper
     * @param index index itself
     * @return idf equation part
     */
    private double getIdf(DocumentsWrapper documentsWrapper, InvertedIndex index){
        if(documentsWrapper == null){
            return 1.0;
        }
        int numberOfDocuments = index.getTotalNumberOfDocuments();
        int numberOfDocumentsThatContainToken = documentsWrapper.getNumberOfDocuments();
        return Math.log(((double)numberOfDocuments) / numberOfDocumentsThatContainToken);
    }

    /**
     * Gets the Term Frequency part of the equation
     * @param documentsWrapper document wrapper
     * @param index index itself
     * @param documentId document id
     * @return tf part of the equation
     */
    private double getTf(DocumentsWrapper documentsWrapper, InvertedIndex index, String documentId){
        int tokenOccurrenceInDocument = documentsWrapper.getNumberOfTokensInDocument(documentId);
        int tokensInDocument = index.getTokensInDocument(documentId);
        return ((double)tokenOccurrenceInDocument)/tokensInDocument;
    }

    private double getQueryTermIdf(String token, InvertedIndex index){
        DocumentsWrapper documentsWrapper = index.getDocumentWrapper(token);
       return getIdf(documentsWrapper, index);
    }

    private double getQueryTermTf(Float termFrequency, int totalTerms ){
        return termFrequency/totalTerms;
    }

    public HashMap<String,Float> getQueryWeights(List<String> tokens , InvertedIndex index){
        HashMap<String,Float> instances = new LinkedHashMap<>();

        for(String token : tokens){
            if(instances.containsKey(token)){
                instances.put(token, instances.get(token) + 1);
            } else {
                instances.put(token, 1f);
            }
        }
        float total = 0;
        for(String key : instances.keySet()){
            double idf = getQueryTermIdf(key,index);
            double tf = getQueryTermTf(instances.get(key),instances.size());
            float value = (float) (tf*idf);
            total += value;
            instances.put(key,value);
        }
        // Normalize
        for(String key : instances.keySet()){
            instances.put(key,instances.get(key)/total);
        }


        return instances;
    }


}
