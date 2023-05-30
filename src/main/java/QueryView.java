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
        setLayout(new GridLayout(4,2));

        jl_select = new JLabel("select ");
        jt_select = new JTextField();

        jl_from = new JLabel("from ");
        jt_from = new JTextField();

        jl_where = new JLabel("where ");
        jt_where = new JTextField();

        BTsubmit = new JButton("RUN");
        BTsubmit.addActionListener(this);

        add(jl_select);add(jt_select);add(jl_from);add(jt_from);add(jl_where);
        add(jt_where);add(BTsubmit);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==BTsubmit){
            b = new BitMap();
            String query = jt_where.getText();
            String select = jt_select.getText();
            String output = "";
            if (select.contains("count")){
                if (query.contains("and")) {
                    String[] conds = query.split("and");
                    String[] cond1 = conds[0].trim().split("=");
                    String[] cond2 = conds[1].trim().split("=");
                    output += b.andBitMap4cnt(tablename, select, cond1[0].trim(), cond1[1].trim(), cond2[0].trim(), cond2[1].trim());
                } else {
                    if (query.contains("or")) {
                        String[] conds = query.split("and");
                        String[] cond1 = conds[0].trim().split("=");
                        String[] cond2 = conds[1].trim().split("=");
                        output += b.orBitMap4cnt(tablename, select, cond1[0].trim(), cond1[1].replace('"', ' ').trim(), cond2[0].trim(), cond2[1].replace('"', ' ').trim());
                    }
                    else{ // 조건 하나
                        String[] conds = query.split("and");
                        String[] cond1 = conds[0].trim().split("=");
                        output += b.oneCondBitMap4cnt(tablename, select, cond1[0].trim(), cond1[1].replace('"', ' ').trim());
                    }
                }
            }else{
                if (query.contains("and")) {
                    String[] conds = query.split("and");
                    String[] cond1 = conds[0].trim().split("=");
                    String[] cond2 = conds[1].trim().split("=");
                    output += b.andBitMap(tablename, select, cond1[0].trim(), cond1[1].trim(), cond2[0].trim(), cond2[1].trim());
                } else {
                    if (query.contains("or")) {
                        String[] conds = query.split("and");
                        String[] cond1 = conds[0].trim().split("=");
                        String[] cond2 = conds[1].trim().split("=");
                        output += b.orBitMap(tablename, select, cond1[0].trim(), cond1[1].replace('"', ' ').trim(), cond2[0].trim(), cond2[1].replace('"', ' ').trim());
                    }
                }
            }
            new OutputView(output, tablename);
            this.setVisible(false);
        }
    }
}
