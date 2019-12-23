package net.blockcade.Moshpit.Variables;

import net.blockcade.Arcade.Utils.Formatting.Item;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Moshpit.Main;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;

public class Classes {

    public static enum CLASS {
        GUN_RUNNER("Gun Runner", Material.LEATHER_BOOTS),
        RANGER("Ranger",Material.BOW),
        ASSASSIN("Assassin",Material.DIAMOND_AXE),
        TANK("Tank",Material.CROSSBOW)
        ;
        String translation;
        Material material;
        CLASS(String translation, Material material){
            this.translation=translation;
            this.material=material;
        }

        public Material getMaterial() {
            return material;
        }

        public String getTranslation() {
            return translation;
        }
    }

    public static void GiveClass(CLASS c, Player player){
        player.getInventory().clear();
        player.getInventory().setArmorContents(Main.game.TeamManager().getArmor(Main.game.TeamManager().getTeam(player)));
        player.getInventory().setItem(8,new ItemStack(Material.COMPASS));
        player.setCompassTarget(Main.current_point.getFire().getLocation());
        switch (c){
            case GUN_RUNNER:
                ItemStack BLACKOUT_GRENADE = new ItemStack(Material.SPLASH_POTION,2);
                PotionMeta BLACKOUT_META = (PotionMeta) BLACKOUT_GRENADE.getItemMeta();
                BLACKOUT_META.setDisplayName(Text.format("&8Blackout Grenade"));
                BLACKOUT_META.setColor(Color.BLACK);
                BLACKOUT_GRENADE.setItemMeta(BLACKOUT_META);

                ItemStack GUN_RUNNER = new ItemStack(Material.BOW);
                ItemMeta GUN_RUNNER_META = GUN_RUNNER.getItemMeta();
                GUN_RUNNER_META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                GUN_RUNNER_META.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                GUN_RUNNER_META.addEnchant(Enchantment.ARROW_DAMAGE,2,true);
                GUN_RUNNER.setItemMeta(GUN_RUNNER_META);
                setUnbreakable(GUN_RUNNER);
                player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                player.getInventory().addItem(GUN_RUNNER);
                player.getInventory().addItem(BLACKOUT_GRENADE);
                player.getInventory().setItem(10,new ItemStack(Material.ARROW,64));
                player.getInventory().setItem(11,new ItemStack(Material.ARROW,64));
                PotionEffect SPEED_1 = new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,0,true,false,false);
                player.addPotionEffect(SPEED_1,false);
                break;
            case RANGER:
                ItemStack SENSOR_GRENADE = new ItemStack(Material.SPLASH_POTION,2);
                PotionMeta SENSOR_META = (PotionMeta) SENSOR_GRENADE.getItemMeta();
                SENSOR_META.setDisplayName(Text.format("&8Sensor Grenade"));
                SENSOR_META.setColor(Color.LIME);
                SENSOR_GRENADE.setItemMeta(SENSOR_META);

                player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                ItemStack RANGER = new ItemStack(Material.BOW);
                ItemMeta RANGER_META = RANGER.getItemMeta();
                RANGER_META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                RANGER_META.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                RANGER_META.setLore(Collections.singletonList("SPEED:40"));
                RANGER_META.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("generic.attackSpeed", 3, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
                RANGER_META.addEnchant(Enchantment.ARROW_DAMAGE,5,true);
                RANGER.setItemMeta(RANGER_META);
                setUnbreakable(RANGER);
                player.getInventory().addItem(RANGER);
                player.getInventory().addItem(SENSOR_GRENADE);
                player.getInventory().setItem(10,new ItemStack(Material.ARROW,15));
                PotionEffect SLOW_2 = new PotionEffect(PotionEffectType.SLOW,Integer.MAX_VALUE,0,true,false,false);
                player.addPotionEffect(SLOW_2,false);
                break;
            case ASSASSIN:
                ItemStack MORPHINE = new ItemStack(Material.POTION,2);
                PotionMeta pm = (PotionMeta) MORPHINE.getItemMeta();
                pm.setDisplayName(Text.format("&eMorphine"));
                pm.setColor(Color.RED);
                MORPHINE.setItemMeta(pm);

                ItemStack ASSASSIN_KNIFE = new ItemStack(Material.DIAMOND_AXE);
                ItemMeta ASSASSIN_KNIFE_META = ASSASSIN_KNIFE.getItemMeta();
                ASSASSIN_KNIFE_META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                ASSASSIN_KNIFE_META.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                ASSASSIN_KNIFE_META.addEnchant(Enchantment.DAMAGE_ALL,1,true);
                ASSASSIN_KNIFE.setItemMeta(ASSASSIN_KNIFE_META);
                setUnbreakable(ASSASSIN_KNIFE);
                player.getInventory().addItem(ASSASSIN_KNIFE);
                player.getInventory().addItem(MORPHINE);
            break;
            case TANK:
                ItemStack ST = new ItemStack(Material.PLAYER_HEAD);
                Item.itemWithBase64(ST,"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMThlZTY3ZDdhNWI2NzViNTdkMWI3YzdhZTc5MjgwZDJjMTk4MjhlNTgyZjA3MzM5Y2ZiM2VkOWI5MGZmMTIzNiJ9fX0=");
                Item STUN_TRAP = new Item(ST,"&eStun Trap");
                STUN_TRAP.setLore("&7This item will stun any","&7enemies that walk past it","","&ePlease on ground");

                player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));

