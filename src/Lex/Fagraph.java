package Lex;

/**
 * Created by redrock on 14/11/3.
 */
public class Fagraph {
    State start;
    State end;
    public Fagraph(Character c){
        start=new State();
        end=new State();
        start.addRelation(c ,end);
    }

    public Fagraph(State str,State ed){
        start=str;
        end=ed;
    }

    public State getStart(){
        return start;
    }
    public State getEnd(){
        return end;
    }
}
