package cz.zcu.kiv.nlp.ir.trec.data.structures;

/**
 * Created by japan on 14-May-17.
 */
public class Position {
    private int index;
    private int startOffset;
    private int endOffset;

    public Position(int index, int startOffset, int endOffset) {
        this.index = index;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (index != position.index) return false;
        if (startOffset != position.startOffset) return false;
        return endOffset == position.endOffset;
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + startOffset;
        result = 31 * result + endOffset;
        return result;
    }
}
