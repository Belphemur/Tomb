/************************************************************************
 * This file is part of DeathTP+.									
 ************************************************************************/
package be.Balor.Listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import be.Balor.Workers.LocaleWorker;
import be.Balor.Workers.TombWorker;
import be.Balor.bukkit.Tomb.Tomb;

/**
 * @author lonelydime (some modifications by Balor)
 * 
 */
public class DeathListener extends EntityListener {
	protected HashMap<String, String> lastPlayerDmg = new HashMap<String, String>();
	protected String beforedamage = "";
	protected TombWorker worker = TombWorker.getInstance();

	public void onEntityDeath(EntityDeathEvent event) {
		beforedamage = "";
		try {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				if (worker.hasTomb(player.getName())) {
					String damagetype = lastPlayerDmg.get(player.getName());
					String[] howtheydied;

					howtheydied = damagetype.split(":");
					Tomb tomb = worker.getTomb(player.getName());
					String signtext;

					if (howtheydied[0].equals("PVP"))
						signtext = LocaleWorker.getInstance().getPvpLocale(howtheydied[2]);
					else
						signtext = LocaleWorker.getInstance().getLocale(
								howtheydied[0].toLowerCase());
					int deathLimit = worker.getConfig().getInt("maxDeaths", 0);
					tomb.addDeath();
					if (deathLimit != 0 && (tomb.getDeaths() % deathLimit) == 0) {
						tomb.resetTombBlocks();
						player.sendMessage(worker.graveDigger
								+ "You reached the number of deaths before tomb reset.("
								+ ChatColor.DARK_RED + deathLimit + ChatColor.WHITE
								+ ") All your tombs are now destroyed.");
					} else {
						tomb.setReason(signtext);
						tomb.setDeathLoc(player.getLocation());
						tomb.updateDeath();
					}
				}

			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {

		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (worker.hasTomb(player.getName()))
				lastDamageDone(player, event);
		}
	}

	/**
	 * Process the damage event, used when the player die.
	 * 
	 * @param player
	 * @param event
	 */
	public void lastDamageDone(Player player, EntityDamageEvent event) {
		String lastdamage = event.getCause().name();
		// player.sendMessage(lastdamage);
		// checks for mob/PVP damage
		if (event instanceof EntityDamageByProjectileEvent) {
			EntityDamageByProjectileEvent mobevent = (EntityDamageByProjectileEvent) event;
			Entity attacker = mobevent.getDamager();
			if (attacker instanceof Ghast) {
				lastdamage = "GHAST";
			} else if (attacker instanceof Monster) {
				lastdamage = "SKELETON";
			} else if (attacker instanceof Player) {
				Player pvper = (Player) attacker;
				String usingitem = pvper.getItemInHand().getType().name();
				if (usingitem == "AIR") {
					usingitem = "BARE_KNUCKLES";
				}
				lastdamage = "PVP:" + usingitem + ":" + pvper.getName();
			}
		}

		else if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent mobevent = (EntityDamageByEntityEvent) event;
			Entity attacker = mobevent.getDamager();

			if (attacker.toString().toLowerCase().matches("craftslime")) {
				lastdamage = "SLIME";
			}

			else if (attacker instanceof Monster) {
				Monster mob = (Monster) attacker;

				if (mob instanceof PigZombie) {
					lastdamage = "PIGZOMBIE";
				} else if (mob instanceof Zombie) {
					lastdamage = "ZOMBIE";
				} else if (mob instanceof Creeper) {
					lastdamage = "CREEPER";
				} else if (mob instanceof Spider) {
					lastdamage = "SPIDER";
				} else if (mob instanceof Skeleton) {
					lastdamage = "SKELETON";
				} else if (mob instanceof Ghast) {
					lastdamage = "GHAST";
				} else if (mob instanceof Slime) {
					lastdamage = "SLIME";
				}
			} else if (attacker instanceof Wolf)
				lastdamage = "WOLF";
			else if (attacker instanceof Player) {
				Player pvper = (Player) attacker;
				String usingitem = pvper.getItemInHand().getType().name();
				if (usingitem == "AIR") {
					usingitem = "fist";
				}
				usingitem = usingitem.toLowerCase();
				usingitem = usingitem.replace("_", " ");
				lastdamage = "PVP:" + usingitem + ":" + pvper.getName();
			}
		}

		if ((beforedamage.equals("GHAST") && lastdamage.equals("BLOCK_EXPLOSION"))
				|| (beforedamage.equals("GHAST") && lastdamage.equals("GHAST"))) {
			lastdamage = "GHAST";
		}

		lastPlayerDmg.put(player.getName(), lastdamage);

		beforedamage = lastdamage;
	}
}
