package org.voidane.dsu.chest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.voidane.dsu.FileConfiguration;

public class ChestLocationOnLook {

	org.voidane.dsu.Plugin plugin = org.voidane.dsu.Plugin.getPlugin(org.voidane.dsu.Plugin.class);
	
	
	public ChestLocationOnLook() {
		
	}
	
	@SuppressWarnings("deprecation")
	public void getTargetBlock(Player player, String playerAdded) {
		
		Block block = player.getTargetBlock((Set<Material>) null, 5);
		
		if (!block.getType().equals(Material.ENDER_CHEST)) {
			return;
		}
		
		if (Bukkit.getPlayer(playerAdded)==null) {
			player.sendMessage("player doesnt exist or must be online");
			return;
		}
		playerAdded = Bukkit.getPlayer(playerAdded).getUniqueId().toString();
		int[] sectors = {block.getX(),block.getY(),block.getZ()};
		if (addPlayerToList(playerAdded, block.getWorld().getName(), sectors, player)) {
		} else {

		}
		
	}

	private boolean addPlayerToList(String player, String world, int[] sectors, Player sender) {
		
		YamlConfiguration addPlayerToUnitConfiguration = new FileConfiguration().getChestOwnerConfig();
		
		String getLocationString = world+sectors[0]+sectors[1]+sectors[2];
		if (addPlayerToUnitConfiguration.getString(getLocationString+".Owner") != null ) {
			
		if (!addPlayerToUnitConfiguration.getString(getLocationString+".Owner").equalsIgnoreCase(sender.getUniqueId().toString())) {
			
			return false;
		}
		
		
		
		List<String> addUserStrings = addPlayerToUnitConfiguration.getStringList(getLocationString+".Allowed Users");
		
		for ( int i = 0 ; i < addUserStrings.size() ; i++ ) {
			addUserStrings.get(i).equalsIgnoreCase(player);
			sender.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &f Player is already added to this unit"));
			
			if (i == addUserStrings.size()-1) {
			return false;
			}
			
		}
		
		addUserStrings.add(player);
		addPlayerToUnitConfiguration.set(getLocationString+".Allowed Users", addUserStrings);
		sender.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &fSuccessfully added player"));
		try {
			addPlayerToUnitConfiguration.save(new File(plugin.getDataFolder(), "Chest Owners.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
			return true;
		} else {
			return false;
		}

	}
	
	
	@SuppressWarnings("deprecation")
	public void removePlayerFromChest(Player player, String removePlayer) {
		

		Block block = player.getTargetBlock((Set<Material>) null, 5);
		
		if (!block.getType().equals(Material.ENDER_CHEST)) {
			return;
		}
		
		int[] sectors = {block.getX(),block.getY(),block.getZ()};
		
		if (Bukkit.getPlayer(removePlayer) != null) {
			
			if (removePlayer(Bukkit.getPlayer(removePlayer).getUniqueId().toString(), block.getWorld().getName(), sectors, player)) {
				player.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &cYou removed " + removePlayer +" from that unit"));
				return;
			} else {
				player.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &fUnable to remove player " + removePlayer));
			}
			
			
		}
		
		
	}
	

	private boolean removePlayer(String removePlayer, String world, int[] sectors, Player sender) {
		
		YamlConfiguration getChestOwnerConfiguration = new FileConfiguration().getChestOwnerConfig();
		String getLocationString = world+sectors[0]+sectors[1]+sectors[2];
		if (getChestOwnerConfiguration.getStringList(getLocationString+".Allowed Users") != null) {
			
			List<String> users = getChestOwnerConfiguration.getStringList(getLocationString+".Allowed Users");
			
			for ( int i = 0 ; i < users.size() ; i++ ) {
				
				if (users.get(i).equalsIgnoreCase(removePlayer)) {
					
					users.remove(i);
					getChestOwnerConfiguration.set(getLocationString+".Allowed Users", users);
					try {
						getChestOwnerConfiguration.save(new File(plugin.getDataFolder(), "Chest Owners.yml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return true;
				}
				
				if (i == users.size()-1) {
					sender.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &fPlayer not found or added to this chest"));
				}
				
			}
			
		}
		
		return false;
	}
	
	
}
