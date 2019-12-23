package net.blockcade.Moshpit.Utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleUtils {

    public static void PingLocation(Location location, Color color, int height){
        for(double i=0;i<height;i=i+0.5)
            PlayParticle(location.clone().add(0,i,0),color);
    }

    private static void PlayParticle(Location location, Color color){
        location.getWorld().spawnParticle(Particle.REDSTONE,location,1,new Particle.DustOptions(color,1));
    }
}
