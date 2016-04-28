package us.battlecraft.battlecore.modules;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ulfric.lib.api.command.SimpleCommand;
import com.ulfric.lib.api.hook.DataHook;
import com.ulfric.lib.api.hook.Hooks;
import com.ulfric.lib.api.hook.PermissionsExHook;
import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.server.Commands;

public class ModuleReclaim extends SimpleModule {
	private Map<String, CommandList> commands;

	public ModuleReclaim() {
		super("reclaim", "Reclaim module", "Packet", "1.0.0");
		this.commands = Maps.newHashMap();
		this.withConf();
	}

	public void postEnable() {
		final FileConfiguration conf = this.getConf().getConf();
		for (final String key : conf.getKeys(false)) {
			try {
				final CommandList list = new CommandList();
				conf.getStringList(key).forEach(list::addCommand);
				this.commands.put(key, list);
			} catch (Exception exception) {
				System.out.println("Could not parse: " + key);
				exception.printStackTrace();
			}
		}
	}

	public void postDisable() {
		this.commands.clear();
	}

	public void onFirstEnable() {
		this.addCommand("reclaim", (CommandExecutor) new SimpleCommand() {
			public void run() {
				final Player player = this.getPlayer();
				final DataHook.IPlayerData data = Hooks.DATA.getPlayerData(player.getUniqueId());
				if (data.getBoolean("reclaim")) {
					return;
				}
				data.set("reclaim", (Object) true);
				PermissionsExHook.Group[] groups;
				for (int length = (groups = Hooks.PERMISSIONS.user(player)
						.getGroups()).length, i = 0; i < length; ++i) {
					final PermissionsExHook.Group group = groups[i];
					final CommandList list = ModuleReclaim.this.commands.get(group.getName().toLowerCase());
					if (list != null) {
						list.execute(player);
						break;
					}
				}
			}
		});
	}

	public class CommandList implements Iterable<String> {
		private List<String> commands;

		public CommandList() {
			this.commands = Lists.newArrayList();
		}

		public void execute(final Player player) {
			final String name = player.getName();
			this.commands.forEach(cmd -> Commands.dispatch(cmd.replace("{name}", name)));
		}

		public CommandList addCommand(final String command) {
			this.commands.add(command);
			return this;
		}

		public CommandList removeCommand(final String command) {
			this.commands.remove(command);
			return this;
		}

		@Override
		public Iterator<String> iterator() {
			return this.commands.iterator();
		}
	}
}
