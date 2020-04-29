package maowcraft.fctest.mixin;

import maowcraft.fctest.FCTest;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    private ButtonWidget advancementViewButton;

    /**
     * @author Maowcraft
     * @reason Needed to change the GameMenuScreen layout, this probably will remain the same in the full version.
     */
    @Overwrite
    public void initWidgets() {
        this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 24 + -16, 204, 20, I18n.translate("menu.returnToGame"), (buttonWidgetx) -> {
            this.minecraft.openScreen((Screen)null);
            this.minecraft.mouse.lockCursor();
        }));
        this.advancementViewButton = this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 48 + -16, 98, 20, I18n.translate("gui.advancements"), (buttonWidgetx) -> {
            this.minecraft.openScreen(new AdvancementsScreen(this.minecraft.player.networkHandler.getAdvancementHandler()));
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 96 + -16, 98, 20, I18n.translate("menu.options"), (buttonWidgetx) -> {
            this.minecraft.openScreen(new SettingsScreen(this, this.minecraft.options));
        }));
        ButtonWidget buttonWidget = (ButtonWidget)this.addButton(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 96 + -16, 98, 20, I18n.translate("menu.shareToLan"), (buttonWidgetx) -> {
            this.minecraft.openScreen(new OpenToLanScreen(this));
        }));
        buttonWidget.active = this.minecraft.isIntegratedServerRunning() && !this.minecraft.getServer().isRemote();
        ButtonWidget buttonWidget2 = (ButtonWidget)this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 120 + -16, 204, 20, I18n.translate("menu.returnToMenu"), (buttonWidgetx) -> {
            boolean bl = this.minecraft.isInSingleplayer();
            boolean bl2 = this.minecraft.isConnectedToRealms();
            buttonWidgetx.active = false;
            this.minecraft.world.disconnect();
            if (bl) {
                this.minecraft.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel", new Object[0])));
            } else {
                this.minecraft.disconnect();
            }

            if (bl) {
                this.minecraft.openScreen(new TitleScreen());
            } else if (bl2) {
                RealmsBridge realmsBridge = new RealmsBridge();
                realmsBridge.switchToRealms(new TitleScreen());
            } else {
                this.minecraft.openScreen(new MultiplayerScreen(new TitleScreen()));
            }

        }));
        if (!this.minecraft.isInSingleplayer()) {
            buttonWidget2.setMessage(I18n.translate("menu.disconnect"));
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(CallbackInfo ci) {
        boolean advancementsVisible =  FCTest.getPropertyBoolean(ComponentProvider.fromWorld(MinecraftClient.getInstance().world), 0);
        this.advancementViewButton.visible = advancementsVisible;
        if (!advancementsVisible) {
            this.advancementViewButton.x = this.width / 2 - 102;
            this.advancementViewButton.setWidth(204);
        }
    }
}
