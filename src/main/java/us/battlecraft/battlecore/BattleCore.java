package us.battlecraft.battlecore;

import com.ulfric.lib.api.module.Plugin;

import us.battlecraft.battlecore.locale.LocaleImport;
import us.battlecraft.battlecore.modules.ModuleAntiglitch;
import us.battlecraft.battlecore.modules.ModuleClearlag;

public class BattleCore extends Plugin {

	private static BattleCore INSTANCE;

	public static BattleCore get() {
		return BattleCore.INSTANCE;
	}

	public void load() {
		BattleCore.INSTANCE = this;
		
		this.withSubModule(new ModuleAntiglitch());
		this.withSubModule(new ModuleClearlag());
	}

	public void enable() {
		new LocaleImport().scanAndImport("en_US");
	}

}