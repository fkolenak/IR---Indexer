package cz.zcu.kiv.nlp.ir.trec.my.process;

import cz.zcu.kiv.nlp.ir.trec.data.Document;
import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;
import cz.zcu.kiv.nlp.ir.trec.data.structures.Position;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.analysis.tokenattributes.*;


import java.io.IOException;
import java.io.StringReader;



/**
 * Class that processes given data and stores it in index
 * Created by japan on 13-May-17.
 */
public class Process {
    private static final Logger log = Logger.getLogger(Process.class);

    /**
     * Czech analyzer. Lower case, stemming, stop words removing
     */
    public static final CzechAnalyzer czechAnalyzer = new CzechAnalyzer();

    /**
     * Parses document and stores tokens in index
     * @param doc document to store
     * @param invertedIndex index itself
     */
    public void parseAndIndex(Document doc, InvertedIndex invertedIndex){
        if(invertedIndex == null){
            return;
        }
        try {
            TokenStream tokenStream = czechAnalyzer.tokenStream(null, new StringReader(doc.getText()));
            tokenStream.addAttribute(PositionIncrementAttribute.class);
            tokenStream.addAttribute(OffsetAttribute.class);

            tokenStream.reset();
            int index = -1;
            while (tokenStream.incrementToken()) {

                String token = (tokenStream.getAttribute(CharTermAttribute.class).toString());
                index += (tokenStream.getAttribute(PositionIncrementAttribute.class)).getPositionIncrement();
                int startOffset = (tokenStream.getAttribute(OffsetAttribute.class)).startOffset();
                int endOffset = (tokenStream.getAttribute(OffsetAttribute.class)).endOffset();

                Position position = new Position(index,startOffset,endOffset);

                invertedIndex.addEntry(token, doc.getId(),position);
                //log.info("Token["+ index + "] : " + token);
            }
            tokenStream.end();
            tokenStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
