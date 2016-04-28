package us.battlecraft.battlecore.modules;

import java.util.Optional;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;

import com.ulfric.lib.api.command.Argument;
import com.ulfric.lib.api.command.SimpleCommand;
import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.module.SimpleModule;

public class ModuleBuycast extends SimpleModule {
	public ModuleBuycast() {
		super("buycast", "Buycast module", "Packet", "1.0.0");
	}

	public void onFirstEnable() {
		this.addCommand("buycast", (CommandExecutor) new SimpleCommand() {
			{
				this.withArgument(Argument.REQUIRED_OFFLINE_PLAYER);
				this.withIndexUnusedArgs();
			}

			public void run() {
				Locale.sendMass("battlecore.buycast",
						new Object[] { ((OfflinePlayer) this.getObject("player")).getName(),
								Optional.ofNullable(this.getUnusedArgs()).orElse("unknown") });
			}
		});
	}
}
