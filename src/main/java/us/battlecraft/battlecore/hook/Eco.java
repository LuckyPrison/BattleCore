package us.battlecraft.battlecore.hook;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.ulfric.lib.api.hook.EconHook;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class Eco implements EconHook.IEconHook {
	private Economy economy;

	public Eco() {
		final RegisteredServiceProvider<Economy> economyProvider = (RegisteredServiceProvider<Economy>) Bukkit
				.getServicesManager().getRegistration((Class) Economy.class);
		if (economyProvider == null) {
			return;
		}
		this.economy = (Economy) economyProvider.getProvider();
	}

	public boolean takeMoney(final UUID uuid, final double amount, final String reason) {
		final EconomyResponse response = this.economy.withdrawPlayer(Bukkit.getOfflinePlayer(uuid).getName(), amount);
		return response != null && response.transactionSuccess();
	}

	public void giveMoney(final UUID uuid, final double amount, final String reason) {
		this.economy.depositPlayer(Bukkit.getOfflinePlayer(uuid).getName(), amount);
	}

	public double getMoney(final UUID uuid) {
		return this.economy.getBalance(Bukkit.getOfflinePlayer(uuid).getName());
	}
}
