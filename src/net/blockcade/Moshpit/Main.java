package net.blockcade.Moshpit;

import net.blockcade.Arcade.Commands.GameCommand;
import net.blockcade.Arcade.Commands.debug;
import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Utils.GameUtils.Hologram;
import net.blockcade.Arcade.Utils.GameUtils.Spectator;
import net.blockcade.Arcade.Varables.*;
import net.blockcade.Moshpit.Utils.MoshTeam;
import net.blockcade.Moshpit.Variables.GameMode;
import net.blockcade.Moshpit.Variables.Hardpoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    public static Game game;
    public static GameMode gameMode;

    public static int id = 0;

    public static Hardpoint current_point = null;
    public static Hologram hologram;

    // All fields work from this (SECONDS)
    public static int BASE_POINT_TIME = 30;

    public static List<Hardpoint> points=new ArrayList<>();
    public static HashMap<TeamColors, MoshTeam> teams = new HashMap<>();

    public static List<Location> spawns = new ArrayList<>();

    @Override
    public void onEnable() {
        Game game = new Game(GameName.TESTING.getName(), GameType.CUSTOM,6,12,Main.getPlugin(net.blockcade.Arcade.Main.class), Bukkit.getWorlds().get(0));
        game.TeamManager().setMaxTeams(2);
        game.setMaxDamageHeight(512);
        game.setGameName(GameName.TESTING);
        getCommand("game").setExecutor(new GameCommand(this, game));
        net.blockcade.Arcade.Main.getPlugin(net.blockcade.Arcade.Main.class).getCommand("debug").setExecutor(new debug(game));

        gameMode=GameMode.HARDPOINT;

        Main.game=game;

        /*
         * Register Handlers
         */
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new GamePlayer(game,false), game.handler());
        pm.registerEvents(new net.blockcade.Moshpit.Events.Game(),game.handler());
        pm.registerEvents(new net.blockcade.Moshpit.Events.Player(),game.handler());

        game.setModule(GameModule.START_MECHANISM,true);
        game.setModule(GameModule.TEAMS,true);
        game.setModule(GameModule.CHEST_BLOCK,false);
        game.setModule(GameModule.DEATH_MANAGER,true);
        game.setModule(GameModule.NO_TOOL_DROP,true);
        game.setModule(GameModule.BLOCK_PLACEMENT,false);
        game.setModule(GameModule.BLOCK_ROLLBACK,false);

        game.setModule(GameModule.NO_CRAFTING,true);
        game.setModule(GameModule.NO_SMELTING,true);
        game.setModule(GameModule.NO_WEATHER_CHANGE,true);
        game.setModule(GameModule.NO_HUNGER,true);

        game.setModule(GameModule.VOID_DEATH,true);
        game.setModule(GameModule.ALLSTATE_JOIN,false);
        game.setModule(GameModule.ERROR_CATCHER,true);
        game.setModule(GameModule.CHAT_MANAGER,true);

        game.map().setTime(1);
        game.map().setWeatherDuration(0);
        Spectator.setSpectatorTime(2);


        new BukkitRunnable() {
            @Override
            public void run() {
                game.GameState(GameState.IN_LOBBY);
            }
        }.runTaskLater(this, 60L);
    }

}
