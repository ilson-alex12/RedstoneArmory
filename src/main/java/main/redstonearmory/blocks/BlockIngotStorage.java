package main.redstonearmory.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import main.redstonearmory.ModInformation;
import main.redstonearmory.RedstoneArmory;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import redstonearsenal.block.BlockStorage;

import java.util.List;

public class BlockIngotStorage extends BlockStorage {

	public IIcon[] icon = new IIcon[2];

	public BlockIngotStorage() {
		super();
		setHardness(25.0F);
		setResistance(120.0F);
		setStepSound(soundTypeMetal);
		this.setCreativeTab(RedstoneArmory.tabRArm);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.icon[0] = iconRegister.registerIcon(ModInformation.ID + ":storage/gelidEnderiumIngotBlock");
//		this.icon[1] = iconRegister.registerIcon(ModInformation.ID + ":storage/gelidGemBlock");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return this.icon[meta];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item id, CreativeTabs tab, List list) {
		for (int i = 0; i < 1; i++) {
			list.add(new ItemStack(id, 1, i));
		}
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}
}
