package AI;

import View.Piece;
import View.Player;

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
    //TODO: Update moves when calling result
    private int state[][] = new int[5][5];
    private Piece aipices[];
    private Piece humanPiece[];
    private Player ai;
    private Player human;
    int maxUtility =100;
    int minUtility = -100;
    public AIFunctions(int state[][],Player ai, Player human){
        this.ai = new Player(false,ai.getPiece());
        this.aipices = new Piece[ai.getPieceVector().size()];
        this.aipices = this.ai.getPiece();
        this.human = new Player(true,human.getPiece());
        this.humanPiece = new Piece[human.getPieceVector().size()];
        this.humanPiece = this.human.getPiece();
        this.state = state;
    }
    int[] nexBestMove(){
        return alphaBetaSearh(state);
    }

    int[] alphaBetaSearh(int state[][]){
        int moves[] = maxValue(state,maxUtility,minUtility,0);
        return moves;
    }

    int[] maxValue(int state[][],int a,int b,int depth){
        updateAllPiecesAction(state);
        int moves[] = new int[3];
        if(terminalTest(state,ai)){
            //return stateUtility(state);
        }

        int v = -1000;
        for(int i=0;i<aipices.length;i++){
            Piece piece = aipices[i];
            for(int j=0;j<piece.action.size();j++){
                if(piece.action.get(j)[0]<0){
                    continue; //checking if valid action or not
                }
                v = max(v,minValue(result(state,piece,piece.action.get(j)),a,b,depth+1)[2]);
                if(v>=b){
                    moves[0] = i;
                    moves[1] = j;
                    moves[2] = v;
                    return moves;
                }
                a = max(a,v);
            }
        }
        return moves;
    }

    int[] minValue(int state[][],int a,int b,int depth){
        updateAllPiecesAction(state);
        int moves[] = new int[3];
        if(terminalTest(state,human)){
            //return stateUtility(state);
        }
        if(depth > 100){
            moves[2] = 10;
            return moves;
        }
        int v = 1000;
        for(int i=0;i<humanPiece.length;i++){
            Piece piece = humanPiece[i];
            for(int j=0;j<piece.action.size();j++){
                if(piece.action.get(j)[0]<0){
                    continue; //checking if valid action or not
                }
                v = min(v,maxValue(result(state,piece,piece.action.get(j)),a,b,depth+1)[2]);
                if(v<=a){
                    moves[0] = i;
                    moves[1]= j;
                    moves[2] = v;
                    return moves;
                }
                b = min(b,v);
            }
        }
        return moves;
    }

    boolean terminalTest(int state[][], Player player){
        //if there is no valid action for anykey
        if(player == human){
            for(int i=0;i<humanPiece.length;i++){
                Piece piece = humanPiece[i];
                for(int j=0;j<piece.action.size();j++){
                    if(piece.action.get(i)[0]>=0 && piece.action.get(i)[1]>=0){
                        return false;
                    }
                }
            }
        }
        else{
            for(int i=0;i<aipices.length;i++){
                Piece piece = aipices[i];
                for(int j=0;j<piece.action.size();j++){
                    if(piece.action.get(i)[0]>=0 && piece.action.get(i)[1]>=0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    int stateUtility(int state[][], Player player){
        return 100;
    }

    //returns new state after performing action
    int[][] result(int state[][],Piece piece, int action[]){
        //write condition to check for capturing key
        int row = piece.getRow();
        int column = piece.getColumn();
        state[row][column] = 0;
        if(piece.withHuman){
            if(state[action[0]][action[1]] == -1){
                ai.removePiece(ai.getPieceAt(action[0],action[1]));
            }
           state[action[0]][action[1]] = 1;
        }
        else{
            state[action[0]][action[1]] = -1;
        }
        return state;
    }

    void updateAllPiecesAction(int state[][]){
        for(int i=0;i<aipices.length;i++){
            if(aipices[i].getBoard()!= null){
                aipices[i].updateAction(state);
            }
        }
        for(int i=0;i<humanPiece.length;i++){
            if(humanPiece[i].getBoard()!=null){
                humanPiece[i].updateAction(state);
            }
        }
    }

    //TODO: implement alphabeta method

    //TODO: implement method to find all the actions
}
