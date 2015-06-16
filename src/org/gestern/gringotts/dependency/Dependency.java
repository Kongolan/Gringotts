package org.gestern.gringotts.dependency;

import static org.gestern.gringotts.Util.versionAtLeast;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.gestern.gringotts.Gringotts;

/**
 * Manages plugin dependencies.
 * 
 * @author jast
 *
 */
public enum Dependency {

    /** Singleton dependency manager instance. */
    DEP;

    private final Logger log = Gringotts.G.getLogger();

    public final DependencyHandler vault;


    /**
     * Initialize plugin dependencies. The plugins themselves do not need to be loaded before this is called, 
     * but the classes must be visible to the classloader. 
     */
    private Dependency() {
        vault = new GenericHandler(hookPlugin("Vault","net.milkbowl.vault.Vault","1.2.24"));
    }

    /**
     * Attempt to hook a plugin dependency.
     * @param name Name of the plugin.
     * @param classpath classpath to check for
     * @param minVersion minimum version of the plugin. The plugin will still be hooked if this version is not satisfied,
     * 		but a warning will be emitted.
     * @return the plugin object when hooked successfully, or null if not.
     */
    private Plugin hookPlugin(String name, String classpath, String minVersion) {
        Plugin plugin;
        if (packagesExists(classpath)) {
            plugin = Bukkit.getServer().getPluginManager().getPlugin(name);
            log.info("Plugin "+name+" hooked.");

            PluginDescriptionFile desc = plugin.getDescription();
            String version = desc.getVersion();
            if (!versionAtLeast(version, minVersion)) {
                log.warning("Plugin dependency "+ name +" is version " + version + 
                        ". Expected at least "+ minVersion +" -- Errors may occur.");
            }
        } else {
            log.info("Unable to hook plugin " + name);
            plugin = null;
        }

        return plugin;
    }

    /**
     * Determines if all packages in a String array are within the Classpath
     * This is the best way to determine if a specific plugin exists and will be
     * loaded. If the plugin package isn't loaded, we shouldn't bother waiting
     * for it!
     * @param packages String Array of package names to check
     * @return Success or Failure
     */
    private static boolean packagesExists(String...packages) {
        try {
            for (String pkg : packages) {
                Class.forName(pkg);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
