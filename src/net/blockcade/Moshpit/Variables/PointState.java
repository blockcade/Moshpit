package net.blockcade.Moshpit.Variables;

import net.blockcade.Moshpit.Main;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum PointState {
    AVAILABLE("Available",Color.WHITE),
    CAPUTED("Captured",Color.AQUA),
    CONTESTED("Contested",Color.YELLOW);

    String translation;
    Color color;

    PointState(String translation, Color chatColor){
        this.translation=translation;
        this.color=chatColor;
    }

    public Color getColor() {
        if(this==CAPUTED&&Main.current_point.getHolding_team()!=null)
            return (Main.current_point.getHolding_team().getColor());
        return (color);
    }

    public ChatColor getChatColor() {
        if(this==CAPUTED&&Main.current_point.getHolding_team()!=null)
            return translateColor(Main.current_point.getHolding_team().getColor());
        return translateColor(this.color);
    }

    public String getFormatted() {
        if(this==CAPUTED&&Main.current_point.getHolding_team()!=null)
            return Main.current_point.getHolding_team().getChatColor()+ChatColor.BOLD+translation.toUpperCase();
        return translateColor(this.color)+""+ChatColor.BOLD+translation.toUpperCase();
    }

    public String getTranslation() {
        return translation;
    }

    private ChatColor translateColor(Color cc) {
        ChatColor color=ChatColor.WHITE;
        if(cc.equals(Color.RED))color=ChatColor.RED;
        if(cc.equals(Color.GREEN))color=ChatColor.GREEN;
        if(cc.equals(Color.BLUE))color=ChatColor.BLUE;
        if(cc.equals(Color.YELLOW))color=ChatColor.YELLOW;
        if(cc.equals(Color.AQUA))color=ChatColor.AQUA;
        return color;
    }
}
