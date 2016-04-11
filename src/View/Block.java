package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Example playing piece. This class is only here as an example,
 * and may be discarded.
 */
public class Block extends Piece {
    
    /**
     * Constructs a <code>Block</code>.
     **/
     Block() {
    }
    
    /**
     * Draws this <code>Block</code> on the given <code>Graphics</code>.
     * 
     * @param g The graphics on which to draw.
     */
    public void paint(Graphics g, Rectangle r) {
        g.setColor(Color.cyan);
        g.fillRect(r.x, r.y, r.width, r.height);
    }
}