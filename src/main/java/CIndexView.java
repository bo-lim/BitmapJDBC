import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CIndexView extends JFrame implements ActionListener {
    String tablename;
    JTextField jt_name,jt_col;
    JLabel jl_name,jl_col, jl_col2;
    JButton BTsubmit;
    JDBC j;
    BitMap b;
    CIndexView(String tname){
        super("BitMapIndex");
        tablename = tname;
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,3));

        jl_name = new JLabel("CREATE INDEX ");
        jt_name= new JTextField();

        jl_col = new JLabel("ON "+tablename + " (");
        jt_col = new JTextField();
        jl_col2 = new JLabel(" )");

        BTsubmit = new JButton("CREATE");
        BTsubmit.addActionListener(this);

        add(jl_name);add(jt_name);add(jl_col);add(jt_col);add(jl_col2);add(BTsubmit);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==BTsubmit){
            b = new BitMap();
            b.mkBitMap(tablename, jt_col.getText());
            new MainView(tablename);
            this.setVisible(false);
        }
    }
}
