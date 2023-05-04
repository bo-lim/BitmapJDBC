import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {

//        JDBC j;
//        j = new JDBC();
//        j.mkTable("cafe");
//        j.insertManyRecord("cafe");
//        j.mkIndex("cafe","no");

        BitMap b;
        b = new BitMap();
//        b.mkBitMap("cafe","mOption");
//        b.mkBitMap("cafe","mCategory");
        b.andBitMap("cafe","mOption","핫","mCategory","Tea");
//        b.orBitMap("cafe","mOption","핫","mCategory","Tea");

    }

}
