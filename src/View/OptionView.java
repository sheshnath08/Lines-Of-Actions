package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sheshnath on 4/19/2016.
 */
public class OptionView extends JFrame implements ActionListener {
    private JRadioButton white;
    private JRadioButton black;
    private JRadioButton fiveXFive;
    private JRadioButton sixXSix;
    private JButton play;
    private ButtonGroup cologrp;
    private ButtonGroup sizegrp;

    public OptionView(){
        white = new JRadioButton("white");
        black = new JRadioButton("black");
        fiveXFive = new JRadioButton("5x5");
        sixXSix = new JRadioButton("6x6");
        play = new JButton("play");
        play.addActionListener(this);
        cologrp = new ButtonGroup();
        sizegrp = new ButtonGroup();
        cologrp.add(white);
        cologrp.add(black);
        sizegrp.add(fiveXFive);
        sizegrp.add(sixXSix);
        this.setSize(100,200);
        this.setLayout( new FlowLayout());
        this.add(white);
        this.add(black);
        white.setSelected(true);
        this.add(fiveXFive);
        this.add(sixXSix);
        fiveXFive.setSelected(true);
    }

    public void showOptionView(){
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
