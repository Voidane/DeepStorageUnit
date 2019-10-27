package org.voidane.dsu;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.voidane.dsu.chest.ChestLocationOnLook;
import org.voidane.dsu.chest.CommandGiveChest;
import org.voidane.dsu.chest.CraftDSU;
import org.voidane.dsu.event.DSUInteractionInventory;
import org.voidane.dsu.event.DSUblockInteract;
import org.voidane.dsu.event.DSUbroken;
import org.voidane.dsu.event.DSUplacement;

public class Plugin extends JavaPlugin {

	public Plugin() {
		
	}
	
	public void onEnable() {
		new FileConfiguration();
		Bukkit.getServer().getConsoleSender().sendMessage("[Deep Storage Unit] Successfully ( attempted ) to load or create file configurations");
		new DSUplacement(this);
		new DSUblockInteract(this);
		new DSUInteractionInventory(this);
		new DSUbroken(this);
		new CraftDSU();
		Bukkit.getServer().getConsoleSender().sendMessage("[Deep Storage Unit] Successfully loaded in classes into method: Block Interaction.class , Inventory Interaction.class ,"+
		" Breaking Units.class , Unit Crafting Recipe.class");
		Bukkit.getServer().getConsoleSender().sendMessage("[Deep Storage Unit] Successfully triggered packages from the ide: org.voidane.dsu , org.voidane.dsu.chest , " + 
		"org.voidane.dsu.event");
		Bukkit.getServer().getConsoleSender().sendMessage("[Deep Storage Unit] Using version v1.0 , by Author: Voidane");
		
		ShapedRecipe craftDSU = new ShapedRecipe(new CommandGiveChest().chestItemStack(1));
		craftDSU.shape("%%%","%*%","%%%");
		craftDSU.setIngredient('%', Material.OBSIDIAN);
		craftDSU.setIngredient('*', Material.ENDER_PEARL);
		
		Bukkit.getServer().addRecipe(craftDSU);
		
	}
	
