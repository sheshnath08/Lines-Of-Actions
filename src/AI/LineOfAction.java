package AI;

import View.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * Created by sheshnath on 4/10/2016.
 */

public class LineOfAction extends JFrame{
    Board board;
    JPanel buttonPanel;
    JPanel display;
    JButton quitButton;
    LineOfAction self;
    final int ROWS = 5;
    final int COLUMNS = 5;
    int selectedRow;
    int selectedColumn;
    int newRow;
    int newColumn;

    Piece redPiece[] = new RoundPiece[6];
    Piece blackPiece[] = new RoundPiece[6];
    HighlightBlock highlight[];
    int state[][];

    static boolean humanPlayerTurn = true;
    Player human;
    Player ai;
    int clickCount;
    
    public static void main(String[] args) {
        LineOfAction test = new LineOfAction();
        try {
            test.initializeBoard();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    
    void initializeBoard() {
        // Provide access to this object from anonymous listeners
        self = this;
        
        // Create a Board (a kind of JPanel) and add it to this Frame.
        board = new Board(ROWS, COLUMNS);
        display = board.getJPanel();

        getContentPane().add(display, BorderLayout.CENTER);
        
        // Install button panel
        buttonPanel = new JPanel();
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        installQuitButton();
        implementCloseBox();
        

        pack();
        setSize(700, 750);
        setVisible(true);
        
        placePiecesOnBoard();

    }

    /**
     * Installs a button to quit the program.
     */
    private void installQuitButton() {
        // Install Quit button
        quitButton = new JButton("Quit");
        buttonPanel.add(quitButton);
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
        }});
    }

    /**
     * Quits the program when the window is closed.
     */
    private void implementCloseBox() {
        // Implement window close box
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
        }});
    }


    /**
     * 
     */
    private void placePiecesOnBoard() {
        // Create and place some pieces
        state = new int[][]{
                {0,1,1,1,0},
                {-1,0,0,0,-1},
                {-1,0,0,0,-1},
                {-1,0,0,0,-1},
                {0,1,1,1,0}
        };
        for(int i = 0;i<6;i++){
            redPiece[i] = new RoundPiece();
            blackPiece[i] = new RoundPiece(Color.black);
        }
        //placing red piece
        board.place(redPiece[0], 1, 0);
        board.place(redPiece[1], 2, 0);
        board.place(redPiece[2], 3, 0);
        board.place(redPiece[3], 1, 4);
        board.place(redPiece[4], 2, 4);
        board.place(redPiece[5], 3, 4);

        //placing ai piece
        board.place(blackPiece[0], 0, 1);
        board.place(blackPiece[1], 0, 2);
        board.place(blackPiece[2], 0, 3);
        board.place(blackPiece[3], 4, 1);
        board.place(blackPiece[4], 4, 2);
        board.place(blackPiece[5], 4, 3);

      human = new Player(false,redPiece);
        ai = new Player(true,blackPiece);
        human.addPieces(redPiece);
        ai.addPieces(blackPiece);
 /*
        human = new Player(true,blackPiece);
        ai = new Player(false,redPiece);
        human.addPieces(blackPiece);
        ai.addPieces(redPiece);

*/
        play();

    }

    private void play() {

        display.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(clickCount == 0){
                    selectedRow = board.getSelectedRow();
                    selectedColumn = board.getSelectedColumn();
                    if(human.hasPiece(board.getPiece(selectedRow,selectedColumn))){
                        clickCount++;
                        board.setSelectedSquare(selectedRow,selectedColumn);
                    }
                }
                else{
                    newRow = board.getSelectedRow();
                    newColumn = board.getSelectedColumn();
                    //check if new selected location is valid
                     // either selecting to capture ai key or selecting new key for move
                    if(human.hasPiece(board.getPiece(newRow,newColumn))) // selecting new key for move
                    {
                            clickCount = 1;
                            board.setSelectedSquare(newRow,newColumn);
                            selectedRow = newRow;
                            selectedColumn = newColumn;
                            //removeHighlight();
                    }
                    else
                    {
                        if(isValidMove(newRow,newColumn)){
                            makeMove(newRow,newColumn);
                            //humanPlayerTurn = false;
                            clickCount = 0;
                            //removeHighlight();
                            //playAI(); // called for AI's Move
                        }
                        else{
                           clickCount = 1;
                        }
                    }
                }
                if(clickCount ==1){
                    //showValidMoves(selectedRow,selectedColumn);
                }

            }
        });
    }

    private void makeMove(int newRow, int newColumn) {
        //cheking is player trying to capture
        if(ai.hasPiece(board.getPiece(newRow,newColumn))){
            ai.removePiece(board.getPiece(newRow,newColumn));
            board.remove(newRow,newColumn);
            state[newRow][newColumn] = 0;
        }
        board.getPiece(selectedRow,selectedColumn).moveTo(newRow,newColumn);
        state[newRow][newColumn] = -1;
        state[selectedRow][selectedColumn] = 0;
    }

    private void removeHighlight() {
        for(int i=0;i<8;i++){
            board.remove(highlight[i]);
        }
    }

    private void playAI() {
        board.getPiece(0,1).moveTo(2,4);
        humanPlayerTurn = true;
        clickCount = 0;
    }

    private void showValidMoves(int selectedRow, int selectedColumn) {
        highlight = new HighlightBlock[8];
        int colSum=0;
        int rowSum=0;
        int dg1Sum=0;
        int dg2Sum = 0;
        for(int i = 1 ;i<board.getRows();i++){
            rowSum = rowSum+ Math.abs(state[selectedRow][i-1]);
            colSum = colSum + Math.abs(state[i-1][selectedColumn]);
            // TODO: find diagonal sum here
        }


        if(board.getPiece(selectedRow,selectedColumn).canMoveTo(selectedRow,selectedColumn+rowSum)){
            highlight[0] = new HighlightBlock();
            highlight[0].place(board,selectedRow,selectedColumn+rowSum);
        }
        if(board.getPiece(selectedRow,selectedColumn).canMoveTo(selectedRow+colSum,selectedColumn)){
            highlight[1] = new HighlightBlock();
            highlight[1].place(board,selectedRow+colSum,selectedColumn);
        }
        if(board.getPiece(selectedRow,selectedColumn).canMoveTo(selectedRow,selectedColumn-rowSum)){
            highlight[2] = new HighlightBlock();
            highlight[2].place(board,selectedRow,selectedColumn-rowSum);
        }
        if(board.getPiece(selectedRow,selectedColumn).canMoveTo(selectedRow-colSum,selectedColumn)){
            highlight[3] = new HighlightBlock();
            highlight[3].place(board,selectedRow-colSum,selectedColumn);
        }
        if(board.getPiece(selectedRow,selectedColumn).canMoveTo(selectedRow+dg1Sum,selectedColumn+dg1Sum)){
            highlight[4] = new HighlightBlock();
            highlight[4].place(board,selectedRow+dg1Sum,selectedColumn+dg1Sum);
        }
        if(board.getPiece(selectedRow,selectedColumn).canMoveTo(selectedRow-dg1Sum,selectedColumn-dg1Sum)){
            highlight[5] = new HighlightBlock();
            highlight[5].place(board,selectedRow-dg1Sum,selectedColumn-dg1Sum);
        }
        if(board.getPiece(selectedRow,selectedColumn).canMoveTo(selectedRow+dg2Sum,selectedColumn+dg2Sum)){
            highlight[4] = new HighlightBlock();
            highlight[4].place(board,selectedRow+dg2Sum,selectedColumn+dg2Sum);
        }
        if(board.getPiece(selectedRow,selectedColumn).canMoveTo(selectedRow-dg2Sum,selectedColumn-dg2Sum)){
            highlight[5] = new HighlightBlock();
            highlight[5].place(board,selectedRow-dg2Sum,selectedColumn-dg2Sum);
        }

    }
