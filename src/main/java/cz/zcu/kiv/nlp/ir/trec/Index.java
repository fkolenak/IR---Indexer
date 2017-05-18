package cz.zcu.kiv.nlp.ir.trec;

import cz.zcu.kiv.nlp.ir.trec.data.Document;
import cz.zcu.kiv.nlp.ir.trec.data.ResultImpl;
import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;
import cz.zcu.kiv.nlp.ir.trec.data.Result;
import cz.zcu.kiv.nlp.ir.trec.data.structures.WeightedDocument;
import cz.zcu.kiv.nlp.ir.trec.my.Weighting.IWeight;
import cz.zcu.kiv.nlp.ir.trec.my.Weighting.WeightTF_IDF;
import cz.zcu.kiv.nlp.ir.trec.my.process.Process;
import cz.zcu.kiv.nlp.ir.trec.my.query.QueryResolver;
import cz.zcu.kiv.nlp.ir.trec.my.results.ResultsHelper;
import org.apache.log4j.Logger;
import org.apache.lucene.search.BooleanQuery;

import java.util.*;

/**
 * @author tigi
 */

public class Index implements Indexer, Searcher {
    private static final Logger log = Logger.getLogger(Index.class);

    private HashMap<String,Document> documents;

    private InvertedIndex index;

    private Process preprocessing = new Process();
    private QueryResolver queryResolver;

    private static int MAX_RESULTS = 500;

    public Index(){
        documents = new LinkedHashMap<String, Document>();
        index = new InvertedIndex();
        queryResolver = new QueryResolver(index);
    }


    public void index(List<Document> documents) {
        long start = System.currentTimeMillis();
        for (Document doc: documents) {
            saveDocument(doc);
            preprocessing.parseAndIndex(doc, index);
        }
        log.info("Indexing took " + (System.currentTimeMillis() - start) + "ms.");
        log.info("Calculating weights");
        start = System.currentTimeMillis();
        index.calculateWeights();
        log.info("Calculating weights took " + (System.currentTimeMillis() - start) + "ms.");
    }

    private void saveDocument(Document doc) {
        documents.put(doc.getId(),doc);
    }

    public List<Result> search(String query) {
        IWeight weight = new WeightTF_IDF();



        BooleanQuery booleanQuery = preprocessing.parseQuery(query);

        HashMap<String, WeightedDocument>  documents = queryResolver.getDocuments(booleanQuery);

        HashMap<String,Float> weightedQuery =  weight.getQueryWeights( preprocessing.parse(query),index);
        ResultsHelper resultsHelper = new ResultsHelper(index);

        List<Result> results = resultsHelper.getBestResults(weightedQuery,documents);
        Comparator<Result> cmp = new Comparator<Result>() {
            public int compare(Result o1, Result o2) {
                if (o1.getScore() > o2.getScore()) return -1;
                if (o1.getScore() == o2.getScore()) return 0;
                return 1;
            }
        };

        Collections.sort(results, cmp);

        List<Result> finalResults = new ArrayList<>();

        int max = MAX_RESULTS;
        if(results.size() < MAX_RESULTS){
            max = results.size() - 1;
        }

        for(int i = 0; i < max; i++){
            finalResults.add(results.get(i));
        }

        return finalResults;
    }








}
