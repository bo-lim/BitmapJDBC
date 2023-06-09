import java.sql.*;
import java.util.*;

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
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/cafe", "root", "11111111");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
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
            stmt.execute(sql);
        }catch(SQLException e) {
            System.out.println("이미 생성된 테이블");
        }finally {
            closeConn(conn);
        }
    }
    public void insertManyRecords(String tablename, int num){
        Connection conn = conDB();
        try{
            Random rand = new Random();
            int rand_id = rand.nextInt(1000000);
            int j = rand.nextInt(1000000);
            String sql = "INSERT INTO " + tablename + " VALUES( 0, ?, ?, ?, ?, ?, ?, now())";
            pstmt = conn.prepareStatement(sql);
            for(int i=1;i<=num;i++) {
                if(i%7!=0){j++;}
                int ctgNum = rand.nextInt(4);
                int chcnum = rand.nextInt(4);
                int optnum = rand.nextInt(2);
                String[] args = {String.valueOf(rand_id+i),String.valueOf(j),ctgrs[ctgNum],
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
            stmt.execute(sql);
        }catch(SQLException e) {
            System.out.println("인덱스 이미 존재");
        }finally {
            closeConn(conn);
        }
    }
    public int getTCnt(String tablename){
        Connection conn = conDB();
        try {
            stmt = conn.createStatement();
            String sql = "SELECT COUNT(*) FROM " + tablename;
            rs = stmt.executeQuery(sql);
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
    public Map getMap(){
        return optMap;
    }
    public int[] getmOptions(String tablename,String colname){
        Connection conn = conDB();

        optMap = getUniqVal(tablename,colname);
        int[] resultSet = new int[cnt];
        try {
            stmt = conn.createStatement();
            String sql = "SELECT "+colname+" FROM " + tablename + " order by no";
            rs = stmt.executeQuery(sql);

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
    public String getRecords(String tablename, List<Integer> index, String cols){
        Connection conn = conDB();
        String out = "";
        try {
            String sql = "SELECT " + cols + " FROM " + tablename + " WHERE no=?";
            pstmt = conn.prepareStatement(sql);
            for(int i=0;i<index.size();i++) {
                pstmt.setInt(1, index.get(i));
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (cols.trim().equals("*")) {
                        out += rs.getInt(2) + " " + rs.getInt(3) + " " +
                                rs.getString(4) + " " + rs.getString(5) + " " +
                                rs.getString(6) + " " + rs.getInt(7) + " " + rs.getString(8) + "\n";
                    }
                    else{
                        out += "\n";
                        String[] col_list = cols.split(",");
                        for(int c=0;c<col_list.length;c++) {
                            col_list[c] = col_list[c].trim();
                            out += rs.getString(c+1) + " ";
                        }
                    }
                }
                rs.close();
            }
            pstmt.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }finally {
            closeConn(conn);
            return out;
        }
    }
}
