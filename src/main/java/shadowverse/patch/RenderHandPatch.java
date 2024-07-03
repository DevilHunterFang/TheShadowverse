package shadowverse.patch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(clz = AbstractPlayer.class, method = "renderHand")
public class RenderHandPatch {
    public static boolean plsDontRenderHand = false;

    public static SpireReturn Prefix(AbstractPlayer __instance, SpriteBatch sb) {
        if (plsDontRenderHand)
            return SpireReturn.Return(null);
        return SpireReturn.Continue();
    }
}