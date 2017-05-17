package cz.zcu.kiv.nlp.ir.trec.my.process;

import cz.zcu.kiv.nlp.ir.trec.data.Document;
import cz.zcu.kiv.nlp.ir.trec.data.structures.InvertedIndex;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

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
            //tokenStream.addAttribute(PositionIncrementAttribute.class);
            //tokenStream.addAttribute(OffsetAttribute.class);

            tokenStream.reset();
            while (tokenStream.incrementToken()) {

                String token = (tokenStream.getAttribute(CharTermAttribute.class).toString());
                invertedIndex.addEntry(token, doc.getId());
                //index += (tokenStream.getAttribute(PositionIncrementAttribute.class)).getPositionIncrement();
                //int startOffset = (tokenStream.getAttribute(OffsetAttribute.class)).startOffset();
                //int endOffset = (tokenStream.getAttribute(OffsetAttribute.class)).endOffset();

            }
            tokenStream.end();
            tokenStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BooleanQuery parseQuery(String query) {
        if(query.split(" ").length == 1){
            query += " " + query;
        }


        QueryParser parser = new QueryParser("",czechAnalyzer);
        parser.setDefaultOperator(QueryParser.Operator.OR);
        try {
            return(BooleanQuery) parser.parse(query);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
