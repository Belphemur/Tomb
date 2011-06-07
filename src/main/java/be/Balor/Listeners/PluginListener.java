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


import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import be.Balor.Workers.TombWorker;
import be.Balor.bukkit.Tomb.TombPlugin;

import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijikokun.register.payment.Methods;

import org.bukkit.plugin.Plugin;

/**
 * @author Balor (aka Antoine Aflalo)
 *
 */
public class PluginListener extends ServerListener {
	private Methods Methods;
   /**
 * 
 */
public PluginListener() {
	this.Methods = new Methods();
}
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
        if(TombWorker.getPayement() == null) {           
            // Check to see if we need a payment method
            if (!this.Methods.hasMethod()) {
                if(this.Methods.setMethod(event.getPlugin())) {
                    // You might want to make this a public variable inside your MAIN class public Method Method = null;
                    // then reference it through this.plugin.Method so that way you can use it in the rest of your plugin ;)
                    TombWorker.setMethod(this.Methods.getMethod());
                    System.out.println("[Tomb] Payment method found (" + TombWorker.getPayement().getName() + " version: " + TombWorker.getPayement().getVersion() + ")");
                }
            }
        }
    }
}
