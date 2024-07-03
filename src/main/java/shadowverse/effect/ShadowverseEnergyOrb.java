package shadowverse.effect;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.graphics.Texture;

public class ShadowverseEnergyOrb extends CustomEnergyOrb {
    private final static Texture BLANK = new Texture("img/ui/layer_blank.png");
    public ShadowverseEnergyOrb(String[] orbTexturePaths, String orbVfxPath, float[] layerSpeeds, Texture baseLayer) {
        super(orbTexturePaths, orbVfxPath, layerSpeeds);
        this.energyLayers[0] = BLANK;
        this.energyLayers[1] = BLANK;
        this.energyLayers[2] = BLANK;
        this.energyLayers[3] = BLANK;
        this.energyLayers[4] = BLANK;
        this.noEnergyLayers[0] = BLANK;
        this.noEnergyLayers[1] = BLANK;
        this.noEnergyLayers[2] = BLANK;
        this.noEnergyLayers[3] = BLANK;
        this.noEnergyLayers[4] = BLANK;
        this.baseLayer = baseLayer;
    }
}
