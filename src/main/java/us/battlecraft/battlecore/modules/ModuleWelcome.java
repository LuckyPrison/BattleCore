package us.battlecraft.battlecore.modules;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ulfric.lib.api.command.SimpleCommand;
import com.ulfric.lib.api.java.Strings;
import com.ulfric.lib.api.java.Wrapper;
import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.math.RandomUtils;
import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.player.PlayerFirstJoinEvent;

public class ModuleWelcome extends SimpleModule {
	List<String> messages;

	public ModuleWelcome() {
		super("welcome", "A module to help welcome new players to the server", "1.0.0", "Packet");
		this.messages = Lists.newArrayList();
		this.withConf();
	}

	public void postEnable() {
		for (String string : Optional.ofNullable(this.getConf().getConf().getStringList("messages"))
				.orElseGet((Supplier<? extends List<String>>) ImmutableList::of)) {
			string = string.trim();
			if (string.isEmpty()) {
				continue;
			}
			this.messages.add(string);
		}
		if (!this.messages.isEmpty()) {
			return;
		}
		this.messages.add("Welcome, {0}!");
	}

	public void postDisable() {
		this.messages.clear();
	}

	public void onFirstEnable() {
		final Set<UUID> uuids = Sets.newHashSet();
		final Wrapper<String> name = (Wrapper<String>) new Wrapper();
		this.addListener((Listener) new Listener() {
			@EventHandler
			public void onJoin(final PlayerFirstJoinEvent event) {
				name.setValue(event.getPlayer().getName());
				uuids.clear();
			}
		});
		this.addCommand("welcome", (CommandExecutor) new SimpleCommand() {
			public void run() {
				final Player sender = this.getPlayer();
				final String player = (String) name.getValue();

				if (player == null || player.isEmpty()) {
					Locale.sendError(sender, "battlecore.welcome_err_none", new Object[0]);
					return;
				}

				if (!uuids.add(sender.getUniqueId())) {
					Locale.sendError(sender, "battlecore.welcome_err_already", new Object[0]);
					return;
				}

				final String message = (String) RandomUtils.randomValueFromList((List) ModuleWelcome.this.messages);

				if (message == null) {
					Locale.sendError(sender, "battlecore.welcome_err_format_not_found", new Object[0]);
					return;
				}
				sender.chat(Strings.format(message, (Object) player));
			}
		});
	}
}
