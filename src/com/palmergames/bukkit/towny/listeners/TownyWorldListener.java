package com.palmergames.bukkit.towny.listeners;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldInitEvent;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.exceptions.AlreadyRegisteredException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.TownyWorld;
import com.sk89q.worldedit.bukkit.BukkitWorld;

public class TownyWorldListener implements Listener {

	//private final Towny plugin;

	private Map<org.bukkit.World, BukkitWorld> _worldCache;
	public Map<org.bukkit.World, BukkitWorld> worldCache;
	
	public TownyWorldListener(Towny instance) {

		//plugin = instance;
		this._worldCache = new HashMap<>();
		this.worldCache = Collections.unmodifiableMap(this._worldCache);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldLoad(WorldLoadEvent event) {

		newWorld(event.getWorld());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldInit(WorldInitEvent event) {

		newWorld(event.getWorld());

	}
	
	public void loadWorld(World bw)
	{
		BukkitWorld wbw = new BukkitWorld(bw);
		this._worldCache.put(bw, wbw);
	}

	private void newWorld(World bw) {

		//String worldName = event.getWorld().getName();
		try {
			TownyUniverse.getDataSource().newWorld(bw.getName());
			TownyWorld world = TownyUniverse.getDataSource().getWorld(bw.getName());
			if (world == null)
				TownyMessaging.sendErrorMsg("Could not create data for " + bw.getName());
			else {
				if (!TownyUniverse.getDataSource().loadWorld(world)) {
					// First time world has been noticed
					TownyUniverse.getDataSource().saveWorld(world);
				}
				BukkitWorld wbw = _worldCache.getOrDefault(bw, null);
				if(wbw == null)
				{
					wbw = new BukkitWorld(bw);
					this._worldCache.put(bw, wbw);
				}
			}
		} catch (AlreadyRegisteredException e) {
			// Allready loaded			
		} catch (NotRegisteredException e) {
			TownyMessaging.sendErrorMsg("Could not create data for " + bw.getName());
			e.printStackTrace();
		}

	}
}
