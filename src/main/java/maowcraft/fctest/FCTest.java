package maowcraft.fctest;

import maowcraft.fctest.api.component.WorldPropertiesComponent;
import maowcraft.fctest.command.SettingsCommand;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.util.Identifier;

public class FCTest implements ModInitializer {
    public static final String MODID = "fctest";

    public static final ComponentType<WorldPropertiesComponent> PROPERTIES =
            ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(FCTest.MODID, "world_properties"), WorldPropertiesComponent.class);

    public static boolean getPropertyBoolean(ComponentProvider provider, int index) {
        switch(index) {
            case 0:
                return PROPERTIES.get(provider).isAdvancementsVisible();
        }
        return false;
    }

    public static void setPropertyBoolean(ComponentProvider provider, int index, boolean newValue) {
        switch(index) {
            case 0:
                PROPERTIES.get(provider).setAdvancementsVisible(newValue);
        }
    }

    @Override
    public void onInitialize() {
        WorldComponentCallback.EVENT.register((world, component) -> component.put(PROPERTIES, new WorldPropertiesComponent()));

        CommandRegistry.INSTANCE.register(false, dispatcher -> {
            SettingsCommand.register(dispatcher);
        });
    }
}
