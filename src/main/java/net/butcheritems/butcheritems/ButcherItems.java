package net.butcheritems.butcheritems;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.item.EntityBoat;
import cn.nukkit.entity.item.EntityEndCrystal;
import cn.nukkit.entity.item.EntityPainting;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import net.butcheritems.butcheritems.commands.ClearLagCommand;
import net.butcheritems.butcheritems.components.language.Language;
import net.butcheritems.butcheritems.components.tasks.ClearLagTask;

public class ButcherItems extends PluginBase {

    private boolean pets = false;
    private boolean nametag = false;
    private boolean holograms = false;

    private Announcement announcement;
    private final Set<Integer> announcements = new HashSet<>();
    private final Set<String> excludedEntities = new HashSet<>();
    private final Set<String> excludedWorlds = new HashSet<>();
    private int seconds;
    private int schedule;

    @Override
    public void onEnable() {
        Language.init(this);
        saveDefaultConfig();
        Config c = getConfig();

        this.nametag = c.getBoolean("KillEntitiesWithNametags", true);
        this.schedule = Math.max(1, c.getInt("Schedule", 300));
        this.seconds = this.schedule;

        this.excludedEntities.clear();
        for (String entityName : c.getStringList("ExcludedEntities")) {
            String normalized = normalizeEntityName(entityName);
            if (!normalized.isEmpty()) {
                this.excludedEntities.add(normalized);
            }
        }

        this.excludedWorlds.clear();
        for (String worldName : c.getStringList("ExcludedWorlds")) {
            if (worldName != null && !worldName.trim().isEmpty()) {
                this.excludedWorlds.add(worldName.trim().toLowerCase(Locale.ROOT));
            }
        }

        String announcementsConfig = c.getString("Announcement", "chat").toLowerCase(Locale.ROOT);
        this.announcements.addAll(c.getIntegerList("Announcements"));

        if (announcementsConfig.equals("chat")) {
            this.announcement = Announcement.CHAT;
        } else if (announcementsConfig.equals("title")) {
            this.announcement = Announcement.TITLE;
        } else {
            this.announcement = Announcement.ACTIONBAR;
        }

        getServer().getPluginManager().getPlugins().forEach((s, p) -> {
            if (p.getName().equalsIgnoreCase("LlamaPets")) {
                this.pets = true;
            } else if (p.getName().equalsIgnoreCase("Holograms")) {
                this.holograms = true;
            } else if (p.getName().equalsIgnoreCase("PlaceholderAPI")) {
                registerPlaceholder();
            }
        });

        getServer().getCommandMap().register(
                "clearlag",
                new ClearLagCommand(this, c.getSection("Commands.Clearlag"))
        );

        getServer().getScheduler().scheduleDelayedRepeatingTask(
                this,
                new ClearLagTask(this),
                100,
                20
        );
    }

    private static String normalizeEntityName(String name) {
        if (name == null) {
            return "";
        }

        String normalized = name.trim();
        int separator = Math.max(normalized.lastIndexOf('.'), normalized.lastIndexOf('$'));
        if (separator >= 0) {
            normalized = normalized.substring(separator + 1);
        }
        normalized = normalized.toUpperCase(Locale.ROOT);
        if (normalized.startsWith("ENTITY")) {
            normalized = normalized.substring("ENTITY".length());
        }
        return normalized;
    }

    private boolean isExcludedEntity(Entity entity) {
        String simpleName = normalizeEntityName(entity.getClass().getSimpleName());
        String fullName = entity.getClass().getName().toLowerCase(Locale.ROOT);

        // PetteriM1 Holograms uses gt.creeperface.holograms.entity.HologramEntity.
        // Keep the Cloudburst Holograms package supported as well.
        if (this.holograms && (
                fullName.startsWith("gt.creeperface.holograms.entity.")
                        || fullName.startsWith("me.onebone.holograms"))) {
            return true;
        }

        if (this.pets && fullName.contains("pet")) {
            return true;
        }

        return this.excludedEntities.contains(simpleName);
    }

    private void registerPlaceholder() {
        PlaceholderAPI api = PlaceholderAPI.getInstance();
        if (api == null) return;

        api.builder("clearlag", Integer.class)
                .loader(entry -> this.seconds)
                .autoUpdate(true)
                .updateInterval(20)
                .build();
    }

    public int clearAll() {
        AtomicInteger killed = new AtomicInteger();

        getServer().getLevels().forEach((i, level) -> {
            String worldName = level.getName();
            if (this.excludedWorlds.contains(worldName.toLowerCase(Locale.ROOT))) {
                return;
            }

            for (Entity entity : level.getEntities()) {
                boolean kill = true;

                // Integraciones y exclusiones configurables.
                if (isExcludedEntity(entity)) {
                    kill = false;
                }
                // Entidades con nombre.
                else if (!this.nametag && entity.hasCustomName()) {
                    kill = false;
                }
                // Entidades protegidas por seguridad.
                else if (entity instanceof EntityHuman
                        || entity instanceof EntityPainting
                        || entity instanceof EntityEndCrystal
                        || entity instanceof EntityBoat
                        || entity instanceof EntityVehicle
                        || entity.getNetworkId() == 61) {
                    kill = false;
                }

                if (kill) {
                    entity.close();
                    killed.incrementAndGet();
                }
            }
            level.doChunkGarbageCollection();
        });

        System.gc();
        return killed.get();
    }

    public void broadcast(int seconds) {
        String secString = (seconds == 1)
                ? Language.getNP("second")
                : Language.getNP("seconds");

        switch (this.announcement) {
            case CHAT:
                getServer().broadcastMessage(
                        Language.get("clearlag.chat", seconds, secString)
                );
                break;
            case TITLE:
                getServer().getOnlinePlayers().values().forEach(p ->
                        p.sendTitle(
                                Language.getNP("clearlag.title"),
                                Language.getNP("clearlag.subtitle", seconds, secString)
                        )
                );
                break;
            case ACTIONBAR:
                getServer().getOnlinePlayers().values().forEach(p ->
                        p.sendActionBar(
                                Language.getNP("clearlag.actionbar", seconds, secString)
                        )
                );
                break;
        }
    }

    public enum Announcement {
        CHAT, TITLE, ACTIONBAR
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSchedule() {
        return this.schedule;
    }

    public Announcement getAnnouncement() {
        return this.announcement;
    }

    public Set<Integer> getAnnouncements() {
        return this.announcements;
    }
}
