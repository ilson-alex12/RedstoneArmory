package tehnut.redstonearmory.items.armor;

import cofh.core.item.ItemArmorAdv;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tehnut.redstonearmory.ModInformation;
import tehnut.redstonearmory.RedstoneArmory;
import tehnut.redstonearmory.blocks.BlockRegistry;
import tehnut.redstonearmory.util.TextHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemLumiumArmor extends ItemArmorAdv {

    public ItemLumiumArmor(ArmorMaterial material, int type) {

        super(material, type);
        isRepairable();
        setRepairIngot("ingotLumium");
        setCreativeTab(RedstoneArmory.tabRArm);

        switch (type) {
            case 0: {
                setTextureName(ModInformation.ID + ":armor/lumiumHelm");
                setUnlocalizedName(ModInformation.ID + ".armor.lumium.helm");
                ;
                break;
            }
            case 1: {
                setTextureName(ModInformation.ID + ":armor/lumiumChestplate");
                setUnlocalizedName(ModInformation.ID + ".armor.lumium.chestplate");
                break;
            }
            case 2: {
                setTextureName(ModInformation.ID + ":armor/lumiumLeggings");
                setUnlocalizedName(ModInformation.ID + ".armor.lumium.leggings");
                break;
            }
            case 3: {
                setTextureName(ModInformation.ID + ":armor/lumiumBoots");
                setUnlocalizedName(ModInformation.ID + ".armor.lumium.boots");
                break;
            }
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        int x = (int) Math.floor(player.posX);
        int y = (int) player.posY + 1;
        int z = (int) Math.floor(player.posZ);

        if (!world.isRemote && world.getBlock(x, y, z) == Blocks.air)
            world.setBlock(x, y, z, BlockRegistry.invisiLight);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack Stack, Entity entity, int Slot, String type) {
        if (Slot == 2)
            return ModInformation.ID + ":textures/models/armor/lumiumArmor_2.png";
        else
            return ModInformation.ID + ":textures/models/armor/lumiumArmor_1.png";

    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {

        return EnumRarity.common;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return TextHelper.YELLOW + super.getItemStackDisplayName(itemStack);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean check) {
        list.add(TextHelper.END);
        list.add(TextHelper.LIGHT_GRAY + TextHelper.localize("info.RArm.tooltip.armor.lumium.shine"));
    }
}