                ItemStack TANK_ARMOR_LEGGINGS = new ItemStack(Material.DIAMOND_LEGGINGS);
                ItemStack TANK_ARMOR_BOOTS = new ItemStack(Material.DIAMOND_BOOTS);

                ItemMeta TANK_ARMOR_LEGGINGS_META = TANK_ARMOR_LEGGINGS.getItemMeta();
                ItemMeta TANK_ARMOR_BOOTS_META = TANK_ARMOR_BOOTS.getItemMeta();

                TANK_ARMOR_LEGGINGS_META.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,3,true);
                TANK_ARMOR_BOOTS_META.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,3,true);
                setUnbreakable(TANK_ARMOR_LEGGINGS);
                setUnbreakable(TANK_ARMOR_BOOTS);

                TANK_ARMOR_LEGGINGS.setItemMeta(TANK_ARMOR_LEGGINGS_META);
                TANK_ARMOR_BOOTS.setItemMeta(TANK_ARMOR_BOOTS_META);


                ItemStack TANK = new ItemStack(Material.CROSSBOW);
                ItemMeta TANK_META = TANK.getItemMeta();
                TANK_META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                TANK_META.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                TANK_META.addEnchant(Enchantment.MULTISHOT,1,true);
                TANK_META.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("generic.attackSpeed", 3, AttributeModifier.Operation.ADD_NUMBER));
                TANK.setItemMeta(TANK_META);
                setUnbreakable(TANK);

                player.getInventory().addItem(TANK);
                player.getInventory().setItem(10,new ItemStack(Material.ARROW,64));
                player.getInventory().addItem(STUN_TRAP.spigot());
                player.getInventory().setLeggings(TANK_ARMOR_LEGGINGS);
                player.getInventory().setBoots(TANK_ARMOR_BOOTS);

                PotionEffect SLOW_1 = new PotionEffect(PotionEffectType.SLOW,Integer.MAX_VALUE,1,true,false,false);
                player.addPotionEffect(SLOW_1,false);
                break;
            default:
                break;
        }
        Text.sendMessage(player,String.format("&e&lCLASS&7 You have been given %s class",c.getTranslation()), Text.MessageType.TEXT_CHAT);
    }

    private static void setUnbreakable(ItemStack item) {
        net.minecraft.server.v1_15_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("Unbreakable", true);
        stack.setTag(tag);
    }
}
