package org.voidane.dsu;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileConfiguration {

	Plugin plugin = Plugin.getPlugin(Plugin.class);
	
	
	public FileConfiguration() {
		
		getCustomConfigContents();
		if (doesFileExist(new File(plugin.getDataFolder(), "Chest Owners.yml")) && doesFileExist(new File(plugin.getDataFolder(), "Interaction Chest.yml")))  {
			return;
		}
		
		Bukkit.getServer().getConsoleSender().sendMessage(plugin.translateChatColor("&6[Deep Storage Unit] This is your first time running this plugin it seems! dont worry you wont get another "+
		"colored notification in here unless its an error. Check out the config.yml to optimize to your likings, and maybe leave a review so I can add more into the plugin! Anyways,"+
		" The following has been ran on your server!"));
		chestOwnersCreate();
		InteractionChestCreate();
		customConfigCreate();
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
	
	
	
	
	
	
	private void customConfigCreate() {
		
		plugin.saveResource("Custom Config.yml", false);	

	}
	
	private boolean checkCustomConfig() {
		File file = new File(plugin.getDataFolder(), "Custom Config.yml");
		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		
		if (configuration.getString("Version") == null) {
			plugin.saveResource("Custom Config.yml", false);
			return false;
		}
		
		if (configuration.getString("Version").equals(plugin.version)) {
			return false;
		} else {
		return true;
		}
	}
	
	
	private void getCustomConfigContents() {
		
		if (checkCustomConfig()) {
			File file = new File(plugin.getDataFolder(), "Custom Config.yml");
			YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
			
			String[] stringDefaults = new String[100];
			int[] intDefaults = new int[100];
			
			stringDefaults[0] = configuration.getString("No Item In Main Interface.Name");
			List<String> noItemInMainInterfaceLore = configuration.getStringList("No Item In Main Interface.Lore");
			stringDefaults[1] = configuration.getString("No Item In Main Interface.Material");
			intDefaults[0] = configuration.getInt("No Item In Main Interface.Material ID");
			
			
			stringDefaults[2] = configuration.getString("Outline In Unit Inventory.Name");
			intDefaults[1] = configuration.getInt("Outline In Unit Inventory.Amount");
			stringDefaults[3] = configuration.getString("Outline In Unit Inventory.Material");
			intDefaults[2] = configuration.getInt("Outline In Unit Inventory.Material ID");
			
			
			stringDefaults[4] = configuration.getString("Interior In Unit Inventory.Name");
			intDefaults[3] = configuration.getInt("Interior In Unit Inventory.Amount");
			stringDefaults[5] = configuration.getString("Interior In Unit Inventory.Material");
			intDefaults[4] = configuration.getInt("Interior In Unit Inventory.Material ID");
			
			List<String> craftRecipeList = configuration.getStringList("Craft Recipe");
			
			
					plugin.saveResource("Custom Config.yml", true);	
					file = new File(plugin.getDataFolder(), "Custom Config.yml");
					configuration = YamlConfiguration.loadConfiguration(file);
					
					
			configuration.set("Craft Recipe", craftRecipeList);
					// No item in main interface	
			configuration.set("No Item In Main Interface.Name", stringDefaults[0]);
			configuration.set("No Item In Main Interface.Lore", noItemInMainInterfaceLore);
			configuration.set("No Item In Main Interface.Material", stringDefaults[1]);
			configuration.set("No Item In Main Interface.Material ID", intDefaults[0]);
					// Outline In Unit Inventory
			configuration.set("Outline In Unit Inventory.Name", stringDefaults[2]);
			configuration.set("Outline In Unit Inventory.Amount", intDefaults[1]);
			configuration.set("Outline In Unit Inventory.Material", stringDefaults[3]);
			configuration.set("Outline In Unit Inventory.Material ID", intDefaults[2]);
			// 4354 Interior In Unit Inventory
			configuration.set("Interior In Unit Inventory.Name", stringDefaults[4]);
			configuration.set("Interior In Unit Inventory.Amount", intDefaults[3]);
			configuration.set("Interior In Unit Inventory.Material", stringDefaults[5]);
			configuration.set("Interior In Unit Inventory.Material ID", intDefaults[4]);
			try {
				configuration.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			return;
		}

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