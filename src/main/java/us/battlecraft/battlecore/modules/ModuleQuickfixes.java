package us.battlecraft.battlecore.modules;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.ulfric.lib.api.hook.Hooks;
import com.ulfric.lib.api.inventory.ItemUtils;
import com.ulfric.lib.api.inventory.MaterialUtils;
import com.ulfric.lib.api.inventory.WeaponType;
import com.ulfric.lib.api.math.RandomUtils;
import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.task.Tasks;

public class ModuleQuickfixes extends SimpleModule {
	public ModuleQuickfixes() {
		super("quickfixes", "Quickfixes module", "Boomclaw", "1.0.1");
	}

	public void onFirstEnable() {
		for (World world : Bukkit.getWorlds()) {
			world.setDifficulty(Difficulty.NORMAL);
		}

		this.addListener(new Listener() {
			@EventHandler
			public void onJoin(PlayerJoinEvent event) {
				event.setJoinMessage(null);
			}

			@EventHandler
			public void onQuit(PlayerQuitEvent event) {
				event.setQuitMessage(null);
			}

			/*
			@EventHandler(ignoreCancelled = true)
			public void onPvP(EntityDamageByEntityEvent event) {
				Entity damaged = event.getEntity();
				Entity damager = event.getDamager();

				if (!(damaged instanceof Player) || !(damager instanceof Player))
					return;

				ItemStack item = ((Player) damager).getItemInHand();

				if (item == null)
					return;

				WeaponType type = MaterialUtils.getWeaponType(item.getType());

				if (type == null)
					return;

				double scale = 0;

				switch (type) {
				case AXE:
					scale = 1.133;
					break;

				case SWORD:
					scale = 1.26;
					break;

				default:
					return;
				}

				double damage = event.getDamage();

				for (ItemStack armor : ((Player) damaged).getInventory().getArmorContents()) {
					if (ItemUtils.isEmpty(armor))
						continue;

					if (!EnchantmentTarget.ARMOR.includes(armor))
						continue;

					damage *= scale;
				}

				event.setDamage(damage);
			}

			@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
			public void onMobSpawn(SpawnerSpawnEvent event) {
				Location location = event.getLocation();
				World world = location.getWorld();

				Entity entity = world.spawnEntity(location, event.getEntityType());
				world.playEffect(location, Effect.MOBSPAWNER_FLAMES, 3);

				if (!(event.getEntity() instanceof Ageable) || !(event.getEntity() instanceof Animals))
					return;

				if (!RandomUtils.randomPercentage(10))
					return;

				// ((Ageable) entity).setBaby();
			}

			@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
			public void onRegen(EntityRegainHealthEvent event) {
				if (!(event.getEntity() instanceof Player))
					return;

				if (!event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED))
					return;

				Player player = (Player) event.getEntity();

				if (!Hooks.COMBATTAG.isTagged(player))
					return;

				event.setCancelled(true);

				event.setAmount(0D);

				double health = player.getHealth();

				Tasks.runLater(() -> {
					if (player.isDead())
						return;

					if (player.getHealth() <= health)
						return;

					player.setHealth(health);
				}, 1);
			} */
		});
	}
}
