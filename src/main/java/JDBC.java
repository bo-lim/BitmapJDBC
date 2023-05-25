import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.sql.PreparedStatement;

public class JDBC {
    private Statement stmt = null;
    private PreparedStatement pstmt = null;
    ResultSet rs = null;
    int result = 0;
    int cnt = -1;

    String[] ctgrs = {"Coffee", "Beverage", "Tea", "Smoothies"};
    String[][] chcs = {{"아메리카노","카페라떼","바닐라라떼","카푸치노"},{"녹차라떼","밀크티","초코라떼","딸기라떼"},
            {"얼그레이","녹차","페퍼민트","영귤차"},{"딸기스무디","요거트스무디","블루베리스무디","망고스무디"}};
    String[] opts = {"핫", "아이스"};
    Integer[][] prcs = {{3500,4000,4000,4000},{4500,4500,4500,4500},
            {4000,4000,4000,4000},{5000,5000,5000,5000}};

    public static Map optMap = new HashMap<Integer,String>();


    public Connection conDB(){
        Scanner sc = new Scanner(System.in);
        Connection conn = null;
        try {
            // Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC Connection Success");

            conn = DriverManager.getConnection("jdbc:mysql://localhost/cafe", "root", "11111111");
            System.out.println("Connection Success!");

        }catch (ClassNotFoundException e){
            System.out.println("Failed Loading Driver");
        }catch (SQLException e){
            System.out.println("Error "+e);
        }
        return conn;
    }
    public void closeConn(Connection conn){
        try{
            if(conn != null && !conn.isClosed()){
                conn.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void mkTable(String tablename) {
        Connection conn = conDB();

        try {

            stmt = conn.createStatement();
            String sql = "CREATE TABLE " + tablename + "("+
                    "no INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"+
                    "id INT NOT NULL,"+
                    "customerID INT,"+
                    "mCategory VARCHAR(20),"+
                    "mChoice VARCHAR(20),"+
                    "mOption VARCHAR(5),"+
                    "price INT,"+
                    "date TIMESTAMP"+
                    ")";
            System.out.println(sql);

            stmt.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            closeConn(conn);
        }
    }
    public void insertManyRecords(String tablename, int num){
        Connection conn = conDB();

        try{

            Random rand = new Random();
            int j = 250;
            String sql = "INSERT INTO " + tablename + " VALUES( 0, ?, ?, ?, ?, ?, ?, now())";
            pstmt = conn.prepareStatement(sql);
            for(int i=1;i<num;i++) {

                if(i%7!=0){j++;}
                int ctgNum = rand.nextInt(4);
                int chcnum = rand.nextInt(4);
                int optnum = rand.nextInt(2);
                String[] args = {String.valueOf(3001+i),String.valueOf(j),ctgrs[ctgNum],
                        chcs[ctgNum][chcnum],opts[optnum],String.valueOf(prcs[ctgNum][chcnum])};
                insertRecord(args);
            }
            pstmt.close();
            System.out.println("Insert many records");

        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            closeConn(conn);
        }
    }
    private void insertRecord(String[] tArgs){
        try {
            pstmt.setInt(1, Integer.parseInt(tArgs[0]));
            pstmt.setInt(2, Integer.parseInt(tArgs[1]));
            pstmt.setString(3, tArgs[2]);
            pstmt.setString(4, tArgs[3]);
            pstmt.setString(5, tArgs[4]);
            pstmt.setInt(6, Integer.parseInt(tArgs[5]));
            result = pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void insertOneRecord(String tablename, String[] tArgs){
        Connection conn = conDB();
        try{
            String sql = "INSERT INTO " + tablename + " VALUES( 0, ?, ?, ?, ?, ?, ?, now())";
            pstmt = conn.prepareStatement(sql);
            insertRecord(tArgs);
            pstmt.close();
            System.out.println("Insert one record");
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            closeConn(conn);
        }
    }
    public void mkIndex(String tablename, String colname){
        Connection conn = conDB();
        try {
            stmt = conn.createStatement();
            String sql = "CREATE INDEX NIDX ON " + tablename + "("+
                    colname+ ")";
            System.out.println(sql);

            stmt.execute(sql);
            System.out.println("Make INDEX");
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            closeConn(conn);
        }

    }
    public int getTCnt(String tablename){
        Connection conn = conDB();
        try {
            stmt = conn.createStatement();
            String sql = "SELECT COUNT(*) FROM " + tablename;
            System.out.println(sql);

            rs = stmt.executeQuery(sql);
            System.out.println("COUNT ROWS");

            if(rs.next()){cnt = rs.getInt(1);}
            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            closeConn(conn);
        }
        return cnt;
    }
    public int getMapSize(String tablename,String colname){
        return optMap.size();
    }
    public Map getReverseMap(){
        Map reverseMap = new HashMap<String,Integer>();
        for(int i=0;i<optMap.size();i++) {
            reverseMap.put(optMap.get(i),i);
        }
        return reverseMap;
    }
    public Map getMap(){
        return optMap;
    }
    public int[] getmOptions(String tablename,String colname){
        Connection conn = conDB();

        optMap = getUniqVal(tablename,colname);
        System.out.println(optMap);
        int[] resultSet = new int[cnt];
        try {
            stmt = conn.createStatement();
            String sql = "SELECT "+colname+" FROM " + tablename;
            System.out.println(sql);

            rs = stmt.executeQuery(sql);
            System.out.println("COUNT ROWS");

            int idx = 0;
            while(rs.next()){
                for(int i=0;i<optMap.size();i++){
                    if(rs.getString(1).equals(optMap.get(i))) {
                        resultSet[idx++] = i;
                    }
                }

            }
            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            closeConn(conn);
        }
        return resultSet;
    }
    public Map getUniqVal(String tablename, String colname){
        Connection conn = conDB();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT DISTINCT("+colname+") FROM " + tablename;
            rs = stmt.executeQuery(sql);

            int i=0;
            while(rs.next()){
                optMap.put(i++,rs.getString(1));
            }

            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            closeConn(conn);
        }
        return optMap;
    }
    public void getRecords(String tablename, List<Integer> index, String cols){
//        int[] resultSet = new int[cnt];
        Connection conn = conDB();
        try {
            String sql = "SELECT " + cols + " FROM " + tablename + " WHERE no=?";
            pstmt = conn.prepareStatement(sql);
            for(int i=0;i<index.size();i++) {
                pstmt.setInt(1, index.get(i));
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    System.out.println(rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getInt(3) + " " +
                            rs.getString(4) + " " + rs.getString(5) + rs.getString(6) + rs.getInt(7) + rs.getDate(8));

                }
                rs.close();
            }
            pstmt.close();

        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            closeConn(conn);
        }
    }


}
