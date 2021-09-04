/*    */ package com.jim20071128.resetpasswd;
/*    */ 
/*    */

import fr.xephi.authme.api.v3.AuthMeApi;
/*    */ import fr.xephi.authme.events.FailedLoginEvent;
/*    */ import fr.xephi.authme.events.LoginEvent;
/*    */ import java.util.HashMap;
/*    */ import java.util.Random;
/*    */ import java.util.UUID;
/*    */ import me.albert.amazingbot.bot.Bot;
/*    */ import me.albert.amazingbot.events.GroupMessageEvent;
/*    */ import org.bukkit.Bukkit;
/*    */
import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerQuitEvent;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ public class ResetPasswd extends JavaPlugin implements Listener {
/* 20 */   private static HashMap<UUID, String> codes = new HashMap<>();
/*    */   
/*    */   public void onEnable() {
/* 23 */     Bukkit.getPluginManager().registerEvents(this, (Plugin)this);
/* 24 */     saveDefaultConfig();
/* 25 */     getLogger().info("§a插件启用 §b作者QQ §63491932059 §a部分代码采用Albert的AMPassword");
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onLogin(LoginEvent e) {
/* 30 */     codes.remove(e.getPlayer().getUniqueId());
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onLeave(PlayerQuitEvent e) {
/* 35 */     codes.remove(e.getPlayer().getUniqueId());
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onFail(FailedLoginEvent e) {
/* 40 */     Long qq = Bot.getApi().getUser(e.getPlayer().getUniqueId());
/* 41 */     if (qq == null) {
/*    */       return;
/*    */     }
/* 44 */     String code = "重置密码" + genCode();


/* 45 */     codes.put(e.getPlayer().getUniqueId(), code);
/* 46 */     e.getPlayer().sendMessage(getConfig().getString("Line3") + "§b§l: " + code);
/* 47 */     e.getPlayer().sendMessage(getConfig().getString("Line4") + getConfig().getString("Line4"));
/* 48 */     e.getPlayer().sendMessage(getConfig().getString("Line5"));
             e.getPlayer().sendTitle(getConfig().getString("Title1") + code,getConfig().getString("SubTitle1"),1,70,20);
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onGroupMessage(GroupMessageEvent e) {
/* 53 */     UUID uuid = Bot.getApi().getPlayer(e.getUserID());
/* 54 */     if (uuid == null || !codes.containsKey(uuid)) {
/*    */       return;
/*    */     }
/* 57 */     String code = codes.get(uuid);
/* 58 */     if (!e.getMsg().replace(" ", "").equalsIgnoreCase(code)) {
/*    */       return;
/*    */     }
/* 61 */     Player p = Bukkit.getPlayer(uuid);
/* 62 */     if (p == null || !p.isOnline()) {
/*    */       return;
/*    */     }
/* 65 */     AuthMeApi.getInstance().forceLogin(p);
/* 66 */     String pass = genPassword();
/* 67 */     AuthMeApi.getInstance().changePassword(p.getName(), pass);
/* 68 */     p.sendMessage(getConfig().getString("Line1") + getConfig().getString("Line1") + pass + getConfig().getString("Line1_2") + pass);
/* 69 */     p.sendMessage(getConfig().getString("Line2"));
/* 70 */     e.response(getConfig().getString("QQ"));
             p.sendTitle(getConfig().getString("Title2"),getConfig().getString("SubTitle2"),1,70,20);
/*    */   }
/*    */ 
/*    */   
/*    */   private static String genCode() {
/* 75 */     String words = "0123456789";
/* 76 */     StringBuilder sb = new StringBuilder();
/* 77 */     for (int i = 0; i < 4; i++) {
/* 78 */       int random = (new Random()).nextInt(words.length());
/* 79 */       sb.append(words.toCharArray()[random]);
/*    */     } 
/* 81 */     return sb.toString().toUpperCase();
/*    */   }
/*    */   private static String genPassword() {
/* 84 */     String words = "123456789ABCDEFGHIJKLMNPQRST";
/* 85 */     StringBuilder sb = new StringBuilder();
/* 86 */     for (int i = 0; i < 8; i++) {
/* 87 */       int random = (new Random()).nextInt(words.length());
/* 88 */       sb.append(words.toCharArray()[random]);
/*    */     } 
/* 90 */     return sb.toString().toUpperCase();
/*    */   }
/*    */ }