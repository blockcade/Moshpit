package net.blockcade.Moshpit.Events;

import net.blockcade.Arcade.Managers.EventManager.GameEndEvent;
import net.blockcade.Arcade.Managers.EventManager.GameStartEvent;
import net.blockcade.Arcade.Managers.ScoreboardManager;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Utils.GameUtils.Hologram;
import net.blockcade.Arcade.Utils.JavaUtils;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.TeamColors;
import net.blockcade.Moshpit.Main;
import net.blockcade.Moshpit.Utils.BoundedRegion;
import net.blockcade.Moshpit.Utils.MoshPit;
import net.blockcade.Moshpit.Utils.MoshPlayer;
import net.blockcade.Moshpit.Utils.MoshTeam;
import net.blockcade.Moshpit.Variables.Classes;
import net.blockcade.Moshpit.Variables.Hardpoint;
import net.blockcade.Moshpit.Variables.PointState;
import net.minecraft.server.v1_15_R1.EntityShulker;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static net.blockcade.Arcade.Varables.TeamColors.BLUE;
import static net.blockcade.Arcade.Varables.TeamColors.RED;
import static net.blockcade.Moshpit.Main.game;

public class Game implements Listener {

    private HashMap<Block, EntityShulker> GlowingBlocks = new HashMap<>();

    @EventHandler
    public void GameEndEvent(GameEndEvent e){
        TeamColors winner = MoshPit.DetermineWinner();
        TeamColors looser = winner.equals(RED)?RED:BLUE;

        for(Player player : game.TeamManager().getTeamPlayers(winner)){
            Text.sendMessage(player,"&6VICTORY", Text.MessageType.TITLE);
            Text.sendMessage(player, "&aYou are the WINNER!", Text.MessageType.SUBTITLE);
            Location loc = player.getLocation();
            Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();

            fwm.setPower(2);
            fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

            fw.setFireworkMeta(fwm);
            fw.detonate();
        }
        for(Player player : game.TeamManager().getTeamPlayers(looser)){
            Text.sendMessage(player, "&cYou Lose", Text.MessageType.TITLE);
            Text.sendMessage(player, "&aBetter luck next time", Text.MessageType.SUBTITLE);
            player.playSound(player.getLocation(),Sound.ENTITY_DONKEY_HURT,0.5f,0.5f);
        }
    }

    @EventHandler
    public void GameStartEvent(GameStartEvent event){

        Bukkit.broadcastMessage("Loading Moshpit");

        List<String> spawns = game.handler().getConfig().getStringList("maps."+game.map().getName()+".SPAWN");
        for(int i=0;i<spawns.size();i++){

            String[] args1 = spawns.get(i).split(":");
            Main.spawns.add(new Location(Bukkit.getWorld(args1[0]),Double.parseDouble(args1[1]),Double.parseDouble(args1[2]),Double.parseDouble(args1[3])));
        }


        List<String> pos1 = game.handler().getConfig().getStringList("maps."+game.map().getName()+".POS1");
        List<String> pos2 = game.handler().getConfig().getStringList("maps."+game.map().getName()+".POS2");
        for(int i=0;i<pos1.size();i++){

            String[] args1 = pos1.get(i).split(":");
            String[] args2 = pos2.get(i).split(":");
            BoundedRegion br = new BoundedRegion(
                    new Location(Bukkit.getWorld(args1[0]),Double.parseDouble(args1[1]),Double.parseDouble(args1[2]),Double.parseDouble(args1[3])),
                    new Location(Bukkit.getWorld(args2[0]),Double.parseDouble(args2[1]),Double.parseDouble(args2[2]),Double.parseDouble(args2[3]))
            );
            Hardpoint hp = new Hardpoint();
            Block block = br.locateBlock(Material.CAMPFIRE);
            if(block!=null){
                Campfire cf = ((org.bukkit.block.data.type.Campfire)block.getBlockData());
                cf.setLit(false);
                cf.setWaterlogged(false);
                cf.setSignalFire(true);
                block.setBlockData(cf);
                hp.setFire(block);
            }
            hp.resetTime();
            hp.setLocation(br);
            Main.points.add(hp);
        }

        setPoint(0);

        for(Player player : Bukkit.getOnlinePlayers()){

            MoshPlayer moshPlayer = MoshPlayer.getMoshPlayer(player);

            player.teleport(game.TeamManager().getSpawn(moshPlayer.getTeam()));

            player.getInventory().setArmorContents(game.TeamManager().getArmor(moshPlayer.getTeam()));

            // Scoreboard //
            ScoreboardManager sm = new ScoreboardManager("G" + player.getName(), game);
            sm.setGamePlayer(moshPlayer);
            sm.registerPlaceholder((p)-> String.valueOf(JavaUtils.FormatMS((long)Main.current_point.getTime()*100, JavaUtils.TimeUnit.SECOND)),":POINT_TIME:");
            sm.registerPlaceholder((p)-> Main.current_point.getState().getFormatted(),":POINT_STATE:");
            sm.registerPlaceholder((p)-> Main.teams.containsKey(RED)?String.valueOf(Main.teams.get(RED).getScore()):"",":RED_SCORE:");
            sm.registerPlaceholder((p)-> Main.teams.containsKey(BLUE)?String.valueOf(Main.teams.get(BLUE).getScore()):"",":BLUE_SCORE:");
            sm.enableHealthCounter();
            String name = "  Moshpit  ";
            sm.setDisplayname(name);
            sm.addBlank();

            sm.addLine("&e&lTime left");
            sm.addLine("  &f:POINT_TIME:");
            sm.addBlank();
            sm.addLine("&c&lRED");
            sm.addLine("&r  &f:RED_SCORE: Points");
            sm.addLine("&b&lBLUE");
            sm.addLine("&r&r  &f:BLUE_SCORE: Points");
            sm.addBlank();
            sm.addLine(":POINT_STATE:");
            sm.addBlank();
            sm.addLine("   &dblockcade.net   ");
            sm.addBlank();

            sm.showFor(player);

            Classes.GiveClass(moshPlayer.getKit(),player);
        }

        for(TeamColors team : game.TeamManager().getActive_teams()){
            Main.teams.put(team,new MoshTeam(team));
        }

        if(Main.current_point.getFire()!=null){
            Campfire fire = (Campfire)Main.current_point.getFire().getBlockData();
            fire.setLit(true);
            fire.setSignalFire(true);
            fire.setWaterlogged(false);
        }

        // Main game tick
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!game.GameState().equals(GameState.IN_GAME)){Bukkit.broadcastMessage("Thread exited");cancel();return;}
                if(Main.current_point==null||Main.current_point.getTime()<=0){
                    clearPoint();
                    // Select new Hardpoint
                    int id = Main.points.indexOf(Main.current_point);
                    int selection = id;
                    while (id==selection)
                        selection=new Random().nextInt(Main.points.size());
                    setPoint(new Random().nextInt(Main.points.size()));
                    return;
                }
                if(Main.current_point.getHolding_team()!=null) {
                    Main.current_point.tickTime(1);
                    if(PointPlayerCount()>=3){
                        Main.teams.get(Main.current_point.getHolding_team()).addScore(0.10);
                    }else if(PointPlayerCount()==2){
                        Main.teams.get(Main.current_point.getHolding_team()).addScore(0.05);
                    }else if(PointPlayerCount()==1){
                        Main.teams.get(Main.current_point.getHolding_team()).addScore(0.025);
                    }else {
                        Main.teams.get(Main.current_point.getHolding_team()).addScore(0.025);
                    }
                }

