package org.voidane.dsu.chest;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.voidane.dsu.FileConfiguration;
import org.voidane.dsu.Plugin;

public class ChestInventory {

	Plugin plugin = Plugin.getPlugin(Plugin.class);
	
	public ChestInventory() {
		
	}

	public Inventory getChestInventory(Player player) {
	
		Inventory Grabinventory = Bukkit.createInventory(null, 54, plugin.translateChatColor(plugin.getConfig().getString("Storage Unit Name")));
		
		String[] setNoLoreStrings = new String[0];
		
		File file = new File(plugin.getDataFolder(), "Custom Config.yml");
		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		
		ItemStack  blackStainedGlass = glassItemStack(Material.getMaterial(configuration.getString("Outline In Unit Inventory.Material")),
				configuration.getInt("Outline In Unit Inventory.Amount"), (byte) configuration.getInt("Outline In Unit Inventory.Material ID"), 
				configuration.getString("Outline In Unit Inventory.Name"), false, setNoLoreStrings);
		
		ItemStack  blueStainedGlass = glassItemStack(Material.getMaterial(configuration.getString("Interior In Unit Inventory.Material")),
				configuration.getInt("Interior In Unit Inventory.Amount"), (byte) configuration.getInt("Interior In Unit Inventory.Material ID"), 
				configuration.getString("Interior In Unit Inventory.Name"), false, setNoLoreStrings);
		
		for ( int i = 0 ; i < 9 ; i++ ) {
			Grabinventory.setItem(i, blackStainedGlass);
		}
			Grabinventory.setItem(9, blackStainedGlass);
			Grabinventory.setItem(17, blackStainedGlass);
			Grabinventory.setItem(18, blackStainedGlass);
			Grabinventory.setItem(26, blackStainedGlass);
			Grabinventory.setItem(27, blackStainedGlass);
			Grabinventory.setItem(35, blackStainedGlass);
			Grabinventory.setItem(36, blackStainedGlass);
			Grabinventory.setItem(44, blackStainedGlass);
			
			for ( int i = 45 ; i < 54 ; i++ ) {
				Grabinventory.setItem(i, blackStainedGlass);
			}
			
			//Blue Glass
			
			Grabinventory.setItem(10, blueStainedGlass);
			Grabinventory.setItem(11, blueStainedGlass);
			Grabinventory.setItem(12, blueStainedGlass);
			Grabinventory.setItem(14, blueStainedGlass);
			Grabinventory.setItem(15, blueStainedGlass);
			Grabinventory.setItem(16, blueStainedGlass);
			Grabinventory.setItem(19, blueStainedGlass);
			Grabinventory.setItem(25, blueStainedGlass);
			Grabinventory.setItem(28, blueStainedGlass);
			Grabinventory.setItem(34, blueStainedGlass);
			for (int i=20;i<25;i++) {
			Grabinventory.setItem(i, blueStainedGlass);
			}
			for (int i=29;i<34;i++) {
				Grabinventory.setItem(i, blueStainedGlass);
			}
			for (int i=37;i<44;i++) {
				if(i==40) {
				}else {
				Grabinventory.setItem(i, blueStainedGlass);	
				}
				
			}
			YamlConfiguration getChestOwnerConfiguration = new FileConfiguration().getChestOwnerConfig();
			YamlConfiguration getRecentInteractionConfiguration = new FileConfiguration().getInteractionChestConfig();
			String recentChest = getRecentInteractionConfiguration.getString(player.getUniqueId().toString()+".Recent Chest Interaction");
			
			if (getChestOwnerConfiguration.getString(recentChest+".Material Used") != null && getChestOwnerConfiguration.getString(recentChest+".Material Used").length() > 0) {
				ItemStack itemStack = new ItemStack(Material.getMaterial(getChestOwnerConfiguration.getString(recentChest+".Material Used")),
						1, (byte) getChestOwnerConfiguration.getInt(recentChest+".Material ID"));
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setDisplayName(plugin.translateChatColor("&7&l"+getChestOwnerConfiguration.getString(recentChest+".Material Used").toUpperCase().replace("_", " ")));
				itemMeta.setLore(Arrays.asList("",plugin.translateChatColor("&fStored: &b"+getChestOwnerConfiguration.getString(recentChest+".Stored"))));
				itemStack.setItemMeta(itemMeta);
				Grabinventory.setItem(40, itemStack);
				ItemStack itemStack2 = new ItemStack(Material.getMaterial(getChestOwnerConfiguration.getString(recentChest+".Material Used")),
						1, (byte) getChestOwnerConfiguration.getInt(recentChest+".Material ID"));
				ItemMeta itemMeta2 = itemStack2.getItemMeta();
				itemMeta2.setDisplayName(plugin.translateChatColor("&6&l"+getChestOwnerConfiguration.getString(recentChest+".Material Used")).toString().replace("_", " "));
				itemStack2.setItemMeta(itemMeta2);
				Grabinventory.setItem(13, itemStack2);
				
				ItemStack infoMaterialItemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4);
				ItemMeta infoItemMeta = infoMaterialItemStack.getItemMeta();
				infoItemMeta.setDisplayName(plugin.translateChatColor("&dHow to use the deep storage unit"));
				List<String> getLoreList = Arrays.asList("","&f&lTransfering items into unit","","&f- Right Click&f: &7Transfer half the items", "",
						"&f- Left Click&f: &7Transfer all the items ",plugin.translateChatColor("&7from there"),"", "&f- ScrollWheel&f: &7Transfer your whole",
						plugin.translateChatColor("&7inventory containing the certain item"));
				
				
				String[] setLoreString = getLoreList.toArray(new String[getLoreList.size()]);
				for (int i = 0 ; i < getLoreList.size() ; i++) {
					setLoreString[i] = plugin.translateChatColor(setLoreString[i]);
				}
				List<String> transvetedList = Arrays.asList(setLoreString);
				
				infoItemMeta.setLore(transvetedList);
				
				infoMaterialItemStack.setItemMeta(infoItemMeta);
				
				Grabinventory.setItem(4, infoMaterialItemStack);
				
				ItemStack infoItemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4 );
				ItemMeta infoMeta = infoItemStack.getItemMeta();
				infoMeta.setDisplayName(plugin.translateChatColor("&dExporting Items Into Your Inventory"));
				infoMeta.setLore(Arrays.asList("",plugin.translateChatColor("&f- Left Click: &7Export up to a stack out"), "" ,
						plugin.translateChatColor("&f- Shift Left Click: &7Export as much as"),plugin.translateChatColor("&7your inventory can hold")));
				infoItemStack.setItemMeta(infoMeta);
				
