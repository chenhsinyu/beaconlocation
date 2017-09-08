package youten.redo.ble.util;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class JDBC_MySQL {
    Connection conn=null;
    public JDBC_MySQL(String IP, String db, String Encoding, String Account, String Password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String strCon="jdbc:mysql://"+IP+"/"+db+"?useUnicode=true&characterEncoding="+Encoding;
            conn = DriverManager.getConnection(strCon,Account,Password);
//            Class.forName("org.gjt.mm.mysql.Driver");
//            Log.e("url", "jdbc:mysql://" + IP + "/" + db + "?useUnicode=true&characterEncoding=");
//            conn = DriverManager.getConnection("jdbc:mysql://" + IP + ":3306/" + db, Account, Password);

        } catch (ClassNotFoundException | SQLException x) {
            Log.e("", "Exception: " + x.toString());
        }
    }

    public void CreateTable(String table) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(table);
        } catch (Exception e) {
            System.out.println("CreateDB Exception :" + e.toString());
        }
    }

    public void dropTable(String table) {
        try {
            Statement stat = conn.createStatement();
            stat.executeUpdate("DROP TABLE " + table);
        } catch (Exception e) {
            System.out.println("DropDB Exception :" + e.toString());
        }
    }

    public void ShowTable(String table) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from " + table);
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
                System.out.print(rs.getMetaData().getColumnName(i) + "\t");
            System.out.println();
            while (rs.next()) {
//		        System.out.println(rs.getInt("id")+"\t"+rs.getString("rssi")+"\t"+rs.getString("distance")+"\t\t"+rs.getString("MAC"));
                System.out.println(rs.getString("location") + "\t" + rs.getString("timing"));
            }
        } catch (Exception e) {
            System.out.println("/**********************************/");
            System.out.println("Select Exception:" + e.toString());
            System.out.println("/**********************************/");
        }
    }

    public void ExecuteIns(String instruction) {
        instruction = instruction.toLowerCase();
        String strType = "";
        if (instruction.indexOf("update set") != -1)
            strType = "update set";
        else if (instruction.indexOf("insert into") != -1)
            strType = "insert into";
        else if (instruction.indexOf("delete") != -1)
            strType = "delete";
        try {
            Statement statement = conn.createStatement();
//			PreparedStatement statement = conn.prepareStatement(instruction);
//			statement.setString(1, "123456789");
//			statement.setString(2, "William Henry Bill Gates");
//			statement.setString(3, "bill.gates@microsoft.com");
//			statement.setString(4, "bill");
            if (statement.executeUpdate(instruction) > 0)
                Log.e("", strType + " Successfully!");
        } catch (Exception e) {
//			System.out.println("/**********************************/");
            Log.e("", strType + " Exception:" + e.toString());
//			System.out.println("/**********************************/");
        }
    }
}