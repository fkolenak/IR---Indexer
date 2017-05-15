package cz.zcu.kiv.nlp.ir.trec;

import cz.zcu.kiv.nlp.ir.trec.data.Document;
import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;
import cz.zcu.kiv.nlp.ir.trec.data.Result;
import cz.zcu.kiv.nlp.ir.trec.my.process.Process;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author tigi
 */

public class Index implements Indexer, Searcher {
    private static final Logger log = Logger.getLogger(Index.class);

    private HashMap<String,Document> documents;
    private InvertedIndex index;

    private Process preprocessing = new Process();

    public Index(){
        documents = new LinkedHashMap<String, Document>();
        index = new InvertedIndex();
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
        List<Result> results = new ArrayList<Result>();











        return results;
    }








}
