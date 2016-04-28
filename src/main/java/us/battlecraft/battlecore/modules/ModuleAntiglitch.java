package us.battlecraft.battlecore.modules;

import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.module.SimpleModule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ModuleAntiglitch extends SimpleModule {

	public ModuleAntiglitch() {
		super("antiglitch", "Antiglitch module", "Boomclaw", "1.0.0");
	}

	public void onFirstEnable() {
		this.addListener((Listener) new Listener() {
			@EventHandler(ignoreCancelled = true)
			public void onPlace(final BlockPlaceEvent event) {
				final Location location = event.getBlock().getLocation();

				if (!location.getWorld().getEnvironment().equals((Object) World.Environment.NETHER)) {
					return;
				}
				
				if (location.getY() < 122.0) {
					return;
				}
				
				event.setCancelled(true);
				
				Locale.sendError(event.getPlayer(), "battlecore.antiglitch");
			}
		});
	}

}