                if(Main.hologram!=null){
                    Main.hologram.editLine(0,Main.current_point.getState().getChatColor()+"HARDPOINT");
                }

                if(Main.teams.get(RED).getScore()>=260||Main.teams.get(BLUE).getScore()==260){
                    // Game Finished
                    if(Main.teams.get(RED).getScore()>=260){
                        game.TeamManager().doEliminateTeam(BLUE);
                    }else {
                        game.TeamManager().doEliminateTeam(RED);
                    }
                    game.stop(true,true);
                }

                Text.sendAll(String.format("&e%s &7: %s", JavaUtils.FormatMS((long)Main.current_point.getTime()*100, JavaUtils.TimeUnit.SECOND),Main.current_point.getState().getFormatted()), Text.MessageType.ACTION_BAR);
            }
        }.runTaskTimer(game.handler(),0L,1L);

    }

    public int PointPlayerCount() {
        int point = 0;
        for(MoshPlayer p : Main.current_point.getLocation().getPlayers()) {
            point++;
        }
        return point;
    }

    public void setPoint(int i){
        Main.current_point=Main.points.get(i);
        Campfire cf = ((org.bukkit.block.data.type.Campfire) Main.current_point.getFire().getBlockData());
        Main.current_point.getFire().getLocation().getWorld().strikeLightning(Main.current_point.getFire().getLocation().add(0.5,0.5,0.5));
        cf.setLit(true);
        cf.setWaterlogged(false);
        cf.setSignalFire(false);
        Main.current_point.getFire().setBlockData(cf);
        Main.hologram=new Hologram(game,Main.current_point.getFire().getLocation().add(0,1,0),
                 Main.current_point.getState().getChatColor()+"Hardpoint"
        );

        Text.sendAll("&a&lHARDPOINT CHANGED&7 The hardpoint has moved to a new location.", Text.MessageType.TEXT_CHAT);
    }

    public void clearPoint() {
        if(Main.current_point!=null){
            Campfire cf = ((org.bukkit.block.data.type.Campfire) Main.current_point.getFire().getBlockData());
            cf.setLit(false);
            cf.setWaterlogged(false);
            cf.setSignalFire(false);
            Main.current_point.getFire().setBlockData(cf);
            Main.current_point.setState(PointState.AVAILABLE);
            Main.current_point.setHolding_team(null);
            Main.current_point.resetTime();
            if(Main.hologram!=null)Main.hologram.remove();
        }
    }
}
