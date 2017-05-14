package cz.zcu.kiv.nlp.ir.trec;

import cz.zcu.kiv.nlp.ir.trec.data.Document;
import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;
import cz.zcu.kiv.nlp.ir.trec.data.Result;
import cz.zcu.kiv.nlp.ir.trec.my.process.Process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author tigi
 */

public class Index implements Indexer, Searcher {
    private HashMap<String,Document> documents;
    private InvertedIndex index;

    private Process preprocessing = new Process();

    public Index(){
        documents = new LinkedHashMap<String, Document>();
        index = new InvertedIndex();
    }


    public void index(List<Document> documents) {
        for (Document doc: documents) {
            saveDocument(doc);

            preprocessing.parseAndIndex(doc, index);
        }



    }

    private void saveDocument(Document doc) {
        documents.put(doc.getId(),doc);
    }

    public List<Result> search(String query) {
        List<Result> results = new ArrayList<Result>();
        // TODO implement
        return results;
    }
}
