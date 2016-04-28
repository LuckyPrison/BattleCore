package us.battlecraft.battlecore.modules;

import java.util.UUID;

import org.bukkit.command.CommandExecutor;

import com.ulfric.lib.api.command.SimpleCommand;
import com.ulfric.lib.api.command.arg.ArgStrategy;
import com.ulfric.lib.api.hook.Hooks;
import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.math.RandomUtils;
import com.ulfric.lib.api.module.SimpleModule;

public class ModuleDoubleOrNothing extends SimpleModule {
	public ModuleDoubleOrNothing() {
		super("double-or-nothing", "Double or nothing gambling", "Packet", "1.0.0");
	}

	public void onFirstEnable() {
		this.addCommand("doubleornothing", (CommandExecutor) new SimpleCommand() {
			{
				this.withArgument("price", (ArgStrategy) ArgStrategy.DOUBLE, "battlecore.don_amount_needed");
			}

			public void run() {
				final double price = (double) this.getObject("price");
				final UUID uuid = this.getPlayer().getUniqueId();
				
				if (!Hooks.ECON.takeMoney(uuid, price, "")) {
					Locale.sendError(this.getPlayer(), "battlecore.don_cannot_afford", new Object[0]);
					return;
				}
				
				if (!RandomUtils.nextBoolean()) {
					Locale.send(this.getPlayer(), "battlecore.don_lost", new Object[0]);
					return;
				}
				
				final double dp = price * 2.0;
				Hooks.ECON.giveMoney(uuid, dp, "");
				Locale.send(this.getPlayer(), "battlecore.don_won", new Object[] { price, dp });
			}
		});
	}
}
