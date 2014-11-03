package Lex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by redrock on 14/11/3.
 */
public class State {
    Map<Character,ArrayList<State>> relation;
    public State(){
        relation=new HashMap<Character, ArrayList<State>>();
    }
    public void addRelation(Character c, State s){
        if(relation.containsKey(c)){
            relation.get(c).add(s);
        }else{
            ArrayList<State> temp=new ArrayList<State>();
            temp.add(s);
            relation.put(c,temp);
        }
    }
}
