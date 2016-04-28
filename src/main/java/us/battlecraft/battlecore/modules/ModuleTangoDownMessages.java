package us.battlecraft.battlecore.modules;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.module.SimpleModule;

public class ModuleTangoDownMessages extends SimpleModule {
	public ModuleTangoDownMessages() {
		super("tango-down-messages", "Player death by player messages", "Packet", "1.0.0");
	}

	public void onFirstEnable() {
		this.addListener((Listener) new Listener() {
			@EventHandler
			public void onDie(final PlayerDeathEvent event) {
				final Player entity = event.getEntity();
				if (entity.getKiller() == null) {
					return;
				}
				Locale.sendMass("battlecore.tango_down",
						new Object[] { entity.getName(), entity.getKiller().getName() });
			}
		});
	}
}
