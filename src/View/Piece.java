package View;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;


public abstract class Piece extends Observable {
    protected Board board;
    private int row = -1;
    private int column = -1;
    private boolean selectable = true;
    private int x;
    private int y;
    private int speed = -1; // Negative means to use Board default
    private static final int PAUSE_MS = 50;
    private static final int FRAME_RATE = 1000 / PAUSE_MS;
    private static int state[][];
    public ArrayList<int[]> action = new ArrayList<int[]>();

    /**
     * Created by sheshnath on 4/10/2016.
     */

    public Piece() {

    }

    /**
     * @return The board (if any) containing this piece.
     */
    public Board getBoard() {
        return board;
    }
    
    /**
     * Returns a printable String representing this Piece.
     * 
     * @return A printable representation of this Piece.
     */
    public String toString() {
        return getClass().getName() + "[" + row + "][" + column + "]";
    }

    public ArrayList<int[]> getAction(){
        return action;
    }

    /**
     * Determines whether the piece can be selected by the mouse.
     * 
    */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * Returns <code>true</code> if the piece can be selected by the mouse.
     * 
     * @return <code>true</code> if the piece is selectable.
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * Returns the x-coordinate at which this piece will be painted.
     * 
     * @return The x-coordinate.
     */
    protected int getX() {
        return board.columnToX(column);
    }

    /**
     * Returns the y-coordinate at which this piece will be painted.
     * 
     * @return The y-coordinate.
     */
    protected int getY() {
        return board.rowToY(row);
    }

    /**
     * Returns the rectangle in which this piece should be painted.
     * 
     * @return The rectangle in which to paint this piece.
     */
    public Rectangle getRectangle() {
        int x = getX() + 1;
        int y = getY() + 1;
        int width;
        int height;
        width = board.columnToX(column + 1) - x;
        height = board.rowToY(row + 1) - y;

        return new Rectangle(x, y, width, height);
    }

