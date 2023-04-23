package cd_project.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LR1Parser extends LRParser {

    private ArrayList<LR1State> canonicalCollection;

    public LR1Parser(Grammar grammar){
        super(grammar);
    }

    protected void createStatesForCLR1() {
        canonicalCollection = new ArrayList<>();
        HashSet<LR1Item> start = new HashSet<>();
        Rule startRule = grammar.getRules().get(0);
        HashSet<String> startLockahead = new HashSet<>();
        startLockahead.add("$");
        start.add(new LR1Item(startRule.getLeftSide(),startRule.getRightSide(),0,startLockahead));

        LR1State startState = new LR1State(grammar, start);
        canonicalCollection.add(startState);

        for (int i = 0; i < canonicalCollection.size(); i++) {
            HashSet<String> stringWithDot = new HashSet<>();
            for (LR1Item item : canonicalCollection.get(i).getItems()) {
                if (item.getCurrent() != null) {
                    stringWithDot.add(item.getCurrent());
                }
            }
            for (String str : stringWithDot) {
                HashSet<LR1Item> nextStateItems = new HashSet<>();
                for (LR1Item item : canonicalCollection.get(i).getItems()) {

                    if (item.getCurrent() != null && item.getCurrent().equals(str)) {
                        LR1Item temp = new LR1Item(item.getLeftSide(),item.getRightSide(),item.getDotPointer()+1,item.getLookahead());
                        nextStateItems.add(temp);
                    }
                }
                LR1State nextState = new LR1State(grammar, nextStateItems);
                boolean isExist = false;
                for (int j = 0; j < canonicalCollection.size(); j++) {
                    if (canonicalCollection.get(j).getItems().containsAll(nextState.getItems())
                            && nextState.getItems().containsAll(canonicalCollection.get(j).getItems())) {
                        isExist = true;
                        canonicalCollection.get(i).getTransition().put(str, canonicalCollection.get(j));
                    }
                }
                if (!isExist) {
                    canonicalCollection.add(nextState);
                    canonicalCollection.get(i).getTransition().put(str, nextState);
                }
            }
        }

    }

    public boolean parseCLR1(){
        createStatesForCLR1();
        createGoToTable();
        return createActionTable();
    }

    protected void createGoToTable() {
        goToTable = new HashMap[canonicalCollection.size()];
        for (int i = 0; i < goToTable.length; i++) {
            goToTable[i] = new HashMap<>();
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (String s : canonicalCollection.get(i).getTransition().keySet()) {
                if (grammar.isVariable(s)) {
                    goToTable[i].put(s, findStateIndex(canonicalCollection.get(i).getTransition().get(s)));
                }
            }
        }
    }

    private int findStateIndex(LR1State state) {
        for (int i = 0; i < canonicalCollection.size(); i++) {
            if (canonicalCollection.get(i).equals(state)) {
                return i;
            }
        }
        return -1;
    }

    private boolean createActionTable() {
        actionTable = new HashMap[canonicalCollection.size()];
        for (int i = 0; i < goToTable.length; i++) {
            actionTable[i] = new HashMap<>();
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (String s : canonicalCollection.get(i).getTransition().keySet()) {
                if (grammar.getTerminals().contains(s)) {
                    actionTable[i].put(s, new Action(ActionType.SHIFT, findStateIndex(canonicalCollection.get(i).getTransition().get(s))));
                }
            }
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (LR1Item item : canonicalCollection.get(i).getItems()) {
                if (item.getDotPointer() == item.getRightSide().length) {
                    if (item.getLeftSide().equals("S'")) {
                        actionTable[i].put("$", new Action(ActionType.ACCEPT, 0));
                    } else {
                        Rule rule = new Rule(item.getLeftSide(), item.getRightSide().clone());
                        int index = grammar.findRuleIndex(rule);
                        Action action = new Action(ActionType.REDUCE, index);
                        for (String str : item.getLookahead()) {
                            if (actionTable[i].get(str) != null) {
                                System.out.println("it has a REDUCE-" + actionTable[i].get(str).getType() + " confilct in state " + i);
                                return false;
                            } else {
                                actionTable[i].put(str, action);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public String canonicalCollectionStr() {
        String str = "Canonical Collection : \n";
        for (int i = 0; i < canonicalCollection.size(); i++) {
            str += "State " + i + " : \n";
            str += canonicalCollection.get(i)+"\n";
        }
        return str;
    }

}
