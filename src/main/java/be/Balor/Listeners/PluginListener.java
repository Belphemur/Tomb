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


import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import be.Balor.Workers.TombWorker;
import be.Balor.bukkit.Tomb.TombPlugin;

import com.nijiko.coelho.iConomy.iConomy;
import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.plugin.Plugin;

/**
 * @author Balor (aka Antoine Aflalo)
 *
 */
public class PluginListener extends ServerListener {
   
    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        if(TombWorker.getPermission() == null) {
            Plugin Permissions = TombPlugin.getBukkitServer().getPluginManager().getPlugin("Permissions");
            if (Permissions != null) {
                if(Permissions.isEnabled()) {
                	TombWorker.setPermission(((Permissions) Permissions).getHandler());
                    System.out.println("[Tomb] Successfully linked with Permissions.");
                }
            }
        }
        if(TombWorker.getiConomy() == null) {
            Plugin iConomy = TombPlugin.getBukkitServer().getPluginManager().getPlugin("iConomy");

            if (iConomy != null) {
                if(iConomy.isEnabled()) {
                	TombWorker.setiConomy((iConomy)iConomy);
                    System.out.println("[Tomb] Successfully linked with iConomy.");
                }
            }
        }
    }
}
