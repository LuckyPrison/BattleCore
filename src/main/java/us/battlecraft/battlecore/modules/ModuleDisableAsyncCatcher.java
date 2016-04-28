package us.battlecraft.battlecore.modules;

import org.spigotmc.AsyncCatcher;

import com.ulfric.lib.api.module.SimpleModule;

public class ModuleDisableAsyncCatcher extends SimpleModule {
	private boolean set;

	public ModuleDisableAsyncCatcher() {
		super("disable-async-catcher", "Async catcher disabler", "Packet", "1.0.0");
	}

	public void onFirstEnable() {
		this.set = AsyncCatcher.enabled;
	}

	public void postEnable() {
		AsyncCatcher.enabled = false;
	}

	public void postDisable() {
		AsyncCatcher.enabled = this.set;
	}

}
