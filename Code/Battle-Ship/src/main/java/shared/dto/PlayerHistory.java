package shared.dto;

import shared.model.Match;

import java.io.Serializable;
import java.util.List;

public class PlayerHistory implements Serializable {

    private String username;
    private int ranking;
    private int points;
    private int totalWins;
    private int totalLosses;
    private int totalAfk;
    private int totalDraw;

    private List<Match> listMatch;

    public PlayerHistory() {
        super();
    }

    public PlayerHistory(String username, int ranking, int points, int totalWins, int totalLosses, int totalAfk, int totalDraw) {
        this.username = username;
        this.ranking = ranking;
        this.points = points;
        this.totalWins = totalWins;
        this.totalLosses = totalLosses;
        this.totalAfk = totalAfk;
        this.totalDraw = totalDraw;
    }

    public PlayerHistory(int ranking, int points, int totalWins, int totalLosses, int totalAfk, int totalDraw) {
        this.ranking = ranking;
        this.points = points;
        this.totalWins = totalWins;
        this.totalLosses = totalLosses;
        this.totalAfk = totalAfk;
        this.totalDraw = totalDraw;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    public int getTotalAfk() {
        return totalAfk;
    }

    public void setTotalAfk(int totalAfk) {
        this.totalAfk = totalAfk;
    }

    public List<Match> getListMatch() {
        return listMatch;
    }

    public void setListMatch(List<Match> listMatch) {
        this.listMatch = listMatch;
    }

    public int getTotalDraw() {
        return totalDraw;
    }

    public void setTotalDraw(int totalDraw) {
        this.totalDraw = totalDraw;
    }

    @Override
    public String toString() {
        return "PlayerHistory{" + "username=" + username + ", ranking=" + ranking + ", points=" + points + ", totalWins=" + totalWins + ", totalLosses=" + totalLosses + ", totalAfk=" + totalAfk + ", listMatch=" + listMatch + '}';
    }
}
