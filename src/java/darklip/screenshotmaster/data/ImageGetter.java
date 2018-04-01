/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package darklip.screenshotmaster.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * It gets image URLs from server via database.
 *
 * @author Darklip
 */
public class ImageGetter {

    private String 
            imgName = "",
            imgFormat = "",
            errorMsg = "No errors";
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet rs;
    
    /**
     * Executes a query. Writes 
     * @param id
     * @return 
     */
    public String getImageFromDb(int id) {
        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setURL("jdbc:mysql://localhost:3306/screenshotmaker");
            dataSource.setUser("root");
            dataSource.setPassword("");
            connection = dataSource.getConnection();
        } catch (SQLException ex) {
            errorMsg = ErrorConstants.ERROR_CONNECTION_FAILED;
        }
        try {
            statement = connection.prepareStatement(
                    "SELECT * \n" +
                    "FROM images\n" +
                    "WHERE id = ?"
            );
            statement.setInt(1, id);
            statement.executeQuery();
            rs = statement.getResultSet();
            rs.next();
            imgName = rs.getString("filename");
            imgFormat = rs.getString("format");
        } catch (SQLException ex) {
            errorMsg = ErrorConstants.ERROR_NOT_FOUND;
        } catch (Exception ex) {
            errorMsg = ex.getMessage();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
            }
        }

        return imgName + '.' + imgFormat;
    }
    
    /**
     * Returns name of last found image or last error message. If didn't get image from DB, returns "Unknown".
     * @return Sring that represents img name WITHOUT file format or an error message.
     */
    public String getImgName() {
        return imgName;
    }
    
    /**
     * Returns format of last found image. If didn't get image from DB, returns an empty string.
     * @return Sring that represents img format: jpg, png, gif, bmp.
     */
    public String getImgFormat() {
        return imgFormat;
    }
    
    /**
     * Returns format of last found image. If didn't get image from DB, returns an empty string.
     * @return Sring that represents img format: jpg, png, gif, bmp.
     */
    public String getError() {
        return errorMsg;
    }
}
