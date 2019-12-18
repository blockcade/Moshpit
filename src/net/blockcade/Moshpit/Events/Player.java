package net.blockcade.Moshpit.Events;

import net.blockcade.Arcade.Managers.EventManager.PlayerRespawnEvent;
import net.blockcade.Arcade.Utils.Formatting.Item;
import net.blockcade.Arcade.Utils.GameUtils.Spectator;
import net.blockcade.Arcade.Varables.TeamColors;
import net.blockcade.Moshpit.Main;
import net.blockcade.Moshpit.Utils.MoshPlayer;
import net.blockcade.Moshpit.Variables.Menus.Classes;
import net.blockcade.Moshpit.Variables.PointState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Random;

public class Player implements Listener {

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e){
        MoshPlayer.getMoshPlayer(e.getPlayer());
        Item item = new Item(Material.DIAMOND_SWORD,"&cClass Selection");
        item.setOnClick((p)->{
            p.openInventory(Classes.getMenu(p));
        });
        e.getPlayer().getInventory().addItem(item.spigot());
    }

    @EventHandler
    public void PlayerMove(PlayerMoveEvent e){
        MoshPlayer player = MoshPlayer.getMoshPlayer(e.getPlayer());
        boolean isSpectator = Spectator.isSpectator(player.getPlayer());
        if(player.getTeam() == null)return;

        if(Main.current_point.getLocation().inRegion(player.getLocation())){
            int opposing_players = 0;
            int friendly_players = 0;
            for(MoshPlayer p : Main.current_point.getLocation().getPlayers()){
                if(!p.getTeam().equals(player.getTeam())){
                    opposing_players++;
                }else {
                    friendly_players++;
                }
            }
            if(opposing_players>=1&&!isSpectator){
                Main.current_point.setHolding_team(null);
                Main.current_point.setState(PointState.CONTESTED);
                return;
            }

            if(Main.current_point.getHolding_team()!=player.getTeam()&&!isSpectator){
                Main.current_point.setHolding_team(player.getTeam());
                Main.current_point.setState(PointState.CAPUTED);
            }

        }else {
            int point = 0;
            TeamColors team = null;
            for(MoshPlayer p : Main.current_point.getLocation().getPlayers()) {
                point++;
                team=p.getTeam();
            }
            if(point==0){
                Main.current_point.setHolding_team(null);
                Main.current_point.setState(PointState.AVAILABLE);
            }else if(!isSpectator) {
                Main.current_point.setHolding_team(team);
                Main.current_point.setState(PointState.CAPUTED);
            }
        }
    }

    @EventHandler
    public void PlayerRespawn(PlayerRespawnEvent e){
        MoshPlayer player = MoshPlayer.getMoshPlayer(e.getPlayer());
        net.blockcade.Moshpit.Variables.Classes.GiveClass(player.getKit(),player.getPlayer());

        Location spawn = Main.spawns.get(new Random().nextInt(Main.spawns.size()));
        ArrayList<Location> possible_points = new ArrayList<>();
        int low_num = Integer.MAX_VALUE;

        for(Location location : Main.spawns){
            int i = 0;
            for(Entity ent : spawn.getWorld().getNearbyEntities(location,20,20,20)){
                if(ent instanceof Player){
                    i++;
                }
            }
            if(i==low_num){
                possible_points.add(location);
            }
            if(i<low_num){
                low_num=i;
                possible_points.clear();
                possible_points.add(location);
            }
        }

        player.teleport(possible_points.get(new Random().nextInt(possible_points.size())));
    }

}
