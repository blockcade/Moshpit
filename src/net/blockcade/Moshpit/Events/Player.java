package net.blockcade.Moshpit.Events;

import net.blockcade.Arcade.Managers.EventManager.PlayerRespawnEvent;
import net.blockcade.Arcade.Utils.Formatting.Item;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Utils.GameUtils.Spectator;
import net.blockcade.Arcade.Utils.JavaUtils;
import net.blockcade.Arcade.Varables.TeamColors;
import net.blockcade.Moshpit.Main;
import net.blockcade.Moshpit.Utils.MoshPlayer;
import net.blockcade.Moshpit.Utils.SensorObject;
import net.blockcade.Moshpit.Variables.Menus.Classes;
import net.blockcade.Moshpit.Variables.PointState;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Player implements Listener {

    public static HashMap<ItemStack, Long> item_delays = new HashMap<>();

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
    public void ItemUse(PlayerInteractEvent e){
        PlayerInventory playerInventory = (e.getPlayer()).getInventory();
        org.bukkit.entity.Player player = e.getPlayer();

        if(playerInventory.getItemInMainHand().getType().equals(Material.PLAYER_HEAD)){
            if(ChatColor.stripColor(playerInventory.getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Stun Trap")){
                // Stun Trap
                Location placeAt;
                if(e.getClickedBlock()==null){
                    placeAt=e.getPlayer().getLocation().toBlockLocation();
                }else {
                    placeAt=e.getClickedBlock().getLocation();
                }

                ArmorStand ent =(ArmorStand) Main.game.EntityManager().CreateEntity(placeAt, EntityType.ARMOR_STAND,"STUN TRAP");
                ent.setSmall(true);
                ent.setGravity(false);
                ent.setMarker(true);
                ent.setCanMove(false);
                ent.setCustomNameVisible(false);
                ent.teleport(Objects.requireNonNull(JavaUtils.getBlockInDirection(JavaUtils.Direction.DOWN, ent.getLocation())).getLocation().add(0,0.5,0));

                SensorObject obj = new SensorObject(Main.game.TeamManager().getTeam(player),ent,100,75,EntityType.PLAYER);
                Main.game.EntityManager().setFunction(ent,(p)->{
                    Text.sendMessage(p,"&c&lMARKED&7 You have marked "+ent.getCustomName()+"&7 for everyone", Text.MessageType.TEXT_CHAT);
                    obj.setMarked(true);
                });
                ent.setHelmet(e.getItem());
                ent.setVisible(false);

                Text.sendMessage(player,"&d&lTRAP ARMED&7 Any crossing player will now set off the STUN TRAP", Text.MessageType.TEXT_CHAT);
            }
        }

        if(playerInventory.getItemInMainHand().getType().equals(Material.POTION)){
            // Grenade
            ItemStack is = playerInventory.getItemInMainHand();
            PotionMeta im = (PotionMeta)is.getItemMeta();
            is.setAmount(is.getAmount()-1);
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK,0.5f,0.5f);
            if(Objects.equals(im.getColor(), Color.RED)){
                // Morphine
                new BukkitRunnable(){
                    double amount = (player.getHealthScale()-player.getHealth())/6;
                    int i = 6;
                    @Override
                    public void run() {
                        i--;
                        if(player.getHealth()>=player.getHealthScale()){cancel();return;}
                        player.setHealth(player.getHealth()+amount);
                        if(i<=0)cancel();
                    }
                }.runTaskTimer(Main.game.handler(),0L,5L);
            }
        }
    }

    @EventHandler
    public void PotionHit(PotionSplashEvent e){
        e.setCancelled(true);
        ItemStack is = e.getPotion().getItem();
        PotionMeta im = (PotionMeta)is.getItemMeta();
        if(im.getColor().equals(Color.BLACK)){
            // Blackout Grenade
            for(Entity ent : e.getEntity().getLocation().getNearbyEntities(5,5,5)){
                if(!(ent instanceof org.bukkit.entity.Player))return;
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) ent;

                if(player.hasLineOfSight(e.getEntity())){
                    PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS,80,4,true,false,false);
                    player.addPotionEffect(pe);
                }
            }
        }else if(im.getColor().equals(Color.LIME)){
            // Sensor Grenade
            for(Entity ent : e.getEntity().getLocation().getNearbyEntities(5,5,5)){
                if(!(ent instanceof org.bukkit.entity.Player))return;
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) ent;

                if(player.hasLineOfSight(e.getEntity())){
                    ent.setGlowing(true);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            ent.setGlowing(false);
                        }
                    }.runTaskLater(Main.game.handler(),100L);
                }
            }
        }
    }

    @EventHandler
    public void BowShoot(EntityShootBowEvent e){
        if(!(e.getEntity() instanceof org.bukkit.entity.Player))return;
        PlayerInventory playerInventory = ((org.bukkit.entity.Player)e.getEntity()).getInventory();
        if(item_delays.containsKey(playerInventory.getItemInMainHand())){
            if(item_delays.get(playerInventory.getItemInMainHand())>=System.currentTimeMillis()){
                item_delays.remove(playerInventory.getItemInMainHand());
                return;
            }
            e.setCancelled(true);
            return;
        }
        item_delays.put(playerInventory.getItemInMainHand(),System.currentTimeMillis()+2000);
    }

    @EventHandler
    public void PlayerRespawn(PlayerRespawnEvent e){
        MoshPlayer player = MoshPlayer.getMoshPlayer(e.getPlayer());
        net.blockcade.Moshpit.Variables.Classes.GiveClass(player.getKit(),player.getPlayer());

        ArrayList<Location> possible_points = new ArrayList<>();
        int low_num = Integer.MAX_VALUE;

        for(Location location : Main.spawns){
            int i = 0;
            if(location.distanceSquared(Main.current_point.getFire().getLocation())<=500)continue;
            for(Entity ent : location.getWorld().getNearbyEntities(location,200,200,200))
                if(ent instanceof Player)
                    i++;

            if(i==low_num){
                possible_points.add(location);
            }
            if(i<low_num){
                low_num=i;
                possible_points.clear();
                possible_points.add(location);
            }
        }

        Location spawn = possible_points.get(new Random().nextInt(possible_points.size()));

        player.teleport(spawn);
    }

    @EventHandler
    public void ArrowLand(ProjectileHitEvent e){
        e.getEntity().remove();
    }

}
