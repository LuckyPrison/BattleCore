package us.battlecraft.battlecore.modules;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.ulfric.lib.api.command.Cooldown;
import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.time.Milliseconds;

public class ModuleEnderpearlcooldown extends SimpleModule {
	private Cooldown cooldown;

	public ModuleEnderpearlcooldown() {
		super("enderpearl-cooldown", "Enderpearl cooldown module", "Packet", "1.0.0-SNAPSHOT");
		this.withConf();
	}

	public void postEnable() {
		this.cooldown = Cooldown.builder().withName("enderpearl")
				.withDefaultDelay(Milliseconds.fromSeconds(this.getConf().getConf().getLong("delay.default", 200L)))
				.build();
	}

	public void onFirstEnable() {
		this.addListener((Listener) new Listener() {
			@EventHandler(ignoreCancelled = true)
			public void onLaunch(final ProjectileLaunchEvent event) {
				if (!event.getEntityType().equals((Object) EntityType.ENDER_PEARL)) {
					return;
				}
				final ProjectileSource source = event.getEntity().getShooter();
				if (!(source instanceof Player)) {
					return;
				}
				final Player player = (Player) source;
				if (!ModuleEnderpearlcooldown.this.cooldown.test(player.getUniqueId()).error(player)) {
					return;
				}
				event.setCancelled(true);
			}
		});
	}
}
