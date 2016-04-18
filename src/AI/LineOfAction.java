package AI;

import View.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
        board.state = new int[][]{
                {0,1,1,1,0},
                {-1,0,0,0,-1},
                {-1,0,0,0,-1},
                {-1,0,0,0,-1},
                {0,1,1,1,0}
        };
        for(int i = 0;i<6;i++){
            board.redPiece[i] = new RoundPiece();
            board.blackPiece[i] = new RoundPiece(Color.black);
        }
        //placing red piece
        board.place(board.redPiece[0], 1, 0);
        board.place(board.redPiece[1], 2, 0);
        board.place(board.redPiece[2], 3, 0);
        board.place(board.redPiece[3], 1, 4);
        board.place(board.redPiece[4], 2, 4);
        board.place(board.redPiece[5], 3, 4);

        //placing ai piece
        board.place(board.blackPiece[0], 0, 1);
        board.place(board.blackPiece[1], 0, 2);
        board.place(board.blackPiece[2], 0, 3);
        board.place(board.blackPiece[3], 4, 1);
        board.place(board.blackPiece[4], 4, 2);
        board.place(board.blackPiece[5], 4, 3);

      human = new Player(true,board.redPiece);
        ai = new Player(false,board.blackPiece);
        human.addPieces(board.redPiece);
        ai.addPieces(board.blackPiece);
 /*
        human = new Player(true,blackPiece);
        ai = new Player(false,redPiece);
        human.addPieces(blackPiece);
        ai.addPieces(redPiece);

*/
        board.updatePiecesActions();
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
                        showValidMoves(selectedRow,selectedColumn);
                    }
                }
                else{
                    newRow = board.getSelectedRow();
                    newColumn = board.getSelectedColumn();
                    //check if new selected location is valid
                     // either selecting to capture ai key or selecting new key for move
                    if(human.hasPiece(board.getPiece(newRow,newColumn))) // selecting new key for move
                    {
                        removeHighlight();
                            clickCount = 1;
                            board.setSelectedSquare(newRow,newColumn);
                            selectedRow = newRow;
                            selectedColumn = newColumn;
                        showValidMoves(selectedRow,selectedColumn);
                    }
                    else
                    {
                        if(board.isValidMove(selectedRow,selectedColumn,newRow,newColumn)){
                            makeMove(newRow,newColumn);
                            //humanPlayerTurn = false;
                            clickCount = 0;
                            removeHighlight();
                            //playAI(); // called for AI's Move
                        }
                        else{
                           clickCount = 1;
                        }
                    }
                }

            }
        });
    }

    private void makeMove(int newRow, int newColumn) {
        //cheking is player trying to capture
        if(ai.hasPiece(board.getPiece(newRow,newColumn))){
            ai.removePiece(board.getPiece(newRow,newColumn));
            board.remove(newRow,newColumn);
            board.state[newRow][newColumn] = 0;
        }
        board.getPiece(selectedRow,selectedColumn).moveTo(newRow,newColumn);
        board.state[newRow][newColumn] = -1;
        board.state[selectedRow][selectedColumn] = 0;
        board.updatePiecesActions();
    }

    private void removeHighlight() {
        for(int i=0;i<8;i++){
            board.remove(board.highlight[i]);
        }
    }

    private void playAI() {
        board.getPiece(0,1).moveTo(2,4);
        humanPlayerTurn = true;
        clickCount = 0;
    }


    //TODO: Not working properly, check Actions of pieces and board state
    private void showValidMoves(int selectedRow, int selectedColumn) {
         board.highlight = new HighlightBlock[8];
        ArrayList actions = board.getPiece(selectedRow,selectedColumn).getAction();
        for(int i=0;i<actions.size();i++){
            int action[] = ((int[]) actions.get(i));
            if(action[0]>=0 && action[1]>=0){
                board.highlight[i] = new HighlightBlock();
                 board.place(board.highlight[i],action[0],action[1]);
            }
        }

    }

}