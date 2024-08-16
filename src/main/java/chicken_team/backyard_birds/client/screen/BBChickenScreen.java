package chicken_team.backyard_birds.client.screen;

import chicken_team.backyard_birds.BackyardBirds;
import chicken_team.backyard_birds.common.entity.BBEntities;
import chicken_team.backyard_birds.common.entity.custom.BBChicken;
import chicken_team.backyard_birds.util.EntityRenderingUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;


public class BBChickenScreen extends AbstractContainerScreen<BBChickenMenu> {

    public static final ResourceLocation BOOK_LOCATION = new ResourceLocation(BackyardBirds.MOD_ID, "textures/gui/book.png");

    private final BBChicken chicken;

    protected static final int X = 390;
    protected static final int Y = 245;

    int currentPage;
    private PageButton forwardButton;
    private PageButton backButton;

    private ResourceLocation currentResourceLocation;

    public BBChickenScreen(BBChickenMenu container, Inventory inventory, BBChicken chicken) {
        super(container, inventory, chicken.getDisplayName());
        this.chicken = chicken;
        this.imageWidth = 192;
        this.imageHeight = 166;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float partialTicks) {

        this.renderBackground(guiGraphics);
        super.render(guiGraphics, pMouseX, pMouseY, partialTicks);
        this.renderTooltip(guiGraphics, pMouseX, pMouseY);

    }

