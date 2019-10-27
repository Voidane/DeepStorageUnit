package org.voidane.dsu.chest;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.voidane.dsu.Plugin;

public class CommandGiveChest {

	Plugin plugin = Plugin.getPlugin(Plugin.class);
	
	public CommandGiveChest() {

	}

	public void onCommandGiveChest(Player player, int grabCommandInt) {
		
		player.getInventory().addItem(chestItemStack(grabCommandInt));
		
	}
	
	public ItemStack chestItemStack(int amount) {
	
		ItemStack itemStack = new ItemStack(Material.ENDER_CHEST, amount);
		ItemMeta itemMeta  =  itemStack.getItemMeta();
		
		itemMeta.setDisplayName(plugin.translateChatColor("&cDeep Storage Unit"));
		itemMeta.setLore(Arrays.asList("",plugin.translateChatColor("&7A deep storage unit that can hold unlimited  amounts"),plugin.translateChatColor(
				"&7of a certain material or item"),"",plugin.translateChatColor("&9Deep Storage Unit")));
		
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
}
