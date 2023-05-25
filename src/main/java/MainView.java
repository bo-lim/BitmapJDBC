import javax.management.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame implements ActionListener {
    JTextField jt;
    JButton BTsubmit, OneBT, QueryBT;
    JDBC j;
    String tablename;
    MainView(String tname){
        super("Main");
        tablename = tname;
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1,5));

        JLabel jl = new JLabel("레코드 수 입력 : ");
        jt = new JTextField();
        BTsubmit = new JButton("INSERT");
        OneBT = new JButton("Only 1 RECORD INSERT");
        QueryBT = new JButton("SQL");
        BTsubmit.addActionListener(this);
        OneBT.addActionListener(this);
        QueryBT.addActionListener(this);
//        BTsubmit.addMouseListener(this);
        jt.setText("1000");
        add(jl);
        add(jt);
        add(BTsubmit);
        add(OneBT);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int num = Integer.parseInt(jt.getText());
        if(e.getSource()==BTsubmit){
            j = new JDBC();
            j.insertManyRecords("cafe",num);
            new MainView(tablename);
            this.setVisible(false);
        }
        if(e.getSource()==OneBT){
            new OneRecordView(tablename);
            this.setVisible(false);
        }
        if(e.getSource()== QueryBT){
            new QueryView(tablename);
            this.setVisible(false);
        }
    }
}
