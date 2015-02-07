package main.redstonearmory.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import main.redstonearmory.ModInformation;
import main.redstonearmory.RedstoneArmory;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemArmorPlatingVanilla extends Item {

	public IIcon[] icon = new IIcon[16];
	private static final String[] names = { "leather", "iron", "chain", "gold", "diamond" };

	public ItemArmorPlatingVanilla() {
		setCreativeTab(RedstoneArmory.tabRArm);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + ".material." + names[stack.getItemDamage() % names.length] + ".plating";
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return this.icon[meta];
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.icon[0] = iconRegister.registerIcon(ModInformation.ID + ":materials/plateLeather");
		this.icon[1] = iconRegister.registerIcon(ModInformation.ID + ":materials/plateIron");
		this.icon[2] = iconRegister.registerIcon(ModInformation.ID + ":materials/plateChain");
		this.icon[3] = iconRegister.registerIcon(ModInformation.ID + ":materials/plateGold");
		this.icon[4] = iconRegister.registerIcon(ModInformation.ID + ":materials/plateDiamond");
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
		for (int i = 0; i <= 4; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
}