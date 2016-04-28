package us.battlecraft.battlecore.modules;

import org.bukkit.command.CommandExecutor;

import com.ulfric.lib.api.command.SimpleCommand;
import com.ulfric.lib.api.hook.Hooks;
import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.time.TimeUtils;

public class ModulePlaytime extends SimpleModule {
	public ModulePlaytime() {
		super("play-time", "Playtime module", "Packet", "1.0.0");
	}

	public void onFirstEnable() {
		this.addCommand("playtime", (CommandExecutor) new SimpleCommand() {
			public void run() {
				Locale.send(this.getPlayer(), "battlecore.playtime", new Object[] { TimeUtils
						.millisecondsToString(Hooks.DATA.getPlayerDataAsLong(this.getUniqueId(), "data.playtime")) });
			}
		});
	}
}
