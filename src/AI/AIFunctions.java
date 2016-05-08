package AI;

import View.Board;
import View.Constants;
import View.Player;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by sheshnath on 4/6/2016.
 */

public class AIFunctions {
    /*
    returns piece number to move to a valid location.
     */
    private Player ai;
    private Player human;
    int maxUtility =100;
    int minUtility = -100;
    Board newboard;
    int nodesCount = 0;
    int maxt = 0;
    int depthLimit;
    int depthCount;

    public AIFunctions(int state[][],Player ai, Player human){
        this.ai = new Player(false,ai.getPiece(),ai.getColor());
        this.human = new Player(true,human.getPiece(),human.getColor());
        newboard = new Board(5,5);
        this.nodesCount = 0;
        this.depthCount = 0;
        this.depthLimit = Constants.depthLevel;
    }

    /*
    * Alpha-Beta pruning algorithm*/
    int[] alphaBetaSearh(int state[][]){
        int moves[] = new int[5];
        moves = minMax(state,minUtility,maxUtility,0,moves);
        System.out.println("Nodes Count:"+nodesCount);
        System.out.println("Depth Count:"+depthCount);
        System.out.println("Max ut: "+maxt);
        return moves;
    }

    /*
    * Min-max Algorithm*/
    int[] minMax(int state[][],int a,int b,int depth,int moves[]){
        int localState[][] = new int[5][5];
        boolean found = false;
        for(int i=0;i<5;i++){
            for (int j = 0;j<5;j++){
                localState[i][j] = state[i][j];
            }
        }
        ArrayList<int[]> actions = new ArrayList<>();
        actions.addAll(getAllValidAction(localState,ai.getId()));
        int v = -100000;
        for(int i=0;i<actions.size();i++){
            int row = actions.get(i)[0];
            int column = actions.get(i)[1];
            int newRow = actions.get(i)[2];
            int newColumn = actions.get(i)[3];
            v = max(v,minValue(result(state,row,column,newRow,newColumn),a,b,depth+1,moves)[4]);
            this.nodesCount++;
            a = max(a,v);
           // elapsedTime = (System.nanoTime() -startTime)/100;
            if(v>=b){
                found = true;
                moves[0] = row;
                moves[1] = column;
                moves[2]=newRow;
                moves[3]=newColumn;
                moves[4] = v;
                return moves;
            }
        }
        if(!found){
            moves = actions.get(actions.size()-2);
        }
        return moves;
    }

    int[] maxValue(int state[][],int a,int b,int depth,int moves[]){
        if(depth>depthCount){
            depthCount= depth;
        }
        int localState[][] = new int[5][5];
        for(int i=0;i<5;i++){
            for (int j = 0;j<5;j++){
                localState[i][j] = state[i][j];
            }
        }
        //checking is minplayer wins
        if(terminalTest(localState,human.getId())){
            moves[4]= minUtility;
            return moves;
        }
        //checking if maxplayer wins
        if(terminalTest(localState,ai.getId())){
            if(maxt<evaluationFunction(localState)){
                maxt =(int)evaluationFunction(localState);
            }
            moves[4] = maxUtility;
            return moves;
        }
        ArrayList<int[]> actions = new ArrayList<>();
        /*adding all valid actions at localstate*/
        actions.addAll(getAllValidAction(localState,ai.getId()));
        int v = -100000;
        for(int i=0;i<actions.size();i++){
            int row = actions.get(i)[0];
            int column = actions.get(i)[1];
            int newRow = actions.get(i)[2];
            int newColumn = actions.get(i)[3];
            v = max(v,minValue(result(state,row,column,newRow,newColumn),a,b,depth+1,moves)[4]);
            this.nodesCount++;
            a = max(a,v);
            if(v>=b){
                moves[4] = v;
                return moves;
            }
        }
        return moves;
    }

