package net.minecraft.entity.ai.attributes;

import net.minecraft.util.registry.Registry;

public class Attributes {
   public static Attribute MAX_HEALTH = register("generic.max_health", (new RangedAttribute("attribute.name.generic.max_health", 20.0D, 1.0D, 1024.0D)).setSyncable(true));
   public static Attribute FOLLOW_RANGE = register("generic.follow_range", new RangedAttribute("attribute.name.generic.follow_range", 32.0D, 0.0D, 2048.0D));
   public static Attribute KNOCKBACK_RESISTANCE = register("generic.knockback_resistance", new RangedAttribute("attribute.name.generic.knockback_resistance", 0.0D, 0.0D, 1.0D));
   public static Attribute MOVEMENT_SPEED = register("generic.movement_speed", (new RangedAttribute("attribute.name.generic.movement_speed", (double)0.7F, 0.0D, 1024.0D)).setSyncable(true));
   public static Attribute FLYING_SPEED = register("generic.flying_speed", (new RangedAttribute("attribute.name.generic.flying_speed", (double)0.4F, 0.0D, 1024.0D)).setSyncable(true));
   public static Attribute ATTACK_DAMAGE = register("generic.attack_damage", new RangedAttribute("attribute.name.generic.attack_damage", 2.0D, 0.0D, 2048.0D));
   public static Attribute ATTACK_KNOCKBACK = register("generic.attack_knockback", new RangedAttribute("attribute.name.generic.attack_knockback", 0.0D, 0.0D, 5.0D));
   public static Attribute ATTACK_SPEED = register("generic.attack_speed", (new RangedAttribute("attribute.name.generic.attack_speed", 4.0D, 0.0D, 1024.0D)).setSyncable(true));
   public static Attribute ARMOR = register("generic.armor", (new RangedAttribute("attribute.name.generic.armor", 0.0D, 0.0D, 30.0D)).setSyncable(true));
   public static Attribute ARMOR_TOUGHNESS = register("generic.armor_toughness", (new RangedAttribute("attribute.name.generic.armor_toughness", 0.0D, 0.0D, 20.0D)).setSyncable(true));
   public static Attribute LUCK = register("generic.luck", (new RangedAttribute("attribute.name.generic.luck", 0.0D, -1024.0D, 1024.0D)).setSyncable(true));
   public static Attribute SPAWN_REINFORCEMENTS_CHANCE = register("zombie.spawn_reinforcements", new RangedAttribute("attribute.name.zombie.spawn_reinforcements", 0.0D, 0.0D, 1.0D));
   public static Attribute JUMP_STRENGTH = register("horse.jump_strength", (new RangedAttribute("attribute.name.horse.jump_strength", 0.7D, 0.0D, 2.0D)).setSyncable(true));

   private static Attribute register(String p_233831_0_, Attribute p_233831_1_) {
      return Registry.register(Registry.ATTRIBUTE, p_233831_0_, p_233831_1_);
   }
}