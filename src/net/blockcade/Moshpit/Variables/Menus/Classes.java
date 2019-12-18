package net.blockcade.Moshpit.Variables.Menus;

import net.blockcade.Arcade.Utils.Formatting.Item;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Moshpit.Utils.MoshPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Classes {

    public static Inventory getMenu(Player player){
        Inventory inventory = Bukkit.createInventory(null,27, Text.format("&cClass Selector"));
        int i = 9;
        for(net.blockcade.Moshpit.Variables.Classes.CLASS kit :net.blockcade.Moshpit.Variables.Classes.CLASS.values()){
            if(kit==null)continue;
            MoshPlayer mp = MoshPlayer.getMoshPlayer(player);
            Item item = new Item(kit.getMaterial()!=null?kit.getMaterial(): Material.WHITE_WOOL,kit.getTranslation());
            if(mp.getKit()!=null&&mp.getKit().equals(kit))item.setEnchanted(true);
            item.setOnClick((p)->{
                MoshPlayer moshPlayer = MoshPlayer.getMoshPlayer(p);
                moshPlayer.setKit(kit);
                p.openInventory(getMenu(p));
            });
            inventory.setItem(i,item.spigot());
            i++;
        }

        return inventory;
    }

}
