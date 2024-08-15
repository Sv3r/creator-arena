package be.sv3r.handler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitHandler {
    public static void equipKitArmor(Player player) {
        player.getInventory().setHelmet(ItemStack.of(Material.IRON_HELMET));
        player.getInventory().setChestplate(ItemStack.of(Material.CHAINMAIL_CHESTPLATE));
        player.getInventory().setLeggings(ItemStack.of(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(ItemStack.of(Material.IRON_BOOTS));
    }

    public static void giveKitItems(Player player) {
        player.getInventory().addItem(ItemStack.of(Material.IRON_SWORD));
        player.getInventory().addItem(ItemStack.of(Material.STONE_AXE));
        player.getInventory().addItem(ItemStack.of(Material.BOW));
        player.getInventory().addItem(ItemStack.of(Material.COBWEB, 3));
        player.getInventory().addItem(ItemStack.of(Material.GOLDEN_APPLE));
        player.getInventory().setItemInOffHand(ItemStack.of(Material.SHIELD));
        player.getInventory().addItem(ItemStack.of(Material.COOKED_BEEF, 64));
        player.getInventory().addItem(ItemStack.of(Material.ARROW, 16));
        player.getInventory().addItem(ItemStack.of(Material.FISHING_ROD));
    }
}
