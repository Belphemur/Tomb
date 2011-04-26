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
package be.Balor.bukkit.Tomb;

import org.bukkit.Location;
import org.bukkit.block.Sign;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class Tomb {
	protected Sign sign;
	protected int deaths = 0;
	protected String worldName;
	protected String playerName;
	protected String reason;
	protected Location deathLoc;

	/**
	 * 
	 */
	public Tomb(Sign sign) {
		this.sign = sign;
		worldName=sign.getBlock().getWorld().getName(); 
	}

	/**
	 * Increment the number of deaths
	 */
	public void addDeath() {
		deaths++;
		sign.setLine(2, deaths+" Deaths");
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
		sign.setLine(3, reason);
	}
	/**
	 * @param player the player to set
	 */
	public void setPlayer(String player) {
		this.playerName = player;
		sign.setLine(1, player);
	}
	/**
	 * @param deathLoc the deathLoc to set
	 */
	public void setDeathLoc(Location deathLoc) {
		this.deathLoc = deathLoc;
	}
	/**
	 * @return the deathLoc
	 */
	public Location getDeathLoc() {
		return deathLoc;
	}
	/**
	 * @return the player
	 */
	public String getPlayer() {
		return playerName;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @return the sign
	 */
	public Sign getSign() {
		return sign;
	}
	/**
	 * @return the deaths
	 */
	public int getDeaths() {
		return deaths;
	}
	/**
	 * @return the world
	 */
	public String getWorld() {
		return worldName;
	}

}
