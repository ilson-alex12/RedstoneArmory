package main.redstonearmory.items.tools;

import main.redstonearmory.ModInformation;
import main.redstonearmory.items.itemutil.ItemToolRF;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

import java.util.Random;

public class ItemGelidEnderiumAxe extends ItemToolRF {

	Icon activeIcon;
	Icon drainedIcon;

    Random random = new Random();

    public ItemGelidEnderiumAxe(int id, EnumToolMaterial toolMaterial) {

        super(id, toolMaterial);
        damage = 6;
        maxEnergy = 320000;
        energyPerUse = 350;
        energyPerUseCharged = 15000;

        effectiveMaterials.add(Material.wood);
        effectiveMaterials.add(Material.plants);
        effectiveMaterials.add(Material.leaves);
        effectiveMaterials.add(Material.vine);
    }

    public ItemGelidEnderiumAxe(int id, EnumToolMaterial toolMaterial, int harvestLevel) {

        this(id, toolMaterial);
        this.harvestLevel = harvestLevel;
    }

	@Override
	public Icon getIcon(ItemStack stack, int pass) {

		return isEmpowered(stack) ? this.activeIcon : getEnergyStored(stack) <= 0 ? this.drainedIcon : this.itemIcon;
	}

	@Override
	public void registerIcons(IconRegister ir) {

		this.itemIcon = ir.registerIcon(ModInformation.ID + ":tools/gelidEnderiumAxe");
		this.activeIcon = ir.registerIcon(ModInformation.ID + ":tools/gelidEnderiumAxe_active");
		this.drainedIcon = ir.registerIcon(ModInformation.ID + ":tools/gelidEnderiumAxe_drained");
	}

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityLivingBase entity) {

        Block block = Block.blocksList[world.getBlockId(x, y, z)];

        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        if (block.getBlockHardness(world, x, y, z) == 0.0D) {
            return true;
        }
        EntityPlayer player = (EntityPlayer) entity;

        if (block.blockMaterial == Material.wood && isEmpowered(stack)) {
            for (int i = x - 2; i <= x + 2; i++) {
                for (int k = z - 2; k <= z + 2; k++) {
                    for (int j = y - 2; j <= y + 2; j++) {
                        if (Block.blocksList[world.getBlockId(i, j, k)].blockMaterial == Material.wood) {
                            harvestBlock(world, i, j, k, player);
                        }
                    }
                }
            }
        }
        if (!player.capabilities.isCreativeMode) {
            useEnergy(stack, false);
        }
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (world.isRaining() && isEmpowered(stack) || world.isThundering() && isEmpowered(stack)) {
            WorldServer worldserver = MinecraftServer.getServer().worldServers[0];
            WorldInfo worldinfo = worldserver.getWorldInfo();

            int i = (300 + (new Random()).nextInt(600)) * 20;

            worldinfo.setRaining(false);
            worldinfo.setThundering(false);
            worldinfo.setRainTime(i);

            if (random.nextInt(50) == 0)
                world.spawnEntityInWorld(new EntityLightningBolt(world, player.posX, player.posY, player.posZ));

            if (!player.capabilities.isCreativeMode)
                useEnergy(stack, false);
            player.swingItem();
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int hitSide, float hitX, float hitY, float hitZ) {
        if (!(getEnergyStored(stack) < energyPerUse)) {
            if (!isEmpowered(stack)) {
                world.spawnEntityInWorld(new EntityLightningBolt(world, x, y, z));
            } else if (isEmpowered(stack) && getEnergyStored(stack) >= energyPerUseCharged) {
                for (int i = 0; i <= 10; i++) {
                    world.spawnEntityInWorld(new EntityLightningBolt(world, x, y, z));
                    if (random.nextInt(50) == 0)
                        world.spawnEntityInWorld(new EntityLightningBolt(world, player.posX, player.posY, player.posZ));
                }
            }
        }

        if (!player.capabilities.isCreativeMode)
            useEnergy(stack, false);

        return true;
    }

}