				Grabinventory.setItem(49, infoItemStack);
				
				
				
				ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 2);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(plugin.translateChatColor("&dPick-Up Deep Storage Unit"));
				meta.setLore(Arrays.asList("",plugin.translateChatColor("&7All items stay in the unit")));
				item.setItemMeta(meta);
				Grabinventory.setItem(10, item);
				
				ItemStack item2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 2);
				ItemMeta meta2 = item.getItemMeta();
				meta2.setDisplayName(plugin.translateChatColor("&cRemove all items"));
				meta2.setLore(Arrays.asList("",plugin.translateChatColor("&cDelete all items in this menu")));
				item2.setItemMeta(meta2);
				Grabinventory.setItem(16, item2);
				
				if (getChestOwnerConfiguration.getString(recentChest+".fakeBlock") != null) {
					player.closeInventory();
				}
				
			} else {

				YamlConfiguration customConfiguration = YamlConfiguration.loadConfiguration(file);
				
				ItemStack itemStack = new ItemStack(Material.getMaterial(customConfiguration.getString("No Item In Main Interface.Material")), 1 , 
						(byte) customConfiguration.getInt("No Item In Main Interface.Material ID"));
				
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setDisplayName(plugin.translateChatColor(customConfiguration.getString("No Item In Main Interface.Name")));
				
				List<String> lore = customConfiguration.getStringList("No Item In Main Interface.Lore");
				itemMeta.setLore(plugin.translateChatColorArray(lore));
				itemStack.setItemMeta(itemMeta);
				Grabinventory.setItem(40, itemStack);
			}
			
			
			return Grabinventory;
	}
	
	
	private ItemStack glassItemStack(Material material, int amount, byte data, String name, boolean acceptLore, String[] loreArray) {
		
		ItemStack item = new ItemStack(material, amount, data);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(plugin.translateChatColor(name));
		if (acceptLore) {
			
		for ( int i = 0 ; i < loreArray.length + 1 ; i++ ) {
			
		meta.setLore(Arrays.asList(plugin.translateChatColor(loreArray[i])));
			
			}
		
		}
		
		item.setItemMeta(meta);
		
		return item;
		
	}
	
}
