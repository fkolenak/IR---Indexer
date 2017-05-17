package cz.zcu.kiv.nlp.ir.trec.my.Weighting;

import cz.zcu.kiv.nlp.ir.trec.data.structures.DocumentsWrapper;
import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;

import java.util.HashMap;
import java.util.List;

/**
 * Interface for weights
 * Created by japan on 15-May-17.
 */
public interface IWeight {

    /**
     * Returns weight of the selected method
     *
     * @param token token that will be weighted
     * @param docId document containing token
     * @param index index itself
     * @return computed weight
     */
    float getWeight(String token, String docId, InvertedIndex index);


    /**
     * Gets weight
     *
     * @param documentsWrapper doc wrapper
     * @param docId            document containing token
     * @param index            index itself
     * @return weight
     */
    float getWeight(DocumentsWrapper documentsWrapper, String docId, InvertedIndex index);

    /**
     * Gets weights for each query token
     * @param tokens  tokens
     * @param index index itself
     * @return calculated weights for tokens
     */
    HashMap<String,Float> getQueryWeights(List<String> tokens , InvertedIndex index);

    }