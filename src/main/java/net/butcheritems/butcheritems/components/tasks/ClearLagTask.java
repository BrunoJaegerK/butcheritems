package net.butcheritems.butcheritems.components.tasks;

import cn.nukkit.scheduler.Task;
import net.butcheritems.butcheritems.ButcherItems;
import net.butcheritems.butcheritems.components.language.Language;

public class ClearLagTask extends Task {

    private final ButcherItems plugin;

    public ClearLagTask(ButcherItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onRun(int currentTick) {

        // Mostrar anuncio si el contador coincide con los valores definidos
        if (this.plugin.getAnnouncements().contains(this.plugin.getSeconds())) {
            this.plugin.broadcast(this.plugin.getSeconds());
        }

        // Ejecutar ClearLag
        if (this.plugin.getSeconds() <= 0) {
            this.plugin.setSeconds(this.plugin.getSchedule()); // Reset del contador
            int killed = this.plugin.clearAll(); // ClearLag real

            String entityWord = (killed == 1) ? Language.getNP("entity") : Language.getNP("entities");

            switch (this.plugin.getAnnouncement()) {
                case CHAT:
                    this.plugin.getServer().broadcastMessage(
                            Language.get("clearlag.chat.end", killed, entityWord)
                    );
                    break;

                case TITLE:
                    this.plugin.getServer().getOnlinePlayers().values().forEach(player ->
                            player.sendTitle(
                                    Language.getNP("clearlag.title"),
                                    Language.getNP("clearlag.subtitle.end", killed, entityWord)
                            )
                    );
                    break;

                case ACTIONBAR:
                    this.plugin.getServer().getOnlinePlayers().values().forEach(player ->
                            player.sendActionBar(
                                    Language.getNP("clearlag.actionbar.end", killed, entityWord)
                            )
                    );
                    break;
            }

            return;
        }

        // Reducir contador cada tick
        this.plugin.setSeconds(this.plugin.getSeconds() - 1);
    }
}