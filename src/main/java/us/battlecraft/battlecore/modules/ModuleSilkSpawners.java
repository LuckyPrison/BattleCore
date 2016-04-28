package us.battlecraft.battlecore.modules;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ulfric.lib.api.block.BlockUtils;
import com.ulfric.lib.api.command.SimpleCommand;
import com.ulfric.lib.api.command.arg.ArgStrategy;
import com.ulfric.lib.api.entity.EntityUtils;
import com.ulfric.lib.api.inventory.EnchantUtils;
import com.ulfric.lib.api.inventory.ItemBuilder;
import com.ulfric.lib.api.inventory.ItemUtils;
import com.ulfric.lib.api.java.StringUtils;
import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.player.PlayerUtils;

public class ModuleSilkSpawners extends SimpleModule {
	public ModuleSilkSpawners() {
		super("silk-spawners", "Silktouch spawners module", "Packet", "1.0.0-REL");
	}

	public void onFirstEnable() {
		this.addListener((Listener) new Listener() {
			@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
			public void onBreak(final BlockBreakEvent event) {
				final Block block = event.getBlock();
				if (!block.getType().equals((Object) Material.MOB_SPAWNER)) {
					return;
				}
				final Player player = event.getPlayer();
				final ItemStack item = player.getItemInHand();
				if (EnchantUtils.getLevel(item, Enchantment.SILK_TOUCH) == 0) {
					return;
				}
				event.setCancelled(true);
				if (!player.hasPermission("battlecore.silktouch")) {
					Locale.sendError(player, "battlecore.silktouch_premium", new Object[0]);
					return;
				}
				// event.setNotify(true)
				final CreatureSpawner spawner = (CreatureSpawner) block.getState();
				final EntityType type = spawner.getSpawnedType();
				final ItemBuilder builder = new ItemBuilder();
				builder.withType(Material.MOB_SPAWNER);
				builder.withDurability(type.getTypeId());
				builder.withLore(
						ChatColor.YELLOW + WordUtils.capitalizeFully(type.name().toLowerCase().replace('_', ' ')));
				block.setType(Material.AIR);
				block.getWorld().dropItem(block.getLocation(), builder.build());
			}

			@EventHandler(ignoreCancelled = true)
			public void onSpawn(final CreatureSpawnEvent event) {
				if (!event.getSpawnReason().equals((Object) CreatureSpawnEvent.SpawnReason.SPAWNER)) {
					return;
				}
				if (event.getLocation().getWorld().getEnvironment().equals((Object) World.Environment.NETHER)) {
					return;
				}
				event.setCancelled(true);
			}

			@EventHandler(ignoreCancelled = true)
			public void onSpawnerPlace(final BlockPlaceEvent event) {
				final Block block = event.getBlock();
				if (!block.getType().equals((Object) Material.MOB_SPAWNER)) {
					return;
				}
				final ItemStack item = event.getItemInHand();
				final ItemMeta meta;
				if (!item.hasItemMeta() || !(meta = item.getItemMeta()).hasLore()) {
					return;
				}
				final CreatureSpawner spawner = (CreatureSpawner) block.getState();
				final EntityType type = EntityUtils
						.parse(ChatColor.stripColor((String) meta.getLore().get(0)).replace(' ', '_'));
				if (type == null) {
					event.getPlayer().sendMessage("Bad type: " + meta.getLore().get(0));
					return;
				}
				spawner.setSpawnedType(type);
			}

			@EventHandler(ignoreCancelled = true)
			public void onSpawnerChange(final PlayerInteractEvent event) {
				final Block block = event.getClickedBlock();
				if (BlockUtils.isEmpty(block)) {
					return;
				}
				if (!block.getType().equals((Object) Material.MOB_SPAWNER)) {
					return;
				}
				if (!ItemUtils.is(event.getPlayer().getItemInHand(), Material.MONSTER_EGG)) {
					return;
				}
				event.setCancelled(true);
			}
		});

		this.addCommand("spawner", new CommandSpawner());
	}

	private class CommandSpawner extends SimpleCommand {

		private CommandSpawner() {
			this.withEnforcePlayer();
			this.withArgument("type", (ArgStrategy) ArgStrategy.ENTITY);
		}

		public void run() {
			final Player player = this.getPlayer();
			if (!this.hasObject("type")) {
				Collection<String> allowed = new ArrayList<String>();

				for (EntityType entity : EntityType.values()) {
					if (player.hasPermission("battlecore.spawner." + entity.name())) {
						allowed.add(entity.name());
					}
				}

				Locale.send(player, "battlecore.spawner_available_mobs", StringUtils.mergeNicely(allowed));
				return;
			}
			final EntityType type = (EntityType) this.getObject("type");
			if (!this.isValidType(type)) {
				Locale.sendError(player, "battlecore.spawner_entity_invalid", this.typeToString(type));
				return;
			}
			final String name2 = type.name().toLowerCase().replace("_", "");
			if (!player.hasPermission("battlecore.spawner." + name2)) {
				Locale.sendError(player, "battlecore.spawner_no_permission", name2);
				return;
			}
			final Block block = PlayerUtils.getTargetBlock(player, 6);
			if (BlockUtils.isEmpty(block) || !block.getType().equals((Object) Material.MOB_SPAWNER)) {
				Locale.sendError(player, "battlecore.spawner_not_in_range", new Object[0]);
				return;
			}
			((CreatureSpawner) block.getState()).setSpawnedType(type);
		}

		private boolean isValidType(final EntityType type) {
			return type.isAlive() && type.isSpawnable() && !type.equals((Object) EntityType.PLAYER);
		}

		private String typeToString(final EntityType type) {
			return type.name().toLowerCase().replace('_', ' ');
		}
	}

}