// below method is not required
    private boolean isValidMove(int row,int column){
        int colSum=0;
        int rowSum=0;
        int dg1Sum = 0;
        int dg2Sum = 0;
        for(int i = 0 ;i<board.getRows();i++){
            rowSum = rowSum+ Math.abs(state[selectedRow][i]);
            colSum = colSum + Math.abs(state[i][selectedColumn]);
        }

        dg1Sum = dg1Sum + Math.abs(state[selectedRow][selectedColumn]);
        dg2Sum = dg2Sum + Math.abs(state[selectedRow][selectedColumn]);
        for(int i=1;i<board.getRows();i++){
            if(selectedRow-i>=0 && selectedColumn - i >= 0) {
                dg1Sum = dg1Sum + Math.abs(state[selectedRow - i][selectedColumn - i]);
            }

            if(selectedRow+i<board.getRows() && selectedColumn +i < board.getRows()) {
                dg1Sum = dg1Sum + Math.abs(state[selectedRow + i][selectedColumn +i]);
            }

            if(selectedRow-i>=0 && selectedColumn +i < board.getRows()) {
                dg2Sum = dg2Sum + Math.abs(state[selectedRow - i][selectedColumn + i]);
            }

            if(selectedRow+i<board.getRows() && selectedColumn - i >= 0) {
                dg2Sum = dg2Sum + Math.abs(state[selectedRow + i][selectedColumn -i]);
            }
        }
        if(row == selectedRow)//moving in same row
        {
            if(column == selectedColumn+rowSum || column == selectedColumn - rowSum){
                return true;
            }
            else{
                return false;
            }
        }
        else if(column == selectedColumn)// moving in same column
        {
            if(row == selectedRow+colSum || row == selectedRow - colSum){
                return true;
            }
            else{
                return false;
            }
        }
        else // moving in diagonal
        {
            if(((row == selectedRow + dg1Sum) && (column == selectedColumn + dg1Sum))
                    || ((row == selectedRow - dg1Sum) && (column == selectedColumn - dg1Sum))){
                return true;
            }

            if(((row == selectedRow + dg2Sum) && (column == selectedColumn - dg2Sum))
                    || ((row == selectedRow - dg2Sum) && (column == selectedColumn +dg2Sum))){
                return true;
            }
        }

        return false;
    }

}