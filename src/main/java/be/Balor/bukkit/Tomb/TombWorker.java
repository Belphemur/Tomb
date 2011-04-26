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

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;
import com.nijiko.coelho.iConomy.iConomy;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class TombWorker {
	private static PermissionHandler permission = null;
	public static final Logger log = Logger.getLogger("Minecraft");
	private static iConomy iConomy = null;
	private static TombWorker instance;
	private HashMap<String, HashMap<String, Boolean>> permissions = new HashMap<String, HashMap<String, Boolean>>();

	/**
	 * Singleton
	 * 
	 * @return
	 */
	public static TombWorker getInstance() {
		if (instance == null)
			instance = new TombWorker();
		return instance;
	}

	private TombWorker() {

	}

	/**
	 * Check the permissions
	 * 
	 * @param player
	 * @param perm
	 * @return boolean
	 */
	public boolean hasPerm(Player player, String perm) {
		return hasPerm(player, perm, true);
	}

	/**
	 * Check the permission with the possibility to disable the error msg
	 * 
	 * @param player
	 * @param perm
	 * @param errorMsg
	 * @return
	 */
	public boolean hasPerm(Player player, String perm, boolean errorMsg) {
		if (permission == null)
			return true;
		String playerName = player.getName();
		if (permissions.containsKey(playerName)) {
			if (permissions.get(playerName).containsKey(perm))
				return permissions.get(playerName).get(perm);

			if (permission.has(player, perm)) {
				permissions.get(playerName).put(perm, true);
				return true;
			} else {
				permissions.get(playerName).put(perm, false);
				if (errorMsg)
					player.sendMessage(ChatColor.RED + "You don't have the Permissions to do that "
							+ ChatColor.BLUE + "(" + perm + ")");
			}
		} else {
			permissions.put(playerName, new HashMap<String, Boolean>());
			if (permission.has(player, perm)) {
				permissions.get(playerName).put(perm, true);
				return true;
			} else {
				permissions.get(playerName).put(perm, false);
				if (errorMsg)
					player.sendMessage(ChatColor.RED + "You don't have the Permissions to do that "
							+ ChatColor.BLUE + "(" + perm + ")");
			}

		}

		return false;

	}

	/**
	 * iConomy plugin
	 * 
	 * @return
	 */
	public static iConomy getiConomy() {
		return iConomy;
	}

	/**
	 * Set iConomy Plugin
	 * 
	 * @param plugin
	 * @return
	 */
	public static boolean setiConomy(iConomy plugin) {
		if (iConomy == null) {
			iConomy = plugin;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Permission plugin
	 * 
	 * @return
	 */
	public static PermissionHandler getPermission() {
		return permission;
	}

	/**
	 * Set iConomy Plugin
	 * 
	 * @param plugin
	 * @return
	 */
	public static boolean setPermission(PermissionHandler plugin) {
		if (permission == null) {
			permission = plugin;
		} else {
			return false;
		}
		return true;
	}
}