    int[] minValue(int state[][],int a,int b,int depth,int moves[]){
        if(depth>depthCount){
            depthCount= depth;
        }
        int localState[][] = new int[5][5];
        for(int i=0;i<5;i++){
            for (int j = 0;j<5;j++){
                localState[i][j] = state[i][j];
            }
        }
        if(terminalTest(localState,human.getId())){
            moves[4]= minUtility;
            return moves;
        }
        if(terminalTest(localState,ai.getId())){
            moves[4] = maxUtility;
            return moves;
        }
        if(depth>= depthLimit){
            moves[4] = (int)evaluationFunction(localState);
            return moves;
        }
        ArrayList<int[]> actions = new ArrayList<>();
        actions.addAll(getAllValidAction(localState,human.getId()));
        int v = 100000;
        for(int i=0;i<actions.size();i++){
            int row = actions.get(i)[0];
            int column = actions.get(i)[1];
            int newRow = actions.get(i)[2];
            int newColumn = actions.get(i)[3];
            v = min(v,maxValue(result(state,row,column,newRow,newColumn),a,b,depth+1,moves)[4]);
            this.nodesCount++;
            b = min(b,v);
            if(v<=a){
                moves[4] = v;
                return moves;
            }
        }
        return moves;
    }

    /*
    evaluation function that takes input state.
    Calculate sum of eculidian distance of each piece with other of a player and return diff minPlayersum-maxPlayersum
    * */
    private double evaluationFunction(int[][] state) {
        int localState[][] = new int[5][5];
        for(int i=0;i<5;i++){
            for (int j = 0;j<5;j++){
                localState[i][j] = state[i][j];
            }
        }
        ArrayList<int[]> aipieces = new ArrayList<>();
        aipieces.addAll(getPiecesLocation(localState,ai.getId()));
        ArrayList<int[]> humanpieces = new ArrayList<>();
        humanpieces.addAll(getPiecesLocation(localState,human.getId()));
        double aiDist = getDistanceBetweenPieces(aipieces);
        double humDist = getDistanceBetweenPieces(humanpieces);
        return humDist-aiDist;
    }


    /* measures the aggregate distance between all pieces*/
    private double getDistanceBetweenPieces(ArrayList<int[]> pieces){
        double runningSum = 0;
        for(int i = 0; i < pieces.size();i++){
            for(int j = 0; j < i;j++){
                runningSum += calcDistanceBetweenPieces(pieces.get(i), pieces.get(j));
            }
        }
        return runningSum;
    }

    /**
     * calculates the distance between 2 Pieces
     * */
    private double calcDistanceBetweenPieces(int[] one, int[] two){
        int xSq = (int) Math.pow(one[0]-two[0],2);
        int YSq = (int) Math.pow(one[1]-two[1],2);
        return Math.sqrt(xSq+YSq);
    }


   /* this method to get all the pieces on perticular state*/
    private ArrayList<int[]> getPiecesLocation(int[][] state,int id) {
        ArrayList<int[]>pieces = new ArrayList<>();
        for(int i=0;i< 5;i++)//modify i<5 for 6X6
        {
            for(int j=0;j<5;j++){
                if(state[i][j] == id){
                    pieces.add(new int[]{i,j});
                }
            }
        }
        return pieces;
    }



    boolean terminalTest(int state[][], int id){
        //if there is no valid action for anykey
        if(newboard.isWinner(state,id)){
            return true;
        }
        return false;
    }

    /*returns new state after performing action*/
    int[][] result(int state[][],int row,int column, int newRow,int newColumn){
        //write condition to check for capturing key
        int result[][] = new int[5][5];
        for(int i=0;i<5;i++){
            for (int j = 0;j<5;j++){
                result[i][j] = state[i][j];
            }
        }
        if(result[row][column] == human.getId()){
            result[newRow][newColumn] = human.getId();
        }
        else{
            result[newRow][newColumn] = ai.getId();
        }
        result[row][column] = 0;
        return result;
    }

    /*Returns an ArrayList containg [row,column] of all valid actions from state for player with id*/
    ArrayList<int[]> getAllValidAction(int state[][],int id){
        ArrayList<int[]> action = new ArrayList<>();
        ArrayList<int[]>pieces = new ArrayList<>();
        pieces.addAll(getPiecesLocation(state,id));
        for(int i=0;i<pieces.size();i++){
            ArrayList<int[]> temaction = new ArrayList<>();
            int row = pieces.get(i)[0];
            int col = pieces.get(i)[1];
            temaction.addAll(newboard.getValidAction(state,row,col));
            for(int j=0;j<temaction.size();j++){
                int move[] = new int[4];
                move[0] = row;
                move[1] = col;
                if(temaction.get(j)[0]<0){
                    continue;
                }
                move[2] = temaction.get(j)[0];
                move[3] = temaction.get(j)[1];
                action.add(move);
            }
        }
        return action;
    }

}