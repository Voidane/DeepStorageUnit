package org.voidane.dsu.event;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.voidane.dsu.FileConfiguration;
import org.voidane.dsu.Plugin;
import org.voidane.dsu.chest.ChestInventory;

public class DSUInteractionInventory implements Listener {

	
	Plugin plugin;
	File file;
	YamlConfiguration configuration;
	
	public DSUInteractionInventory(Plugin plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@EventHandler
	public void removeItemPanel(InventoryClickEvent event) {
		
		if (event.getCurrentItem() == null) {
			return;
		}
		
		if (!event.getView().getTitle().contains("Deep Storage Unit")
				|| !event.getCurrentItem().hasItemMeta()) {
			return;
		}
		
		YamlConfiguration getInteractionConfiguration = new FileConfiguration().getInteractionChestConfig();
		YamlConfiguration getOwnerConfiguration = new FileConfiguration().getChestOwnerConfig();
		Player player = (Player) event.getWhoClicked();
		String getLocationString = getInteractionConfiguration.getString(player.getUniqueId().toString()+".Recent Chest Interaction");
		
		if (getOwnerConfiguration.getBoolean(getLocationString+".Disallow Usage")) {
			player.closeInventory();
			return;
		}
		
		if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Remove all items") && event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE) && event.getSlot() == 16) {

			getOwnerConfiguration.set(getLocationString+".Material Used", "");
			getOwnerConfiguration.set(getLocationString+".Material ID", 0);
			getOwnerConfiguration.set(getLocationString+".Stored", 0);
			
			try {

				getOwnerConfiguration.save(new File(plugin.getDataFolder(), "Chest Owners.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			player.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &fUnit has been cleared"));
			event.setCancelled(true);
			player.closeInventory();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	@EventHandler
	public void itemPickupUnitPanel(InventoryClickEvent event) {
		
		if (event.getCurrentItem() == null) {
			return;
		}
		
		if (!event.getCurrentItem().hasItemMeta()) {
			 return;
		}
		
		if (!event.getView().getTitle().contains("Deep Storage Unit")) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		YamlConfiguration getLastInteractionConfiguration = new FileConfiguration().getInteractionChestConfig();
		YamlConfiguration getChestOwnerConfiguration = new FileConfiguration().getChestOwnerConfig();
		String getChest = getLastInteractionConfiguration.getString(player.getUniqueId().toString()+".Recent Chest Interaction");
		if (getChestOwnerConfiguration.getBoolean(getChest+".Disallow Usage")) {
			player.closeInventory();
			return;
		}
				
		if (event.getSlot() == 10 && event.getCurrentItem().getItemMeta().getDisplayName().contains("Pick-Up Deep Storage Unit")) {
			for (int i = 0 ; i < player.getInventory().getSize() ; i++) {
				if (player.getInventory().getItem(i) == null) {
					break;
				}
				if (i == player.getInventory().getSize()-1) {
					player.sendMessage(plugin.translateChatColor("&b[ Deep Storage Unit ] &cNot enough inventory room"));
					event.setCancelled(true);
					player.closeInventory();
					return;
				}
			}
			
			ItemStack giveChestBack = new ItemStack(Material.ENDER_CHEST, 1);
			ItemMeta giveChestMeta = giveChestBack.getItemMeta();
			giveChestMeta.setDisplayName(plugin.translateChatColor(
					"&bDeep Storage Unit &f["+getChestOwnerConfiguration.getString(getChest+".Stored")+" "+getChestOwnerConfiguration.getString(getChest+".Material Used").replace("_", " ")+"]"));
			giveChestMeta.setLore(Arrays.asList("", plugin.translateChatColor("&cMaterial: &f"+ getChestOwnerConfiguration.getString(getChest+".Material Used")+" : "+
			getChestOwnerConfiguration.getString(getChest+".Material ID")),plugin.translateChatColor("&cStored: &f"+getChestOwnerConfiguration.getString(getChest+".Stored")),
					getLastInteractionConfiguration.getString(player.getUniqueId().toString()+".Recent Chest Interaction"),player.getUniqueId().toString(),"",plugin.translateChatColor("&9Deep Storage Unit")));
			giveChestBack.setItemMeta(giveChestMeta);
			player.closeInventory();
			player.getInventory().addItem(giveChestBack);
			
			if (getLastInteractionConfiguration.getList(player.getUniqueId().toString()+".Pickup Chest") == null) {
				getLastInteractionConfiguration.set(player.getUniqueId().toString()+".Pickup Chest", Arrays.asList(getChest));
			} else {
				
				List<String> addList = getLastInteractionConfiguration.getStringList(player.getUniqueId().toString()+".Pickup Chest");
				addList.add(getChest);
				getLastInteractionConfiguration.set(player.getUniqueId().toString()+".Pickup Chest", addList);
				
			}
				getChestOwnerConfiguration.set(getChest+".fakeBlock", "s");
			       org.bukkit.Location loc = new org.bukkit.Location(player.getWorld(), 
							getLastInteractionConfiguration.getInt(player.getUniqueId().toString()+".X"), getLastInteractionConfiguration.getInt(player.getUniqueId().toString()+".Y"), 
							getLastInteractionConfiguration.getInt(player.getUniqueId().toString()+".Z"));
			       Block block = loc.getBlock();
			       block.setType(Material.AIR);
			       getChestOwnerConfiguration.set(getChest+".Disallow Usage", true);
			try {
				getChestOwnerConfiguration.save(new File(plugin.getDataFolder(),"Chest Owners.yml"));
				getLastInteractionConfiguration.save(new File(plugin.getDataFolder(), "Interaction Chest.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	@EventHandler
	public void cancelActionsOnItems(InventoryClickEvent event) {
	
		if (event.getCurrentItem() == null) {
			return;
		}
		
		if (!event.getView().getTitle().contains("Deep Storage Unit")
				|| !event.getCurrentItem().hasItemMeta()) {
			return;
		}
		
		Player player = (Player) event.getWhoClicked();
		YamlConfiguration getLastInteractionConfiguration = new FileConfiguration().getInteractionChestConfig();
		YamlConfiguration getChestOwnerConfiguration = new FileConfiguration().getChestOwnerConfig();
		
		String getChest = getLastInteractionConfiguration.getString(player.getUniqueId().toString()+".Recent Chest Interaction");
		
		if (getChestOwnerConfiguration.getBoolean(getChest+".Disallow Usage")) {
			player.closeInventory();
			return;
		}
		
		if (event.getSlot() == 13 && event.getCurrentItem().hasItemMeta()) {
			player.closeInventory();
			return;
		}
		file = new File(plugin.getDataFolder(), "Custom Config.yml");
		configuration = YamlConfiguration.loadConfiguration(file);
		
		if (event.getCurrentItem().getType().equals(Material.getMaterial(configuration.getString("Outline In Unit Inventory.Material"))) && 
				event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(configuration.getString("Outline In Unit Inventory.Name")) || 
				event.getCurrentItem().getType().equals(Material.getMaterial(configuration.getString("Interior In Unit Inventory.Material"))) && 
				event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(configuration.getString("Interior In Unit Inventory.Name"))
				|| event.getSlot() == 40 || event.getCurrentItem().getItemMeta().getDisplayName().contains("Pick-Up Deep Storage Unit") && event.getSlot() == 10 || 
				event.getCurrentItem().getItemMeta().getDisplayName().contains("How to use the deep storage unit") && event.getSlot() == 4 ||
				event.getSlot() == 49) {
			event.setCancelled(true);
		}
	}
	
	
	
	
	
	
	
	
	
	
	@EventHandler
	public void setItemInSelection(InventoryClickEvent event) {
		
		if (event.getCurrentItem() == null) {
			return;
		}
		
		if (!event.getView().getTitle().contains("Deep Storage Unit")) {
			return;
		}

		Player player = (Player) event.getWhoClicked();
		YamlConfiguration getLastInteractionConfiguration = new FileConfiguration().getInteractionChestConfig();
		YamlConfiguration getChestOwnerConfiguration = new FileConfiguration().getChestOwnerConfig();
		String getChest = getLastInteractionConfiguration.getString(player.getUniqueId().toString()+".Recent Chest Interaction");

		if (getChestOwnerConfiguration.getBoolean(getChest+".Disallow Usage")) {
			player.closeInventory();
			return;
		}
		
		if (getChestOwnerConfiguration.getString(getChest+".Material Used") == null || getChestOwnerConfiguration.getString(getChest+".Material Used").length() <= 0) {
			
			if (event.getInventory().getItem(13) == null) {
				
			return;	
			} else {
				
				List<String> disableItems = plugin.getConfig().getStringList("Disabled Items");
				
				if (disableItems.contains(event.getInventory().getItem(13).getType().toString())) {
					player.sendMessage(plugin.translateChatColor("&b[Deep Storage Unit] &fThis item is disabled"));
					return;
				}
				
				getChestOwnerConfiguration.set(getChest+".Material Used", event.getInventory().getItem(13).getType().toString());
				getChestOwnerConfiguration.set(getChest+".Material ID", event.getInventory().getItem(13).getDurability());
				getChestOwnerConfiguration.set(getChest+".Stored", event.getInventory().getItem(13).getAmount());
				
				File file = new File(plugin.getDataFolder(), "Chest Owners.yml");
				try {
					getChestOwnerConfiguration.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.closeInventory();
			}
		}
		
		if (event.getInventory().getItem(13) == null) {

		} else {
			
			getChest = getLastInteractionConfiguration.getString(player.getUniqueId().toString()+".Recent Chest Interaction");
			if (event.getCurrentItem().getType().toString().equals((getChestOwnerConfiguration.getString(getChest+".Material Used")))){
			int getAmount = event.getCurrentItem().getAmount();
			
			if (event.getClick().toString().equalsIgnoreCase("RIGHT") && event.getCurrentItem().getAmount() > 1) {
				
				if (event.getCurrentItem().getAmount() % 2 == 0) {
					double newInt = getAmount/2;
					int roundOddNum = (int) Math.round(newInt);
					newInt = newInt+getChestOwnerConfiguration.getInt(getChest+".Stored");
					player.getInventory().getItem(event.getSlot()).setAmount(roundOddNum);
					getChestOwnerConfiguration.set(getChest+".Stored", newInt);
					event.setCancelled(true);
				} else {
					double oddNum = event.getCurrentItem().getAmount()/2;
					int roundOddNum = (int) Math.round(oddNum);
					player.getInventory().getItem(event.getSlot()).setAmount(roundOddNum);
					roundOddNum = roundOddNum+getChestOwnerConfiguration.getInt(getChest+".Stored")+1;
					getChestOwnerConfiguration.set(getChest+".Stored", roundOddNum);
					event.setCancelled(true);
				}
				
			} else if (event.getClick().toString().equalsIgnoreCase("LEFT") || event.getClick().toString().equalsIgnoreCase("SHIFT_LEFT")) {
				getAmount = getAmount+getChestOwnerConfiguration.getInt(getChest+".Stored");
				if (event.getSlot() == 40 && event.getCurrentItem().hasItemMeta() || event.getSlot() == 13 && event.getCurrentItem().hasItemMeta()) {
					
				} else {
					getChestOwnerConfiguration.set(getChest+".Stored", getAmount);
					player.getInventory().setItem(event.getSlot(), null);
					event.setCancelled(true);
				}

			} else if (event.getClick().toString().equalsIgnoreCase("MIDDLE")){
				
				if (!event.getView().getTitle().contains("Deep Storage Unit")) {
					return;
				}
				
				for ( int i = 0 ; i < player.getInventory().getSize() ; i++ ) {
					
					if (player.getInventory().getItem(i) == null) {
						continue;
					}
					
				if (player.getInventory().getItem(i).getType().toString().equalsIgnoreCase(getChestOwnerConfiguration.getString(getChest+".Material Used").toString())
						&& player.getInventory().getItem(i).getDurability() == getChestOwnerConfiguration.getInt(getChest+".Material ID")) {
						 int addAmount = getChestOwnerConfiguration.getInt(getChest+".Stored")+player.getInventory().getItem(i).getAmount();
						 getChestOwnerConfiguration.set(getChest+".Stored", addAmount);
						 player.getInventory().setItem(i, null);
				}
				
				}
				
			}else {
				event.setCancelled(true);
			}
			player.openInventory(new ChestInventory().getChestInventory(player));
			File file = new File(plugin.getDataFolder(), "Chest owners.yml");
			try {
				getChestOwnerConfiguration.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@EventHandler
	public void exportItems(InventoryClickEvent event) {
		
		if (event.getCurrentItem() == null) {
			return;
		}
		
		if (!event.getView().getTitle().contains("Deep Storage Unit")) {
			return;
		}
		
		Player player = (Player) event.getWhoClicked();
		YamlConfiguration getLastInteractionConfiguration = new FileConfiguration().getInteractionChestConfig();
		YamlConfiguration getChestOwnerConfiguration = new FileConfiguration().getChestOwnerConfig();
		
		String getChest = getLastInteractionConfiguration.getString(player.getUniqueId().toString()+".Recent Chest Interaction");
		
		if (getChestOwnerConfiguration.getBoolean(getChest+".Disallow Usage")) {
			player.closeInventory();
			return;
		}
		
		if (event.getSlot() == 40) {
				
				
				int stored = getChestOwnerConfiguration.getInt(getChest+".Stored");
					if (event.getClick().toString().equalsIgnoreCase("LEFT")) {
						if (stored > 63) {
						for (int i = 0; i < player.getInventory().getSize() ; i++ ) {
						if (player.getInventory().getItem(i) == null) {
						player.getInventory().setItem(i, new ItemStack(Material.getMaterial(getChestOwnerConfiguration.getString(getChest+".Material Used")),
								64,  (byte) getChestOwnerConfiguration.getInt(getChest+".Material ID"))); 
						int subtract = stored-64;
						getChestOwnerConfiguration.set(getChest+".Stored", subtract);
						break;
					}
							if (i == player.getInventory().getSize()-1) {
									player.closeInventory();
									player.sendMessage(plugin.translateChatColor("&c[Deep Storage Unit] &bNo room in inventory"));
							}
						}
					} else {
						if (stored == 0) {
							return;
						}
						for (int i = 0; i < player.getInventory().getSize() ; i++ ) {
							if (player.getInventory().getItem(i) == null) {
								int restore = 64-stored;
							player.getInventory().setItem(i, new ItemStack(Material.getMaterial(getChestOwnerConfiguration.getString(getChest+".Material Used")),
									64-restore,  (byte) getChestOwnerConfiguration.getInt(getChest+".Material ID"))); 

							getChestOwnerConfiguration.set(getChest+".Stored", 0);
							break;
						}
								if (i == player.getInventory().getSize()-1) {
										player.closeInventory();
										player.sendMessage(plugin.translateChatColor("&c[Deep Storage Unit] &bNo room in inventory"));
							}
						}
					}
				} else if (event.getClick().toString().equalsIgnoreCase("SHIFT_LEFT")) {
					int countAmountOfEmptySpaces = 0;
					for (int i = 0; i < player.getInventory().getSize() ; i++ ) {
						
					if (player.getInventory().getItem(i) == null) {
						countAmountOfEmptySpaces++;
						
						if (player.getInventory().getSize()-1 == i) {
							
							
						}
					}
					}
					if (countAmountOfEmptySpaces == 0) {
						player.sendMessage(plugin.translateChatColor("&c[Deep Storage Unit] &bNo room in inventory"));
					}
					countAmountOfEmptySpaces = countAmountOfEmptySpaces*64;
				
							if (countAmountOfEmptySpaces <= getChestOwnerConfiguration.getInt(getChest+".Stored")) {
								if (stored == 0) {
									return;
								}
									player.getInventory().addItem(new ItemStack(Material.getMaterial(getChestOwnerConfiguration.getString(getChest+".Material Used")),
									countAmountOfEmptySpaces, (byte) getChestOwnerConfiguration.getInt(getChest+".Material ID")));
					
									countAmountOfEmptySpaces=stored-countAmountOfEmptySpaces;
									getChestOwnerConfiguration.set(getChest+".Stored", countAmountOfEmptySpaces);
									
				} else {
					
					if (stored < 64) {
						if (stored == 0) {
							return;
						}
						int lowerThan64=64-stored;
						
						player.getInventory().addItem(new ItemStack(Material.getMaterial(getChestOwnerConfiguration.getString(getChest+".Material Used")),
						64-lowerThan64, (byte) getChestOwnerConfiguration.getInt(getChest+".Material ID")));
						
						getChestOwnerConfiguration.set(getChest+".Stored", 0);
						
					} else {
						if (stored == 0) {
							return;
						}
						player.getInventory().addItem(new ItemStack(Material.getMaterial(getChestOwnerConfiguration.getString(getChest+".Material Used")),
							getChestOwnerConfiguration.getInt(getChest+".Stored"), (byte) getChestOwnerConfiguration.getInt(getChest+".Material ID")));
						
						getChestOwnerConfiguration.set(getChest+".Stored", 0);
						
						}
						
					}
				}
				try {
					getChestOwnerConfiguration.save(new File(plugin.getDataFolder(), "Chest Owners.yml"));
				} catch (IOException e) {
					e.printStackTrace();
			}
		}
	}
}
