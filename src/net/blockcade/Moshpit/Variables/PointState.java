package net.blockcade.Moshpit.Variables;

import net.blockcade.Moshpit.Main;
import org.bukkit.ChatColor;

public enum PointState {
    AVAILABLE("Available",ChatColor.WHITE),
    CAPUTED("Captured",ChatColor.AQUA),
    CONTESTED("Contested",ChatColor.YELLOW);

    String translation;
    ChatColor chatColor;

    PointState(String translation, ChatColor chatColor){
        this.translation=translation;
        this.chatColor=chatColor;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public String getFormatted() {
        if(this==CAPUTED&&Main.current_point.getHolding_team()!=null)
            return Main.current_point.getHolding_team().getChatColor()+translation;
        return chatColor+translation;
    }

    public String getTranslation() {
        return translation;
    }
}
