package net.blockcade.Moshpit.Utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BoundedRegion {

    private Location pos1;
    private Location pos2;

    // POS1 = FLOOR
    // POS2 = ROOF

    public BoundedRegion(Location pos1, Location pos2) {
        this.pos1=pos1;
        this.pos2=pos2;
    }

    public Block locateBlock(Material material){
        Location point1 = pos1;
        Location point2 = pos2;
        Vector max = Vector.getMaximum(point1.toVector(), point2.toVector());
        Vector min = Vector.getMinimum(point1.toVector(), point2.toVector());
        int count = 0;

        for (int i = min.getBlockX(); i <= max.getBlockX();i++) {
            for (int j = 0; j <= max.getY(); j++) {
                for (int k = min.getBlockZ(); k <= max.getBlockZ();k++) {
                    Block block = pos1.getBlock().getWorld().getBlockAt(i,j,k);
                    count++;
                    if(block.getType().equals(Material.AIR)||block.getType().equals(Material.VOID_AIR))continue;
                    if(block.getType().equals(material))return block;
                }
            }
        }
        Bukkit.broadcastMessage("Found "+count+" blocks");
        return null;
    }

    public List<MoshPlayer> getPlayers() {
        List<MoshPlayer> players = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()){
            if(inRegion(player.getLocation())){
                players.add(MoshPlayer.getMoshPlayer(player));
            }
        }
        return players;
    }

    public boolean inRegion(Location location) {

        if((location.getBlockX() >= pos1.getBlockX() && location.getBlockX() <= pos2.getBlockX()) || (location.getBlockX() <= pos1.getBlockX() && location.getBlockX() >= pos2.getBlockX())){
            if((location.getBlockZ() >= pos1.getBlockZ() && location.getBlockZ() <= pos2.getBlockZ()) || (location.getBlockZ() <= pos1.getBlockZ() && location.getBlockZ() >= pos2.getBlockZ())){
                return true;
            }
        }
        return false;
    }

}