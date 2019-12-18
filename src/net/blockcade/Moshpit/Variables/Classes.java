package net.blockcade.Moshpit.Variables;

import net.blockcade.Arcade.Utils.Formatting.Text;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        switch (c){
            case GUN_RUNNER:
                ItemStack GUN_RUNNER = new ItemStack(Material.BOW);
                ItemMeta GUN_RUNNER_META = GUN_RUNNER.getItemMeta();
                GUN_RUNNER_META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                GUN_RUNNER_META.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                GUN_RUNNER_META.addEnchant(Enchantment.ARROW_DAMAGE,2,true);
                GUN_RUNNER.setItemMeta(GUN_RUNNER_META);
                setUnbreakable(GUN_RUNNER);
                player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                player.getInventory().addItem(GUN_RUNNER);
                player.getInventory().setItem(10,new ItemStack(Material.ARROW,64));
                player.getInventory().setItem(11,new ItemStack(Material.ARROW,64));
                PotionEffect SPEED_1 = new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,0,true,false,false);
                player.addPotionEffect(SPEED_1,false);
                break;
            case RANGER:
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
                player.getInventory().setItem(10,new ItemStack(Material.ARROW,15));
                PotionEffect SLOW_2 = new PotionEffect(PotionEffectType.SLOW,Integer.MAX_VALUE,0,true,false,false);
                player.addPotionEffect(SLOW_2,false);
                break;
            case ASSASSIN:
                ItemStack ASSASSIN_KNIFE = new ItemStack(Material.DIAMOND_AXE);
                ItemMeta ASSASSIN_KNIFE_META = ASSASSIN_KNIFE.getItemMeta();
                ASSASSIN_KNIFE_META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                ASSASSIN_KNIFE_META.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                ASSASSIN_KNIFE_META.addEnchant(Enchantment.DAMAGE_ALL,3,true);
                ASSASSIN_KNIFE_META.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("generic.attackSpeed", 1, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
                ASSASSIN_KNIFE.setItemMeta(ASSASSIN_KNIFE_META);
                player.getInventory().addItem(ASSASSIN_KNIFE);
            break;
            case TANK:
                player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                ItemStack TANK = new ItemStack(Material.CROSSBOW);
                ItemMeta TANK_META = TANK.getItemMeta();
                TANK_META.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                TANK_META.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                TANK_META.addEnchant(Enchantment.QUICK_CHARGE,2,true);
                TANK_META.addEnchant(Enchantment.MULTISHOT,1,true);
                TANK_META.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("generic.attackSpeed", 3, AttributeModifier.Operation.ADD_NUMBER));
                TANK.setItemMeta(TANK_META);
                player.getInventory().addItem(TANK);
                player.getInventory().setItem(10,new ItemStack(Material.ARROW,64));
                player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
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
