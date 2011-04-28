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

import java.util.ArrayList;
import java.util.InputMismatchException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import be.Balor.Workers.TombWorker;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class Tomb {
	protected ArrayList<Block> signBlocks;
	protected int deaths = 0;
	protected String playerName;
	protected String reason;
	protected Location deathLoc;

	public Tomb() {
		this.signBlocks = new ArrayList<Block>();
	}

	/**
	 * 
	 */
	public Tomb(Block sign) throws InputMismatchException {
		this.signBlocks = new ArrayList<Block>();
		addSignBlock(sign);
	}

	/**
	 * update the sign in the game
	 */
	private void setLine(final int line, final String message) {
		if (!signBlocks.isEmpty())
			TombPlugin.getBukkitServer().getScheduler()
					.scheduleSyncDelayedTask(TombWorker.getInstance().getPlugin(), new Runnable() {
						public void run() {
							Sign sign;
							for (Block block : signBlocks) {
								if (block.getState() instanceof Sign) {
									sign = (Sign) block.getState();
									sign.setLine(line, message);
									sign.update();
								} else
									signBlocks.remove(block);
							}
						}
					});
	}

	/**
	 * Increment the number of deaths
	 */
	public void addDeath() {
		deaths++;
		setLine(2, deaths + " Deaths");
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
		setLine(3, reason);
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(String player) {
		this.playerName = player;
		setLine(1, player);
	}

	/**
	 * @param deathLoc
	 *            the deathLoc to set
	 */
	public void setDeathLoc(Location deathLoc) {
		this.deathLoc = deathLoc;
	}

	/**
	 * @param deaths
	 *            the deaths to set
	 */
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	/**
	 * @return the deathLoc
	 */
	public Location getDeathLoc() {
		return deathLoc;
	}

	/**
	 * @param signBlock
	 *            the signBlock to set
	 */
	public void addSignBlock(Block sign) {
		if (sign.getType() == Material.WALL_SIGN || sign.getType() == Material.SIGN
				|| sign.getType() == Material.SIGN_POST) {
			this.signBlocks.add(sign);
		} else
			throw new InputMismatchException("The block must be a SIGN or WALL_SIGN or SIGN_POST");
	}

	/**
	 * Remove the given sign from the list.
	 * 
	 * @param sign
	 */
	public void removeSignBlock(Block sign) {
		if (hasSign(sign))
			signBlocks.remove(sign);
	}

	/**
	 * Check if the block is used as a tomb.
	 * 
	 * @param sign
	 * @return
	 */
	public boolean hasSign(Block sign) {
		return signBlocks.contains(sign);
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
	 * @return the deaths
	 */
	public int getDeaths() {
		return deaths;
	}

	/**
	 * @return the signBlocks
	 */
	public ArrayList<Block> getSignBlocks() {
		return signBlocks;
	}

	/**
	 * To save the Tomb
	 * 
	 * @return
	 */
	public TombSave save() {
		return new TombSave(this);
	}

}
