package cz.zcu.kiv.nlp.ir.trec.my.results;

import cz.zcu.kiv.nlp.ir.trec.data.Result;
import cz.zcu.kiv.nlp.ir.trec.data.ResultImpl;
import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;
import cz.zcu.kiv.nlp.ir.trec.data.structures.WeightedDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by japan on 17-May-17.
 */
public class ResultsHelper {

    private InvertedIndex index;

    public ResultsHelper(InvertedIndex index) {
        this.index = index;
    }

    public List<Result> getBestResults(HashMap<String,Float> weightedQuery, HashMap<String, WeightedDocument> documents){
        List<Result> results = new ArrayList<>();

        for (String docId : documents.keySet()) {
            float sim = cosineSimilarity(weightedQuery, docId);
            ResultImpl result = new ResultImpl();
            result.setDocumentID(docId);
            result.setScore(sim);
            results.add(result);
        }
        return results;
    }

    private float cosineSimilarity(HashMap<String,Float> weightedQuery, String docId){
        float dot = 0, queryLength = 0, docLength = 0;

        for(String token : weightedQuery.keySet()){
            if( index.getDocumentWrapper(token) == null) continue;
            WeightedDocument doc = index.getDocumentWrapper(token).getDocument(docId);
            // If term actualy in document
            if(doc != null) {
                float docWeight = doc.getWeight();
                dot += weightedQuery.get(token) * docWeight;
                queryLength += weightedQuery.get(token) * weightedQuery.get(token);
                docLength += docWeight * docWeight;
            }
        }
        dot = Math.abs(dot);
        queryLength = Math.abs(queryLength);
        // Unnecesary but why not
        docLength = Math.abs(docLength);

        // Also shouldn't happen but who knows
        if(queryLength <= 0 || docLength <= 0){
            return -99;
        }
        return dot;
        //return (float) (dot / ((Math.sqrt(queryLength) * Math.sqrt(docLength))));
    }




}
