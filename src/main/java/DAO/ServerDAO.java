package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.conpool;

public class ServerDAO {
	public String checkServer(String jsonstr) throws NamingException, SQLException, ParseException, ClassNotFoundException {
		Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
		try {
            synchronized(this) {
            	conn = conpool.get();
            	String sql = "SELECT SERVER_CODE FROM SERVER_TABLE WHERE USER_ID = ?";
            	stmt = conn.prepareStatement(sql);
            	stmt.setString(1, jsonstr);
            	rs = stmt.executeQuery(); 
            	String str = "";
            	while(rs.next()) {
            		str += rs.getString("SERVER_CODE");
            		str += " ";
            	}
            	
            	return str;
            }
		}finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
	}
}
