package respawnarisemod.item.custom;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import respawnarisemod.RespawnariseMod;
import respawnarisemod.item.ModItems;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {

    public static final Holder<ArmorMaterial> SOUL_TOUCHED = register("soul_touched",
            Util.make(new EnumMap<>(ArmorItem.Type.class),atribute->{
                atribute.put(ArmorItem.Type.BOOTS,4);
                atribute.put(ArmorItem.Type.LEGGINGS,7);
                atribute.put(ArmorItem.Type.CHESTPLATE,9);
                atribute.put(ArmorItem.Type.HELMET,4);
                atribute.put(ArmorItem.Type.BODY,12);
            }),30,2.5f,0.1f,()-> ModItems.SOUL_TOUCHED_CORE.get());


    private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type,Integer> typeProtection,
                                                  int enchantability, float toughness, float knockbackResistance,
                                                  Supplier<Item> ingredientItem){
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(RespawnariseMod.MOD_ID,name);
        Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_NETHERITE;
        Supplier<Ingredient> ingredient = ()-> Ingredient.of(ingredientItem.get());
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(location));
        EnumMap<ArmorItem.Type,Integer> typeMap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            typeMap.put(type,typeProtection.get(type));
        }
        return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL,location,
                new ArmorMaterial(typeProtection,enchantability,equipSound,ingredient,layers,toughness,knockbackResistance));
    }
}
