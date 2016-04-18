package View;

import java.util.Vector;

/**
 * Created by sheshnath on 4/10/2016.
 */
public class Player {
    boolean human;
    int key; // number of key a player currently has
    Vector<Piece> pieceVector;
    public Player(boolean human, Piece pieceVector[]){
        this.human = human;
        this.pieceVector = new Vector(pieceVector.length);
        key = pieceVector.length;
    }
    //adding piece to player while initializing board
    public void addPieces(Piece c[]){
        for(int i=0;i<key;i++){
            if(human){
                c[i].withHuman = true;
            }
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

    //remove piece when other player captures the piece
    public boolean removePiece(Piece piece){
        return pieceVector.remove(piece);
    }

}
