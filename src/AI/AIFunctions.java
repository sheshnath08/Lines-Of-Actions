package AI;

import View.Board;
import View.Constants;
import View.Piece;
import View.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by sheshnath on 4/6/2016.
 */
public class AIFunctions {
    /*
    returns piece number to move to a valid location.
     */
    //TODO: Terminal test and debug errors
    //TODO: winner test method
    private int state[][] = new int[5][5];
    private Player ai;
    private Player human;
    int maxUtility =100;
    int minUtility = -100;
    Board newboard;
    public AIFunctions(int state[][],Player ai, Player human){
        this.ai = new Player(false,ai.getPiece(),ai.getColor());
        this.human = new Player(true,human.getPiece(),human.getColor());
        this.state = state;
        newboard = new Board(5,5);
    }
    int[] nexBestMove(){
        return alphaBetaSearh(state);
    }

    int[] alphaBetaSearh(int state[][]){
        int moves[] = maxValue(state,maxUtility,minUtility,0);
        return moves;
    }

    int[] maxValue(int state[][],int a,int b,int depth){
        ArrayList<int[]> actions = new ArrayList<>();
        int moves[] = new int[4];
        if(terminalTest(state,ai.getId())){
            return moves;
        }
        ArrayList<int[]> aiPieces = new ArrayList<>();
        aiPieces.addAll(getPiecesLocation(state,ai.getId()));
        int v = -1000;
        for(int i=0;i<aiPieces.size();i++){
            int row = aiPieces.get(i)[0];
            int column = aiPieces.get(i)[1];
            actions.addAll(newboard.getValidAction(state,row,column));
            for(int j=0;j<actions.size();j++){
                if(actions.get(j)[0]<0){
                    continue; //checking if valid action or not
                }
                v = max(v,minValue(result(state,row,column,actions.get(j)),a,b,depth+1)[2]);
                if(v>=b){
                    moves[0] = row;
                    moves[1] = j;
                    moves[2] = v;
                    moves[3] = column;
                    return moves;
                }
                a = max(a,v);
            }
        }
        return moves;
    }

    int[] minValue(int state[][],int a,int b,int depth){
        ArrayList<int[]> actions = new ArrayList<>();
        int moves[] = new int[4];
        if(terminalTest(state,human.getId())){
            return moves;
            //return stateUtility(state);
        }
        if(depth > 2){
            moves[2] = minUtility;
            return moves;
        }
        ArrayList<int[]> humanPieces = new ArrayList<>();
        humanPieces.addAll(getPiecesLocation(state,human.getId()));
        int v = 1000;
        for(int i=0;i<humanPieces.size();i++){
            int row = humanPieces.get(i)[0];
            int column = humanPieces.get(i)[1];
            actions.addAll(newboard.getValidAction(state,row,column));
            for(int j=0;j<actions.size();j++){
                if(actions.get(j)[0]<0){
                    continue; //checking if valid action or not
                }
                v = min(v,maxValue(result(state,row,column,actions.get(j)),a,b,depth+1)[2]);
                if(v<=a){
                    moves[0] = row;
                    moves[1]= j;
                    moves[2] = v;
                    moves[3] = column;
                    return moves;
                }
                b = min(b,v);
            }
        }
        return moves;
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
        ArrayList<int[]> pieces = new ArrayList<>();
        ArrayList<int[]> actions = new ArrayList<>();
        pieces.addAll(getPiecesLocation(state,id));
        for(int i=0;i<pieces.size();i++) {
            int row = pieces.get(i)[0];
            int column = pieces.get(i)[1];
            actions.addAll(newboard.getValidAction(state, row, column));
            for (int j = 0; j < actions.size(); j++) {
                if (actions.get(j)[0] >0) {
                    return false;
                }
            }
        }
        return true;
    }

    int stateUtility(int state[][], Player player){
        return 100;
    }

    //returns new state after performing action
    int[][] result(int state[][],int row,int column, int action[]){
        //write condition to check for capturing key
        if(state[row][column] == human.getId()){
           state[action[0]][action[1]] = human.getId();
        }
        else{
            state[action[0]][action[1]] = ai.getId();
        }
        state[row][column] = 0;
        return state;
    }

    boolean isWinner(int state[][],Player player){
        HashSet<Piece> connectedPieces = new HashSet<>();
        Piece pieces[] = new Piece[player.getPiece().length];
        pieces = player.getPiece();
        if(!connectedPieces.contains(pieces[0])){
            connectedPieces.add(pieces[0]);
          //  Piece neighbourpiece = getNeighbouringPiece(state,pieces[0]);
        }
        return false;
    }

}
