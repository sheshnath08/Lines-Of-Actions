package View;

import java.awt.*;
import java.util.Vector;

/**
 * Created by sheshnath on 4/10/2016.
 */
public class Player {
    boolean human;
    int key; // number of key a player currently has
    boolean isBlack;
    int id;
    private Vector<Piece> pieceVector;
    private Piece piece[];
    public Player(boolean human, Piece pieceVector[], boolean isBlack){
        this.human = human;
        this.piece = pieceVector;
        this.isBlack = isBlack;
        this.pieceVector = new Vector(pieceVector.length);
        key = pieceVector.length;
        if(this.human){
          this.id = 1;  //if player chose black color
        }
        else{
            this.id = -1; //if player chose red color
        }
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
    public boolean getColor(){
        return isBlack;
    }
    public int getId(){
        return id;
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

    public Piece[] getPiece(){
        return piece;
    }
    public Vector<Piece> getPieceVector() {
        return pieceVector;
    }

    //remove piece when other player captures the piece
    public boolean removePiece(Piece piece){
        boolean remove = false;
        remove = pieceVector.remove(piece);
        this.piece = new Piece[pieceVector.size()];
        for(int i=0;i<pieceVector.size();i++){
            this.piece[i] = pieceVector.get(i);
        }
        return remove;
    }

    public Piece getPieceAt(int row,int column){
        Piece piece = null;
        for(int i=0;i<pieceVector.size();i++){
            piece = pieceVector.get(i);
            if(piece.getRow() == row && piece.getColumn() == column){
                return piece;
            }
        }
        piece = null;
        return piece;
    }

}
