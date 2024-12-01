package server.dao;

import shared.dto.PlayerHistory;
import shared.model.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO extends DAO {

    public PlayerDAO() {
        super();
    }

    public Player getPlayer(String username) {
        Player player = null;
        String sql = "SELECT * FROM players WHERE username = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                player = new Player();
                player.setUsername(rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return player;
    }
    
    public void CreateAccount(Player player){
        String sql = "INSERT INTO players (username, password, points, total_wins, total_losses, total_afk, total_draw) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, player.getUsername());
            ps.setString(2, player.getPassword());
            ps.setString(3, Integer.toString(player.getPoints()));
            ps.setString(4, Integer.toString(player.getTotalWins()));
            ps.setString(5, Integer.toString(player.getTotalLosses()));
            ps.setString(6, Integer.toString(player.getTotalAfk()));
            ps.setString(7, Integer.toString(player.getTotalDraw()));
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Player> getAllUser(){
        ArrayList<Player> list = new ArrayList<>();

        String sql = "SELECT * FROM players";
        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                Player player = new Player();
                player.setUsername(username);
                list.add(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean checkExistAccount(Player player){
        boolean result = false;
        String sql = "SELECT username FROM players WHERE username = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, player.getUsername());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public String checkLogin(Player player) {
        boolean result = false;
        System.out.println(player.getUsername() + " " + player.getPassword());
        String sql = "SELECT username, password, points FROM players WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, player.getUsername());
            ps.setString(2, player.getPassword());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return String.valueOf(rs.getString("points"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    public boolean updateAfk(String username) {
        String sql = "UPDATE players SET total_afk = total_afk + 1, points = points - 1 WHERE username = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDraw(String username) {
        String sql = "UPDATE players SET total_draw = total_draw + 1 WHERE username = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateWin(String username) {
        String sql = "UPDATE players SET total_wins = total_wins + 1, points = points + 1 WHERE username = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateLoss(String username) {
        String sql = "UPDATE players SET total_losses = total_losses + 1 WHERE username = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }    // Lấy thông tin người chơi

    public PlayerHistory getPlayerInfo(String username) {
        PlayerHistory playerHistory = null;
        String sql = "SELECT points, total_wins, total_losses, total_afk, total_draw FROM players WHERE username = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int points = rs.getInt("points");
                int totalWins = rs.getInt("total_wins");
                int totalLosses = rs.getInt("total_losses");
                int totalAfk = rs.getInt("total_afk");
                int totalDraw = rs.getInt("total_draw");
                int ranking = calculateRanking(username);
                playerHistory = new PlayerHistory(ranking, points, totalWins, totalLosses, totalAfk, totalDraw);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerHistory;
    }

    // Tính toán thứ hạng người chơi dựa trên điểm và số lần AFK
    private int calculateRanking(String username) {
        String sql = "SELECT username FROM players ORDER BY points DESC, total_afk ASC";
        int rank = 1;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("username").equals(username)) {
                    break;
                }
                rank++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rank;
    }

    // Lấy danh sách bảng xếp hạng tất cả người chơi
    public List<PlayerHistory> getLeaderboard() {
        List<PlayerHistory> leaderboard = new ArrayList<>();
        String sql = "SELECT username, points, total_wins, total_losses, total_afk, total_draw FROM players ORDER BY points DESC, total_afk ASC, total_wins DESC, total_losses DESC";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                String username = rs.getString("username");
                int points = rs.getInt("points");
                int totalWins = rs.getInt("total_wins");
                int totalLosses = rs.getInt("total_losses");
                int totalAfk = rs.getInt("total_afk");
                int totalDraw = rs.getInt("total_draw");
                leaderboard.add(new PlayerHistory(username, rank, points, totalWins, totalLosses, totalAfk, totalDraw));
                rank++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leaderboard;
    }
}
