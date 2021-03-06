package tehnut.redstonearmory.items.baubles;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import cofh.api.energy.IEnergyContainerItem;
import cofh.core.util.CoreUtils;
import cofh.lib.util.helpers.EnergyHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import tehnut.redstonearmory.ModInformation;
import tehnut.redstonearmory.compat.CompatibilityBaubles;
import tehnut.redstonearmory.enums.EnergyType;
import tehnut.redstonearmory.util.TooltipHelper;

import java.util.List;

public class ItemCapacitorAmulet extends ItemBaubleBase implements IEnergyContainerItem {

    private IIcon[] icon = new IIcon[EnergyType.values().length + 1];

    public ItemCapacitorAmulet() {
        super("capacitor.", BaubleType.AMULET);
    }

    public static ItemStack getStackItem(EnergyType type) {
        return new ItemStack(CompatibilityBaubles.capacitorBauble, 1, type.ordinal());
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (CoreUtils.isFakePlayer(player)) {
            return stack;
        } else {
            if (player.isSneaking() && this.setActiveState(stack, !this.isActive(stack))) {
                if (this.isActive(stack)) {
                    world.playSoundAtEntity(player, "random.orb", 0.2F, 0.8F);
                } else {
                    world.playSoundAtEntity(player, "random.orb", 0.2F, 0.5F);
                }

                player.swingItem();
            }

            return stack;
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack, int pass) {
        return this.isActive(stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getItemDamage() != EnergyType.CREATIVE.ordinal();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.stackTagCompound == null)
            EnergyHelper.setDefaultEnergyTag(stack, 0);

        int currentEnergy = stack.stackTagCompound.getInteger("Energy");

        return 1.0 - ((double) currentEnergy / (double) EnergyType.values()[stack.getItemDamage()].capacity);
    }

    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs tabs, List list) {

        list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item, 1, EnergyType.CREATIVE.ordinal()), EnergyType.CREATIVE.capacity));
        list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item, 1, EnergyType.TUBEROUS.ordinal()), EnergyType.TUBEROUS.capacity));

        for (int i = 2; i < EnergyType.values().length; ++i) {
            list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item, 1, i), 0));
            list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item, 1, i), EnergyType.values()[i].capacity));
        }
    }

    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + EnergyType.values()[stack.getItemDamage()].toString();
    }

    public EnumRarity getRarity(ItemStack stack) {
        if (stack.getItemDamage() == EnergyType.CREATIVE.ordinal())
            return EnumRarity.epic;
        else if (stack.getItemDamage() == EnergyType.REINFORCED.ordinal())
            return EnumRarity.uncommon;
        else if (stack.getItemDamage() == EnergyType.RESONANT.ordinal())
            return EnumRarity.rare;

        return EnumRarity.common;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean held) {
        TooltipHelper.doCapacitorTip(stack, list);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
        if (pass == 1)
            return this.icon[meta];
        else
            return this.icon[6];

    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        icon[0] = ir.registerIcon("ThermalExpansion:capacitor/CapacitorCreative");
        icon[1] = ir.registerIcon("ThermalExpansion:capacitor/CapacitorPotato");
        icon[2] = ir.registerIcon("ThermalExpansion:capacitor/CapacitorBasic");
        icon[3] = ir.registerIcon("ThermalExpansion:capacitor/CapacitorHardened");
        icon[4] = ir.registerIcon("ThermalExpansion:capacitor/CapacitorReinforced");
        icon[5] = ir.registerIcon("ThermalExpansion:capacitor/CapacitorResonant");
        icon[6] = ir.registerIcon(ModInformation.ID + ":capacitorNecklace");
    }

    @Override
    public int getRenderPasses(int metadata) {
        return requiresMultipleRenderPasses() ? 2 : 1;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    public boolean isActive(ItemStack stack) {
        return stack.stackTagCompound != null && stack.stackTagCompound.getBoolean("Active");
    }

    public boolean setActiveState(ItemStack stack, boolean active) {
        if (this.getEnergyStored(stack) > 0) {
            stack.stackTagCompound.setBoolean("Active", active);
            return true;
        } else {
            stack.stackTagCompound.setBoolean("Active", false);
            return false;
        }
    }

    // Energy Stuff

    @Override
    public int receiveEnergy(ItemStack stack, int i, boolean simulate) {
        if (stack.stackTagCompound == null)
            EnergyHelper.setDefaultEnergyTag(stack, 0);

        int energy = stack.stackTagCompound.getInteger("Energy");
        int energyReceived = Math.min(i, Math.min(EnergyType.values()[stack.getItemDamage()].capacity - energy, EnergyType.values()[stack.getItemDamage()].recieve));

        if (!simulate) {
            energy += energyReceived;
            stack.stackTagCompound.setInteger("Energy", energy);
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack stack, int extract, boolean simulate) {
        if (stack.stackTagCompound == null)
            EnergyHelper.setDefaultEnergyTag(stack, 0);

        int energy = stack.stackTagCompound.getInteger("Energy");
        int energyExtracted = Math.min(extract, Math.min(energy, EnergyType.values()[stack.getItemDamage()].send));

        if (!simulate) {
            energy -= energyExtracted;
            stack.stackTagCompound.setInteger("Energy", energy);
        }

        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack stack) {
        if (stack.stackTagCompound == null)
            EnergyHelper.setDefaultEnergyTag(stack, 0);

        return stack.stackTagCompound.getInteger("Energy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack stack) {
        return EnergyType.values()[stack.getItemDamage()].capacity;
    }

    // Baubley Stuff

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entityLivingBase) {
        if (this.isActive(stack) && !entityLivingBase.worldObj.isRemote) {
            InventoryPlayer inventory = ((EntityPlayer) entityLivingBase).inventory;
            int toSend = Math.min(this.getEnergyStored(stack), EnergyType.values()[stack.getItemDamage()].send);
            ItemStack currentItem = inventory.getCurrentItem();
            if (EnergyHelper.isEnergyContainerItem(currentItem)) {
                IEnergyContainerItem containerItem = (IEnergyContainerItem) currentItem.getItem();
                this.extractEnergy(stack, containerItem.receiveEnergy(currentItem, toSend, false), false);
            }

            for (int i = 0; i <= 3; i++) {
                ItemStack check = inventory.armorItemInSlot(i);
                if (EnergyHelper.isEnergyContainerItem(check)) {
                    IEnergyContainerItem containerItem = (IEnergyContainerItem) check.getItem();
                    this.extractEnergy(stack, containerItem.receiveEnergy(check, toSend, false), false);
                }
            }

            if (stack.getItemDamage() == EnergyType.TUBEROUS.ordinal() && getEnergyStored(stack) <= 0) {
                BaublesApi.getBaubles((EntityPlayer) entityLivingBase).decrStackSize(0, 1);
                ((EntityPlayer) entityLivingBase).inventory.addItemStackToInventory(new ItemStack(Items.baked_potato, 1, 0));
            }
        }
    }
}
