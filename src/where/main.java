/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package where;

/**
 *
 * @author PAK
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.*;
import org.bukkit.event.EventPriority;

public class main extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();
    private Connection connection;
    public String host,database,username,password,table;
    public int port;
    private ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    @Override
    public void onEnable() {
        this.SetdDefaultConfigMySQL();
        this.openConnection();
        this.createTable();
        this.getServer().getPluginManager().registerEvents(new MysqlWhere(), this);
        console.sendMessage(ChatColor.GREEN + "[Where] Plugin is Enable");
    }

    @Override
    public void onDisable() {

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            console.sendMessage(ChatColor.RED + "[Where]->Connect Database MySQL Error !!");
        }

        console.sendMessage(ChatColor.RED + "[Where] Plugin is Disable");
    }

    public void openConnection() {
        host = config.getString("host");
        database=config.getString("database");
        port = config.getInt("port");
        username=config.getString("username");
        password=config.getString("password");
        table=config.getString("table");
        try {
            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" +host+ ":" +port+ "/" + database,username,password);
            }
        } catch (ClassNotFoundException e) {
            console.sendMessage(ChatColor.RED + "[Where]->Connect Database MySQL Error !!");
        } catch (SQLException e) {
            console.sendMessage(ChatColor.RED + "[Where]->Connect Database MySQL Error !!");
        }
    }
    
    public Connection getConnection(){
        return connection;
    }

    protected void SetdDefaultConfigMySQL() {
        this.saveDefaultConfig();
        config.addDefault("host", "localhost");
        config.addDefault("port", 3306);
        config.addDefault("database", "minecraft");
        config.addDefault("table", "userwhere");
        config.addDefault("username", "root");
        config.addDefault("password", "");
        this.saveDefaultConfig();
    }
    
    
    public void createTable(){
        try{
        PreparedStatement create = getConnection()
                .prepareStatement("CREATE TABLE IF NOT EXISTS "+table+" ("
                        + "id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,"
                        + "uuid VARCHAR(255) NOT NULL,"
                        + "name VARCHAR(255) NOT NULL,"
                        + "server VARCHAR(255) NOT NULL,"
                        + "server_port INT(6))");
                create.executeUpdate();
        }catch (SQLException e){
        e.printStackTrace();
        }
    }
}
