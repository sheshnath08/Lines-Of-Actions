package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by sheshnath on 4/27/2016.
 */
public class OptionPanel {
    private JPanel mainPanel = new JPanel();
    private JRadioButton whiteradio = new JRadioButton("White");
    private JRadioButton blackradio = new JRadioButton("black");
    private ButtonGroup colorButtionGrp = new ButtonGroup();

    private JRadioButton easyLevel = new JRadioButton("Easy");
    private JRadioButton mediumLevel = new JRadioButton("Medium");
    private JRadioButton difficultLevel = new JRadioButton("Hard");
    private ButtonGroup levelButtionGrp = new ButtonGroup();

    private JButton play = new JButton("Start");

    public OptionPanel() {
        mainPanel.setLayout(new FlowLayout());
        play = new JButton("Start");

        mainPanel.add(new JLabel("Select Color"));
        colorButtionGrp.add(whiteradio);
        colorButtionGrp.add(blackradio);
        mainPanel.add(whiteradio);
        mainPanel.add(blackradio);
        mainPanel.add(new JLabel("|| Select Difficulty Level:"));
        mainPanel.add(easyLevel,BorderLayout.CENTER);
        mainPanel.add(mediumLevel,BorderLayout.CENTER);
        mainPanel.add(difficultLevel,BorderLayout.CENTER);
        mainPanel.add(play,BorderLayout.SOUTH);
        levelButtionGrp.add(easyLevel);
        levelButtionGrp.add(mediumLevel);
        levelButtionGrp.add(difficultLevel);
        whiteradio.setSelected(true);
        mediumLevel.setSelected(true);
    }

    public void addPlayBtnActionListener(ActionListener listener) {
        getValues();
        play.addActionListener(listener);
    }

    public JComponent getMainComponent() {
        return mainPanel;
    }

    public void getValues(){
        if(whiteradio.isSelected()){
            Constants.COLOR = false;
        }
        else
        {
            Constants.COLOR = true;
        }

        if(easyLevel.isSelected()){
            Constants.depthLevel =53;
        }
        else if(mediumLevel.isSelected())
        {
            Constants.depthLevel = 7;
        }
        else{
            Constants.depthLevel = 9;
        }
    }

}