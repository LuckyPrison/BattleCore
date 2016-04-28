package us.battlecraft.battlecore.modules;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.ulfric.lib.api.chat.Chat;
import com.ulfric.lib.api.command.SimpleCommand;
import com.ulfric.lib.api.hook.DataHook;
import com.ulfric.lib.api.hook.Hooks;
import com.ulfric.lib.api.java.Booleans;
import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.math.RandomUtils;
import com.ulfric.lib.api.module.ModuleTask;
import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.task.ATask;
import com.ulfric.lib.api.task.Tasks;
import com.ulfric.lib.api.time.Ticks;

public class ModuleAutomessage extends SimpleModule {
	private List<String> automessages;
	private long delay;

	public ModuleAutomessage() {
		super("automessage", "Automated help and tips module", "Packet", "1.0.0-REL");
		this.withConf();
		this.addTask(new ModuleTask((ATask) new TaskAutomessage(), ModuleTask.StartType.AUTOMATIC));
	}

	public void onFirstEnable() {
		this.addCommand("tips", (CommandExecutor) new SimpleCommand() {
			public void run() {
				final DataHook.IPlayerData data = Hooks.DATA.getPlayerData(this.getUniqueId());
				final boolean newValue = !data.getBoolean("battlecore.notips");
				data.set("battlecore.notips", (Object) newValue);
				Locale.sendSuccess(this.getPlayer(), "battlecore.tips_toggled",
						new Object[] { Booleans.fancify(newValue) });
			}
		});
	}

	public void postEnable() {
		this.automessages = (List<String>) this.getConf().getValueAsStringList("messages").stream()
				.map(str -> Chat.color(str).replace("<n>", "\n").replace("<P>", "locale.automessage.prefix"))
				.collect(Collectors.toList());
		this.delay = this.getConf().getConf().getLong("delay", Ticks.fromSeconds(30L));
	}

	private class TaskAutomessage extends ATask {
		public void start() {
			super.start();
			this.setTaskId(Tasks.runRepeatingAsync((Runnable) this, ModuleAutomessage.this.delay).getTaskId());
		}

		public void run() {
			final String message = (String) RandomUtils.randomValueFromList(ModuleAutomessage.this.automessages);
			final Matcher match;

			if (!message.contains("locale.") || !(match = Locale.pattern().matcher(message)).find()) {
				Locale.sendMass((String) RandomUtils.randomValueFromList(ModuleAutomessage.this.automessages),
						new Object[0]);
				return;
			}

			final List<String> list = Lists.newArrayListWithExpectedSize(match.groupCount());

			do {
				list.add(match.group());
			} while (match.find());

			for (final Player player : Bukkit.getOnlinePlayers()) {
				if (Hooks.DATA.getPlayerDataAsBoolean(player.getUniqueId(), "battlecore.notips")) {
					continue;
				}
				String clone = message;
				for (final String replace : list) {
					clone = clone.replace(replace, Locale.getMessage(player, replace.substring(7)));
				}
				player.sendMessage(clone.replace("<player>", player.getName()));
			}
		}
	}
}
