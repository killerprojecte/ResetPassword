package com.jim20071128.resetpasswd;



import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.FailedLoginEvent;
import fr.xephi.authme.events.LoginEvent;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import me.albert.amazingbot.bot.Bot;
import me.albert.amazingbot.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.ezapi.chat.ChatMessage;
import org.ezapi.module.bossbar.BarColor;
import org.ezapi.module.bossbar.EzBossBar;

public class ResetPasswd extends JavaPlugin implements Listener {
    private static HashMap<UUID, String> codes = new HashMap<>();

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, (Plugin)this);
        saveDefaultConfig();
        getLogger().info("§a插件启用 §b作者QQ §63491932059 §a部分代码采用Albert的AMPassword");
    }

    @EventHandler
    public void onLogin(LoginEvent e) {
        codes.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        codes.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onFail(FailedLoginEvent e) {
        Long qq = Bot.getApi().getUser(e.getPlayer().getUniqueId());
        if (qq == null) {
            return;
        }
        String code = "重置密码" + genCode();


        codes.put(e.getPlayer().getUniqueId(), code);
        e.getPlayer().sendMessage(getConfig().getString("Line3") + "§b§l: " + qq);
        e.getPlayer().sendMessage(getConfig().getString("Line4") + code);
        e.getPlayer().sendMessage(getConfig().getString("Line5"));
        e.getPlayer().sendTitle(getConfig().getString("Title1") + code,getConfig().getString("SubTitle1"),1,70,20);
        EzBossBar bossBar = new EzBossBar(new ChatMessage(getConfig().getString("Bar") + code, false));
        bossBar.setProgress(0.5f);
        bossBar.setColor(BarColor.WHITE);
        bossBar.addViewer(e.getPlayer());
        bossBar.show(e.getPlayer());
    }

    @EventHandler
    public void onGroupMessage(GroupMessageEvent e) {
        UUID uuid = Bot.getApi().getPlayer(e.getUserID());
        if (uuid == null || !codes.containsKey(uuid)) {
            return;
        }
        String code = codes.get(uuid);
        if (!e.getMsg().replace(" ", "").equalsIgnoreCase(code)) {
            return;
        }
        Player p = Bukkit.getPlayer(uuid);
        if (p == null || !p.isOnline()) {
            return;
        }
        AuthMeApi.getInstance().forceLogin(p);
        String pass = genPassword();
        AuthMeApi.getInstance().changePassword(p.getName(), pass);
        p.sendMessage(getConfig().getString("Line1") + getConfig().getString("Line1") + pass + getConfig().getString("Line1_2") + pass);
        p.sendMessage(getConfig().getString("Line2"));
        e.response(getConfig().getString("QQ"));
        p.sendTitle(getConfig().getString("Title2"),getConfig().getString("SubTitle2"),1,70,20);
    }


    private static String genCode() {
        String words = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int random = (new Random()).nextInt(words.length());
            sb.append(words.toCharArray()[random]);
        }
        return sb.toString().toUpperCase();
    }
    private static String genPassword() {
        String words = "123456789ABCDEFGHIJKLMNPQRST";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int random = (new Random()).nextInt(words.length());
            sb.append(words.toCharArray()[random]);
        }
        return sb.toString().toUpperCase();
    }
}