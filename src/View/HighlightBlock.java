package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Created by sheshnath on 4/10/2016.
 */
public class HighlightBlock extends Piece {

     public HighlightBlock() {
    }
    

    public void paint(Graphics g, Rectangle r) {
        g.setColor(Color.cyan);
        g.fillRect(r.x, r.y, r.width/2, r.height/2);
    }
}