package cz.zcu.kiv.nlp.ir.trec.my.Weighting;

import cz.zcu.kiv.nlp.ir.trec.data.structures.DocumentsWrapper;
import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;


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
        DocumentsWrapper documentsWrapper = index.getDocuments(token);

        double idf = getIdf(documentsWrapper, index);
        double tf = getTf(documentsWrapper,index,docId);

        return  (float) (tf * idf);
    }

    /**
     * Gets idf part of the equation
     * @param documentsWrapper document wrapper
     * @param index index itself
     * @return idf equation part
     */
    private double getIdf(DocumentsWrapper documentsWrapper, InvertedIndex index){
        int numberOfDocuments = index.getTotalNumberOfDocuments();
        int numberOfDocumentsThatContainToken = documentsWrapper.getNumberOfDocuments();
        return Math.log(numberOfDocuments / numberOfDocumentsThatContainToken);
    }

    /**
     * Gets the tf part of the equation
     * @param documentsWrapper document wrapper
     * @param index index itself
     * @param documentId document id
     * @return tf part of the equation
     */
    private double getTf(DocumentsWrapper documentsWrapper, InvertedIndex index, String documentId){
        int tokenOccurrenceInDocument = documentsWrapper.getNumberOfTokensInDocument(documentId);
        int tokensInDocument = index.getTokensInDocument(documentId);

        return 1 + Math.log(tokenOccurrenceInDocument/tokensInDocument);
    }

}
