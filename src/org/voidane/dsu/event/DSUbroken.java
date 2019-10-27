package org.voidane.dsu.event;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.voidane.dsu.FileConfiguration;
import org.voidane.dsu.Plugin;

public class DSUbroken implements Listener {

	
	Plugin plugin;
	
	public DSUbroken(Plugin plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void denyDSUBreak(BlockBreakEvent event) {
		
		if (!event.getBlock().getType().equals(Material.ENDER_CHEST)) {
			return;
		}
		
		YamlConfiguration getChestOwnerConfiguration = new FileConfiguration().getChestOwnerConfig();
		
		String worldString = event.getBlock().getWorld().getName();
		int x = event.getBlock().getX();
		int y = event.getBlock().getY();
		int z = event.getBlock().getZ();
		
		String getChest = worldString+x+y+z;
		
		if (getChestOwnerConfiguration.getString(getChest) != null) {
			event.setCancelled(true);
			Player player = (Player) event.getPlayer();
			player.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &fYou must pick this item up from the menu"));
		}
		
	}
	
	
}
