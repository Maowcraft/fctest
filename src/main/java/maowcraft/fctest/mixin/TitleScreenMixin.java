package maowcraft.fctest.mixin;

import maowcraft.fctest.FCTest;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.options.AccessibilityScreen;
import net.minecraft.client.gui.screen.options.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    private String splashText;
    @Shadow
    private int copyrightTextWidth;
    @Shadow
    private int copyrightTextX;

    private int maowcraftCreditTextWidth;

    private static final Identifier ACCESSIBILITY_ICON_TEXTURE = new Identifier(FCTest.MODID, "textures/gui/accessibility.png");
    private static final Identifier SUBTITLE_TITLE_TEXTURE = new Identifier(FCTest.MODID, "textures/gui/subtitle.png");
    private static final Identifier LANGUAGE_ICON_TEXTURE = new Identifier(FCTest.MODID, "textures/gui/language.png");

    private static final String MAOWCRAFT_CREDIT_TEXT = "FCTest by Maowcraft.";
    private static final String MOJANG_COPYRIGHT_TEXT = "Copyright Mojang AB. Do not distribute!";

    /**
     * @author Maowcraft
     * @reason Needed to replace TitleScreen's layout, will replace with custom screen during release.
     */
    @Overwrite
    public void init() {
        int j = this.height / 4 + 48;
        this.splashText = "";
        this.copyrightTextWidth = this.font.getStringWidth(MOJANG_COPYRIGHT_TEXT);
        this.maowcraftCreditTextWidth = this.font.getStringWidth(MAOWCRAFT_CREDIT_TEXT);
        this.copyrightTextX = this.width - this.copyrightTextWidth - 2;
        this.addButton(new TexturedButtonWidget(this.width / 2 - 124, j + 72 + 12, 20, 20, 0, 0, 20, LANGUAGE_ICON_TEXTURE, 32, 64, (buttonWidget) -> {
            this.minecraft.openScreen(new LanguageOptionsScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        }, I18n.translate("narrator.button.language")));
        this.addButton(new ButtonWidget(this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.translate("menu.options"), (buttonWidget) -> {
            this.minecraft.openScreen(new SettingsScreen(this, this.minecraft.options));
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.translate("menu.quit"), (buttonWidget) -> {
            this.minecraft.scheduleStop();
        }));
        this.addButton(new TexturedButtonWidget(this.width / 2 + 104, j + 72 + 12, 20, 20, 0, 0, 20, ACCESSIBILITY_ICON_TEXTURE, 32, 64, (buttonWidget) -> {
            this.minecraft.openScreen(new AccessibilityScreen(this, this.minecraft.options));
        }, I18n.translate("narrator.button.accessibility")));

        this.addButton(new ButtonWidget(this.width / 2 - 100, j + 24 * 1, 200, 20, I18n.translate("Play w/ Friends"), (buttonWidget) -> {
            if (this.minecraft.options.skipMultiplayerWarning) {
                this.minecraft.openScreen(new MultiplayerScreen(this));
            } else {
                this.minecraft.openScreen(new MultiplayerWarningScreen(this));
            }
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 100, j, 200, 20, "Play", (buttonWidget) -> {
            this.minecraft.openScreen(new SelectWorldScreen(this));
        }));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawString(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"), method = "render")
    public void injectMaowcraftCredits(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.drawString(this.font, MAOWCRAFT_CREDIT_TEXT, this.width - this.maowcraftCreditTextWidth - 2, this.height - 21, 4144959);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V", ordinal = 2), method = "render")
    public void replaceEdition(TextureManager textureManager, Identifier id) {
        this.minecraft.getTextureManager().bindTexture(SUBTITLE_TITLE_TEXTURE);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;blit(IIFFIIII)V"), method = "render")
    public void positionSubtitle(int x, int y, float u, float v, int width, int height, int texWidth, int texHeight) {
        int j = this.width / 2 - 153;
        blit(j + 88, 67, 0.0F, 0.0F, 127, 14, 128, 16);
    }
}
