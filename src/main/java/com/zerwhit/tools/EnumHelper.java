package com.zerwhit.tools;

import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;

public class EnumHelper {
    public static <T extends Enum> UseAction addEnumAction(Object... enumValues) {
        UseAction[] baseEnum = new UseAction[0];
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);

            Field valuesField = UseAction.class.getDeclaredField("$VALUES");

            long offset = unsafe.staticFieldOffset(valuesField);

            UseAction[] instanceToArray = UseAction.values();
            baseEnum = Arrays.copyOf(instanceToArray, instanceToArray.length + 1);

            UseAction enumChanged = null;
            try {
                Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafeInstance.setAccessible(true);
                Unsafe unsafe0 = (Unsafe) theUnsafeInstance.get(Unsafe.class);
                enumChanged = (UseAction) unsafe0.allocateInstance(UseAction.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Field field0;
            try {
                field0 = enumChanged.getClass().getSuperclass().getDeclaredField("name");
                field0.setAccessible(true);
                field0.set(enumChanged, enumValues[0]);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            baseEnum[baseEnum.length - 1] = enumChanged;

            System.arraycopy(instanceToArray, 0, baseEnum, 0, instanceToArray.length);

            unsafe.putObject(UseAction.class, offset, baseEnum);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return baseEnum[baseEnum.length - 1];
    }
}