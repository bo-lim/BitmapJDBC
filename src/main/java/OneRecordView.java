import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OneRecordView extends JFrame implements ActionListener {
    JTextField jt_id,jt_cID, jt_mCat, jt_mCho, jt_mOpt, jt_price, jt_date;
    JLabel jl_id,jl_cID, jl_mCat, jl_mCho, jl_mOpt, jl_price, jl_date;
    JButton BTsubmit;
    JDBC j;
    BitMap b;
    String tablename;
    OneRecordView(String tname){
        super("OneRecord");
        tablename = tname;
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8,2));

        jl_id = new JLabel("id : ");
        jt_id = new JTextField();

        jl_cID = new JLabel("customerID : ");
        jt_cID = new JTextField();

        jl_mCat = new JLabel("mCategory : ");
        jt_mCat = new JTextField();

        jl_mCho = new JLabel("mChoice : ");
        jt_mCho = new JTextField();

        jl_mOpt = new JLabel("mOption : ");
        jt_mOpt = new JTextField();

        jl_price = new JLabel("price : ");
        jt_price = new JTextField();

        jl_date = new JLabel("date : ");
        jt_date = new JTextField();
        jt_date.setText("NOW");

        BTsubmit = new JButton("INSERT");
        BTsubmit.addActionListener(this);
        add(jl_id);add(jt_id);add(jl_cID);add(jt_cID);add(jl_mCat);add(jt_mCat);add(jl_mCho);add(jt_mCho);
        add(jl_mOpt);add(jt_mOpt);add(jl_price);add(jt_price);add(jl_date);add(jt_date);add(BTsubmit);

        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==BTsubmit) {
            j = new JDBC();
            b = new BitMap();
            String tmp_id = jt_id.getText();
            String tmp_cid = jt_cID.getText();
            String tmp_ctg = jt_mCat.getText();
            String tmp_cho = jt_mCho.getText();
            String tmp_opt = jt_mOpt.getText();
            String tmp_price = jt_price.getText();
            String[] data = {tmp_id, tmp_cid, tmp_ctg, tmp_cho, tmp_opt, tmp_price};
            j.insertOneRecord(tablename, data);
            b.updateBitmap(data);
            new MainView(tablename);
            this.setVisible(false);
        }
    }
}
