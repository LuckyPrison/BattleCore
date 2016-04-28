package us.battlecraft.battlecore.modules;

import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.ulfric.lib.api.chat.Chat;
import com.ulfric.lib.api.command.SimpleCommand;
import com.ulfric.lib.api.hook.DataHook;
import com.ulfric.lib.api.hook.Hooks;
import com.ulfric.lib.api.java.Booleans;
import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.math.RandomUtils;
import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.task.Tasks;

public class ModuleQuoteOfTheDay extends SimpleModule {
	private long delay;
	private String message;

	public ModuleQuoteOfTheDay() {
		super("quote-of-the-day", "QOTD", "Packet", "1.0.0");
		this.withConf();
	}

	public void postEnable() {
		final FileConfiguration conf = this.getConf().getConf();
		this.delay = conf.getLong("delay", 120L);
		final List<String> messages = (List<String>) conf.getStringList("messages");

		if (messages.size() == 0) {
			this.message = "No QOTD value found...";
			return;
		}

		this.message = Chat.color((String) RandomUtils.randomValueFromList((List) messages));
	}

	public void onFirstEnable() {
		this.addCommand("qotd", (CommandExecutor) new SimpleCommand() {
			public void run() {
				final DataHook.IPlayerData data = Hooks.DATA.getPlayerData(this.getUniqueId());
				final boolean newValue = !data.getBoolean("battlecore.noqotd");
				data.set("battlecore.noqotd", (Object) newValue);
				Locale.sendSuccess(this.getPlayer(), "battlecore.qotd_toggled",
						new Object[] { Booleans.fancify(newValue) });
			}
		});
		this.addListener((Listener) new Listener() {
			@EventHandler
			public void onJoin(final PlayerJoinEvent event) {
				final Player player = event.getPlayer();
				if (Hooks.DATA.getPlayerDataAsBoolean(player.getUniqueId(), "battlecore.noqotd")) {
					return;
				}
				Tasks.runLater(
						() -> Locale.send(player, "battlecore.qotd", new Object[] { ModuleQuoteOfTheDay.this.message }),
						ModuleQuoteOfTheDay.this.delay);
			}
		});
	}
}
