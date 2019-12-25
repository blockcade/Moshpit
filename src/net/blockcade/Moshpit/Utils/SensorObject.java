package net.blockcade.Moshpit.Utils;

import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.TeamColors;
import net.blockcade.Moshpit.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class SensorObject {

    Entity e;
    boolean destroyed = false;
    boolean marked = false;
    TeamColors team;
    int fov;
    int range;

    EntityType lookingFor;

    public SensorObject(TeamColors team, Entity e, int range, int fov, EntityType lookingFor){
        this.range=range;
        this.fov=fov;
        this.lookingFor=lookingFor;
        new BukkitRunnable(){
            @Override
            public void run() {
                if(destroyed){Main.game.EntityManager().remove(e);cancel();return;}
                if(marked){ParticleUtils.PingLocation(e.getLocation(),team.getColor(),5);}
                Collection<LivingEntity> entities = e.getLocation().getNearbyLivingEntities(range);
                for(Entity e1 : entities){
                    if(!e1.getType().equals(lookingFor))continue;
                    if(!(AngleBetween((ArmorStand)e,e1)<=fov))continue;
                    if(e1 instanceof Player && Main.game.TeamManager().hasTeam((Player)e1) && Main.game.TeamManager().getTeam((Player)e1)==team)continue;
                    destroyed=true;
                    Bukkit.broadcastMessage(Text.format("&c&lSTUNNED &7"+e1.getName()+" has been stunned"));
                    assert e1 instanceof Player;
                    ((Player)e1).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60,10),true);
                }
            }
        }.runTaskTimer(Main.game.handler(),0L,20L);
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public static double AngleBetween(ArmorStand e1, Entity e2){
        return Math.toDegrees(e1.getLocation().getDirection().angle(e2.getLocation().clone().subtract(e1.getEyeLocation()).toVector()));
    }
}
