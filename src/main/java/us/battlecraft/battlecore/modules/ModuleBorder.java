package us.battlecraft.battlecore.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.ConfigurationSection;

import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.persist.ConfigFile;

public class ModuleBorder extends SimpleModule {
	public ModuleBorder() {
		super("border", "World borders module", "Packet", "1.0.0");
		this.withConf();
	}

	public void postEnable() {
		final ConfigFile conf = this.getConf();
		for (final String key : conf.getKeys(false)) {
			final World world = Bukkit.getWorld(key);
			if (world == null) {
				continue;
			}
			final ConfigurationSection section = conf.getSection(key);
			final WorldBorder border = world.getWorldBorder();
			border.setCenter(new Location(world, 0.0, 0.0, 0.0));
			border.setSize(section.getDouble("size", 1000.0));
			border.setWarningDistance(section.getInt("warning", 1));
		}
	}
}
