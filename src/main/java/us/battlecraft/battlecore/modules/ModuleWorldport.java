package us.battlecraft.battlecore.modules;

import org.bukkit.World;
import org.bukkit.command.CommandExecutor;

import com.ulfric.lib.api.command.SimpleCommand;
import com.ulfric.lib.api.command.arg.ArgStrategy;
import com.ulfric.lib.api.module.SimpleModule;

public class ModuleWorldport extends SimpleModule {
	public ModuleWorldport() {
		super("world-port", "World teleporting module", "Packet", "1.0.0");
	}

	public void onFirstEnable() {
		this.addCommand("worldport", (CommandExecutor) new SimpleCommand() {
			{
				this.withArgument("world", (ArgStrategy) ArgStrategy.WORLD, "battlecore.world_required");
			}

			public void run() {
				this.getPlayer().teleport(((World) this.getObject("world")).getSpawnLocation());
			}
		});
	}
}
