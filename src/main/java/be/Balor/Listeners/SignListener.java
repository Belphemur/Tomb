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
package be.Balor.Listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

import be.Balor.Workers.TombWorker;
import be.Balor.bukkit.Tomb.Tomb;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class SignListener extends BlockListener {
	private TombWorker worker;

	public SignListener() {
		worker = TombWorker.getInstance();
	}

	@Override
	public void onSignChange(SignChangeEvent e) {
		String line0 = e.getLine(0);
		Player p = e.getPlayer();
		boolean admin = false;
		if (line0.indexOf(worker.getConfig().getString("TombKeyword", "[Tomb]")) == 0) {
			if (!e.getLine(1).isEmpty() && worker.hasPerm(p, "tomb.admin"))
				admin = true;
			// Sign check
			Tomb tomb = null;
			String deadName;
			if (admin)
				deadName = e.getLine(1);
			else
				deadName = e.getPlayer().getName();
			if (worker.hasTomb(deadName)) {
				tomb = worker.getTomb(deadName);
				tomb.checkSign();
			}
			// max check
			int maxTombs = worker.getConfig().getInt("maxTombStone", 0);
			if (!admin && maxTombs != 0 && (worker.getNbTomb(deadName) + 1) > maxTombs) {
				p.sendMessage(worker.graveDigger + "You have reached your tomb limit.");
				e.setCancelled(true);
				return;
			}
			// perm and iConomy check
			if ((!admin && !worker.hasPerm(p, "tomb.create"))
					|| !worker.iConomyCheck(p, "creation-price")) {
				e.setCancelled(true);
				return;
			}
			Block block = e.getBlock();
			try {

				if (tomb != null) {
					tomb.addSignBlock(block);
				} else {
					tomb = new Tomb(block);
					tomb.setPlayer(deadName);
					worker.setTomb(deadName, tomb);
				}
				tomb.updateNewBlock();
				if (worker.getConfig().getBoolean("use-tombAsSpawnPoint", true)) {
					tomb.setRespawn(p.getLocation());
					if (admin)
						p.sendMessage(worker.graveDigger + " When " + deadName
								+ " die, he/she will respawn here.");
					else
						p.sendMessage(worker.graveDigger + " When you die you'll respawn here.");
				}
			} catch (IllegalArgumentException e2) {
				p.sendMessage(worker.graveDigger
						+ " It's not a good place for a Tomb. Try somewhere else.");
			}

		}

	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getState() instanceof Sign) {
			Block block = event.getBlock();
			String playerName = event.getPlayer().getName();
			Sign sign = (Sign) block.getState();
			if (sign.getLine(0).indexOf(worker.getConfig().getString("TombKeyword", "[Tomb]")) == 0) {
				if (worker.hasPerm(event.getPlayer(), "tomb.admin")) {
					Tomb tomb;
					if ((tomb = worker.getTomb(block)) != null)
						tomb.removeSignBlock(block);
					return;
				}
				if (worker.hasTomb(playerName)) {
					if (!worker.getTomb(playerName).hasSign(block))
						event.setCancelled(true);
					else
						worker.getTomb(playerName).removeSignBlock(block);
				} else
					event.setCancelled(true);
			}

		}
	}
}
