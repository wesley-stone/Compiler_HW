package Lex;

import java.util.*;

/**
 * Created by redrock on 14/11/3.
 */
public class RE2NFA {
    Fagraph buttom;
    Stack<Fagraph> graphcompution;
    Stack<Character> symbols;
    Map<Character,Integer> OPERATION_PRIORTY;

    public RE2NFA(){
        buttom=new Fagraph(null);
        symbols=new Stack<Character>();
        symbols.push('#');
        graphcompution =new Stack<Fagraph>();
        graphcompution.push(buttom);
        OPERATION_PRIORTY=new HashMap<Character, Integer>();
        OPERATION_PRIORTY.put('#',-2);
        OPERATION_PRIORTY.put('(',-1);
        OPERATION_PRIORTY.put('|',1);
        OPERATION_PRIORTY.put('.',2);
        OPERATION_PRIORTY.put('*',3);
        OPERATION_PRIORTY.put('$',-1);
    }

    Fagraph re2nfa(String re) throws Exception{
        ArrayList<Character> rexp = addDot(re);
        Iterator<Character> iterator = rexp.iterator();

        while (iterator.hasNext()) {
            char c = iterator.next();
            switch (gettype(c)) {
                case LETTER:
                    convert2NFA(c);
                    break;
                case STAR:
                case OPSYMBOL:
                    char top = symbols.peek();
                    //若栈顶元素的优先级高于当前元素则pop出来
                    if (OPERATION_PRIORTY.get(top) >= OPERATION_PRIORTY.get(c)) {
                        symbols.pop();
                        convert2NFA(top);
                        top = symbols.peek();
                        while (OPERATION_PRIORTY.get(top) >= OPERATION_PRIORTY.get(c)) {
                            symbols.pop();
                            convert2NFA(top);
                            top = symbols.peek();
                        };
                    }
                    symbols.push(c);
                    break;
                case RBraket:
                    while (symbols.peek() != '(') {
                        convert2NFA(symbols.pop());
                    }
                    symbols.pop();
                    break;
                case LBraket:
                    symbols.push('(');
                    break;
                case END:
                    //遇到结束符全部出栈
                    while(symbols.peek()!='#'){
                        convert2NFA(symbols.pop());

                    };
                    break;
            }
        }
        Fagraph result=getNext();
        if(graphcompution.peek()!=buttom){
            throw new Exception("正则表达式语法错误");
        }
        return result;
    }

    /**
     * 将扫描到的点放入图中
     * @param nchar
     */
    void convert2NFA(char nchar) throws Exception{
        System.out.print(nchar);
        switch (nchar) {
            case '.':
                Fagraph f1 = getNext();
                Fagraph f2 = getNext();
                f1.end.addRelation(null, f2.start);
                graphcompution.push(new Fagraph(f1.start, f2.end));
                break;
            case '|':
                Fagraph f3 = getNext();
                Fagraph f4 = getNext();
                State start1=new State();
                State end1=new State();
                start1.addRelation(null,f3.start);
                start1.addRelation(null,f4.start);
                f3.end.addRelation(null,end1);
                f4.end.addRelation(null,end1);
                break;
            case '*':
        //若为*运算，取出栈顶元素，处理一下，再送回去
                State start2=new State();
                State end2=new State();
                Fagraph former= graphcompution.pop();
                start2.addRelation(null, former.start);
                former.end.addRelation(null,end2);
                end2.addRelation(null,former.start);
                graphcompution.push(new Fagraph(start2, end2));
                break;
            default:
                graphcompution.push(new Fagraph(nchar));
                break;
        }

    }

    ArrayList<Character> addDot(String re){
        int temp[]=new int[re.length()];
        for(int i=0;i<re.length();i++){
            temp[i]=gettype(re.charAt(i));
        }
        ArrayList<Character> result=new ArrayList<Character>();
        for(int j=0;j<re.length()-1;j++){
            result.add(re.charAt(j));
            if((temp[j]==LETTER && temp[j+1]==LBraket)
                    ||temp[j]==RBraket && temp[j+1]==LETTER
                    ||temp[j]==LETTER && temp[j+1]==LETTER
                    ||temp[j]==RBraket && temp[j+1]==LBraket
                    ||temp[j]==STAR && temp[j+1]==LETTER
                    ||temp[j]==STAR && temp[j+1]==LBraket
                    ){
                result.add('.');
            }
        }
        result.add(re.charAt(re.length()-1));
        result.add('$');
        return result;
    };

    static final int LBraket=0;
    static final int RBraket=1;
    static final int LETTER=2;
    static final int OPSYMBOL=4;
    static final int OTHER=6;
    static final int END=5;
    static final int STAR=7;
    private int gettype(char c){
        if(c=='('){
            return LBraket;
        }
        else if(c==')'){
            return RBraket;
        }
        else if((c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c>='0'&& c<='9')){
            return LETTER;
        }
        else if(c=='|'||c=='.'){
            return OPSYMBOL;
        }else if(c=='$'){
            return END;
        }else if(c=='*'){
            return STAR;
        }else{
            return OTHER;
        }
    }

    private Fagraph getNext() throws Exception{
        Fagraph next=graphcompution.peek();
        if(next==buttom){
            throw new Exception("正则表达式语法错误！");
        }
        return next;
    }

    public static void main(String[] args){
        /*
        ArrayList<Character> redot=new RE2NFA().addDot("(aa|b)*((ab|ba)(aa|bb)*(ab|ba)(aa|bb)*)*");
        for(int i=0;i<redot.size();i++){
            System.out.print(redot.get(i));
        }
        System.out.println();
        */
        try{
            new RE2NFA().re2nfa("(aa|b)*((ab|ba)(aa|bb)*(ab|ba)(aa|bb)*)*");
            System.out.println();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