	public void onDisable() {
		
	}
	
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("deepstorageunit") || cmd.getName().equalsIgnoreCase("dsu")) {
			
			if (!hasPermission(player, "dsu.dsu")) {
				player.sendMessage(translateChatColor("&b[Deep Storage Unit] &c You do not have access to permission &fdsu.dsu"));
				return true;
			}
			
			
			if (noArgs(player,args.length)) {
				return true;
			}
			
			if (secondArgs(player, args.length, Arrays.asList(args))) {
				return true;
			}
			
			if (giveCommandDSU(player, args.length, Arrays.asList(args))) {
				return true;
			}
			
			if (addUseToChestPlayer(player, args.length,Arrays.asList(args))) {
				return true;
			}
			
			if (removeUserFromChest(player, args.length,Arrays.asList(args))) {
				return true;
			}
			
			if (reloadPlugin(player,args.length,args[0])) {
				return true;
			}
			
		return true;
		}
		return false;
	}
	
	
	
	
	
	private boolean reloadPlugin(Player player, int length, String string) {
		
		if (length == 1) {
			if (string.equalsIgnoreCase("reload") || string.equalsIgnoreCase("rl")) {
				if (hasPermission(player, "dsu.reload")) {
					reloadConfig();
					player.sendMessage("&b[Deep Storage Unit] &fConfig.yml reloaded");
				} else {
					player.sendMessage(translateChatColor("&b[Deep Storage unit] &fyou do not have permission for dsu.reload"));
				}
			}
			return false;
		} else {
			player.sendMessage(translateChatColor("&b[Deep Storage Unit] &fTo many args"));
		}
		
		return false;
	}

	private boolean noArgs(Player player, int args) {
		if (args == 0) {
		player.sendMessage(translateChatColor("&b[Deep Storage Unit]&a Help and information"));
		player.sendMessage(translateChatColor("&a/dsu help &f: Information on what commands there are"));
		return true;
		}
	return false;
	}
	
	
	
	
	
	private boolean secondArgs(Player player, int argLength, List<String> args) {	
		if (argLength == 1 && args.get(0).equalsIgnoreCase("help")) {
			
			if (!hasPermission(player, "dsu.help")) {
				player.sendMessage(translateChatColor("&b[Deep Storage Unit] &c You do not have access to permission &fdsu.help"));
				return true;
			}
			
			String[] commandList = {
					"&b[Deep Storage Unit] &aCommands List",
					"&a/dsu help &f: Show helpful information",
					"&a/dsu give &f: Attempt to give a player a unit",
					"&a/dsu allow &f: Allow a player to have access to a unit",
					"&a/dsu remove &f: Remove players from a unit",
					"&a/dsu reload &f: Reload the config.yml file"
					};
			player.sendMessage(translateChatColorArray(commandList));
			return true;
			
		} else if (argLength == 1 && args.get(0).equalsIgnoreCase("give")) {
			
			if (!hasPermission(player, "dsu.give")) {
				player.sendMessage(translateChatColor("&b[Deep Storage Unit] &c You do not have access to permission &fdsu.give"));
				return true;
			}
			
			String[] commandList = {
					"&b[Deep Storage Unit] &aCommand /dsu give",
					"&a/dsu give &b<player> <amount 1-64>"};
			player.sendMessage(translateChatColorArray(commandList));
			return true;
		
		} else if (argLength == 1 && args.get(0).equalsIgnoreCase("allow")) {
			if (!hasPermission(player, "dsu.allow")) {
				player.sendMessage(translateChatColor("&b[Deep Storage Unit] &c You do not have access to permission &fdsu.allow"));
				return true;
			}
			player.sendMessage(translateChatColor("&b[Deep Storage Unit] &a/dsu allow &f<player>"));
			return true;
		
		} else if (argLength == 1 && args.get(0).equalsIgnoreCase("remove")) {
			if (hasPermission(player, "dsu.remove")) {
				return true;
			}
			player.sendMessage(translateChatColor("&b[Deep Storage Unit] &a/dsu remove &f<player>"));
			return true;
		}
		return false;
	}
	
	
	
	
	
	@SuppressWarnings("deprecation")
	private boolean giveCommandDSU(Player player, int argLength, List<String> args) {
		
		if (!args.get(0).equalsIgnoreCase("give")) {
			return false;
		}
		
		if (argLength == 2) {
			String[] commandList = {
					"&b[Deep Storage Unit] &aCommand /dsu give",
					"&a/dsu give "+args.get(1)+"&b <amount 1-64>"};
			player.sendMessage(translateChatColorArray(commandList));
			return true;
		
		} else if (argLength == 3 && Integer.valueOf(args.get(2)) > 0 && Integer.valueOf(args.get(2)) < 65) {
			
			if (!getRecieverPlayer(Bukkit.getServer().getPlayer(args.get(1)))) {
				player.sendMessage(translateChatColor("&b[Deep Storage Unit] &fplayer is in a disabled world to recieve this item"));
				return true;
			}
			
			String[] commandList = {
					"&b[Deep Storage Unit] &a"+args.get(1)+" was given "+args.get(2) + " Deep Storage Units"};
			player.sendMessage(translateChatColorArray(commandList));
			int grabCommandInt = Integer.parseInt(args.get(2));
			
			if (Bukkit.getPlayer(args.get(1)) != null) {
				
				
				
			new CommandGiveChest().onCommandGiveChest(Bukkit.getServer().getPlayer(args.get(1)),grabCommandInt);
			Bukkit.getServer().getPlayer(args.get(1)).sendMessage(translateChatColor("&b[Deep Storage Unit] &aYou recieved "+args.get(2)+ " Deep Storage Units"));
			return true;
			} else {
				Bukkit.getConsoleSender().sendMessage("[ Deep Storage Unit] Error player "+args.get(1)+ " does not exist on the server");
				return false;
			}
			
		}
		return false;
	}

	
	
	
	
	private boolean getRecieverPlayer(Player player) {
		
		if (getConfig().getBoolean("Use Disable Worlds")) {
			List<String> disableWorlds = getConfig().getStringList("Disabled Worlds");
			if (disableWorlds.contains(player.getWorld().getName())) {
				return false;
			}
			
		} else if (getConfig().getBoolean("Use Enable Worlds")) {
			List<String> enableWorlds = getConfig().getStringList("Enabled Worlds");
			if (enableWorlds.contains(player.getWorld().getName())) {
				return true;
			} else {
				return false;
			}
			
		}
		return true;
	}
	
	
	
	
	
	private boolean addUseToChestPlayer(Player player, int argLength, List<String> args) {
		if (!args.get(0).equalsIgnoreCase("allow")) {
			return false;
		}
		
		if (argLength == 2) {
			new ChestLocationOnLook().getTargetBlock(player, args.get(1));
			return true;
		}
		return false;
	}
	
	
	
	
	
	private boolean removeUserFromChest(Player player, int argLength, List<String> args) {
		
		if (!args.get(0).equalsIgnoreCase("remove")) {
			return false;
		}
		
		if (argLength == 2) {
			new ChestLocationOnLook().removePlayerFromChest(player, args.get(1));
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	public String translateChatColor(String chat) {
		chat = ChatColor.translateAlternateColorCodes('&', chat);
		return chat;
	}
	
	
	
	
	
	
	public String[] translateChatColorArray(String[] chat) {
		for ( int i = 0 ; i < chat.length ; i++ ) {
		chat[i] = ChatColor.translateAlternateColorCodes('&', chat[i]);
		}
		return chat;
	}
	
	
	
	
	
	public boolean hasPermission(Player player, String commandOrAction) {
	
		if (player.hasPermission(commandOrAction)) {
			return true;
		}
		return false;
	}
	
	
}
