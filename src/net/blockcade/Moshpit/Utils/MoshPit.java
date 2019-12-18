package net.blockcade.Moshpit.Utils;

import net.blockcade.Arcade.Managers.EventManager.GameEndEvent;
import net.blockcade.Arcade.Varables.TeamColors;
import net.blockcade.Moshpit.Main;
import org.bukkit.event.EventHandler;

public class MoshPit {

    @EventHandler
    public void GameEnd(GameEndEvent e){

    }

    public static TeamColors DetermineWinner() {

        if(Main.teams.get(TeamColors.RED).getScore()>Main.teams.get(TeamColors.BLUE).getScore()){
            // Red Wins
            return TeamColors.RED;
        }else if(Main.teams.get(TeamColors.RED).getScore()<Main.teams.get(TeamColors.BLUE).getScore()){
            // Red Wins
            return TeamColors.BLUE;
        }else {
            // Tie (I vote red team)
            return TeamColors.RED;
        }

    }

}
