/*This file is part of Tomb.

    Tomb is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Tomb is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Tomb.  If not, see <http://www.gnu.org/licenses/>.*/
package be.Balor.bukkit.Tomb;

import java.io.Serializable;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.Block;

import be.Balor.Workers.TombWorker;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class TombSave implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 312699013882578456L;
	protected ArrayList<LocSave> signBlocks = new ArrayList<LocSave>();
	protected int deaths;
	protected String player;
	protected String reason;
	protected LocSave deathLoc;

	public TombSave(Tomb tomb) {
		for (Block b : tomb.getSignBlocks())
			signBlocks.add(new LocSave(b));
		reason = tomb.getReason();
		player = tomb.getPlayer();
		deaths = tomb.getDeaths();
		if (tomb.getDeathLoc() != null)
			deathLoc = new LocSave(tomb.getDeathLoc());
		else
			deathLoc = null;
	}

	public Tomb load() {
		Tomb tomb = new Tomb();
		for (LocSave loc : signBlocks) {
			try {
				tomb.addSignBlock(loc.getBlock());
			} catch (IllegalArgumentException e) {
				TombWorker.log.info("[Tomb] One of the tomb of " + player + " was destroyed. :\n"+loc);
			}
		}
		if (deathLoc != null)
			tomb.setDeathLoc(deathLoc.getLoc());
		else
			tomb.setDeathLoc(null);
		tomb.setDeaths(deaths);
		tomb.setPlayer(player);
		tomb.setReason(reason);
		return tomb;
	}
}

class LocSave implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3307231458020728331L;
	private double x;
	private double y;
	private double z;
	private String world;

	public LocSave(Location loc) {
		x = loc.getX();
		y = loc.getY();
		z = loc.getZ();
		world = loc.getWorld().getName();
	}
	public LocSave(Block block) {
		this(block.getLocation());
	}

	public Location getLoc() {
		return new Location(TombPlugin.getBukkitServer().getWorld(world), x, y, z);
	}

	public Block getBlock() {
		return TombPlugin.getBukkitServer().getWorld(world).getBlockAt(getLoc());
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocSave={World="+world+", x="+x+", y="+y+", z="+z+"}";
		
	}
}
