package org.voidane.dsu;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileConfiguration {

	Plugin plugin = Plugin.getPlugin(Plugin.class);
	
	
	public FileConfiguration() {
		
		if (doesFileExist(new File(plugin.getDataFolder(), "Chest Owners.yml")) && doesFileExist(new File(plugin.getDataFolder(), "Interaction Chest.yml")))  {
			return;
		}

		Bukkit.getServer().getConsoleSender().sendMessage(plugin.translateChatColor("&6[Deep Storage Unit] This is your first time running this plugin it seems! dont worry you wont get another "+
		"colored notification in here unless its an error. Check out the config.yml to optimize to your likings, and maybe leave a review so I can add more into the plugin! Anyways,"+
		" The following has been ran on your server!"));
		chestOwnersCreate();
		InteractionChestCreate();
		createDefaultConfig();
		Bukkit.getServer().getConsoleSender().sendMessage(plugin.translateChatColor("&c[Deep Storage Unit] RELOAD THE PLUGIN, Since this is the first time deep storage unit is running, to become bug-free please reloead the plugin"));
	}


	
	
	
	private void chestOwnersCreate() {
		File chestOwnerFile = new File(plugin.getDataFolder(), "Chest Owners.yml");
		YamlConfiguration chestOwnerFileConfiguration = YamlConfiguration.loadConfiguration(chestOwnerFile);
		
		if (!chestOwnerFile.exists()) {
			chestOwnerFileConfiguration.set("Units", 0);
			try {
			chestOwnerFileConfiguration.save(chestOwnerFile);
			Bukkit.getConsoleSender().sendMessage(plugin.translateChatColor("&a[Deep Storage Unit] &7Successfully saved file Chest Owners.yml ( &bOne Time Notification&7 )"));
		}
			catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage(plugin.translateChatColor("&c[Deep Storage Unit] &7Could not save file Chest Owners.yml (IOException Error)"));
			e.printStackTrace();
			}
		}
	}

	public YamlConfiguration getChestOwnerConfig() {
		File chestOwnerFile = new File(plugin.getDataFolder(), "Chest Owners.yml");
		YamlConfiguration chestOwnerFileConfiguration = YamlConfiguration.loadConfiguration(chestOwnerFile);
		
		return chestOwnerFileConfiguration;
	}
	
	
	
	
	
	private void InteractionChestCreate() {
		File interactionChestFile = new File(plugin.getDataFolder(), "Interaction Chest.yml");
		YamlConfiguration interactionChestConfiguration  =  YamlConfiguration.loadConfiguration(interactionChestFile);
		
		if (!interactionChestFile.exists()) {
			
			try {
				interactionChestConfiguration.save(interactionChestFile);
				Bukkit.getConsoleSender().sendMessage(plugin.translateChatColor("&a[Deep Storage Unit] &7Successfully saved file Interation Chest.yml ( &bOne Time Notification&7 )"));
			} 
			catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage(plugin.translateChatColor("&c[Deep Storage Unit] &7Could not save file Interation Chest.yml (IOException Error)"));
				e.printStackTrace();
			}
		}
	}

	public YamlConfiguration getInteractionChestConfig() {
		File interactionChestFile = new File(plugin.getDataFolder(), "Interaction Chest.yml");
		YamlConfiguration interactionChestConfiguration  =  YamlConfiguration.loadConfiguration(interactionChestFile);
		
		return interactionChestConfiguration;
	}
	
	
	
	
	
	private boolean doesFileExist(File file) {
		if (!file.exists()) {
			return false;
		}
		return true;
	}
	
	
	private void createDefaultConfig() {
		plugin.saveResource("config.yml", false);
	}
	
	
}
