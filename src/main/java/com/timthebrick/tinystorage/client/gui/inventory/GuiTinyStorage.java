package com.timthebrick.tinystorage.client.gui.inventory;

import com.timthebrick.tinystorage.client.gui.widgets.*;
import com.timthebrick.tinystorage.client.gui.widgets.settings.AccessMode;
import com.timthebrick.tinystorage.client.gui.widgets.settings.ButtonSettings;
import com.timthebrick.tinystorage.common.core.TinyStorageLog;
import com.timthebrick.tinystorage.common.tileentity.TileEntityTinyStorage;
import com.timthebrick.tinystorage.network.PacketHandler;
import com.timthebrick.tinystorage.network.message.MessageConfigButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

public class GuiTinyStorage extends GuiContainer implements IWidgetProvider {

    protected Container container;
    private GuiImageButton accessMode;
    private TileEntityTinyStorage tileEntity;
    protected List<IGuiWidgetAdvanced> widgets = new ArrayList<IGuiWidgetAdvanced>();
    protected List<IGuiAnimation> animations = new ArrayList<IGuiAnimation>();

    protected GuiTinyStorage(Container container, TileEntityTinyStorage te) {
        super(container);
        this.container = container;
        this.tileEntity = te;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float btn) {
        super.drawScreen(mouseX, mouseY, btn);
        boolean hasClicked = Mouse.isButtonDown(0);
        for (IGuiWidgetAdvanced widget : widgets) {
            if (widget instanceof IGuiWidgetBackground) {
                // TinyStorageLog.info(getGuiLeft() + ", " + getXSize() + " | " + getGuiTop() + ", " + getYSize());
                widget.drawWidget(this, xSize, ySize);
            }
        }
        for (Object c : this.buttonList) {
            if (c instanceof IButtonTooltip) {
                IButtonTooltip tooltip = (IButtonTooltip) c;
                int x = tooltip.xPos(); // ((GuiImgButton) c).xPosition;
                int y = tooltip.yPos(); // ((GuiImgButton) c).yPosition;
                if (x < mouseX && x + tooltip.getWidth() > mouseX && tooltip.isVisible()) {
                    if (y < mouseY && y + tooltip.getHeight() > mouseY) {
                        if (y < 15) {
                            y = 15;
                        }
                        String msg = tooltip.getMessage();
                        if (msg != null) {
                            this.drawTooltip(x + 11, y + 4, 0, msg);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateScreen() {
        for (IGuiWidgetAdvanced widget : widgets) {
            widget.updateWidget();
        }
        super.updateScreen();
    }

    private void drawTooltip(int x, int y, int forceWidth, String Msg) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        String[] var4 = Msg.split("\n");
        if (var4.length > 0) {
            int var5 = 0;
            int var6;
            int var7;
            for (var6 = 0; var6 < var4.length; ++var6) {
                var7 = this.fontRendererObj.getStringWidth(var4[var6]);
                if (var7 > var5) {
                    var5 = var7;
                }
            }
            var6 = x + 12;
            var7 = y - 12;
            int var9 = 8;
            if (var4.length > 1) {
                var9 += 2 + (var4.length - 1) * 10;
            }
            if (this.guiTop + var7 + var9 + 6 > this.height) {
                var7 = this.height - var9 - this.guiTop - 6;
            }
            if (forceWidth > 0) {
                var5 = forceWidth;
            }
            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int var10 = -267386864;
            this.drawGradientRect(var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10);
            this.drawGradientRect(var6 - 3, var7 + var9 + 3, var6 + var5 + 3, var7 + var9 + 4, var10, var10);
            this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10);
            this.drawGradientRect(var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10);
            this.drawGradientRect(var6 + var5 + 3, var7 - 3, var6 + var5 + 4, var7 + var9 + 3, var10, var10);
            int var11 = 1347420415;
            int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
            this.drawGradientRect(var6 - 3, var7 - 3 + 1, var6 - 3 + 1, var7 + var9 + 3 - 1, var11, var12);
            this.drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, var6 + var5 + 3, var7 + var9 + 3 - 1, var11, var12);
            this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11);
            this.drawGradientRect(var6 - 3, var7 + var9 + 2, var6 + var5 + 3, var7 + var9 + 3, var12, var12);
            for (int var13 = 0; var13 < var4.length; ++var13) {
                String var14 = var4[var13];
                if (var13 == 0) {
                    var14 = '\u00a7' + Integer.toHexString(15) + var14;
                } else {
                    var14 = "\u00a77" + var14;
                }
                this.fontRendererObj.drawStringWithShadow(var14, var6, var7, -1);
                if (var13 == 0) {
                    var7 += 2;
                }
                var7 += 10;
            }
            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
        }
        GL11.glPopAttrib();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    protected void handleButtonVisibility() {
        if (this.accessMode != null) {
            this.accessMode.setVisibility(true);
        }
    }

    @Override
    public void handleWidgetVisibility() {
    }

    @Override
    public void initGui() {
        super.initGui();
        this.addButtons();
        this.addWidgets();
    }

    @Override
    public void addWidgets() {
    }

    protected void addButtons() {
        this.accessMode = new GuiImageButton(this.guiLeft - 18, this.guiTop + 8, ButtonSettings.AUTOMATED_SIDE_ACCESS, AccessMode.DISABLED);
        if (tileEntity.hasUniqueOwner()) {
            this.buttonList.add(accessMode);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        int ox = this.guiLeft; // (width - xSize) / 2;
        int oy = this.guiTop; // (height - ySize) / 2;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawFG(ox, oy, x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float opacity, int x, int y) {
        this.drawBG(0, 0, x, y);
    }

    protected void drawFG(int ox, int oy, int x, int y) {
        if (this.accessMode != null) {
            this.accessMode.set(this.tileEntity.accessMode);
        }
        for (IGuiWidgetAdvanced widget : this.widgets) {
            if (widget instanceof IGuiWidgetBackground) {
                continue;
            }
            if (widget.isVisible()) {
                widget.drawWidget(this, xSize, ySize);
            }
        }
        for (IGuiAnimation animation : this.animations) {
            if (animation.isVisible()) {
                animation.drawAnimationBackground(this, xSize, ySize);
            }
        }
        for (IGuiAnimation animation : this.animations) {
            if (animation.isVisible()) {
                animation.drawAnimationForeground(this, xSize, ySize);
            }
        }
    }

    protected void drawBG(int ox, int oy, int x, int y) {
        this.handleButtonVisibility();
        this.handleWidgetVisibility();
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int b = Mouse.getEventButton();
        int delta = Mouse.getEventDWheel();
        if (delta != 0) {
            mouseWheelEvent(x, y, delta);
        }
    }

    @Override
    protected void mouseClicked(int xCoord, int yCoord, int btn) {
        if (!widgets.isEmpty()) {
            for (IGuiWidgetAdvanced widget : widgets) {
                widget.mouseClicked(xCoord, yCoord, btn);
            }
        }
        if (btn == 1) {
            for (Object o : this.buttonList) {
                GuiButton guibutton = (GuiButton) o;
                if (guibutton.mousePressed(this.mc, xCoord, yCoord)) {
                    super.mouseClicked(xCoord, yCoord, 0);
                    return;
                }
            }
        }
        super.mouseClicked(xCoord, yCoord, btn);
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
        super.mouseMovedOrUp(x, y, button);
    }

    @Override
    protected void mouseClickMove(int x, int y, int button, long time) {
        super.mouseClickMove(x, y, button, time);
    }

    protected void mouseWheelEvent(int x, int y, int delta) {
        if (!widgets.isEmpty()) {
            for (IGuiWidgetAdvanced widget : widgets) {
                widget.mouseWheel(x, y, delta);
            }
        }
    }

    @Override
    public void addWidget(IGuiWidgetSimple widget) {
        if (widget instanceof IGuiAnimation) {
            animations.add((IGuiAnimation) widget);
            widgets.add((IGuiWidgetAdvanced) widget);
            ((IGuiWidgetAdvanced) widget).adjustPosition();
        } else {
            widgets.add((IGuiWidgetAdvanced) widget);
            ((IGuiWidgetAdvanced) widget).adjustPosition();
        }
    }

    @Override
    public void removeWidget(IGuiWidgetSimple widget) {
        if (widget instanceof IGuiAnimation) {
            animations.remove(widget);
            widgets.remove(widget);
        } else {
            widgets.remove(widget);
        }
    }

    @Override
    public void handleWidgetFunctionality(IGuiWidgetAdvanced widget) {
    }

    @Override
    public int getGuiLeft() {
        return guiLeft;
    }

    @Override
    public int getGuiTop() {
        return guiTop;
    }

    @Override
    public int getXSize() {
        return xSize;
    }

    @Override
    public int getYSize() {
        return ySize;
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
        super.actionPerformed(btn);
        boolean backwards = Mouse.isButtonDown(1);
        if (btn == this.accessMode) {
            PacketHandler.INSTANCE.sendToServer(new MessageConfigButton(Minecraft.getMinecraft().thePlayer, this.accessMode.getSetting(), backwards, this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord));
        }
    }

    public void bindTexture(String base, String file) {
        ResourceLocation loc = new ResourceLocation(base, "textures/" + file);
        this.mc.getTextureManager().bindTexture(loc);
    }

    @Override
    public int getInvLeft() {
        return 0;
    }

    @Override
    public int getInvTop() {
        return 0;
    }

    @Override
    public int getInvWidth() {
        return 0;
    }

    @Override
    public int getInvHeight() {
        return 0;
    }
}
