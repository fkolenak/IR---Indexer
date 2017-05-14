package cz.zcu.kiv.nlp.ir.trec.my.Weighting;

import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;

/**
 * Interface for weights
 * Created by japan on 15-May-17.
 */
public interface IWeight {

    /**
     * Returns weight of the selected method
     * @param token token that will be weighted
     * @param docId document containing token
     * @param index index itself
     * @return computed weight
     */
    float getWeight(String token, String docId, InvertedIndex index);
}