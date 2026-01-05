/*    */ package net.butcheritems.butcheritems.components.language;
/*    */ 
/*    */ import cn.nukkit.utils.Config;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.butcheritems.butcheritems.ButcherItems;
/*    */ 
/*    */ public class Language {
/* 10 */   public static HashMap<String, String> messages = new HashMap<>();
/*    */   
/*    */   public static String prefix;
/*    */   
/*    */   public static void init(ButcherItems plugin) {
/* 14 */     messages.clear();
/* 15 */     plugin.saveResource("messages.yml");
/* 16 */     Config m = new Config(plugin.getDataFolder() + "/messages.yml");
/* 17 */     for (Map.Entry<String, Object> map : (Iterable<Map.Entry<String, Object>>)m.getAll().entrySet()) {
/* 18 */       String key = map.getKey();
/* 19 */       if (map.getValue() instanceof String) {
/* 20 */         String val = (String)map.getValue();
/* 21 */         messages.put(key, val);
/*    */       } 
/*    */     } 
/* 24 */     prefix = m.getString("prefix");
/*    */   }
/*    */   
/*    */   public static String get(String key, Object... replacements) {
/* 28 */     String message = prefix.replace("&", "§") + ((String)messages.getOrDefault(key, "null")).replace("&", "§");
/* 29 */     for (int i = 0; i < replacements.length; i++)
/* 30 */       message = message.replace("[" + i + "]", String.valueOf(replacements[i])); 
/* 32 */     return message;
/*    */   }
/*    */   
/*    */   public static String getNP(String key, Object... replacements) {
/* 36 */     String message = ((String)messages.getOrDefault(key, "null")).replace("&", "§");
/* 37 */     for (int i = 0; i < replacements.length; i++)
/* 38 */       message = message.replace("[" + i + "]", String.valueOf(replacements[i])); 
/* 40 */     return message;
/*    */   }
/*    */ }


/* Location:              C:\Users\leona\OneDrive\Desktop\servidor2\plugins\butcheritems.jar!\net\butcheritems\butcheritems\components\language\Language.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */