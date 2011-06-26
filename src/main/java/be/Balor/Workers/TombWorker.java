/************************************************************************
 * This file is part of Tomb.									
 *																		
 * Tomb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by	
 * the Free Software Foundation, either version 3 of the License, or		
 * (at your option) any later version.									
 *																		
 * Tomb is distributed in the hope that it will be useful,	
 * but WITHOUT ANY WARRANTY; without even the implied warranty of		
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the			
 * GNU General Public License for more details.							
 *																		
 * You should have received a copy of the GNU General Public License
 * along with Tomb.  If not, see <http://www.gnu.org/licenses/>.
 ************************************************************************/
package be.Balor.Workers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;
import be.Balor.bukkit.Tomb.Tomb;
import be.Balor.bukkit.Tomb.TombPlugin;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class TombWorker extends Worker {
	private static TombWorker instance;
	protected HashMap<String, Tomb> tombs = new HashMap<String, Tomb>();
	protected static TombPlugin pluginInstance;
	protected SaveSystem saveSys;
	protected Configuration config;
	public String graveDigger = "[" + ChatColor.GOLD + "Gravedigger" + ChatColor.WHITE + "] ";
	public static final Logger workerLog = Logger.getLogger("TombLogger");

	public static TombWorker getInstance() {
		if (instance == null)
			instance = new TombWorker();
		return instance;
	}

	public static void killInstance() {
		workerLog.info("Worker Instance destroyed");
		for (Handler h : workerLog.getHandlers()) {
			h.close();
			workerLog.removeHandler(h);
		}
		instance = null;
	}

	private TombWorker() {
	}

	/**
	 * @param pluginInstance
	 *            the pluginInstance to set
	 */
	public void setPluginInstance(TombPlugin pluginInstance) {
		TombWorker.pluginInstance = pluginInstance;
		String path = pluginInstance.getDataFolder().getPath();
		saveSys = new SaveSystem(path);
		try {

			// This block configure the logger with handler and formatter
			File logger = new File(path + File.separator + "log.txt");
			if (logger.exists())
				logger.delete();
			FileHandler fh = new FileHandler(logger.getPath(), true);
			workerLog.addHandler(fh);
			workerLog.setUseParentHandlers(false);
			workerLog.setLevel(Level.ALL);
			LogFormatter formatter = new LogFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
			workerLog.info("Logger created");

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		configInit();
	}

	/**
	 * Check if the config file exist, load it if exist else create it
	 */
	private void configInit() {
		config = pluginInstance.getConfiguration();
		if (!new File(pluginInstance.getDataFolder().getPath() + File.separator + "config.yml")
				.exists()) {
			config.setProperty("reset-deathloc", true);
			config.setProperty("use-iConomy", true);
			config.setProperty("creation-price", 10.0D);
			config.setProperty("deathtp-price", 50.0D);
			config.setProperty("allow-tp", true);
			config.setProperty("maxTombStone", 0);
			config.setProperty("TombKeyword", "[Tomb]");
			config.setProperty("use-tombAsSpawnPoint", true);
			config.setProperty("cooldownTp", 5.0D);
			config.save();
			workerLog.info("Config created");
		}
		config.load();
	}

	/**
	 * Return the number of tomb the player has.
	 * 
	 * @param player
	 * @return
	 */
	public int getNbTomb(String player) {
		if (hasTomb(player))
			return tombs.get(player).getNbSign();
		else
			return 0;
	}

	/**
	 * Function to check if iConomy is found and the setting used for it.
	 * 
	 * @param player
	 * @param action
	 * @return
	 */
	public boolean iConomyCheck(Player player, String action) {
		if (Worker.getPayement() != null && this.getConfig().getBoolean("use-iConomy", true)
				&& !this.hasPerm(player, "tomb.free", false)) {
			if (Worker.getPayement().hasAccount(player.getName())) {
				if (!Worker.getPayement().getAccount(player.getName())
						.hasEnough(this.getConfig().getDouble(action, 1.0))) {
					player.sendMessage(graveDigger + ChatColor.RED + "You don't have "
							+ Worker.getPayement().format(this.getConfig().getDouble(action, 1.0))
							+ " to pay me.");
					return false;
				} else {
					Worker.getPayement().getAccount(player.getName())
							.subtract(this.getConfig().getDouble(action, 1.0));
					if (this.getConfig().getDouble(action, 1.0) != 0)
						player.sendMessage(graveDigger
								+ Worker.getPayement().format(
										this.getConfig().getDouble(action, 1.0))
								+ ChatColor.DARK_GRAY + " used to paying me.");
					return true;
				}

			} else {
				player.sendMessage(graveDigger + ChatColor.RED
						+ "You must have an account to paying me.");
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the config
	 */
	public Configuration getConfig() {
		return config;
	}

	/**
	 * @return the pluginInstance
	 */
	public TombPlugin getPlugin() {
		return pluginInstance;
	}

	/**
	 * Check if the player have already a tomb
	 * 
	 * @param player
	 * @return
	 */
	public boolean hasTomb(String player) {
		return tombs.containsKey(player);
	}

	/**
	 * Add the tomb
	 * 
	 * @param player
	 * @param sign
	 */
	public void setTomb(String player, Tomb tomb) {
		tombs.put(player, tomb);
	}

	/**
	 * Remove the tomb of the player.
	 * 
	 * @param player
	 */
	public void removeTomb(String player) {
		tombs.remove(player);
	}

	/**
	 * 
	 * @param player
	 * @return the tombs of the player
	 */
	public Tomb getTomb(final String player) {
		Tomb t = null;

		if ((t = tombs.get(player)) != null)
			return t;
		else {
			String found = null;
			String lowerName = player.toLowerCase();
			int delta = Integer.MAX_VALUE;
			for (String p : tombs.keySet()) {
				if (p.toLowerCase().startsWith(lowerName)) {
					int curDelta = p.length() - lowerName.length();
					if (curDelta < delta) {
						found = p;
						delta = curDelta;
					}
					if (curDelta == 0)
						break;
				}
			}
			if (found != null)
				return tombs.get(found);
			else
				return null;
		}
	}

	public Tomb getTomb(Block sign) {
		for (String name : tombs.keySet()) {
			Tomb result;
			if ((result = tombs.get(name)).hasSign(sign))
				return result;
		}
		return null;
	}

	public synchronized void save() {
		saveSys.save(tombs);
		workerLog.info("[SAVE] Tombs saved !");
	}

	public synchronized void load() {
		tombs = saveSys.load();
		workerLog.info("[LOAD] Tombs loaded !");
	}

}
