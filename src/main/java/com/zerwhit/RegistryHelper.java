package com.zerwhit;

import com.zerwhit.annotations.EventRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.Item;
import net.minecraft.theTitans.TheTitans;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RegistryHelper {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TheTitans.modid);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TheTitans.modid);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, TheTitans.modid);
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TheTitans.modid);
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, TheTitans.modid);
    private static boolean initialized;
    private static Map<Field, Item> registeredItemsMap;
    private static Map<Field, Block> registeredBlocksMap;
    private static Map<Field, EntityType<?>> registeredEntitiesMap;
    private static Map<Field, SoundEvent> registeredSoundsMap;
    private static Map<Field, Attribute> registeredAttributesMap;

    public RegistryHelper() {
        registeredItemsMap = new HashMap<>();
        registeredBlocksMap = new HashMap<>();
        registeredEntitiesMap = new HashMap<>();
        registeredSoundsMap = new HashMap<>();
        registeredAttributesMap = new HashMap<>();
    }

    private void init() {
        if (!initialized) {
            TheTitans.log("Initialization registers...");
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
            BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
            ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
            SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
            ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
            initialized = true;
            TheTitans.log("Initialization registers finished.");
        }
    }

    public void registry() {
    }

    public void register(Object obj) {
        init();
        TheTitans.log("Starting registry...");
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == EventRegistry.class) {
                    TheTitans.log("Field has annotation: " + field);
                    field.setAccessible(true);
                    Object value = null;
                    try {
                        value = field.get(obj);
                    } catch (IllegalAccessException error) {
                        TheTitans.error(error);
                    }
                    if (value instanceof Item) {
                        registeredItemsMap.put(field, (Item) value);
                    }
                    if (value instanceof Block) {
                        registeredBlocksMap.put(field, (Block) value);
                    }
                    if (value instanceof EntityType) {
                        registeredEntitiesMap.put(field, (EntityType<?>) value);
                    }
                    if (value instanceof SoundEvent) {
                        registeredSoundsMap.put(field, (SoundEvent) value);
                    }
                    if (value instanceof Attribute) {
                        registeredAttributesMap.put(field, (Attribute) value);
                    }
                }
            }
        }
    }

    public void postInit() {
        itemsActivation();
        blocksActivation();
        entitiesActivation();
        soundsActivation();
        attributesActivation();
        TheTitans.log("Finished registry.");
    }

    private void itemsActivation() {
        TheTitans.log("Initialization items...");
        registeredItemsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(key -> key.getAnnotation(EventRegistry.class).priority())))
                .forEach((itemSet) -> {
                    Field field = itemSet.getKey();
                    String identifier = field.getAnnotation(EventRegistry.class).identifier();
                    RegistryObject<Item> callback = ITEMS.register(identifier, itemSet::getValue);
                    TheTitans.log("Try to registry item: " + callback.getId());
                });
        TheTitans.log("Initialization items done.");
    }

    private void blocksActivation() {
        TheTitans.log("Initialization blocks...");
        registeredBlocksMap.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getAnnotation(EventRegistry.class).identifier()))
                .forEach((blockSet) -> {
                    Field field = blockSet.getKey();
                    String identifier = field.getAnnotation(EventRegistry.class).identifier();
                    RegistryObject<Block> callback = BLOCKS.register(identifier, blockSet::getValue);
                    TheTitans.log("Try to registry block: " + callback.getId());
                });
        TheTitans.log("Initialization blocks done.");
    }

    private void entitiesActivation() {
        TheTitans.log("Initialization entities...");
        registeredEntitiesMap.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getAnnotation(EventRegistry.class).identifier()))
                .forEach((entitySet) -> {
                    Field field = entitySet.getKey();
                    String identifier = field.getAnnotation(EventRegistry.class).identifier();
                    RegistryObject<EntityType<?>> callback = ENTITIES.register(identifier, entitySet::getValue);
                    TheTitans.log("Try to registry entity type: " + callback.getId());
                });
        TheTitans.log("Initialization entities done.");
    }

    private void soundsActivation() {
        TheTitans.log("Initialization sounds...");
        registeredSoundsMap.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getAnnotation(EventRegistry.class).identifier()))
                .forEach((soundSet) -> {
                    Field field = soundSet.getKey();
                    String identifier = field.getAnnotation(EventRegistry.class).identifier();
                    RegistryObject<SoundEvent> callback = SOUNDS.register(identifier, soundSet::getValue);
                    TheTitans.log("Try to registry sound event: " + callback.getId());
                });
        TheTitans.log("Initialization sounds done.");
    }

    private void attributesActivation() {
        TheTitans.log("Initialization attributes...");
        registeredAttributesMap.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getAnnotation(EventRegistry.class).identifier()))
                .forEach((attributeSet) -> {
                    Field field = attributeSet.getKey();
                    String identifier = field.getAnnotation(EventRegistry.class).identifier();
                    RegistryObject<Attribute> callback = ATTRIBUTES.register(identifier, attributeSet::getValue);
                    TheTitans.log("Try to registry attribute: " + callback.getId());
                });
        TheTitans.log("Initialization attributes done.");
    }
}
