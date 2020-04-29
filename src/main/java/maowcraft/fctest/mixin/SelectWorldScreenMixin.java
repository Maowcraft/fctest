package maowcraft.fctest.mixin;

import maowcraft.fctest.FCTest;
import maowcraft.fctest.util.NonpersistentOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SelectWorldScreen.class)
public class SelectWorldScreenMixin extends Screen {
    protected SelectWorldScreenMixin(Text title) {
        super(title);
    }

    @Shadow private WorldListWidget levelList;
    @Shadow protected TextFieldWidget searchBox;
    @Shadow @Final protected Screen parent;
    @Shadow private ButtonWidget selectButton;
    @Shadow private ButtonWidget deleteButton;
    @Shadow private ButtonWidget editButton;
    private ButtonWidget cancelButton;
    private ButtonWidget addButton;
    private TexturedButtonWidget mapMakerToggleButton;

    private static final Identifier MAP_MAKER_TOGGLE_ICON_TEXTURE = new Identifier(FCTest.MODID, "textures/gui/map_maker_toggle.png");

    /**
     * @author Maowcraft
     * @reason Needed to replace SelectWorldScreen's layout, will replace with custom screen during release.
     */
    @Overwrite
    public void init() {
        this.minecraft.keyboard.enableRepeatEvents(true);
        this.searchBox = new TextFieldWidget(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, I18n.translate("selectWorld.search"));
        this.searchBox.setChangedListener((string) -> {
            this.levelList.filter(() -> {
                return string;
            }, false);
        });
        if (MinecraftClient.getInstance().currentScreen instanceof SelectWorldScreen) {
            this.levelList = new WorldListWidget((SelectWorldScreen) MinecraftClient.getInstance().currentScreen, this.minecraft, this.width, this.height, 48, this.height - 64, 36, () -> {
                return this.searchBox.getText();
            }, this.levelList);
        }
        this.children.add(this.searchBox);
        this.children.add(this.levelList);
        this.selectButton = this.addButton(new ButtonWidget(this.width / 2 - 154, this.height - 52, 150, 20, "Play Map", (buttonWidget) -> {
            this.levelList.method_20159().ifPresent(WorldListWidget.Entry::play);
        }));
        this.cancelButton = this.addButton(new ButtonWidget(this.width / 2 + 4, this.height - 52, 48, 20, I18n.translate("gui.cancel"), (buttonWidget) -> {
            this.minecraft.openScreen(this.parent);
        }));
        this.editButton = (ButtonWidget)this.addButton(new ButtonWidget(this.width / 2 - 154, this.height - 28, 150, 20, "Edit Map", (buttonWidget) -> {
            this.levelList.method_20159().ifPresent(WorldListWidget.Entry::edit);
        }));
        this.mapMakerToggleButton = this.addButton(new TexturedButtonWidget(this.width / 2 - 125, 22, 20, 20, 0, 0, 20, MAP_MAKER_TOGGLE_ICON_TEXTURE, 32, 64, (button) -> {
            NonpersistentOptions.isMapMaker = !NonpersistentOptions.isMapMaker;
        }));
        this.addButton = this.addButton(new ButtonWidget(this.width / 2 + 4, this.height - 52, 150, 20, "Make a Map", (buttonWidget) -> {
            this.minecraft.openScreen(new CreateWorldScreen(this));
        }));
        this.deleteButton = this.addButton(new ButtonWidget(this.width / 2 + 4, this.height - 28, 150, 20, "Delete Map", (buttonWidget) -> {
            this.levelList.method_20159().ifPresent(WorldListWidget.Entry::delete);
        }));
        this.addButton.visible = false;
        this.deleteButton.visible = false;
        this.worldSelected(false);
        this.setInitialFocus(this.searchBox);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (NonpersistentOptions.isMapMaker) {
            this.addButton.visible = true;
            this.deleteButton.visible = true;
            this.editButton.visible = true;
            this.cancelButton.x = this.width / 2 + 160;
            this.cancelButton.y = this.height - 28;
        } else {
            this.addButton.visible = false;
            this.deleteButton.visible = false;
            this.editButton.visible = false;
            this.cancelButton.x = this.width / 2 + 4;
            this.cancelButton.y = this.height - 52;
        }
    }

    /**
     * @author Maowcraft
     */
    @Overwrite
    public void worldSelected(boolean active) {
        this.selectButton.active = active;
        this.editButton.active = active;
        this.deleteButton.active = active;
    }
}
