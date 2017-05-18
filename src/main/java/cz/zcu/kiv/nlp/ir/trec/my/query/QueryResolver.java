package cz.zcu.kiv.nlp.ir.trec.my.query;

import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;
import cz.zcu.kiv.nlp.ir.trec.data.structures.WeightedDocument;
import org.apache.log4j.Logger;
import org.apache.lucene.search.*;

import java.util.*;

/**
 * Created by japan on 15-May-17.
 */
public class QueryResolver {
    private static final Logger log = Logger.getLogger(QueryResolver.class);
    private InvertedIndex index;

    public QueryResolver(InvertedIndex index) {
        this.index = index;
    }

    public HashMap<String, WeightedDocument> getDocuments(BooleanQuery parsedQuery, boolean searchTitles){



        HashMap<String, WeightedDocument>  documents = new HashMap<>();



        boolean first = true;
        for(BooleanClause clause : parsedQuery.clauses()){
            Query query = clause.getQuery();

            if(first){
                documents.putAll(getDocumentIds(query.toString(), searchTitles));
                first = false;
                continue;
            }

            if(query.getClass().equals(TermQuery.class)){
                if(clause.getOccur() == BooleanClause.Occur.MUST){
                    documents = and(documents,query, searchTitles);
                    continue;
                }
                if(clause.getOccur() == BooleanClause.Occur.SHOULD){
                    documents = or(documents,query, searchTitles);
                    continue;
                }
                if(clause.getOccur() == BooleanClause.Occur.MUST_NOT){
                    documents = not(documents,query, searchTitles);
                }

            } else if(query.getClass().equals(BooleanQuery.class)){
                if(clause.getOccur() == BooleanClause.Occur.MUST){
                    documents = and(documents, getDocuments((BooleanQuery) query, searchTitles));
                    continue;
                }
                if(clause.getOccur() == BooleanClause.Occur.SHOULD){
                    documents = or(documents, getDocuments((BooleanQuery) query, searchTitles));
                    continue;
                }
                if(clause.getOccur() == BooleanClause.Occur.MUST_NOT){
                    documents = not(documents, getDocuments((BooleanQuery) query, searchTitles));
                }
            }
        }

        return documents;
    }



    private HashMap<String, WeightedDocument> getDocumentIds(String token, boolean searchTitles){
        if(searchTitles) {
            if (index.getTitleDocumentWrapper(token) == null) {
                return new HashMap<>();
            }
            return index.getTitleDocumentWrapper(token).getDocuments();
        } else {
            if (index.getDocumentWrapper(token) == null) {
                return new HashMap<>();
            }
            return index.getDocumentWrapper(token).getDocuments();
        }
    }

    private HashMap<String, WeightedDocument> not(HashMap<String, WeightedDocument> documents, Query query, boolean searchTitles) {
        HashMap<String, WeightedDocument> mustNots = getDocumentIds(query.toString(), searchTitles);

        if(documents.isEmpty()){
            return documents;
        }

        for(String mustNot: mustNots.keySet()){
            documents.remove(mustNot);
        }

        return documents;
    }

    private HashMap<String, WeightedDocument>  not(HashMap<String, WeightedDocument>  documents, HashMap<String, WeightedDocument>  subClause) {
        if(documents.isEmpty()){
            return documents;
        }


        for(String mustNot: subClause.keySet()){
            documents.remove(mustNot);
        }

        return documents;
    }

    private HashMap<String, WeightedDocument>  or(HashMap<String, WeightedDocument> documents, Query query, boolean searchTitles) {
        HashMap<String, WeightedDocument>  docsIds = getDocumentIds(query.toString(), searchTitles);

        if(documents == null ){
            return new HashMap<> (docsIds);
        }
        if(docsIds == null){
            return documents;
        }

        documents.putAll(docsIds);

        return documents;
    }
    private HashMap<String, WeightedDocument>  or(HashMap<String, WeightedDocument>  documents,HashMap<String, WeightedDocument>  subClause) {

        if(documents == null ){
            return new HashMap<> (subClause);
        }
        if(subClause == null){
            return documents;
        }



        documents.putAll(subClause);

        return documents;
    }



    private HashMap<String, WeightedDocument>  and(HashMap<String, WeightedDocument> documents, Query query, boolean searchTitles) {
        HashMap<String, WeightedDocument>  ands = getDocumentIds(query.toString(), searchTitles);

        if(documents == null || documents.isEmpty()){
            return new HashMap<> ();
        }
        if(ands == null || ands.isEmpty()){
            return new HashMap<>();
        }

        HashMap<String, WeightedDocument>  newDocs = new HashMap<>();
        for(String docId: ands.keySet()){
            if (documents.containsKey(docId)){
                newDocs.put(docId,ands.get(docId));
            }
        }
        return newDocs;
    }

    private HashMap<String, WeightedDocument>  and(HashMap<String, WeightedDocument>  documents,HashMap<String, WeightedDocument>  subClause) {

        if(documents == null || documents.isEmpty()){
            return new HashMap<>();
        }
        if(subClause == null || subClause.isEmpty()){
            return new HashMap<>();
        }

        HashMap<String, WeightedDocument>  newDocs = new HashMap<>();
        for(String docId: subClause.keySet()){
            if (documents.containsKey(docId)){
                newDocs.put(docId,subClause.get(docId));
            }
        }
        return newDocs;
    }


    public HashMap<String,WeightedDocument> getDocumentsFromTitle(BooleanQuery booleanQuery) {
        return getDocuments(booleanQuery,true);
    }
}
