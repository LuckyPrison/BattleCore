package us.battlecraft.battlecore.modules;

import org.bukkit.Material;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.task.Tasks;

public class ModuleNoWithers extends SimpleModule {
	public ModuleNoWithers() {
		super("no-withers", "Wither disabling module", "Packet", "1.0.0");
	}

	public void onFirstEnable() {
		this.addListener((Listener) new Listener() {
			@EventHandler(ignoreCancelled = true)
			public void onSpawn(final EntitySpawnEvent event) {
				if (!(event.getEntity() instanceof Wither)) {
					return;
				}
				final Wither wither = (Wither) event.getEntity();
				wither.getWorld().dropItemNaturally(wither.getLocation(), new ItemStack(Material.NETHER_STAR));
				Tasks.runLater(wither::remove, 1L);
			}
		});
	}
}
