package pers.towdium.just_enough_calculation.gui.guis;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pers.towdium.just_enough_calculation.JustEnoughCalculation;
import pers.towdium.just_enough_calculation.gui.JECContainer;
import pers.towdium.just_enough_calculation.gui.JECGuiContainer;
import pers.towdium.just_enough_calculation.util.Utilities;
import pers.towdium.just_enough_calculation.util.helpers.ItemStackHelper;
import pers.towdium.just_enough_calculation.util.helpers.PlayerRecordHelper;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Author: Towdium
 * Date:   2016/8/24.
 */
public class GuiOreDict extends JECGuiContainer {
    int page = 1;
    int total;
    JECGuiButton buttonAdd;

    public GuiOreDict(GuiScreen parent) {
        super(new JECContainer() {
            @Override
            protected void addSlots() {
                addSlotGroup(8, 36, 18, 18, 6, 9);
                addSlotSingle(9, 9);
            }

            @Override
            public EnumSlotType getSlotType(int index) {
                return index == 54 ? EnumSlotType.SELECT : EnumSlotType.PICKER;
            }
        }, parent);
    }

    @Override
    protected int getSizeSlot(int index) {
        return 18;
    }

    @Override
    public void init() {
        buttonList.add(new JECGuiButton(0, guiLeft + 7, guiTop + 147, 13, 12, "<", false).setLsnLeft(() -> {
            page = total == 0 ? 0 : page == total ? 1 : page + 1;
            updateLayout();
        }));
        buttonList.add(new JECGuiButton(1, guiLeft + 156, guiTop + 147, 13, 12, ">", false).setLsnLeft(() -> {
            page = total == 0 ? 0 : page == 1 ? total : page - 1;
            updateLayout();
        }));
        buttonAdd = new JECGuiButton(2, 117 + guiLeft, 7 + guiTop, 52, 20, "add").setLsnLeft(() -> {
            PlayerRecordHelper.addOreDictPref(inventorySlots.getSlot(54).getStack());
            updateLayout();
        });
        buttonList.add(buttonAdd);
        onItemStackSet(54, null);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(JustEnoughCalculation.Reference.MODID, "textures/gui/gui_ore_dict.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        //fontRenderer.drawString("Search:", guiLeft + 7, guiTop + 13, 4210752);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        ItemStack itemStack = inventorySlots.getSlot(54).getStack();
        if (!itemStack.isEmpty()) {
            drawString(fontRenderer, Utilities.cutString(itemStack.getDisplayName(), 72, fontRenderer), 35, 13, 0xFFFFFF);
        }
        drawCenteredStringMultiLine(fontRenderer, page + "/" + total, 7, 169, 147, 159, 0xFFFFFF);
    }

    @Override
    public void updateLayout() {
        List<ItemStack> buffer = PlayerRecordHelper.getOreDictPref();
        int row = 6;
        total = (buffer.size() + (9 * row) - 1) / (9 * row);
        page = page > total ? total : page == 0 && total != 0 ? 1 : page;
        putStacks(0, row * 9 - 1, buffer, page != 0 ? row * 9 * (page - 1) : 0);
    }

    @Override
    public void onItemStackSet(int index, ItemStack s) {
        buttonAdd.enabled = inventorySlots.getSlot(54).getHasStack();
    }

    @Override
    protected void onItemStackPick(ItemStack itemStack) {
        PlayerRecordHelper.removeOreDictPref(itemStack);
        updateLayout();
    }

    @Override
    protected BiFunction<Long, ItemStackHelper.EnumStackAmountType, String> getFormer(int id) {
        return (aLong, type) -> "";
    }

    @Override
    protected int getDestSlot(int button) {
        return 54;
    }
}
