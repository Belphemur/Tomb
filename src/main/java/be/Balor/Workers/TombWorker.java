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
import be.Balor.bukkit.Tomb.Tomb;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class TombWorker extends Worker {
	private static TombWorker instance;
	protected HashMap<String, Tomb> tombs = new HashMap<String, Tomb>();

	public static TombWorker getInstance() {
		if (instance == null)
			instance = new TombWorker();
		return instance;
	}

	private TombWorker() {

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

}
