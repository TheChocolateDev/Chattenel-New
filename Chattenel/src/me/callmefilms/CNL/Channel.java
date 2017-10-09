package me.callmefilms.CNL;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Channel {
	
	private String name;
	private List<Player> players;
	
	public Channel(String name) {
		this.name = name;
		this.players = new ArrayList<Player>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<Player> getPlayers() {
		return this.players;
	}
	
	public void addPlayer(Player player) {
		if(getPlayers().contains(player)) {
			return;
		} else {
			this.players.add(player);
		}
	}
	
	public void removePlayer(Player player) {
		if(getPlayers().contains(player)) {
			this.getPlayers().remove(player);
		}
	}
	
	public void runMessage(Player player, String message) {
		for(int i = 0; i < getPlayers().size(); i++) {
			getPlayers().get(i).sendMessage(ChatColor.GREEN + "[" + getName().charAt(0) + "] " + player.getName() + ": " + ChatColor.DARK_GREEN + message);
		}
	}
	
}