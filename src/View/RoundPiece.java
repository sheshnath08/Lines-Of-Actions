package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Example playing piece. This class is only here as an example,
 * and may be discarded.
 */
public class RoundPiece extends Piece {
    
    public Color color = Color.red;
    

    public RoundPiece() {
    }
    
    /**
     * Constructs a <code>RoundPiece</code> of the given color.
     * 
     * @param color The <code>Color</code> of the new piece.
     **/
    public RoundPiece(Color color) {
        this.color = color;
    }
    
    /**
     * Draws this <code>RoundPiece</code> on the given <code>Graphics</code>.
     * Drawing should be limited to the provided <code>java.awt.Rectangle</code>.
     * 
     * @param g The graphics on which to draw.
     * @param r The rectangle in which to draw.
     */
    public void paint(Graphics g, Rectangle r) {
        Color oldColor = g.getColor();
        g.setColor(color);
        g.fillOval(r.x, r.y, r.width, r.height);
        if (this.equals(board.getSelectedPiece())) {
            g.setColor(Color.BLACK);
            g.drawOval(r.x, r.y, r.width, r.height);
        }
        g.setColor(oldColor);
    }
}