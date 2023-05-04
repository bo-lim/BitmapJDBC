import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.*;

public class BitMap {
    JDBC jdbc = null;
    int total = -1;
    public Map maps = new HashMap<String, Map>();
    public void conJDBC(){
        jdbc = new JDBC();
        jdbc.conDB();
    }
    public void writeFile(char[] BMArr, String filename){
        try {
            PrintWriter pw = new PrintWriter("./" + filename + ".txt");
            pw.print(BMArr);
            pw.close();
        }catch(IOException e){e.printStackTrace();}
    }
    public char[] readFile(String filename){
        String line = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader("./"+filename+".txt"));
            line = br.readLine();
            br.close();
        }catch(IOException e){e.printStackTrace();}
        return line.toCharArray();
    }
    public String[] readMetaFile(String filename){
        String line = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader("./"+filename+".txt"));
            line = br.readLine();
            br.close();
        }catch(IOException e){e.printStackTrace();}
        String[] mapData = line.split(","); // 핫,아이스,
        return mapData;
    }


    public void mkBitMap(String tablename, String colname){
        conJDBC();
        total = jdbc.getTCnt(tablename);

        int[] baseArr = jdbc.getmOptions(tablename,colname);
        Map map = jdbc.getMap();
//        maps.put(colname,map);
        int size = jdbc.getMapSize(tablename,colname);

        String[] BMs = new String[size];
        for(int j=0; j<size;j++){BMs[j]="";}
        for(int i=0;i<total;i++){
            for(int j=0; j<size;j++){
                if(baseArr[i]==j){
                    BMs[j] += '1';
                }else{
                    BMs[j] += '0';
                }
            }
        }
        String text = "";
        for(int i=0;i<map.size();i++) {
            text += map.get(i) + ",";
            writeFile(text.toCharArray(),colname+"_metadata");
        }
        for(int i=0;i<size;i++){
            writeFile(BMs[i].toCharArray(),colname+'_'+Integer.toString(i));
        }
    }

    public void andBitMap(String tablename, String colname1, String cond1 ,String colname2, String cond2){
        String[] col1key = readMetaFile(colname1 + "_metadata");
        String[] col2key = readMetaFile(colname2 + "_metadata");
        List<Integer> result = new ArrayList<Integer>();

//        Object col_num1 = col_map1.get(cond1);
        int col_num1 = Arrays.asList(col1key).indexOf(cond1);
        int col_num2 = Arrays.asList(col2key).indexOf(cond2);
//        int col_num2 = (int) col_map2.get(cond1);

        System.out.println(col_num1);

        char[] bitStream1 = readFile(colname1+"_"+col_num1);
        char[] bitStream2 = readFile(colname2+"_"+col_num2);

        for(int i=0;i<bitStream1.length;i++){
            if(bitStream1[i]=='1' && bitStream2[i]=='1'){
                result.add(i+1);
            }
        }
        getRecord(result);
    }
    public void orBitMap(String tablename, String colname1, String cond1 ,String colname2, String cond2){
        String[] col1key = readMetaFile(colname1 + "_metadata");
        String[] col2key = readMetaFile(colname2 + "_metadata");
        List<Integer> result = new ArrayList<Integer>();

//        Object col_num1 = col_map1.get(cond1);
        int col_num1 = Arrays.asList(col1key).indexOf(cond1);
        int col_num2 = Arrays.asList(col2key).indexOf(cond2);
//        int col_num2 = (int) col_map2.get(cond1);

        System.out.println(col_num1);

        char[] bitStream1 = readFile(colname1+"_"+col_num1);
        char[] bitStream2 = readFile(colname2+"_"+col_num2);

        for(int i=0;i<bitStream1.length;i++){
            if(bitStream1[i]=='1' || bitStream2[i]=='1'){
                result.add(i+1);
            }
        }
        getRecord(result);
    }
    public void getRecord(List<Integer> index){
        conJDBC();
        jdbc.getRecords("cafe", index, "*");
    }
}
