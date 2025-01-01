package com.zerwhit.mixin.block;

import net.minecraft.item.UseAction;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(UseAction.class)
@Unique
public class MixinUseAction {
    @Shadow
    @Final
    @Mutable
    private static UseAction[] $VALUES;
    @Unique
    private static final UseAction nOBTG$SWORD = useAction$addAnim();
    @Invoker("<init>")
    public static UseAction useAction$invokeInit(String name, int id) {
        throw new AssertionError();
    }
    @Unique
    private static UseAction useAction$addAnim() {
        ArrayList<UseAction> animations;
        UseAction animation = null;
        if (MixinUseAction.$VALUES != null) {
            animations = new ArrayList<>(Arrays.asList(MixinUseAction.$VALUES));
            animation = useAction$invokeInit("BLOCKED", animations.get(animations.size() - 1).ordinal() + 1);
            animations.add(animation);
            MixinUseAction.$VALUES = animations.toArray(new UseAction[0]);
        }
        return animation;
    }
}
