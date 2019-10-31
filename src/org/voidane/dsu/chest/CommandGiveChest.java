package org.voidane.dsu.chest;

import java.io.File;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
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
	
		File file = new File(plugin.getDataFolder(), "Custom Config.yml");
		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		
		ItemStack itemStack = new ItemStack(Material.ENDER_CHEST, amount);
		ItemMeta itemMeta  =  itemStack.getItemMeta();
		
		itemMeta.setDisplayName(plugin.translateChatColor(configuration.getString("Deep Storage Unit.Name")));
		itemMeta.setLore(plugin.translateChatColorArray(configuration.getStringList("Deep Storage Unit.Lore")));
		
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
}
