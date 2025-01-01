package net.minecraft.theTitans.configs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class TitanConfigGui extends Screen {
    private static final int TITLE_HEIGHT = 8;
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DONE_BUTTON_TOP_OFFSET = 26;
    private final Screen parentScreen;
    private OptionsRowList optionsRowList;

    public TitanConfigGui(Screen parentScreen) {
        super(new TranslationTextComponent("config.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.optionsRowList = new OptionsRowList(
                this.minecraft, this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        this.optionsRowList.addBig(new BooleanOption(
                "config.boolean.TitansFFAMode",
                unused -> {
                    TitanConfig.TitansFFAMode = TitanConfig.TitansFFAModeBuilder.get();
                    return TitanConfig.TitansFFAMode;
                },
                (unused, flag) -> {
                    TitanConfig.TitansFFAMode = flag;
                    TitanConfig.buildConfig();
                }
        ));

        this.optionsRowList.addBig(new BooleanOption(
                "config.boolean.NightmareMode",
                unused -> {
                    TitanConfig.NightmareMode = TitanConfig.NightmareModeBuilder.get();
                    return TitanConfig.NightmareMode;
                },
                (unused, flag) -> {
                    TitanConfig.NightmareMode = flag;
                    TitanConfig.buildConfig();
                }
        ));

        this.optionsRowList.addBig(new BooleanOption(
                "config.boolean.TotalDestructionMode",
                unused -> {
                    TitanConfig.TotalDestructionMode = TitanConfig.TotalDestructionModeBuilder.get();
                    return TitanConfig.TotalDestructionMode;
                },
                (unused, flag) -> {
                    TitanConfig.TotalDestructionMode = flag;
                    TitanConfig.buildConfig();
                }
        ));

        this.optionsRowList.addBig(new SliderPercentageOption(
                "config.slider.ZombieTitanMinionSpawnrate",
                1.0, 10.0,
                1.0F,
                unused -> {
                    TitanConfig.ZombieTitanMinionSpawnrate = TitanConfig.ZombieTitanMinionSpawnrateBuilder.get();
                    return (double) TitanConfig.ZombieTitanMinionSpawnrate;
                },
                (unused, newValue) -> {
                    TitanConfig.ZombieTitanMinionSpawnrate = newValue.intValue();
                    TitanConfig.buildConfig();
                },
                (gs, option) -> new StringTextComponent(
                        I18n.get("config.slider.ZombieTitanMinionSpawnrate")
                                + ": "
                                + (int) option.get(gs)
                )
        ));

        this.optionsRowList.addBig(new SliderPercentageOption(
                "config.slider.GhastTitanMinionSpawnrate",
                1.0, 10.0,
                1.0F,
                unused -> {
                    TitanConfig.GhastTitanMinionSpawnrate = TitanConfig.GhastTitanMinionSpawnrateBuilder.get();
                    return (double) TitanConfig.GhastTitanMinionSpawnrate;
                },
                (unused, newValue) -> {
                    TitanConfig.GhastTitanMinionSpawnrate = newValue.intValue();
                    TitanConfig.buildConfig();
                },
                (gs, option) -> new StringTextComponent(
                        I18n.get("config.slider.GhastTitanMinionSpawnrate")
                                + ": "
                                + (int) option.get(gs)
                )
        ));

        this.optionsRowList.addBig(new SliderPercentageOption(
                "config.slider.WitherzillaMinionSpawnrate",
                1.0, 10.0,
                1.0F,
                unused -> {
                    TitanConfig.WitherzillaMinionSpawnrate = TitanConfig.WitherzillaMinionSpawnrateBuilder.get();
                    return (double) TitanConfig.WitherzillaMinionSpawnrate;
                },
                (unused, newValue) -> {
                    TitanConfig.WitherzillaMinionSpawnrate = newValue.intValue();
                    TitanConfig.buildConfig();
                },
                (gs, option) -> new StringTextComponent(
                        I18n.get("config.slider.WitherzillaMinionSpawnrate")
                                + ": "
                                + (int) option.get(gs)
                )
        ));

        this.children.add(this.optionsRowList);

        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslationTextComponent("gui.done"),
                (b) -> {
                    this.minecraft.setScreen(this.parentScreen);
                }
        ));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    class MinionSpawnrateOption extends SliderPercentageOption {
        public MinionSpawnrateOption(String p_i51155_1_, double p_i51155_2_, double p_i51155_4_, float p_i51155_6_, Function<GameSettings, Double> p_i51155_7_, BiConsumer<GameSettings, Double> p_i51155_8_, BiFunction<GameSettings, SliderPercentageOption, ITextComponent> p_i51155_9_) {
            super(p_i51155_1_, p_i51155_2_, p_i51155_4_, p_i51155_6_, p_i51155_7_, p_i51155_8_, p_i51155_9_);
        }
    }
}