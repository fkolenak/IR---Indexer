package cz.zcu.kiv.nlp.ir.trec.data.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by japan on 14-May-17.
 */
public class DocumentsWrapper {

    /**
     * Total tokens across the documents
     */
    private int totalTokens = 0;
    /**
     * Hash map {DocID, Positions in document}
     * list of positions in that document is also frequency of the word in document
     */
    private final HashMap<String, List<Position>> documents = new LinkedHashMap<String,  List<Position>>();

    public DocumentsWrapper(String docId, Position position){
        if(position != null){
            totalTokens++;
            List<Position> positions = new ArrayList<Position>();
            positions.add(position);
            documents.put(docId,positions);
        }
    }


    public void addEntry(String documentId, Position position) {
        if(documentId == null || position == null){
            return;
        }
        List<Position> list;
        if(!documents.containsKey(documentId)){
            list = new ArrayList<Position>();
            list.add(position);
        } else {
            list = documents.get(documentId);
            list.add(position);
        }
        documents.put(documentId,list);
        totalTokens++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DocumentsWrapper wrapper = (DocumentsWrapper) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(totalTokens, wrapper.totalTokens)
                .append(documents, wrapper.documents)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(totalTokens)
                .append(documents)
                .toHashCode();
    }
}
