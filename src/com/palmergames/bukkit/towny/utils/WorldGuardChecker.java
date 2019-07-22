package com.palmergames.bukkit.towny.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.listeners.TownyWorldListener;
import com.palmergames.bukkit.towny.object.TownyPermission.ActionType;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class WorldGuardChecker {
	public static final int DEFAULT_WG_PRIORITY = 10;
	private static Towny plugin;
	private static boolean isWorldGuardActive = false;
	private static RegionContainer wgRegionContainer;
	
	public static void init(Towny plugin)
	{
		WorldGuardChecker.plugin = plugin;
		try {
			WorldGuard wg = plugin.getWorldGuard();
			WorldGuardChecker.isWorldGuardActive = true;
			WorldGuardChecker.wgRegionContainer = wg.getPlatform().getRegionContainer();
		} catch (TownyException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean isTopPriority(Location l)
	{
		if(isWorldGuardActive)
		{

			BukkitWorld wbw = plugin.getWorldListener().worldCache.get(l.getWorld());
			if(wbw == null)
			{
				plugin.getWorldListener().loadWorld(l.getWorld());
				wbw = plugin.getWorldListener().worldCache.get(l.getWorld());
			}

			ApplicableRegionSet regionSet = wgRegionContainer.get(wbw).getApplicableRegions(BlockVector3.at(l.getX(), l.getY(), l.getZ()));
			for(ProtectedRegion region : regionSet.getRegions())
			{
				if(region.getPriority() > DEFAULT_WG_PRIORITY)
				{
					return false;
				}
			}
			
		}
		
		return true;
	}
	
}
