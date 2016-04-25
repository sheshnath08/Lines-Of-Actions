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
    public JPanel optionPanel;

    public OptionView(){
        optionPanel = new JPanel();
        white = new JRadioButton("white");
        black = new JRadioButton("black");
        fiveXFive = new JRadioButton("5x5");
        sixXSix = new JRadioButton("6x6");
        play = new JButton("play");
        cologrp = new ButtonGroup();
        sizegrp = new ButtonGroup();
        cologrp.add(white);
        cologrp.add(black);
        sizegrp.add(fiveXFive);
        sizegrp.add(sixXSix);
        optionPanel.setSize(200,200);
        optionPanel.setLayout( new FlowLayout());
        optionPanel.add(white);
        optionPanel.add(black);
        white.setSelected(true);
        optionPanel.add(fiveXFive);
        optionPanel.add(sixXSix);
        fiveXFive.setSelected(true);
        play.addActionListener(this);
    }

    public void showOptionView(){
        this.setVisible(true);
    }

    public JPanel getOptionPanel(){
        return optionPanel;
    }
    public void closeOptionView(){
        this.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(white.isSelected()){
            Constants.COLOR = false;
        }
        else{
            Constants.COLOR = true;
        }

        if(fiveXFive.isSelected()){
            Constants.ROWS = 5;
            Constants.COLUMNS = 5;
        }
        else{
            Constants.COLUMNS = 6;
            Constants.ROWS = 6;
        }
        closeOptionView();
    }
}
