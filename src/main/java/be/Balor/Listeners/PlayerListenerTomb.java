/************************************************************************
 * This file is part of GiftPost.									
 *																		
 * GiftPost is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by	
 * the Free Software Foundation, either version 3 of the License, or		
 * (at your option) any later version.									
 *																		
 * GiftPost is distributed in the hope that it will be useful,	
 * but WITHOUT ANY WARRANTY; without even the implied warranty of		
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the			
 * GNU General Public License for more details.							
 *																		
 * You should have received a copy of the GNU General Public License
 * along with GiftPost.  If not, see <http://www.gnu.org/licenses/>.
 ************************************************************************/
package be.Balor.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import be.Balor.Workers.TombWorker;
import be.Balor.Workers.Worker;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class PlayerListenerTomb extends PlayerListener {
	private Worker worker;

	public PlayerListenerTomb() {
		worker = TombWorker.getInstance();
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		// TODO
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = event.getClickedBlock();
			if (block.getType().equals(Material.WALL_SIGN)
					|| block.getType().equals(Material.SIGN_POST)) {
				Sign sign = (Sign) block.getState();
				//TODO
			}
		}
	}

}