    /**
     * Returns the row that this piece is in, or -1 if this piece is not on the
     * board.
     * 
     * @return The row number.
     */
    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }


    protected boolean atLegalPosition() {
        return board.isLegalPosition(row, column);
    }


    public void place(Board board, int row, int column) {
        board.place(this, row, column);
    }
    

    protected void placeHelper(Board board, int row, int column) {
        this.board = board;
        this.row = row;
        this.column = column;
        x = board.columnToX(column);
        y = board.rowToY(row);
        addObserver(board);
        redraw(getRectangle());
    }

    /**
     * Removes this piece from whatever board it may be on,
     * but does not delete the piece itself.
     */
    public void remove() {
        if (!atLegalPosition())
            return;
        board.remove(this);
    }
    
    /** For internal use only! */
    protected void removeHelper() {
        redraw();
        board = null;
        row = column = -1;
    }

    /**
     * @return True if this piece is currently on the board.
     */
    protected boolean isOnBoard() {
        return board != null;
    }

    /**
     * Sets the speed of movement (in squares/second) for this piece. Values of
     * 1 to 10 are reasonable; very large values will cause this piece to
     * "teleport" to the new location; a zero or negative value will cause the
     * default speed (set by the board) to be used.
     * 
     * @param speed
     *        The desired speed of movement, in pixels/redraw.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
        if (speed <= 0)
            speed = board.getSpeed();
    }

    /**
     * Get the speed (in pixels/redraw) at which this piece will move.
     * 
     * @return This Piece's speed of movement, in pixels/redraw.
     */
    public int getSpeed() {
        if (speed <= 0)
            if (board != null) {
                return board.getSpeed();
            } else {
                return 0;
            }
        else
            return speed;
    }

    /**
     * Paints this piece on the board within the given rectangle;
     * must be implemented by a subclass.
     * 
     * @param g The Graphics object on which painting should be done.
     * @param r The rectangle in which to paint this piece.
     */
    public abstract void paint(Graphics g, Rectangle r);
    
    /**
     * Ensures that this piece will be drawn on top of any other pieces
     * in the same array location.
     */
    public void moveToTop() {
        if (board != null) {
            board.moveToTop(this);
        }
    }

    /**
     * Moves this piece the given number of rows and columns.
     * 
     * @param deltaRow
     *        The number of rows down to move this piece; a negative number will
     *        move the piece up.
     * @param deltaColumn
     *        The number of columns to move this piece to the right; a negative
     *        number will move the piece left.
     * @return False if the move would take the piece outside the boundaries of
     *         the board.
     */
    public boolean move(int deltaRow, int deltaColumn) {
        return moveTo(row + deltaRow, column + deltaColumn);
    }

    /**
     * Moves this piece to a new position on the board.
     * 
     * @param newRow
     *        The destination row.
     * @param newColumn
     *        The destination column.
     * @return <code>false</code> if the destination is not a legal board
     *         position, or if the piece is already moving.
     */
    public boolean moveTo(int newRow, int newColumn) {
        if (!board.isLegalPosition(newRow, newColumn))
            return false;
        int startX = board.columnToX(column);
        int startY = board.rowToY(row);
        int finishX = board.columnToX(newColumn);
        int finishY = board.rowToY(newRow);
        int changeInX = finishX - startX;
        int changeInY = finishY - startY;
        Rectangle oldRect = getRectangle();
        Rectangle newRect = new Rectangle(oldRect);

        // compensate for squares being slightly different sizes
        oldRect.width += 2;
        newRect.width += 2;
        oldRect.height += 2;
        newRect.height += 2;

        // Move smoothly towards new position
        board.moveToTop(this);
        int deltaRow = Math.abs(row - newRow);
        int deltaColumn = Math.abs(column - newColumn);
        int distance = Math.max(deltaRow, deltaColumn)
                - Math.min(deltaRow, deltaColumn) / 2;
        int numberOfSteps = distance * FRAME_RATE / getSpeed();

        for (int i = 1; i <= numberOfSteps; i++) {
            oldRect.x = x;
            oldRect.y = y;
            x = startX + (i * changeInX) / numberOfSteps;
            y = startY + (i * changeInY) / numberOfSteps;
            newRect.x = x;
            newRect.y = y;
            redraw(oldRect.union(newRect));
        }

        if (canMoveTo(newRow, newColumn)) {
            changePosition(newRow, newColumn);
        }
        redraw(oldRect.union(newRect));
        return true;
    }

    /**
     * Determines whether this piece can be moved to the specified
     * location. This method returns <code>true</code> if this
     * piece is on a board and the location is legal for that board.
     * This method can be overridden with more specific tests.
     * 
     * @param deltaRow The distance down to the desired row.
     * @param deltaColumn The distance right to the desired column.
     * @return <code>true</code> if the move can be made.
     */
    public boolean canMove(int deltaRow, int deltaColumn) {
        return canMoveTo(row + deltaRow, column + deltaColumn);
    }
    
    /**
     */
    public boolean canMoveTo(int newRow, int newColumn) {
        if (board == null) return false;
        else return board.isLegalPosition(newRow, newColumn);
    }

    /**
     * Change the position of this piece on the board.
     *
     */
    private void changePosition(int newRow, int newColumn) {
        if (!atLegalPosition())
            return;
        int oldRow = row;
        int oldColumn = column;
        board.changePositionOnBoard(this, oldRow, oldColumn, newRow, newColumn);
        row = newRow;
        column = newColumn;
        x = board.columnToX(column);
        y = board.rowToY(row);
        redraw();
    }

    /**
     * Causes this piece to be redrawn.
     */
    public void redraw() {
        redraw(getRectangle());
    }

    /**
     * Causes the given rectangle to be redrawn.
     */
    public void redraw(Rectangle rect) {
        setChanged();
        notifyObservers(rect);
        try {
            Thread.sleep(PAUSE_MS);
        }
        catch (InterruptedException e) {}
    }



    private Rectangle enlarge(Rectangle r) {
        r.x -= 2;
        r.y -= 2;
        r.width += 4;
        r.height += 4;
        return r;
    }

    public void updateAction(){
        int rowSum = board.rowSum(row,column);
        int colSum = board.colSum(row,column);
        int dg1Sum = board.dg1Sum(row,column);
        int dg2Sum = board.dg2Sum(row,column);
        int move[] = new int[2]; // to store action's row,column
        //moving up
        if(board.isValidMove(row,column,row+colSum,column)){
            move[0] = row+colSum;
            move[1] = column;
        }
        else{
            move[0] = -1;
            move[1] = -1;
        }
        action.add(move);
        move = new int[2];
        //down
        if(board.isValidMove(row,column,row-colSum,column)){
            move[0] = row-colSum;
            move[1] = column;
        }
        else{
            move[0] = -1;
            move[1] = -1;
        }
        action.add(move);
        move = new int[2];
        //moving right
        if(board.isValidMove(row,column,row,column+rowSum)){
            move[0] = row;
            move[1] = column+rowSum;
        }
        else{
            move[0] = -1;
            move[1] = -1;
        }
        action.add(move);
        move = new int[2];
        // moving left
        if(board.isValidMove(row,column,row,column-rowSum)){
            move[0] = row;
            move[1] = column-rowSum;
        }
        else{
            move[0] = -1;
            move[1] = -1;
        }
        action.add(move);
        move = new int[2];
        //moving right up
        if(board.isValidMove(row,column,row+dg1Sum,column+dg1Sum)){
            move[0] = row+dg1Sum;
            move[1] = column+dg1Sum;
        }
        else{
            move[0] = -1;
            move[1] = -1;
        }
        action.add(move);
        move = new int[2];
        //left down
        if(board.isValidMove(row,column,row-dg1Sum,column-dg1Sum)){
            move[0] = row-dg1Sum;
            move[1] = column-dg1Sum;
        }
        else{
            move[0] = -1;
            move[1] = -1;
        }
        action.add(move);
        move = new int[2];
        //moving left up
        if(board.isValidMove(row,column,row-dg2Sum,column+dg2Sum)){
            move[0] = row-dg2Sum;
            move[1] = column+dg2Sum;
        }
        else{
            move[0] = -1;
            move[1] = -1;
        }
        action.add(move);
        move = new int[2];
        // moving left
        if(board.isValidMove(row,column,row+dg2Sum,column-dg2Sum)){
            move[0] = row+dg2Sum;
            move[1] = column-dg2Sum;
        }
        else{
            move[0] = -1;
            move[1] = -1;
        }
        action.add(move);
    }

}