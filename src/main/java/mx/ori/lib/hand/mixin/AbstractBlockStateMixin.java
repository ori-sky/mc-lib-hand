package mx.ori.lib.hand.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    private static final TagKey<Block> HAND_FORBIDDEN = TagKey.of(Registry.BLOCK_KEY, new Identifier("hand", "forbidden"));
    private static final TagKey<Block> HAND_PERMITTED = TagKey.of(Registry.BLOCK_KEY, new Identifier("hand", "permitted"));

    @Shadow
    public abstract boolean isIn(TagKey<Block> tag);

    @Shadow public abstract Stream<TagKey<Block>> streamTags();

    @Shadow public abstract Block getBlock();

    @Inject(at = @At("HEAD"), method = "isToolRequired()Z", cancellable = true)
    private void isToolRequired(CallbackInfoReturnable<Boolean> cir) {
        if(isIn(HAND_FORBIDDEN)) {
            cir.setReturnValue(true);
        } else if(isIn(HAND_PERMITTED)) {
            cir.setReturnValue(false);
        }
    }
}
