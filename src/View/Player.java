package View;

import java.util.Vector;

/**
 * Created by sheshnath on 4/10/2016.
 */
public class Player {
    boolean black;
    int key; // number of key a player currently has
    Vector<Piece> checkers;
    public Player(boolean black, Piece checkers[]){
        this.black = black;
        this.checkers = new Vector(checkers.length);
        key = checkers.length;
    }
    public void addPieces(Piece c[]){
        for(int i=0;i<key;i++){
            checkers.addElement(c[i]);
        }
    }
    private void setKey(int key){
        this.key = key;
    }
    int getKey(){
        return key;
    }
    public boolean hasPiece(Piece piece){
        if(checkers.contains(piece)){
            return true;
        }
        else{
            return false;
        }
    }
}
