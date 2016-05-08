package AI;

import View.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;


/**
 * Created by sheshnath on 4/27/2016.
 * Class that connects all the components.
 * Run this class to run the game.
 */
public class Controller {
    private static final String INTRO = "intro";
    private static final String GAME = "game";
    private CardLayout cardlayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardlayout);
    private JPanel display;
    private JPanel resultJpanel;
    private JLabel resultLabel;
    private JPanel analysis;
    private JLabel analysisText;
    private OptionPanel optionPanel = new OptionPanel();
    private JButton playAIButton;
    int ROWS =5;
    int COLUMNS =5;
    private Board board = new Board(5,5);
    private int selectedRow;
    private int selectedColumn;
    private int newRow;
    private int newColumn;
    private boolean colorBlack = Constants.COLOR; //human selected black color
    private static boolean humanPlayerTurn = Constants.COLOR;
    private Player human;
    private Player ai;
    private int clickCount;
    private int laststate[][] = new int[5][5];
    private AIFunctions aiFunctions;
    private Thread aithred;
    private Thread humanThread;
    public Controller(){
        mainPanel.add(optionPanel.getMainComponent(), INTRO);
        display = board.getJPanel();
        mainPanel.add(display, GAME);
        resultJpanel = new JPanel();
        resultLabel = new JLabel();
        resultLabel.setText("Result here :");
        resultJpanel.setSize(700,100);
        resultJpanel.add(resultLabel);
        analysis = new JPanel();
        analysisText = new JLabel("Analysis here");
        analysis.add(analysisText);
        analysis.setSize(700,150);
        playAIButton = new JButton("Click here for AI to Play");
        analysis.add(playAIButton);
        optionPanel.addPlayBtnActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionPanel.getValues();
                colorBlack = Constants.COLOR;
                if(colorBlack){
                    humanPlayerTurn = true;
                }
                else{
                    humanPlayerTurn = false;
                }
                cardlayout.show(mainPanel, GAME);
                placePiecesOnBoard();
            }
        });
        playAIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playAIButton.setVisible(false);
                playAI();
            }
        });

    }

    static private void startNewGame() {
        Controller controller = new Controller();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                controller.createAndShowUI(controller);
            }
        });
    }

    private JComponent getMainComponent() {
        return mainPanel;
    }

    /*creates UI*/
    private void createAndShowUI(Controller controller) {
        JFrame frame = new JFrame("Lines of Actions");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(controller.getMainComponent());
        frame.getContentPane().add(resultJpanel, BorderLayout.NORTH);
        frame.getContentPane().add(analysis,BorderLayout.SOUTH);
        frame.pack();
        frame.setSize(700, 750);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        startNewGame();
    }

    public void startGame(){
        playHuman();
        Player player = null;
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                while(!gameOver())
                {
                    for(int i = 0; i < Integer.MAX_VALUE; i++)
                    {
                        if(humanPlayerTurn)
                        {
                            synchronized(player)
                            { //pause
                                try
                                {
                                    humanThread.wait();
                                }
                                catch (InterruptedException e)
                                {
                                }
                            }
                        }
                        // Write to text area
                        System.out.print("Waiting for your Action");
                        // Sleep
                        try
                        {
                            Thread.sleep(500);
                        }
                        catch (InterruptedException e)
                        {
                        }
                    }
                }
            }
        };
        humanThread = new Thread(runnable);
        humanThread.start();

        Runnable aiRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                while(!gameOver())
                {
                    for(int i = 0; i < Integer.MAX_VALUE; i++)
                    {
                        if(!humanPlayerTurn)
                        {
                            synchronized(player)
                            {
                                try
                                {
                                    playAI();
                                    aithred.wait();
                                }
                                catch (InterruptedException e)
                                {
                                }
                            }
                        }
                        // Write to text area
                        System.out.print("Waiting for AI Action");
                        // Sleep
                        try
                        {
                            Thread.sleep(500);
                        }
                        catch (InterruptedException e)
                        {
                        }
                    }
                }
            }
        };
        aithred = new Thread(aiRunnable);
        aithred.start();
    }

   /* placing pieces on the board
   * and initializes players
   * */
    private void placePiecesOnBoard() {
        // Create and place some pieces
        board.state = new int[][]{
                {0, 1, 1, 1, 0},
                {-1, 0, 0, 0, -1},
                {-1, 0, 0, 0, -1},
                {-1, 0, 0, 0, -1},
                {0, 1, 1, 1, 0}
        };


        for (int i = 0; i < 6; i++) {
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
        if (colorBlack) {
            human = new Player(true, board.blackPiece, true);
            ai = new Player(false, board.whitePiece, false);
            human.addPieces(board.blackPiece);
            ai.addPieces(board.whitePiece);
            resultLabel.setText("Your Turn");

        } else {
            human = new Player(true, board.whitePiece, false);
            ai = new Player(false, board.blackPiece, true);
            human.addPieces(board.whitePiece);
            ai.addPieces(board.blackPiece);
            resultLabel.setText("My Turn");
        }
        board.updatePiecesActions(board.state);
        playHuman();
    }

    /*
    * Method for handling human player clicks
    * */
    private boolean playHuman() {
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
                          if(!gameOver())
                            resultLabel.setText("My Turn");
                            playAIButton.setVisible(true);
                        }
                        else{
                            clickCount = 1;
                        }
                    }
                }

            }
        });
        return gameOver();
    }

    /*
    * method to save current state before move.
     */
    private void savelastSate(int[][] state) {
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLUMNS;j++){
                laststate[i][j] = board.state[i][j];
            }
        }
    }

    /*
    * This method moves piece to newrow and new Column
    * */
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

    /*
    * This method to remove highlighted block*/
    private void removeHighlight() {
        for(int i=0;i<8;i++){
            board.remove(board.highlight[i]);
        }
    }

    /*
    * Method that calls alphabeta and AI to play*/
    private boolean playAI() {
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
        String s = "Time:"+(endTime - starttime)/1000+"sec, Depth: "+aiFunctions.depthCount
                +", Node Counted: "+ aiFunctions.nodesCount;
        analysisText.setText(s);
        board.updatePiecesActions(board.state);
        humanPlayerTurn = true;
       /* humanThread.notify();*/
        if(!gameOver())
            resultLabel.setText("Your Turn");
        return gameOver();
    }
    /*
    method to get saved state before last move
    * */
    private void getLastState(int[][] laststate) {
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLUMNS;j++){
                board.state[i][j] = laststate[i][j];
            }
        }
    }

    /*Method to highlight valid location for human player to move*/
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

    /*
    * returns true if gameover else false
    * */
    private boolean gameOver(){
        boolean win = false;
        if(board.isWinner(board.state,ai.getId())){
            win = true;
            resultLabel.setText("You Lost");
            resultJpanel.setBackground(Color.red);
            resultJpanel.setVisible(true);
            System.out.println("You lost");
            // System.exit(0);
        }
        if(board.isWinner(board.state,human.getId())){
            win = true;
            resultLabel.setText("You Won");
            resultJpanel.setBackground(Color.green);
            resultJpanel.setVisible(true);
            System.out.println("You Won");
            // System.exit(0);
        }
        return win;
    }

}
