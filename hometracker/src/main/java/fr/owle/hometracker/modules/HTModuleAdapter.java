package fr.owle.hometracker.modules;

import java.util.List;

/**
 * HTModuleAdapter is just a {@link fr.owle.hometracker.modules.HTModule} with a default implementation of the {@link #onLoad()} {@link #onStart()} {@link #onStop()} methods.<br>
 * You can extend of this class to create your module but you'll have to override those methods by yourself (override those method if you need them)
 * @author Geoffrey Vaniscotte
 */
public class HTModuleAdapter extends HTModule {

    /**
     * @param name             it's how your HTModule will be call by <i>HomeTracker</i><br>
     *                         note that two HTModules can't have the same name (you won't be able to add it to the {@link fr.owle.hometracker.modules.ModuleManager}
     * @param version          the actual version of your module
     * @param authors          the list of persons that works on your module
     * @param dependencies     the list of modules that your module need to work, the dependencies will be load before your module get load by the {@link fr.owle.hometracker.modules.ModuleLoader} (your module <b>cannot</b> load without them)
     * @param softDependencies the list of modules that your module can use to work with but without being an obligation (your module <b>can</b> load without them)
     * @param main             the main file of your jar (example: com.dev.MyAwesomeModule)
     */
    public HTModuleAdapter(String name, String version, List<String> authors, List<String> dependencies, List<String> softDependencies, String main) {
        super(name, version, authors, dependencies, softDependencies, main);
    }

    /**
     * Init your module by passing the {@link fr.owle.hometracker.modules.HTModuleConfig} object
     * @param config the configuration of your module
     */
    public HTModuleAdapter(HTModuleConfig config) {
        super(config);
    }

    public HTModuleAdapter() { }

    /**
     * Method called when the {@link HTModule#load()}  method is called
     */
    protected void onLoad() throws Exception {

    }

    /**
     * Method called when the {@link HTModule#start()} method is called
     */
    protected void onStart() throws Exception {

    }

    /**
     * Method called when the {@link HTModule#stop()}  method is called
     */
    @Override
    protected void onStop() throws Exception {

    }
}
