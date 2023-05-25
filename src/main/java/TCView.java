import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;

public class TCView extends JFrame implements ActionListener{

    JTextField jt;
    JButton BTsubmit;
    JDBC j;

    public TCView(){
        super("테이블 생성하기");
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3,3));

        JLabel jl = new JLabel("테이블명 입력 : ");
        jt = new JTextField();
        BTsubmit = new JButton("START");
        BTsubmit.addActionListener(this);
//        BTsubmit.addMouseListener(this);
        jt.setText("cafe");
        add(jl);
        add(jt);
        add(BTsubmit);
        setVisible(true);
    }
    public class windowExit extends WindowAdapter{
        public void windowClosing(WindowEvent e){
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String table_name = jt.getText();
        if(e.getSource()==BTsubmit){
            j = new JDBC();
//            j.mkTable(table_name);
            new MainView(table_name);
            this.setVisible(false);
        }
    }
}
