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

import java.util.HashMap;

import org.bukkit.block.Block;

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
	public synchronized void load()
	{
		tombs = saveSys.load();
		log.info("[Tomb] Tombs loaded !");
	}

}
