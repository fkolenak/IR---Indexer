package cz.zcu.kiv.nlp.ir.trec.data;

/**
 * Created by Tigi on 8.1.2015.
 */
public class ResultImpl extends AbstractResult {


    public void addWeight(float value){
        this.setScore((this.getScore() + value)/2);
    }



}
