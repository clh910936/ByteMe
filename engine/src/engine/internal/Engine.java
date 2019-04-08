package engine.internal;

import engine.external.Entity;
import engine.external.IEventEngine;
import engine.external.Level;
import engine.external.component.*;
import engine.internal.systems.*;
import engine.internal.systems.System;
import javafx.scene.input.KeyCode;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Engine {
    //TODO: Exception Handling
    private final ResourceBundle SYSTEM_COMPONENTS_RESOURCES = ResourceBundle.getBundle("SystemRequiredComponents.properties");


    protected Collection<Entity> myEntities;
    protected Collection<IEventEngine> myEvents;
    private MovementSystem myMovementSystem;
    private ImageViewSystem myImageViewSystem;
    private CollisionSystem myCollisionSystem;
    private HealthSystem myHealthSystem;
    private EventHandlerSystem myEventHandlerSystem;
    private CleanupSystem myCleanupSystem;
    private List<System> mySystems;

    public Engine(Level level) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        myEntities = level.getEntities();
        myEvents = level.getEvents();
        initSystems();
    }

    public Collection<Entity> updateState(Collection<KeyCode> inputs){
        for(System system :mySystems) {
            system.update(myEntities,inputs);
        }
        return this.getEntities();
    }

    public Collection<Entity> getEntities(){
        return Collections.unmodifiableCollection(myEntities);
    }

    private void initSystems() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        mySystems = Arrays.asList(myMovementSystem,myImageViewSystem,myCollisionSystem,myHealthSystem,myEventHandlerSystem,myCleanupSystem);
        for (System system:mySystems){
            initSystem(system);
        }
    }

    private void initSystem(System system) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        Class systemClazz = system.getClass();
        Collection<Class<?extends Component>> systemComponents = retrieveComponentClasses(systemClazz);
        system = (System) systemClazz.getConstructor().newInstance(systemComponents,this);
    }

    @SuppressWarnings({"unchecked"})
    private Collection<Class<? extends Component>> retrieveComponentClasses(Class systemClazz) throws ClassNotFoundException {
        String[] componentArr = SYSTEM_COMPONENTS_RESOURCES.getStringArray(systemClazz.getSimpleName());
        ArrayList<Class<? extends Component>> componentList = new ArrayList<>();
        for(String component:componentArr){
            componentList.add((Class<? extends Component>)Class.forName(component));
        }
        return componentList;
    }
}