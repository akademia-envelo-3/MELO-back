package pl.envelo.melo.utils;

import org.h2.jdbc.JdbcSQLSyntaxErrorException;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class H2Utils {
    public static void clearDb(DataSource dataSource){
        try {
            Connection c = dataSource.getConnection();
            Statement s = c.createStatement();

            s.execute("SET REFERENTIAL_INTEGRITY FALSE");

            Set<String> tables = new HashSet<String>();
            ResultSet rs = s.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA='PUBLIC'");
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
            rs.close();
            for (String table : tables) {
                s.executeUpdate("TRUNCATE TABLE " + table + " RESTART IDENTITY");
            }
            s.execute("SET REFERENTIAL_INTEGRITY TRUE");
            s.close();
            c.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
