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

import org.bukkit.block.Block;
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
		if(!new File(pluginInstance.getDataFolder().getPath()+File.separator+"config.yml").exists())
		{			
			config.setProperty("reset-deathloc", true);
			config.setProperty("use-iConomy", true);
			config.setProperty("creation-price", 10.0D);
			config.setProperty("deathtp-price",50.0D);
			config.save();
		}
		config.load();
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
