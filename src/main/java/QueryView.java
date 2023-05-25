import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueryView extends JFrame implements ActionListener {
    String tablename;
    JTextField jt_select,jt_from, jt_where;
    JLabel jl_select,jl_from, jl_where;
    JButton BTsubmit;
    JDBC j;
    BitMap b;
    QueryView(String tname){
        super("Query");
        tablename = tname;
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,4));

        jl_select = new JLabel("id : ");
        jt_select = new JTextField();

        jl_from = new JLabel("customerID : ");
        jt_from = new JTextField();

        jl_where = new JLabel("mCategory : ");
        jt_where = new JTextField();

        BTsubmit = new JButton("RUN");
        BTsubmit.addActionListener(this);

        add(jl_select);add(jl_from);add(jl_where);
        add(jt_select);add(jt_from);add(jt_where);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
