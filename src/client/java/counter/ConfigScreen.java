package counter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private int specTitleY;

    public ConfigScreen() {
        super(Component.literal("Paramètres CounterMod"));
    }

    @Override
    protected void init() {
        super.init();
        ConfigHolder.loadConfig();

        int cx = this.width / 2;
        int y = 20;
        int spacing = 24;

        // 1) Alignement horizontal
        addRenderableWidget(Button.builder(
            Component.literal("Alignement horizontal : " + ConfigHolder.horizontalAlign.name()),
            b -> {
                ConfigHolder.horizontalAlign = HorizontalAlign.values()[
                    (ConfigHolder.horizontalAlign.ordinal()+1)%HorizontalAlign.values().length];
                ConfigHolder.saveConfig();
                b.setMessage(Component.literal("Alignement horizontal : " + ConfigHolder.horizontalAlign.name()));
            }).bounds(cx-100, y,200,20).build());
        y += spacing;

        // 2) Alignement vertical
        addRenderableWidget(Button.builder(
            Component.literal("Alignement vertical : " + ConfigHolder.verticalAlign.name()),
            b -> {
                ConfigHolder.verticalAlign = VerticalAlign.values()[
                    (ConfigHolder.verticalAlign.ordinal()+1)%VerticalAlign.values().length];
                ConfigHolder.saveConfig();
                b.setMessage(Component.literal("Alignement vertical : " + ConfigHolder.verticalAlign.name()));
            }).bounds(cx-100, y,200,20).build());
        y += spacing;

        // 3) Couleur du texte
        addRenderableWidget(Button.builder(
            Component.literal("Couleur du texte : " + ConfigHolder.textColor),
            b -> {
                ConfigHolder.colorIndex = (ConfigHolder.colorIndex+1)%ConfigHolder.COLOR_OPTIONS.length;
                ConfigHolder.textColor = ConfigHolder.COLOR_OPTIONS[ConfigHolder.colorIndex];
                ConfigHolder.saveConfig();
                b.setMessage(Component.literal("Couleur du texte : " + ConfigHolder.textColor));
            }).bounds(cx-100, y,200,20).build());
        y += spacing;

        // 4) Taille de la police
        addRenderableWidget(Button.builder(
            Component.literal("Taille de la police : " + ConfigHolder.fontScale),
            b -> {
                ConfigHolder.scaleIndex = (ConfigHolder.scaleIndex+1)%ConfigHolder.SCALE_OPTIONS.length;
                ConfigHolder.fontScale = ConfigHolder.SCALE_OPTIONS[ConfigHolder.scaleIndex];
                ConfigHolder.saveConfig();
                b.setMessage(Component.literal("Taille de la police : " + ConfigHolder.fontScale));
            }).bounds(cx-100, y,200,20).build());
        y += spacing;

        // 5) Opacité de l'ombre
        addRenderableWidget(Button.builder(
            Component.literal("Opacité de l'ombre : " + ConfigHolder.shadowOpacity),
            b -> {
                ConfigHolder.shadowIndex = (ConfigHolder.shadowIndex+1)%ConfigHolder.SHADOW_OPTIONS.length;
                ConfigHolder.shadowOpacity = ConfigHolder.SHADOW_OPTIONS[ConfigHolder.shadowIndex];
                ConfigHolder.saveConfig();
                b.setMessage(Component.literal("Opacité de l'ombre : " + ConfigHolder.shadowOpacity));
            }).bounds(cx-100, y,200,20).build());
        y += spacing;

        // 6) Opacité du texte
        addRenderableWidget(Button.builder(
            Component.literal("Opacité du texte : " + ConfigHolder.textOpacity),
            b -> {
                ConfigHolder.opacityIndex = (ConfigHolder.opacityIndex+1)%ConfigHolder.OPACITY_OPTIONS.length;
                ConfigHolder.textOpacity = ConfigHolder.OPACITY_OPTIONS[ConfigHolder.opacityIndex];
                ConfigHolder.saveConfig();
                b.setMessage(Component.literal("Opacité du texte : " + ConfigHolder.textOpacity));
            }).bounds(cx-100, y,200,20).build());
        // petit espace
        y += spacing;

        // enregistrer specTitleY ici (juste avant + boutons)
        specTitleY = y;
        y += spacing / 2;

        // boutons +
        int totalWidth = 4*20 + 3*10;
        int startX = cx - totalWidth/2;
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            addRenderableWidget(Button.builder(
                Component.literal("+"),
                b -> Minecraft.getInstance().setScreen(new SpecificSpeciesScreen(idx))
            ).bounds(startX + i*30, y,20,20).build());
        }
        y += spacing;

        // Afficher total
        addRenderableWidget(Button.builder(
            Component.literal("Afficher total : " + ConfigHolder.showTotal),
            b -> {
                ConfigHolder.showTotal = !ConfigHolder.showTotal;
                ConfigHolder.saveConfig();
                b.setMessage(Component.literal("Afficher total : " + ConfigHolder.showTotal));
            }).bounds(cx-100, y,200,20).build());
        y += spacing;

        // Réinitialiser le compteur
        addRenderableWidget(Button.builder(
            Component.literal("Réinitialiser le compteur"),
            b -> RencontresTracker.reset()
        ).bounds(cx-100, y,200,20).build());
        y += spacing;

        // Fermer
        addRenderableWidget(Button.builder(
            Component.literal("Fermer"),
            b -> Minecraft.getInstance().setScreen(null)
        ).bounds(cx-100, y,200,20).build());
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float delta) {
        // Correct signature for renderBackground
        this.renderBackground(g, mx, my, delta);
        super.render(g, mx, my, delta);

        int cx = this.width/2;
        // titre
        String title = "Configuration :";
        int tw = this.font.width(title);
        g.drawString(this.font, title, cx - tw/2, 5, 0xFFFFFF, false);

        // titre spécifique sans espace avant
        String spec = "Compteurs Pokémon Spécifiques :";
        int sw = this.font.width(spec);
        g.drawString(this.font, spec, cx - sw/2, specTitleY, 0xFFFFFF, false);
    }
}
