package AI;

import View.*;
import javafx.scene.control.RadioButton;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 *
 * Created by sheshnath on 4/10/2016.
 */

public class LineOfAction extends JFrame implements Runnable{
    Board board;
    JPanel buttonPanel;
    JPanel resultJPanel;
    JPanel colorJpanel;
    JPanel display;
    JButton quitButton;
    JRadioButton whiteRadio;
    JRadioButton blackRadio;
    JLabel resultLabel;
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
        resultJPanel = new JPanel();
        resultJPanel.setVisible(false);
        resultLabel = new JLabel();
        resultJPanel.add(resultLabel);
        resultJPanel.setBackground(Color.RED);
        resultJPanel.setForeground(Color.LIGHT_GRAY);
        resultJPanel.setBounds(150,150,200,200);
        getContentPane().add(display, BorderLayout.CENTER);
        // Install button panel
        buttonPanel = new JPanel();
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(resultJPanel, BorderLayout.NORTH);

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
        // Install test button
        quitButton = new JButton("Play");
        buttonPanel.add(quitButton);
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quitButton.setText("Playing");
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
            board.whitePiece[i] = new RoundPiece(Color.WHITE);
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
            human = new Player(true,board.blackPiece,true);
            ai = new Player(false,board.whitePiece,false);
            human.addPieces(board.blackPiece);
            ai.addPieces(board.whitePiece);
        }

        else{
            human = new Player(true,board.whitePiece,false);
            ai = new Player(false,board.blackPiece,true);
            human.addPieces(board.whitePiece);
            ai.addPieces(board.blackPiece);
        }
        board.updatePiecesActions(board.state);
        playHuman();
    }


    private void playHuman() {
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
                            humanPlayerTurn = false;
                            clickCount = 0;
                            savelastSate(board.state);
                        }
                        else{
                            clickCount = 1;
                        }
                    }
                }

            }
        });
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

    private boolean playAI() {
        //removeHighlight();
        savelastSate(board.state);
        aiFunctions = new AIFunctions(board.state,ai,human);
        long starttime = System.currentTimeMillis();
        int moves[] = aiFunctions.alphaBetaSearh(board.state);
        long endTime = System.currentTimeMillis();
        System.out.println("Time Taken: "+(endTime - starttime)/1000);
        getLastState(laststate);
        board.updatePiecesActions(board.state);
        int i = moves[0];
        int j = moves[1];
        int newRow = moves[2];
        int newColumn = moves[3];
        System.out.println("Moved from "+i +","+j+"to "+ newRow +", "+newColumn);

        if(newRow>=0){
            makeMove(humanPlayerTurn,newRow,newColumn,board.getPiece(i,j));
        }
        board.updatePiecesActions(board.state);
        humanPlayerTurn = true;
        if(checkWin()){
            return false;
        }
        return false;
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

    private boolean checkWin(){
        boolean win = false;
        if(board.isWinner(board.state,ai.getId())){
            win = true;
            resultLabel.setText("You Lost");
            resultJPanel.setBackground(Color.red);
            resultJPanel.setVisible(true);
            System.out.println("You lost");
            // System.exit(0);
        }
        if(board.isWinner(board.state,human.getId())){
            win = true;
            resultLabel.setText("You Won");
            resultJPanel.setBackground(Color.green);
            resultJPanel.setVisible(true);
            System.out.println("You Won");
            // System.exit(0);
        }
        return win;
    }

    @Override
    public void run() {
        playAI();
    }
}