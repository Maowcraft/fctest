package maowcraft.fctest.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.io.File;
import java.nio.file.Path;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Final
    @Shadow
    private File gameDir;

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelStorage;<init>(Ljava/nio/file/Path;Ljava/nio/file/Path;Lcom/mojang/datafixers/DataFixer;)V"), method = "<init>", index = 0)
    public Path modifyLevelStoragePath(Path saves) {
        return gameDir.toPath().resolve("../fctest-saves");
    }
}
