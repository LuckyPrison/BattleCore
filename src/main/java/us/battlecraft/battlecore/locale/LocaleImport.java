package us.battlecraft.battlecore.locale;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.ulfric.lib.Lib;
import com.ulfric.lib.api.locale.Locale;
import com.ulfric.lib.api.locale.LocaleModule;
import com.ulfric.lib.api.module.IModule;

import us.battlecraft.battlecore.BattleCore;

public class LocaleImport extends Locale {

	public void scanAndImport(String language) {
		List<String> missingKeys = getMissingKeys(language);

		if (missingKeys.isEmpty())
			return;

		FileConfiguration localeConfig = getLocaleConfig(language);

		for (String key : missingKeys) {
			String value = getValue(language, key);

			if (!value.equals("")) {
				localeConfig.set(key, getValue(language, key));
			}
		}

		try {
			localeConfig.save(getLocaleFile(language));
		} catch (IOException e) {
			e.printStackTrace();
		}

		callReload();
	}

	public String getValue(String language, String key) {
		FileConfiguration config = YamlConfiguration
				.loadConfiguration(BattleCore.get().getResource("locale/" + language + ".yml"));

		return config.getString(key, "");
	}

	private List<String> getMissingKeys(String language) {
		List<String> missingKeys = new ArrayList<String>();

		return missingKeys;
	}

	private File getLocaleFile(String language) {
		File dataFolder = new File(Lib.get().getDataFolder(), "locale");

		File[] files = dataFolder.listFiles();

		for (File file : files) {
			if ((file.getName() + ".yml").equals(language)) {
				return file;
			}
		}

		return null;
	}

	private FileConfiguration getLocaleConfig(String language) {
		if (getLocaleFile(language) == null)
			return null;

		return YamlConfiguration.loadConfiguration(getLocaleFile(language));
	}

	private void callReload() {
		Bukkit.getLogger().info("Calling reload for LocaleModule from BattleCore.");

		for (IModule module : Lib.get().getSubModules()) {
			if (module instanceof LocaleModule) {
				module.disable();
				module.enable();
			}
		}
	}

}