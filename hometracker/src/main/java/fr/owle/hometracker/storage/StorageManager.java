package fr.owle.hometracker.storage;

import fr.owle.hometracker.HTAPI;
import fr.owle.hometracker.event.StorageGetRequestEvent;
import fr.owle.hometracker.event.StorageSetRequestEvent;
import fr.owle.hometracker.events.EventManager;
import fr.owle.hometracker.modules.HTModule;

public class StorageManager {

    private final EventManager eventManager;

    public StorageManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public StorageManager() {
        this(HTAPI.getEvent().getEventManager());
    }

    public void set(HTModule module, String key, Node<?> node) {
        final StorageSetRequestEvent event = new StorageSetRequestEvent(module, key, node);
        eventManager.emitEvent(HTAPI.getHTAPI(), event);
    }

    public Node<?> get(HTModule module, String request) {
        final StorageGetRequestEvent event = new StorageGetRequestEvent(module, request);
        eventManager.emitEvent(HTAPI.getHTAPI(), event);
        return event.getResult();
    }
}
