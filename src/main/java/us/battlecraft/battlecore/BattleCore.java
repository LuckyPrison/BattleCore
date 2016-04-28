package us.battlecraft.battlecore;

import com.ulfric.lib.api.hook.Hooks;
import com.ulfric.lib.api.module.Plugin;

import us.battlecraft.battlecore.hook.Eco;
import us.battlecraft.battlecore.locale.LocaleImport;
import us.battlecraft.battlecore.modules.ModuleAntiglitch;
import us.battlecraft.battlecore.modules.ModuleAutomessage;
import us.battlecraft.battlecore.modules.ModuleBorder;
import us.battlecraft.battlecore.modules.ModuleBuycast;
import us.battlecraft.battlecore.modules.ModuleClearlag;
import us.battlecraft.battlecore.modules.ModuleDisableAsyncCatcher;
import us.battlecraft.battlecore.modules.ModuleDoubleOrNothing;
import us.battlecraft.battlecore.modules.ModuleEnderpearlcooldown;
import us.battlecraft.battlecore.modules.ModuleNoWithers;
import us.battlecraft.battlecore.modules.ModuleOnline;
import us.battlecraft.battlecore.modules.ModulePlaytime;
import us.battlecraft.battlecore.modules.ModuleQuickfixes;
import us.battlecraft.battlecore.modules.ModuleQuoteOfTheDay;
import us.battlecraft.battlecore.modules.ModuleRainbowSheep;
import us.battlecraft.battlecore.modules.ModuleReclaim;
import us.battlecraft.battlecore.modules.ModuleSilkSpawners;
import us.battlecraft.battlecore.modules.ModuleTabmenu;
import us.battlecraft.battlecore.modules.ModuleTangoDownMessages;
import us.battlecraft.battlecore.modules.ModuleWelcome;
import us.battlecraft.battlecore.modules.ModuleWorldport;

public class BattleCore extends Plugin {

	private static BattleCore INSTANCE;

	public static BattleCore get() {
		return BattleCore.INSTANCE;
	}

	public void load() {
		BattleCore.INSTANCE = this;

		this.withSubModule(new ModuleAntiglitch());
		this.withSubModule(new ModuleClearlag());
		this.withSubModule(new ModuleSilkSpawners());
		this.withSubModule(new ModuleAutomessage());
		this.withSubModule(new ModuleRainbowSheep());
		this.withSubModule(new ModuleTangoDownMessages());
		this.withSubModule(new ModuleDoubleOrNothing());
		this.withSubModule(new ModuleEnderpearlcooldown());
		this.withSubModule(new ModuleTabmenu());
		this.withSubModule(new ModuleAntiglitch());
		this.withSubModule(new ModuleQuoteOfTheDay());
		this.withSubModule(new ModuleDisableAsyncCatcher());
		this.withSubModule(new ModuleBorder());
		this.withSubModule(new ModulePlaytime());
		this.withSubModule(new ModuleWorldport());
		this.withSubModule(new ModuleBuycast());
		this.withSubModule(new ModuleReclaim());
		this.withSubModule(new ModuleNoWithers());
		this.withSubModule(new ModuleOnline());
		this.withSubModule(new ModuleWelcome());
		this.withSubModule(new ModuleQuickfixes());
	}

	public void enable() {
		this.registerHook(Hooks.ECON, new Eco());

		new LocaleImport().scanAndImport("en_US");
	}

}