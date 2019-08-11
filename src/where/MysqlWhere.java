/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package where;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.*;

/**
 *
 * @author PAK
 */
public class MysqlWhere implements Listener {

    main plugin = main.getPlugin(main.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createPlayer(player.getUniqueId(), player);
    }

    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement;
            statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createPlayer(final UUID uuid, Player player) {
        try {
            if (playerExists(uuid) != true) {
                PreparedStatement insert = plugin.getConnection()
                        .prepareStatement("INSERT INTO " + plugin.table + "(uuid,name,server,server_port) VALUE (?,?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setString(3, Bukkit.getServerName());
                insert.setInt(4, Bukkit.getPort());
                insert.executeUpdate();
            }else{
                PreparedStatement update = plugin.getConnection()
                    .prepareStatement("UPDATE "+plugin.table+" SET server = ?,server_port = ? WHERE uuid = ?");
                update.setString(1,Bukkit.getServerName());
                update.setInt(2,Bukkit.getPort());
                update.setString(3,uuid.toString());
                update.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
//     public void createTabel(){
//        try{
//         PreparedStatement create = this.getConnection();
//        }cath(SQLException e){}
//    CREATE TABLE IF NOT EXISTS userwhere (
//id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
//uuid VARCHAR(255) NOT NULL,
//name VARCHAR(255) NOT NULL,
//server VARCHAR(255) NOT NULL,
//server_port INT(6)
//);
//    }
}
