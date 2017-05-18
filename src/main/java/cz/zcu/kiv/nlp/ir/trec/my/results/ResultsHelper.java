package cz.zcu.kiv.nlp.ir.trec.my.results;

import cz.zcu.kiv.nlp.ir.trec.data.Result;
import cz.zcu.kiv.nlp.ir.trec.data.ResultImpl;
import cz.zcu.kiv.nlp.ir.trec.data.structures.DocumentsWrapper;
import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;
import cz.zcu.kiv.nlp.ir.trec.data.structures.WeightedDocument;

import java.util.*;

/**
 * Created by japan on 17-May-17.
 */
public class ResultsHelper {

    private InvertedIndex index;

    public ResultsHelper(InvertedIndex index) {
        this.index = index;
    }

    public List<Result> getBestResults(HashMap<String, Float> weightedQuery, HashMap<String, WeightedDocument> documents, boolean searchTitles){
        List<Result> results = new ArrayList<>();

        for (String docId : documents.keySet()) {
            float sim = cosineSimilarity(weightedQuery, docId, searchTitles);
            ResultImpl result = new ResultImpl();
            result.setDocumentID(docId);
            result.setScore(sim);
            results.add(result);
        }
        return results;
    }

    private float cosineSimilarity(HashMap<String,Float> weightedQuery, String docId, boolean searchTitles){
        float dot = 0, queryLength = 0, docLength = 0;

        for(String token : weightedQuery.keySet()){
            DocumentsWrapper documentsWrapper;

            if(searchTitles){
                documentsWrapper = index.getTitleDocumentWrapper(token);
            } else {
                documentsWrapper = index.getDocumentWrapper(token);
            }

            if( documentsWrapper == null) continue;
            WeightedDocument doc = documentsWrapper.getDocument(docId);
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

        // Its normalized so no more math required
        return dot;
        //return (float) (dot / ((Math.sqrt(queryLength) * Math.sqrt(docLength))));
    }

    /**
     * Brute force merge
     */
    public List<Result> mergeResults(List<Result> resultsTitles, List<Result> resultsText) {
        List<Result> toAdd = new ArrayList<>();

        for (int i = 0; i < resultsTitles.size(); i++) {
            boolean discovered = false;
            Result r = resultsTitles.get(i);
            for (int j = 0; j < resultsText.size(); j++) {
                Result res =  resultsText.get(j);

                if(r.equals(resultsText.get(j))){
                    discovered = true;
                    res.addWeight(res.getScore());
                    break;
                }
            }
            if(!discovered) toAdd.add(resultsTitles.get(i));
        }
        resultsText.addAll(toAdd);


        return resultsText;
    }
}
