package maowcraft.fctest.api.component;

import maowcraft.fctest.FCTest;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.util.sync.WorldSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

public class WorldPropertiesComponent implements Component, WorldSyncedComponent {
    private boolean advancementsVisible;

    public boolean isAdvancementsVisible() {
        return this.advancementsVisible;
    }
    public void setAdvancementsVisible(boolean newValue) { this.advancementsVisible = newValue; }

    @Override
    public void fromTag(CompoundTag tag) {
        if (tag.contains("advancementsVisible")) {
            this.advancementsVisible = tag.getBoolean("advancementsVisible");
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putBoolean("advancementsVisible", advancementsVisible);
        return tag;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return FCTest.PROPERTIES;
    }

    @Override
    public World getWorld() {
        return null;
    }
}
