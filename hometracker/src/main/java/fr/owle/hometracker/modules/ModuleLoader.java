package fr.owle.hometracker.modules;

import fr.owle.hometracker.HTAPI;
import fr.owle.hometracker.utils.exception.FileIsNotAModuleDirectoryException;
import fr.owle.hometracker.utils.exception.MissingDependenciesModuleException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Class that build and load all the {@link HTModule} in the modules folder.
 * @author henouille
 */
public class ModuleLoader {

    private final List<HTModule> loadedModules;
    private final List<HTModule> startedModule;

    /**
     * Crate a new ModuleLoader.
     */
    public ModuleLoader() {
        loadedModules = new ArrayList<>();
        startedModule = new ArrayList<>();
    }

    /**
     * Load jar inside a folder if the jar was a module.
     * It will call the load method of the module.
     *
     * @param file folder to load.
     * @throws FileIsNotAModuleDirectoryException If the target path was not a module directory.
     * @throws IOException If the file was not a JarFile.
     */
    public void loadModules(File file) throws FileIsNotAModuleDirectoryException, IOException {
        if (!file.isDirectory()) throw new FileIsNotAModuleDirectoryException(file);
        for (File child : Objects.requireNonNull(file.listFiles())) {
            loadModule(child);
        }
    }


    /**
     * Method call to load the jar files in a folder path.
     * It will call the load method of the module.
     *
     * @param filepath path of the folder file.
     * @throws FileIsNotAModuleDirectoryException If the target path was not a module directory.
     * @throws IOException If the file was not a JarFile.
     */
    public void loadModules(String filepath) throws FileIsNotAModuleDirectoryException, IOException {
        loadModules(new File(filepath));
    }

    /**
     * Load modules already built.<br>
     * It will call the load method of the module.
     *
     * @param modules The modules you want to load.
     */
    public void loadModules(HTModule...modules) {
        Arrays.stream(modules).forEach(this::loadModule);
    }

    /**
     * Load a module already built<br>
     * It will call the load method of the module.
     *
     * @param module The module you want to load.
     */
    public void loadModule(HTModule module) {
        if (!loadedModules.contains(module)) {
            module.load();
            loadedModules.add(module);
        }
    }

    /**
     * Load a module.
     *
     * @param file The jar file. It should be a module.
     * @throws IOException If the file was not a JarFile.
     */
    public void loadModule(File file) throws IOException {
        loadModule(file, URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}));
    }

    /**
     * Load a module.
     *
     * @param file The jar file. It should be a module.
     * @param urlClassLoader The used classLoader.
     * @throws IOException If the file was not a JarFile.
     */
    public void loadModule(File file, URLClassLoader urlClassLoader) throws IOException {
        if (isValid(file)) {
            final HTModule module = ModuleBuilder.build(file, urlClassLoader);
            loadModule(module);
        }
    }

    /**
     * Put and start all loaded modules in the {@link ModuleManager}.
     */
    public void startModules() {
        new ArrayList<>(loadedModules).forEach(this::startModule);
    }

    private void startModule(HTModule modules)  {
        if (!startedModule.contains(modules)) {
            final ModuleManager moduleManager = HTAPI.getModule().getModuleManager();
            final List<String> dependencies = modules.getDependencies();
            final List<String> softDependencies = modules.getSoftDependencies();
            dependencies.stream().filter(this::containLoadedModuleWithName).map(this::getLoadedModuleByName).filter(Objects::nonNull).forEach(this::startModule);
            softDependencies.stream().filter(this::containLoadedModuleWithName).map(this::getLoadedModuleByName).filter(Objects::nonNull).forEach(this::startModule);
            try {
                if (!startedModule.containsAll(dependencies.stream().map(this::getLoadedModuleByName).collect(Collectors.toList())))
                    throw new MissingDependenciesModuleException(modules, getMissingDependenciesList(dependencies));
                moduleManager.addModule(modules);
                startedModule.add(modules);
            } catch (MissingDependenciesModuleException e) {
                final String message = e.getMessage();
                HTAPI.getLogger().error(HTAPI.getHTAPI(), message);
            }
        }
    }

    private List<String> getMissingDependenciesList(List<String> dependencies) {
        dependencies.removeAll(startedModule.stream().map(HTModule::getName).collect(Collectors.toList()));
        return new ArrayList<>(dependencies);
    }

    private HTModule getLoadedModuleByName(String name) {
        for (HTModule module : loadedModules)
            if (module.getName().equals(name))
                return module;
        return null;
    }

    private boolean containLoadedModuleWithName(String name) {
        return getLoadedModuleByName(name) != null;
    }

    /**
     * Load a module from a jar file path
     *
     * @param filepath jar file path you want to load
     * @throws IOException If the file was not a JarFile.
     */
    public void loadModule(String filepath) throws IOException {
        loadModule(new File(filepath));
    }

    /**
     * Check if a jar file is in the good format<br>
     * see {@link fr.owle.hometracker.modules.ModuleBuilder} for details.
     *
     * @param file Jar file you want to check.
     * @return True if it's valid, false if it isn't.
     */
    public boolean isValid(JarFile file) {
        if (file == null) throw new NullPointerException();
        return file.getJarEntry("module.yml") != null;
    }

    /**
     * Check if a file is in the good format<br>
     * see {@link fr.owle.hometracker.modules.ModuleBuilder} for details.
     *
     * @param file File you want to check
     * @return True if it's valid, false if it isn't
     * @throws IOException If the file was not a JarFile.
     */
    public boolean isValid(File file) throws IOException {
        return isValid(new JarFile(file));
    }

    /**
     * Check if a jar file is in the good format<br>
     * see {@link fr.owle.hometracker.modules.ModuleBuilder} for details.
     *
     * @param filepath jar file path you want to check
     * @return true if it's valid, false if it isn't
     * @throws IOException If the file was not a JarFile.
     */
    public boolean isValid(String filepath) throws IOException {
        return isValid(new File(filepath));
    }

    /**
     * Get a list of loaded {@link HTModule}.
     * @return The loaded {@link HTModule} list.
     */
    public List<HTModule> getLoadedModules() {
        return new ArrayList<>(loadedModules);
    }

    /**
     * Get a list of started {@link HTModule}.
     * @return The started {@link HTModule} list.
     */
    public List<HTModule> getStartedModule() {
        return new ArrayList<>(startedModule);
    }
}
