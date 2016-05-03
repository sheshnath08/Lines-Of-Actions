package AI;

import View.*;
import com.sun.rowset.internal.Row;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

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
    AIFunctions aiFunctions;
    final int ROWS = 5;
    final int COLUMNS = 5;
    int selectedRow;
    int selectedColumn;
    int newRow;
    int newColumn;
    boolean colorBlack = true; //human selected black color

    static boolean humanPlayerTurn = true;
    Player human;
    Player ai;
    int clickCount;
    private int laststate[][] = new int[5][5];

    public static void main(String[] args) {
        LineOfAction game = new LineOfAction();
        try {
            game.initializeBoard();
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
        quitButton = new JButton("Play");
        buttonPanel.add(quitButton);
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playAI();
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
            board.whitePiece[i] = new RoundPiece();
            board.blackPiece[i] = new RoundPiece(Color.black);
        }
        //placing white piece
        board.place(board.whitePiece[0], 1, 0);
        board.place(board.whitePiece[1], 2, 0);
        board.place(board.whitePiece[2], 3, 0);
        board.place(board.whitePiece[3], 1, 4);
        board.place(board.whitePiece[4], 2, 4);
        board.place(board.whitePiece[5], 3, 4);

        //placing ai piece
        board.place(board.blackPiece[0], 0, 1);
        board.place(board.blackPiece[1], 0, 2);
        board.place(board.blackPiece[2], 0, 3);
        board.place(board.blackPiece[3], 4, 1);
        board.place(board.blackPiece[4], 4, 2);
        board.place(board.blackPiece[5], 4, 3);

        //assigning pieces to player
        if(colorBlack){
            human = new Player(true,board.blackPiece,colorBlack);
            ai = new Player(false,board.whitePiece,colorBlack);
            human.addPieces(board.blackPiece);
            ai.addPieces(board.whitePiece);
        }

        else{
            human = new Player(true,board.whitePiece,colorBlack);
            ai = new Player(false,board.blackPiece,colorBlack);
            human.addPieces(board.whitePiece);
            ai.addPieces(board.blackPiece);
        }
        board.updatePiecesActions(board.state);
        if(colorBlack){
            playHuman();
        }
        else{
            // playAI();
            //updating states
            board.updatePiecesActions(board.state);
        }

    }


    private void playHuman() {
        if(humanPlayerTurn){
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
                        // selecting new key for move
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
                            if(board.isValidMove(board.state,selectedRow,selectedColumn,newRow,newColumn)){
                                removeHighlight();
                                makeMove(humanPlayerTurn,newRow,newColumn,board.getPiece(selectedRow,selectedColumn));
                                board.updatePiecesActions(board.state);
                                if(board.isWinner(board.state,human.getId())){
                                    System.exit(0);
                                }
                                humanPlayerTurn = false;
                                clickCount = 0;
                                savelastSate(board.state);
                                //check winner here
                            }
                            else{
                                clickCount = 1;
                            }
                        }
                    }

                }
            });
        }
        if(!humanPlayerTurn){
            playAI(); // called for AI's Move
        }
    }

    private void savelastSate(int[][] state) {
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLUMNS;j++){
                laststate[i][j] = board.state[i][j];
            }
        }
    }

    private void makeMove(boolean humanPlayerTurn, int newRow, int newColumn, Piece piece) {
        //cheking is player trying to capture
        Piece newPiece = board.getPiece(newRow,newColumn);
        if(humanPlayerTurn){
            if(ai.hasPiece(newPiece)){
                ai.removePiece(newPiece);
                board.remove(newPiece);
                board.state[newRow][newColumn] = 0;
            }
        }
        else // if AI is trying to capture human piece
        {
            if(human.hasPiece(newPiece)){
                human.removePiece(newPiece);
                board.remove(newPiece);
                board.state[newRow][newColumn] = 0;
            }
        }
        if(humanPlayerTurn){
            board.state[newRow][newColumn] = human.getId(); // returns 1 or -1 depending on color
        }
        else{
            board.state[newRow][newColumn] = ai.getId();
        }
        board.state[piece.getRow()][piece.getColumn()] = 0;
        piece.moveTo(newRow,newColumn);
        board.updatePiecesActions(board.state);

    }

    private void removeHighlight() {
        for(int i=0;i<8;i++){
            board.remove(board.highlight[i]);
        }
    }

    private void playAI() {
        savelastSate(board.state);
        aiFunctions = new AIFunctions(board.state,ai,human);
        int moves[] = aiFunctions.nexBestMove(board.state);
        getLastState(laststate);
        board.updatePiecesActions(board.state);
        int i = moves[0];
        int j = moves[1];
        int newRow = moves[2];
        int newColumn = moves[3];
        Piece piece = board.getPiece(i,j);
        if(newRow>=0){
            makeMove(humanPlayerTurn,newRow,newColumn,piece);
        }
        if(board.isWinner(board.state,ai.getId())){
            System.out.println("AI won");
            System.exit(0);
        }
        humanPlayerTurn = true;
    }

    private void getLastState(int[][] laststate) {
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLUMNS;j++){
                board.state[i][j] = laststate[i][j];
            }
        }
    }


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