package us.battlecraft.battlecore.modules;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;

import com.ulfric.lib.api.command.SimpleCommand;
import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.module.SimpleModule;

public class ModuleOnline extends SimpleModule {
	public ModuleOnline() {
		super("online", "Online command module", "Packet", "1.0.0");
	}

	public void onFirstEnable() {
		this.addCommand("online", (CommandExecutor) new SimpleCommand() {
			public void run() {
				Locale.send(this.getSender(), "battlecore.online", new Object[] { Bukkit.getOnlinePlayers().size() });
			}
		});
	}
}
