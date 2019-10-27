package org.voidane.dsu.chest;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftDSU implements Listener {

	org.voidane.dsu.Plugin plugin = org.voidane.dsu.Plugin.getPlugin(org.voidane.dsu.Plugin.class);
	
	public CraftDSU() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void craftRecipeDSU(CraftItemEvent event) {
		
		Player player = ( Player ) event.getWhoClicked();
		if (!plugin.getConfig().getBoolean("Allow Crafting")) {
			event.setCancelled(true);
			player.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &fYou cannot craft this item"));
			player.closeInventory();
			return;
		}
		
		if (!event.getCurrentItem().hasItemMeta()) {
			return;
		}
		
		if (!event.getCurrentItem().getItemMeta().getDisplayName().toString().contains("Deep Storage Unit")) {
			return;
		}
		
		if (plugin.getConfig().getBoolean("Use Disable Worlds")) {
			List<String> disableWorlds = plugin.getConfig().getStringList("Disabled Worlds");
			if (disableWorlds.contains(player.getWorld().getName())) {
				event.setCancelled(true);
				player.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &fYou cannot craft this item"));
				player.closeInventory();
			}
			
		} else if (plugin.getConfig().getBoolean("Use Enable Worlds")) {
			List<String> enableWorlds = plugin.getConfig().getStringList("Enabled Worlds");
			if (enableWorlds.contains(player.getWorld().getName())) {
				
			} else {
				event.setCancelled(true);
				player.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &fYou cannot craft this item"));
				player.closeInventory();
			}
			
		}

	}
}
