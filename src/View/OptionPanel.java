package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OptionPanel {
    private JPanel mainPanel = new JPanel();
    private JRadioButton whiteradio = new JRadioButton("White");
    private JRadioButton blackradio = new JRadioButton("black");
    private ButtonGroup colorButtionGrp = new ButtonGroup();
    private JButton play = new JButton("Start");

    public OptionPanel() {
        mainPanel.setLayout(new FlowLayout());
        play = new JButton("Start");

        mainPanel.add(new JLabel("Select Color"));
        colorButtionGrp.add(whiteradio);
        colorButtionGrp.add(blackradio);
        mainPanel.add(whiteradio);
        mainPanel.add(blackradio);
        mainPanel.add(play);
        whiteradio.setSelected(true);

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
    }

}