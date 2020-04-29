package maowcraft.fctest.mixin;

import io.netty.buffer.Unpooled;
import maowcraft.fctest.FCTest;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.nio.file.Path;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private File runDirectory;

    @Environment(EnvType.CLIENT)
    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelStorage;<init>(Ljava/nio/file/Path;Ljava/nio/file/Path;Lcom/mojang/datafixers/DataFixer;)V"), method = "<init>", index = 0)
    public Path modifyLevelStoragePath(Path saves) {
        return runDirectory.toPath().resolve("fctest-saves");
    }
}
