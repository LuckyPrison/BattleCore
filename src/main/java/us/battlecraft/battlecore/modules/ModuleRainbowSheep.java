package us.battlecraft.battlecore.modules;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.EnumUtils;
import org.bukkit.DyeColor;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.ulfric.lib.api.math.RandomUtils;
import com.ulfric.lib.api.module.SimpleModule;

public class ModuleRainbowSheep extends SimpleModule {
	private Set<DyeColor> colors;

	public ModuleRainbowSheep() {
		super("rainbow-sheep", "Rainbow sheep like rainbow sheep like rainbow sheep!", "Packet", "1.0.0");
		this.withConf();
	}

	public void postEnable() {
		final Set<DyeColor> badColors = EnumSet.noneOf(DyeColor.class);
		this.getConf().getValueAsStringList("blocked-colors").stream()
				.map(str -> (DyeColor) EnumUtils.getEnum(DyeColor.class, str)).filter(Objects::nonNull)
				.forEach(badColors::add);
		this.colors = EnumSet.noneOf(DyeColor.class);
		Arrays.stream(DyeColor.values()).filter(color -> !badColors.contains(color)).forEach(this.colors::add);
	}

	public void postDisable() {
		this.colors.clear();
	}

	public void onFirstEnable() {
		this.addListener((Listener) new Listener() {
			@EventHandler(ignoreCancelled = true)
			public void onEntitySpawn(final CreatureSpawnEvent event) {
				if (!(event.getEntity() instanceof Sheep)) {
					return;
				}
				final DyeColor color = (DyeColor) RandomUtils.randomValueFromCollection(ModuleRainbowSheep.this.colors);
				if (color == null) {
					return;
				}
				((Sheep) event.getEntity()).setColor(color);
			}
		});
	}
}
