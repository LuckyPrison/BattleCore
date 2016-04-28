package us.battlecraft.battlecore.modules;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.ulfric.lib.api.java.Strings;
import com.ulfric.lib.api.module.SimpleModule;
import com.ulfric.lib.api.nms.CraftPlayerVI;
import com.ulfric.lib.api.nms.PacketWrapper;
import com.ulfric.lib.api.nms.Packets;

import net.minecraft.server.v1_8_R3.Packet;

public class ModuleTabmenu extends SimpleModule {
	private PacketWrapper joinPacket;
	private PacketWrapper quitPacket;

	public ModuleTabmenu() {
		super("tab-menu", "Tab menu mod", "Packet", "1.0.0-SNAPSHOT");
		this.withConf();
	}

	public void postEnable() {
		final FileConfiguration conf = this.getConf().getConf();
		this.joinPacket = Packets.wrap((Packet) Packets.newPacketPlayOutPlayerListHeaderFooter(
				Strings.formatF(conf.getString("join.header"), new Object[0]),
				Strings.formatF(conf.getString("join.footer"), new Object[0])));
		this.quitPacket = Packets.wrap((Packet) Packets.newPacketPlayOutPlayerListHeaderFooter(
				Strings.formatF(conf.getString("quit.header"), new Object[0]),
				Strings.formatF(conf.getString("quit.footer"), new Object[0])));
	}

	public void onFirstEnable() {
		this.addListener((Listener) new Listener() {
			@EventHandler
			public void onJoin(final PlayerJoinEvent event) {
				CraftPlayerVI.of(event.getPlayer()).sendPacket((Packet) ModuleTabmenu.this.joinPacket.getValue());
			}

			@EventHandler
			public void onJoin(final PlayerQuitEvent event) {
				CraftPlayerVI.of(event.getPlayer()).sendPacket((Packet) ModuleTabmenu.this.quitPacket.getValue());
			}
		});
	}
}
