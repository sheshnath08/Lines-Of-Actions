package AI;

import View.Board;
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
    public AIFunctions(int state[][],Player ai, Player human){
        this.ai = new Player(false,ai.getPiece(),ai.getColor());
        this.human = new Player(true,human.getPiece(),human.getColor());
        newboard = new Board(5,5);
    }
    int[] nexBestMove(int state[][]){
        return alphaBetaSearh(state);
    }

    int[] alphaBetaSearh(int state[][]){
        int moves[] = new int[5];
         maxValue(state,minUtility,maxUtility,0,moves);
        System.out.println("Nodes Count:"+nodesCount);
        this.nodesCount = 0;
        return moves;
    }

    int[] maxValue(int state[][],int a,int b,int depth,int moves[]){
        //int moves[] = new int[5];
        int localState[][] = new int[5][5];
        for(int i=0;i<5;i++){
            for (int j = 0;j<5;j++){
                localState[i][j] = state[i][j];
            }
        }
        if(terminalTest(localState,human.getId())){
            moves[4]= -100;
            System.out.println("depth humanwin:"+depth);
            return moves;
            //return stateUtility(state);
        }
        if(terminalTest(localState,ai.getId())){
           System.out.println("depth aiwin:"+depth);
            moves[4] = 100;;
            return moves;
        }
        int alpha =a;
        int beta = b;
        ArrayList<int[]> actions = new ArrayList<>();
        actions.addAll(getAllValidAction(localState,ai.getId()));
        int v = -100000;
        for(int i=0;i<actions.size();i++){
            int row = actions.get(i)[0];
            int column = actions.get(i)[1];
            int newRow = actions.get(i)[2];
            int newColumn = actions.get(i)[3];
            v = max(v,minValue(result(state,row,column,newRow,newColumn),alpha,beta,depth+1,moves)[4]);
            this.nodesCount++;
            a = max(a,v);
            if(v>=b){
                moves[0] = row;
                moves[1] = column;
                moves[2]=newRow;
                moves[3]=newColumn;
                moves[4] = v;
                return moves;
            }
        }
        return moves;
    }

    int[] minValue(int state[][],int a,int b,int depth,int moves[]){
        //int moves[] = new int[5];
        int localState[][] = new int[5][5];
        int alpha;
        int beta;
        for(int i=0;i<5;i++){
            for (int j = 0;j<5;j++){
                localState[i][j] = state[i][j];
            }
        }
        if(terminalTest(localState,human.getId())){
            moves[4]= -100;
            //System.out.println("depth humanwin:"+depth);
            return moves;
            //return stateUtility(state);
        }
        if(terminalTest(localState,ai.getId())){
            moves[4] = 100;
            //System.out.println("depth aiwin:"+depth);
            return moves;
        }

        if(depth>= 6){
            moves[4] = (int)evaluationFunction(localState);
            return moves;
        }

        alpha = a;
        beta = b;
        ArrayList<int[]> actions = new ArrayList<>();
        actions.addAll(getAllValidAction(localState,human.getId()));
        int v = 100000;
        for(int i=0;i<actions.size();i++){
            int row = actions.get(i)[0];
            int column = actions.get(i)[1];
            int newRow = actions.get(i)[2];
            int newColumn = actions.get(i)[3];
            v = min(v,maxValue(result(state,row,column,newRow,newColumn),alpha,beta,depth+1,moves)[4]);
            this.nodesCount++;
            b = min(b,v);
            if(v<=a){
                moves[0] = row;
                moves[1] = column;
                moves[2]=newRow;
                moves[3]=newColumn;
                moves[4] = v;
                return moves;
            }
        }
        return moves;
    }

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


    /*** measures the aggregate distance between all pieces
    * @param pieces the pieces to measure
    * @return
            */
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
     * @param one
     * @param two
     * @return
     */
    private double calcDistanceBetweenPieces(int[] one, int[] two){
        int xSq = (int) Math.pow(one[0]-two[0],2);
        int YSq = (int) Math.pow(one[1]-two[1],2);
        return Math.sqrt(xSq+YSq);
    }


    // this method to get all the pieces on perticular state
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

    int stateUtility(int state[][], Player player){
        return 100;
    }

    //returns new state after performing action
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