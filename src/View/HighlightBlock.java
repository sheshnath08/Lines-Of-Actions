package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Created by sheshnath on 4/10/2016.
 */
public class HighlightBlock extends Piece {
    
    /**
     * Constructs a <code>HighlightBlock</code>.
     **/
     public HighlightBlock() {
    }
    
    /**
     * Draws this <code>HighlightBlock</code> on the given <code>Graphics</code>.
     * 
     * @param g The graphics on which to draw.
     */
    public void paint(Graphics g, Rectangle r) {
        g.setColor(Color.cyan);
        g.fillRect(r.x, r.y, r.width, r.height);
    }
}