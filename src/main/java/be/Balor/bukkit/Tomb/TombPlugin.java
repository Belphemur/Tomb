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

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import be.Balor.Listeners.DeathListener;
import be.Balor.Listeners.PlayerListenerTomb;
import be.Balor.Listeners.PluginListener;
import be.Balor.Listeners.SignListener;
import be.Balor.Listeners.WorldSaveListener;
import be.Balor.Workers.Downloader;
import be.Balor.Workers.TombWorker;

/**
 * @author Balor (aka Antoine Aflalo)
 * 
 */
public class TombPlugin extends JavaPlugin {
	private static Server server = null;
	public static final Logger log = Logger.getLogger("Minecraft");

	public static Server getBukkitServer() {
		return server;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	public void onDisable() {
		if (Downloader.pluginName == null) {
			TombWorker.getInstance().save();
			server.getScheduler().cancelTasks(this);
			TombWorker.setDisable(true);
			TombWorker.killInstance();
			log.info("[" + this.getDescription().getName() + "]" + " Disabled");
		}

	}

	private void setupListeners() {
		PluginListener pL = new PluginListener();
		SignListener sL = new SignListener();
		PlayerListenerTomb pLt = new PlayerListenerTomb();
		DeathListener dL = new DeathListener();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.SIGN_CHANGE, sL, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLUGIN_ENABLE, pL, Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, pLt, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, pLt, Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, sL, Priority.High, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, dL, Priority.High, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, dL, Priority.High, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, pLt, Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, pLt, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, pLt, Priority.Normal, this);
		pm.registerEvent(Event.Type.WORLD_SAVE, new WorldSaveListener(), Priority.Normal, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.Plugin#onEnable()
	 */
	public void onEnable() {
		if (!new File("lib" + File.separator, "Register-1.8.jar").exists()) {
			Downloader.pluginName = "Tomb";
			Downloader.install("http://gestdown.info/minecraft/Register-1.8.jar",
					"Register-1.8.jar");
			getServer().reload();
		} else {
			server = getServer();
			setupListeners();
			TombWorker.setDisable(false);
			TombWorker.getInstance().setPluginInstance(this);
			log.info("[" + this.getDescription().getName() + "]" + " (version "
					+ this.getDescription().getVersion() + ") Enabled");
			TombWorker.getInstance().load();
		}

	}

}
