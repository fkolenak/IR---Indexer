package cz.zcu.kiv.nlp.ir.trec.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Tigi on 6.1.2015.
 */
public abstract class AbstractResult implements Result {
    String documentID;
    int rank = -1;
    float score = -1;

    public String getDocumentID() {
        return documentID;
    }

    public float getScore() {
        return score;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Result{" +
                "documentID='" + documentID + '\'' +
                ", rank=" + rank +
                ", score=" + score +
                '}';
    }

    public String toString(String topic) {
        return topic + " Q0 " + documentID + " " + rank + " " + score + " runindex1";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AbstractResult that = (AbstractResult) o;

        return new EqualsBuilder()
                .append(documentID, that.documentID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(documentID)
                .toHashCode();
    }
}
