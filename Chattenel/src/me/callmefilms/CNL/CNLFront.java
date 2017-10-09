package me.callmefilms.CNL;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CNLFront extends JavaPlugin{
	
	Channel global = new Channel("global");
	Channel local = new Channel("local");
	Channel teamOne = new Channel("team1");
	Channel teamTwo = new Channel("teamTwo");
	
	List<Channel> channels = new ArrayList<Channel>();
	
	public void onEnable() {
		
		this.channels.add(global);
		this.channels.add(local);
		this.channels.add(teamOne);
		this.channels.add(teamTwo);
		
		Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ChatRunner(), this);
		
		Bukkit.getServer().getPluginCommand("channel").setExecutor(new Commands());
		
	}
	
	public Channel getPlayerChannel(Player player) {
		for(int i = 0; i < channels.size(); i++) {
			for(int j = 0; j < channels.get(i).getPlayers().size(); j++) {
				if(channels.get(i).getPlayers().get(j).getUniqueId().toString() == player.getUniqueId().toString()) {
					return channels.get(i);
				}
			}
		}
		return global;
	}
	
	public List<Channel> getPlayerChannels(String playerUUID) {
		List<Channel> channels = new ArrayList<Channel>();
		
		for(int i = 0; i < this.channels.size(); i++) {
			for(int j = 0; j < this.channels.get(i).getPlayers().size(); j++) {
				if(this.channels.get(i).getPlayers().get(j).getUniqueId().toString().equalsIgnoreCase(playerUUID)) {
					channels.add(this.channels.get(i));
				}
			}
		}
		
		return channels;
	}
	
	public boolean playerIsInChannel(String playerUUID, Channel channel) {
		List<Channel> playerChans = getPlayerChannels(playerUUID);
		for(int i = 0; i < playerChans.size(); i++) {
			if(playerChans.get(i) == channel) {
				return true;
			}
		}
		return false;
	}
	
	public void switchChan(Player player, Channel channel) {
		String playerUUID = player.getUniqueId().toString();
		List<Channel> playerChans = getPlayerChannels(playerUUID);
		if(playerIsInChannel(playerUUID, channel)) {
			player.sendMessage("You are already in the " + channel.getName() + " channel.");
		} else {
			for(int i = 0; i < playerChans.size(); i++) {
				playerChans.get(i).removePlayer(player);
			}
			channel.addPlayer(player);
			player.sendMessage("Switched to " + channel.getName() + " successfully.");
		}
	}
	
	public Channel getChannel(String name) {
		for(int i = 0; i < channels.size(); i++) {
			if(channels.get(i).getName().equalsIgnoreCase(name)) {
				return channels.get(i);
			}
		}
		return null;
	}
	
	public class JoinListener implements Listener {
		
		@EventHandler
		public void onPlayerJoinEvent(PlayerJoinEvent event) {
			Player player = event.getPlayer();
			for(int i = 0; i < channels.size(); i++) {
				for(int j = 0; j < channels.get(i).getPlayers().size(); j++) {
					if(channels.get(i).getPlayers().get(j).getUniqueId().toString() == player.getUniqueId().toString()) {
						channels.get(i).removePlayer(player);
					}
				}
			}
			global.addPlayer(player);
		}
		
	}
	
	public class ChatRunner implements Listener {
		
		@EventHandler
		public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
			Player player = event.getPlayer();
			String playerUUID = player.getUniqueId().toString();
			String message = event.getMessage();
			List<Channel> playerChans = getPlayerChannels(playerUUID);
			for(int i = 0; i < playerChans.size(); i++) {
				Channel currentChan = playerChans.get(i);
				for(int j = 0; j < currentChan.getPlayers().size(); j++) {
					currentChan.getPlayers().get(j).sendMessage(ChatColor.GREEN + "[" + currentChan.getName().charAt(0) + "] " + player.getDisplayName() + ChatColor.RESET + ChatColor.BOLD + ">> " + ChatColor.RESET + message);
				}
			}
		}
		
	}
	
	public class Commands implements CommandExecutor {
		
		@Override
		public boolean onCommand(CommandSender sndr, Command cmd, String label, String[] args) {
			if(!(sndr instanceof Player)) {
				sndr.sendMessage("This command can not be executed by console personnel. Please try again in-game.");
			} else {
				Player player = (Player) sndr;
				String playerUUID = player.getUniqueId().toString();
				switch(cmd.getName()) {
				case "channel":
					if(args.length < 1) {
//						DISPLAY HELP MENU
					} else {
						switch(args[0].toLowerCase()) {
						case "status":
							player.sendMessage("You are in the following channels:");
							List<Channel> playerChans = getPlayerChannels(playerUUID);
							for(int i = 0; i < playerChans.size(); i++) {
								player.sendMessage(playerChans.get(i).getName());
							}
							break;
						case "switch":
							if(args.length < 2) {
//								DISPLAY SWITCH HELP MENU
							} else {
								switch(args[1].toLowerCase()) {
								case "global":
									switchChan(player, global);
									break;
								case "local":
									switchChan(player, local);
									break;
								case "team1":
									switchChan(player, teamOne);
									break;
								case "teamtwo":
									switchChan(player, teamTwo);
									break;
								default:
//									DISPLAY LIST OF CHANNELS
									break;
								}
							}
							break;
						default:
//							DISPLAY HELP MENU
						}
					}
				}
			}
			return true;
		}
		
	}
	
}