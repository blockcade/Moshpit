package net.blockcade.Moshpit.Utils;

import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Moshpit.Variables.Classes;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MoshPlayer extends GamePlayer {

    private static HashMap<Player, MoshPlayer> players = new HashMap<>();
    private Classes.CLASS kit = Classes.CLASS.GUN_RUNNER;

    public MoshPlayer(Player player) {
        super(player);
        players.put(player,this);
    }

    public Classes.CLASS getKit() {
        return kit;
    }

    public void setKit(Classes.CLASS kit) {
        this.kit = kit;
    }

    public static MoshPlayer getMoshPlayer(GamePlayer player){
        if(!players.containsKey(player.getPlayer()))new MoshPlayer(player.getPlayer());
        return players.get(player.getPlayer());
    }
    public static MoshPlayer getMoshPlayer(Player player){
        if(!players.containsKey(player))new MoshPlayer(player);
        return players.get(player);
    }
}
