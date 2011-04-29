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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import be.Balor.Workers.TombWorker;
import be.Balor.bukkit.Tomb.Tomb;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class PlayerListenerTomb extends PlayerListener {
	private TombWorker worker;

	public PlayerListenerTomb() {
		worker = TombWorker.getInstance();
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		// TODO
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				&& (worker.getConfig().getBoolean("allow-tp", true) || worker.hasPerm(p, "tomb.tp",
						false))) {
			Block block = event.getClickedBlock();
			if (block.getType().equals(Material.WALL_SIGN)
					|| block.getType().equals(Material.SIGN_POST)) {
				if (worker.hasTomb(p.getName())) {
					Tomb tomb = worker.getTomb(p.getName());
					if (tomb.hasSign(block)) {
						Location toTp;
						if ((toTp = tomb.getDeathLoc()) != null
								&& worker.iConomyCheck(p, "deathtp-price")) {
							p.teleport(toTp);
							if (worker.getConfig().getBoolean("reset-deathloc", true))
								tomb.setDeathLoc(null);

						}
					}
				}
			}
		}
	}

}
