package AI;

import View.Board;
import View.Piece;
import View.Player;
import View.RoundPiece;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Start class class for the board game API.
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
    Piece highlight = new RoundPiece(Color.cyan);

    static boolean turn  = true;
    Player white;
    Player black;
    
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

        //placing black piece
        board.place(blackPiece[0], 0, 1);
        board.place(blackPiece[1], 0, 2);
        board.place(blackPiece[2], 0, 3);
        board.place(blackPiece[3], 4, 1);
        board.place(blackPiece[4], 4, 2);
        board.place(blackPiece[5], 4, 3);

        white = new Player(false,redPiece);
        white.addPieces(redPiece);
        black = new Player(true,blackPiece);
        black.addPieces(blackPiece);


        play();

    }

    private void play() {

        display.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int clickCount = 0;
                if(clickCount == 0){
                    selectedRow = board.getSelectedRow();
                    selectedColumn = board.getSelectedColumn();
                    clickCount++;
                }
                else{
                    newRow = board.getSelectedRow();
                    newColumn = board.getSelectedColumn();
                    clickCount++;

                }
                if(turn) // if Human Players turn
               {
                   clickCount ++;
                   if(white.hasPiece(board.getPiece(selectedRow,selectedColumn))){
                       board.setSelectedPiece(board.getPiece(selectedRow,selectedColumn));
                       if(board.isEmpty(newRow,newColumn) && clickCount == 2){
                           board.getPiece(selectedRow,selectedColumn).moveTo(newRow,newColumn);
                           clickCount = 0;
                       }
                   }

                // showValidMoves(selectedRow,selectedColumn);
               }
                else // AI's Turn
               {

               }
            }
        });
    }

    private void showValidMoves(int selectedRow, int selectedColumn) {

    }


}