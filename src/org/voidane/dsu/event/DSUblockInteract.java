package org.voidane.dsu.event;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.voidane.dsu.FileConfiguration;
import org.voidane.dsu.Plugin;
import org.voidane.dsu.chest.ChestInventory;

public class DSUblockInteract implements Listener {

	Plugin  plugin;
	
	public DSUblockInteract(Plugin plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void deepSUInteraction(PlayerInteractEvent event) {
		
		if (event.getClickedBlock() == null) {
			return;
		}
		
		if (!event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
			return;
		}
		
		if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			return;
		}
		Player player = event.getPlayer();
		
		if (!plugin.hasPermission(player, "dsu.interface.interaction")) {
			return;
		}
		
		String world = event.getClickedBlock().getWorld().getName();
		int x = event.getClickedBlock().getX();
		int y = event.getClickedBlock().getY();
		int z = event.getClickedBlock().getZ();
		String setNameLocationalString = ""+world+x+y+z;
		
		YamlConfiguration loadChestOwnerConfiguration = new FileConfiguration().getChestOwnerConfig();
		if (loadChestOwnerConfiguration.getString(setNameLocationalString+".Owner") != null) {
		
			List<String> userList = loadChestOwnerConfiguration.getStringList(setNameLocationalString+".Allowed Users");
			
			
			
			if (loadChestOwnerConfiguration.getString(setNameLocationalString+".Owner").equalsIgnoreCase(player.getUniqueId().toString())) {

			} else if (userList.contains(player.getUniqueId().toString())) {

			} else {
			return;
			}
			
		YamlConfiguration loadInteractionChestConfiguration = new FileConfiguration().getInteractionChestConfig();
		loadInteractionChestConfiguration.set(player.getUniqueId().toString() + ".Recent Chest Interaction", setNameLocationalString);
		loadInteractionChestConfiguration.set(player.getUniqueId().toString() + ".World", event.getClickedBlock().getWorld().getName());
		loadInteractionChestConfiguration.set(player.getUniqueId().toString() + ".X", event.getClickedBlock().getX());
		loadInteractionChestConfiguration.set(player.getUniqueId().toString() + ".Y", event.getClickedBlock().getY());
		loadInteractionChestConfiguration.set(player.getUniqueId().toString() + ".Z", event.getClickedBlock().getZ());
		File interactionChestFile = new File(plugin.getDataFolder(), "Interaction Chest.yml");
			
		try {
				loadInteractionChestConfiguration.save(interactionChestFile);
			} catch (IOException e) {
			e.printStackTrace();
			}
		 
		 event.setCancelled(true);
		 player.openInventory(new ChestInventory().getChestInventory(player));
		 
		}

	}
}

