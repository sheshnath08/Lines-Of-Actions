package View;

import java.util.Vector;

/**
 * Created by sheshnath on 4/10/2016.
 */
public class Player {
    boolean black;
    int key; // number of key a player currently has
    Vector<Piece> pieceVector;
    public Player(boolean black, Piece pieceVector[]){
        this.black = black;
        this.pieceVector = new Vector(pieceVector.length);
        key = pieceVector.length;
    }
    public void addPieces(Piece c[]){
        for(int i=0;i<key;i++){
            pieceVector.addElement(c[i]);
        }
    }
    private void setKey(int key){
        this.key = key;
    }
    int getKey(){
        return key;
    }
    public boolean hasPiece(Piece piece){
        if(pieceVector.contains(piece)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean removePiece(Piece piece){
        return pieceVector.remove(piece);
    }

}
