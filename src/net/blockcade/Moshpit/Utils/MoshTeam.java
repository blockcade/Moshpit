package net.blockcade.Moshpit.Utils;

import net.blockcade.Arcade.Varables.TeamColors;

public class MoshTeam {

    TeamColors team;
    double score = 0;

    public MoshTeam(TeamColors team){
        this.team=team;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTeam(TeamColors team) {
        this.team = team;
    }

    public void addScore(double score) {
        this.score+=score;
    }

    public int getScore() {
        return (int)Math.round(score);
    }

    public TeamColors getTeam() {
        return team;
    }
}
