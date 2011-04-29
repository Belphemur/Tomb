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
import java.util.HashMap;

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
	protected TombPlugin pluginInstance;
	protected SaveSystem saveSys;
	protected Configuration config;
	public String graveDigger = "[" + ChatColor.GOLD + "Gravedigger" + ChatColor.WHITE + "] ";

	public static TombWorker getInstance() {
		if (instance == null)
			instance = new TombWorker();
		return instance;
	}

	private TombWorker() {

	}

	/**
	 * @param pluginInstance
	 *            the pluginInstance to set
	 */
	public void setPluginInstance(TombPlugin pluginInstance) {
		this.pluginInstance = pluginInstance;
		saveSys = new SaveSystem(pluginInstance.getDataFolder().getPath());
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
			config.save();
		}
		config.load();
	}

	/**
	 * Function to check if iConomy is found and the setting used for it.
	 * 
	 * @param player
	 * @param action
	 * @return
	 */
	public boolean iConomyCheck(Player player, String action) {
		if (Worker.getiConomy() != null && this.getConfig().getBoolean("use-iConomy", true)
				&& !this.hasPerm(player, "tomb.free", false)) {
			if (com.nijiko.coelho.iConomy.iConomy.getBank().hasAccount(player.getName())) {
				if (com.nijiko.coelho.iConomy.iConomy.getBank().getAccount(player.getName())
						.getBalance() < this.getConfig().getDouble(action, 1.0)) {
					player.sendMessage(graveDigger + ChatColor.RED + "You don't have enough "
							+ com.nijiko.coelho.iConomy.iConomy.getBank().getCurrency()
							+ " to paying me.");
					return false;
				} else {
					com.nijiko.coelho.iConomy.iConomy.getBank().getAccount(player.getName())
							.subtract(this.getConfig().getDouble(action, 1.0));
					if (this.getConfig().getDouble(action, 1.0) != 0)
						player.sendMessage(graveDigger + this.getConfig().getDouble(action, 1.0)
								+ " " + com.nijiko.coelho.iConomy.iConomy.getBank().getCurrency()
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
	public Tomb getTomb(String player) {

		return tombs.get(player);
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
		log.info("[Tomb] Tombs saved !");
	}

	public synchronized void load() {
		tombs = saveSys.load();
		log.info("[Tomb] Tombs loaded !");
	}

}
