import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OutputView extends JFrame implements ActionListener {
    String tablename;
    JButton BTMain;
    JTextArea j_out;
    OutputView(String out_text, String tname){
        super("Output");
        tablename = tname;
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1,2));

        BTMain = new JButton("Main");
        BTMain.addActionListener(this);

        j_out = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(j_out);
        j_out.append(out_text);
        j_out.setEditable(false);
        add(scrollPane);

        add(BTMain);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==BTMain){
            new MainView(tablename);
            this.setVisible(false);
        }
    }
}
