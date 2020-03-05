package org.voidane.dsu.event;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.voidane.dsu.FileConfiguration;
import org.voidane.dsu.Plugin;

public class DSUplacement implements Listener {

	Plugin plugin = Plugin.getPlugin(Plugin.class);
	
	public DSUplacement(Plugin plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onChestPlacement(BlockPlaceEvent event) {
		
		if (!event.getBlockPlaced().getType().equals(Material.ENDER_CHEST))  {
			return;
		}
		
		if (!event.getItemInHand().hasItemMeta()) {
			return;
		}
		
		if (!event.getItemInHand().hasItemMeta() && !event.getItemInHand().getItemMeta().getDisplayName().contentEquals("Deep Storage Unit")) {
			return;
		}
		
		if (plugin.getConfig().getBoolean("Use Disable Worlds")) {
			List<String> disableWorlds = plugin.getConfig().getStringList("Disabled Worlds");
			if (disableWorlds.contains(event.getBlock().getWorld().getName())) {
				return;
			}
			
		} else if (plugin.getConfig().getBoolean("Use Enable Worlds")) {
			List<String> enableWorlds = plugin.getConfig().getStringList("Enabled Worlds");
			if (enableWorlds.contains(event.getBlock().getWorld().getName())) {
				
			} else {
				return;
			}
			
		}
		
		YamlConfiguration chestOwnerConfiguration =  new FileConfiguration().getChestOwnerConfig();
		
		
		Player player = event.getPlayer();
		if 	(chestCheckForStatistics(player, event.getBlock().getWorld().getName(), 
					event.getBlockPlaced().getX(), 
					event.getBlockPlaced().getY(), 
					event.getBlockPlaced().getZ(),
					event.getItemInHand().getItemMeta().getLore(), event.getBlock().getLocation(), event)) {
			
			return;
		}
		
		
		String worldName = event.getBlockPlaced().getWorld().getName();
		int x  = event.getBlockPlaced().getX();
		int y  = event.getBlockPlaced().getY();
		int z  = event.getBlockPlaced().getZ();
		
		String setLocationalNameString = worldName+x+y+z;
		
		chestOwnerConfiguration.set(setLocationalNameString+".Owner", player.getUniqueId().toString());
		
		int unit = chestOwnerConfiguration.getInt("Units"); unit++; chestOwnerConfiguration.set("Units", unit);
		chestOwnerConfiguration.set(setLocationalNameString+".Unit", unit);
		
		chestOwnerConfiguration.set(setLocationalNameString+".Unit", unit);
		chestOwnerConfiguration.set(setLocationalNameString+".Material Used", "");
		chestOwnerConfiguration.set(setLocationalNameString+".Material ID", 0);
		chestOwnerConfiguration.set(setLocationalNameString+".Stored", 0);
		chestOwnerConfiguration.set(setLocationalNameString+".Permission", "dsu.chestunit."+unit);
		chestOwnerConfiguration.set(setLocationalNameString+".Allowed Users", "");
		chestOwnerConfiguration.set(setLocationalNameString+".Location", event.getBlock().getLocation().toString());
		try {
			chestOwnerConfiguration.save(new File(plugin.getDataFolder(), "Chest Owners.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean chestCheckForStatistics(Player player, String world, int x, int y, int z, List<String> lore, Location location, BlockPlaceEvent event) {
		
		YamlConfiguration getInteractionConfig =  new FileConfiguration().getInteractionChestConfig();
		YamlConfiguration chestOwnerConfiguration = new FileConfiguration().getChestOwnerConfig();
		
		List<String> checkList = getInteractionConfig.getStringList(lore.get(4)+".Pickup Chest");
		
		for (int i = 0 ; i < checkList.size() ; i++) {

			if (checkList.get(i).equalsIgnoreCase(lore.get(3))) {
				
				if (chestOwnerConfiguration.getString(checkList.get(i)+".Location").equalsIgnoreCase(event.getBlock().getLocation().toString())) {
					chestOwnerConfiguration.set(world+x+y+z+".Owner", chestOwnerConfiguration.getString(checkList.get(i)+".Owner"));
					chestOwnerConfiguration.set(world+x+y+z+".Unit", chestOwnerConfiguration.getInt(checkList.get(i)+".Unit"));
					chestOwnerConfiguration.set(world+x+y+z+".Material Used", chestOwnerConfiguration.getString(checkList.get(i)+".Material Used"));
					chestOwnerConfiguration.set(world+x+y+z+".Material ID", chestOwnerConfiguration.getInt(checkList.get(i)+".Material ID"));
					chestOwnerConfiguration.set(world+x+y+z+".Stored", chestOwnerConfiguration.getInt(checkList.get(i)+".Stored"));
					chestOwnerConfiguration.set(world+x+y+z+".Permission", chestOwnerConfiguration.getString(checkList.get(i)+".Permission"));
					chestOwnerConfiguration.set(world+x+y+z+".Allowed Users", chestOwnerConfiguration.getStringList(checkList.get(i)+".Allowed User"));
					chestOwnerConfiguration.set(world+x+y+z+".Location", location.toString());
					chestOwnerConfiguration.set(world+x+y+z+".Disallow Usage", "false");
				} else {

				chestOwnerConfiguration.set(world+x+y+z+".Owner", chestOwnerConfiguration.getString(checkList.get(i)+".Owner"));
				chestOwnerConfiguration.set(world+x+y+z+".Unit", chestOwnerConfiguration.getInt(checkList.get(i)+".Unit"));
				chestOwnerConfiguration.set(world+x+y+z+".Material Used", chestOwnerConfiguration.getString(checkList.get(i)+".Material Used"));
				chestOwnerConfiguration.set(world+x+y+z+".Material ID", chestOwnerConfiguration.getInt(checkList.get(i)+".Material ID"));
				chestOwnerConfiguration.set(world+x+y+z+".Stored", chestOwnerConfiguration.getInt(checkList.get(i)+".Stored"));
				chestOwnerConfiguration.set(world+x+y+z+".Permission", chestOwnerConfiguration.getString(checkList.get(i)+".Permission"));
				chestOwnerConfiguration.set(world+x+y+z+".Allowed Users", chestOwnerConfiguration.getStringList(checkList.get(i)+".Allowed User"));
				chestOwnerConfiguration.set(world+x+y+z+".Location", location.toString());
				

				chestOwnerConfiguration.set(checkList.get(i), null);
				getInteractionConfig.set(checkList.get(i), null);
				}
				try {
					chestOwnerConfiguration.save(new File(plugin.getDataFolder(), "Chest Owners.yml"));
					getInteractionConfig.save(new File(plugin.getDataFolder(), "Interaction Chest.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
				}
			}
			
		return false;
	}

}