    @Override
    protected void init() {
        super.init();
        this.renderables.clear();
        currentPage = 0;
        this.createPageControlButtons();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int mouseX, int mouseY) {
        this.renderBackground(graphics);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        graphics.blit(BOOK_LOCATION, k-20, l-60, 0, 0, 255, 250);

        BBChicken chickenRender = BBEntities.BBCHICKEN.get().create(chicken.level());
        chickenRender.setSize(chicken.getSize());
        chickenRender.setColor(chicken.getColor());
        chickenRender.setWhiteMarking(chicken.getWhiteMarking());
        chickenRender.setBlackMarking(chicken.getBlackMarking());
        chickenRender.setAge(chicken.getAge());
        chickenRender.setIsMale(chicken.getIsMale());

        BBChicken chickenRenderMom = BBEntities.BBCHICKEN.get().create(chicken.level());
        chickenRenderMom.setSize(chicken.getMomSize());
        chickenRenderMom.setColor(chicken.getMomColor());
        chickenRenderMom.setWhiteMarking(chicken.getMomWhiteMarking());
        chickenRenderMom.setBlackMarking(chicken.getMomBlackMarking());
        chickenRenderMom.setIsMale(false);

        BBChicken chickenRenderDad = BBEntities.BBCHICKEN.get().create(chicken.level());
        chickenRenderDad.setSize(chicken.getDadSize());
        chickenRenderDad.setColor(chicken.getDadColor());
        chickenRenderDad.setWhiteMarking(chicken.getDadWhiteMarking());
        chickenRenderDad.setBlackMarking(chicken.getDadBlackMarking());
        chickenRenderDad.setIsMale(true);

        if (this.currentPage==0){
            EntityRenderingUtil.drawEntityOnScreen(graphics, k + 55, l + 110, 80, k + 75 - 50-800, l + 75 - 50-800, chickenRender, false);
        }
        if (this.currentPage==1){
            if (chicken.getHasParents()) {
                EntityRenderingUtil.drawEntityOnScreen(graphics, k + 52, l + 50, 30, k + 75 - 50-800, l + 75 - 50-800, chickenRenderMom, false);
                EntityRenderingUtil.drawEntityOnScreen(graphics, k + 168, l + 50, 30, k + 75 - 50-800, l + 75 - 50-800, chickenRenderDad, false);
            }
        }
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        int xOffset = 35;
        int yOffset = 40;
        Component none = Component.translatable("screen.none");
        if (currentPage == 0) {
            graphics.drawString(this.font, this.getTitle(), this.imageWidth / 2 - this.font.width(this.getTitle()) / 2 + 37 + xOffset, 0, 0XFFFFFF, true);

            Component health = Component.translatable("screen.health");
            String currentHealth = health.getString() + chicken.getHealth() + "/" + chicken.getMaxHealth();
            graphics.drawString(this.font, currentHealth , this.imageWidth / 2 - this.font.width(currentHealth) / 2 + 38 + xOffset, 19, 0XFFFFFF, true);

            Component sex = Component.translatable("screen.is_male.chicken."+chicken.getIsMale());
            graphics.drawString(this.font, sex, this.imageWidth / 2 - this.font.width(sex) / 2 + 38 + xOffset, 31, 0XFFFFFF, true);

            Component color = Component.translatable( "screen.base_color.chicken." + chicken.getColor());
            graphics.drawString(this.font, color, this.imageWidth / 2 - this.font.width(color) / 2 + 36 + xOffset, 44, 0XFFFFFF, true);

            Component white_mark = Component.translatable("screen.white_mark.chicken."+chicken.getWhiteMarking());
            graphics.drawString(this.font, white_mark, this.imageWidth / 2 - this.font.width(white_mark) / 2 + 36 + xOffset, 57, 0XFFFFFF, true);

            Component black_mark = Component.translatable("screen.black_mark.chicken."+chicken.getBlackMarking());
            graphics.drawString(this.font, black_mark, this.imageWidth / 2 - this.font.width(black_mark) / 2 + 36 + xOffset, 70, 0XFFFFFF, true);

            Component tilt = Component.translatable("screen.tilt");
            String netTilt = tilt.getString() + chicken.getTilt() + "/4";
            graphics.drawString(this.font, netTilt, this.imageWidth / 2 - this.font.width(netTilt) / 2 + 36 + xOffset, 83, 0XFFFFFF, true);

            Component size = Component.translatable("screen.size");
            String netSize = size.getString() + chicken.getSize() + "/4";
            graphics.drawString(this.font, netSize, this.imageWidth / 2 - this.font.width(netSize) / 2 + 36 + xOffset, 96, 0XFFFFFF, true);

            Component dominant = Component.translatable("screen.dominant.chicken."+chicken.getIsDominant());
            graphics.drawString(this.font, dominant, this.imageWidth / 2 - this.font.width(dominant) / 2 + 36 + xOffset, 109, 0XFFFFFF, true);

            Component canBePet = Component.translatable("screen.can_be_pet.chicken."+chicken.getIsTame());
            graphics.drawString(this.font, canBePet, this.imageWidth / 2 - this.font.width(canBePet) / 2 + 36 + xOffset, 122, 0XFFFFFF, true);
        }
        if (currentPage == 1){
            //Mom
            int momXOffset = 82;
            Component mom = Component.translatable("screen.mom");
            graphics.drawString(this.font, mom, this.imageWidth / 2 - this.font.width(mom)/ 2 + 37 - momXOffset, 0, 0XFFFFFF, true);

            Component name = Component.translatable("screen.name");
            String momName = name.getString() + (chicken.getHasParents() ? chicken.getMomName() : none.getString()) ;
            graphics.drawString(this.font, momName , this.imageWidth / 2 - this.font.width(momName) / 2 + 38 - momXOffset, 19+yOffset, 0XFFFFFF, true);

            Component color = Component.translatable( "screen.base_color.chicken." +
                    (chicken.getHasParents() ?  chicken.getMomColor() : "none"));
            graphics.drawString(this.font, color, this.imageWidth / 2 - this.font.width(color) / 2 + 36 - momXOffset, 31+yOffset, 0XFFFFFF, true);

            Component white_mark = Component.translatable("screen.white_mark.chicken." +
                    (chicken.getHasParents() ? chicken.getMomWhiteMarking() : "none" ));
            graphics.drawString(this.font, white_mark, this.imageWidth / 2 - this.font.width(white_mark) / 2 + 36 - momXOffset, 44+yOffset, 0XFFFFFF, true);

            Component black_mark = Component.translatable("screen.black_mark.chicken."
                    + (chicken.getHasParents() ? chicken.getMomBlackMarking() : "none" ) );
            graphics.drawString(this.font, black_mark, this.imageWidth / 2 - this.font.width(black_mark) / 2 + 36 - momXOffset, 57+yOffset, 0XFFFFFF, true);

            Component tilt = Component.translatable("screen.tilt");
            String netTilt = chicken.getHasParents() ? (tilt.getString() + chicken.getMomTilt() + "/4") : none.getString();
            graphics.drawString(this.font, netTilt, this.imageWidth / 2 - this.font.width(netTilt) / 2 + 36 - momXOffset, 70+yOffset, 0XFFFFFF, true);

            Component size = Component.translatable("screen.size");
            String netSize = chicken.getHasParents() ? size.getString() + chicken.getMomSize() + "/4" : none.getString();
            graphics.drawString(this.font, netSize, this.imageWidth / 2 - this.font.width(netSize) / 2 + 36 - momXOffset, 83+yOffset, 0XFFFFFF, true);



            //Dad
            Component dad = Component.translatable("screen.dad");
            graphics.drawString(this.font, dad, this.imageWidth / 2 - this.font.width(dad) / 2 + 37 + xOffset, 0, 0XFFFFFF, true);

            Component dad_name = Component.translatable("screen.name");
            String dadName = dad_name.getString() + (chicken.getHasParents() ? chicken.getDadName() : none.getString());
            graphics.drawString(this.font, dadName , this.imageWidth / 2 - this.font.width(dadName) / 2 + 38 + xOffset, 19+yOffset, 0XFFFFFF, true);

            Component dad_color = Component.translatable( "screen.base_color.chicken." +
                    (chicken.getHasParents() ? chicken.getDadColor() : "none"));
            graphics.drawString(this.font, dad_color, this.imageWidth / 2 - this.font.width(dad_color) / 2 + 36 + xOffset, 31+yOffset, 0XFFFFFF, true);

            Component dad_white_mark = Component.translatable("screen.white_mark.chicken."+
                    (chicken.getHasParents() ? chicken.getDadWhiteMarking() : "none"));
            graphics.drawString(this.font, dad_white_mark, this.imageWidth / 2 - this.font.width(dad_white_mark) / 2 + 36 + xOffset, 44+yOffset, 0XFFFFFF, true);

            Component dad_black_mark = Component.translatable("screen.black_mark.chicken."+
                    (chicken.getHasParents() ? chicken.getDadBlackMarking() : "none"));
            graphics.drawString(this.font, dad_black_mark, this.imageWidth / 2 - this.font.width(dad_black_mark) / 2 + 36 + xOffset, 57+yOffset, 0XFFFFFF, true);

            Component dad_tilt = Component.translatable("screen.tilt");
            String net_dad_Tilt = chicken.getHasParents() ? dad_tilt.getString() + chicken.getDadTilt() + "/4" : none.getString();
            graphics.drawString(this.font, net_dad_Tilt, this.imageWidth / 2 - this.font.width(net_dad_Tilt) / 2 + 36 + xOffset, 70+yOffset, 0XFFFFFF, true);

            Component dad_size = Component.translatable("screen.size");
            String dad_netSize = chicken.getHasParents() ? dad_size.getString() + chicken.getDadSize() + "/4" : none.getString();
            graphics.drawString(this.font, dad_netSize, this.imageWidth / 2 - this.font.width(dad_netSize) / 2 + 36 + xOffset, 83+yOffset, 0XFFFFFF, true);
        }
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
        this.backButton.visible = this.currentPage > 0;
    }

    private int getNumPages() {
        return 2;
    }

    protected void createPageControlButtons() {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.forwardButton = this.addRenderableWidget(new PageButton(i + 200, j+125, true, (p_98297_) -> {
            this.pageForward();
        }, true));
        this.backButton = this.addRenderableWidget(new PageButton(i-10, j+125, false, (p_98287_) -> {
            this.pageBack();
        }, true));
        this.updateButtonVisibility();
    }

    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
    }

    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
    }

    public boolean setPage(int pPageNum) {
        int i = Mth.clamp(pPageNum, 0, 1);
        if (i != this.currentPage) {
            this.currentPage = i;
            this.updateButtonVisibility();
            return true;
        } else {
            return false;
        }
    }

}
