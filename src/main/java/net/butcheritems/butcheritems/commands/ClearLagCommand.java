package net.butcheritems.butcheritems.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.utils.ConfigSection;
import net.butcheritems.butcheritems.ButcherItems;
import net.butcheritems.butcheritems.components.language.Language;

public class ClearLagCommand extends PluginCommand<ButcherItems> {

    public ClearLagCommand(ButcherItems plugin, ConfigSection section) {
        super(section.getString("Name"), plugin); // Eliminamos el cast a Plugin
        setDescription(section.getString("Description"));
        setPermission(section.getString("Permission"));
        setUsage(section.getString("Usage"));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender.hasPermission(getPermission())) {
            // Obtenemos el plugin principal y ejecutamos clearAll
            int killed = ((ButcherItems) getPlugin()).clearAll();

            // Elegimos la palabra correcta (singular/plural)
            String entString = (killed == 1) ? Language.getNP("entity") : Language.getNP("entities");

            // Enviamos mensaje al jugador/comando
            sender.sendMessage(Language.get("clearlag.command", killed, entString));
        }
        return false;
    }
}
