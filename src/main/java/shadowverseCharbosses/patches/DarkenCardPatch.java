 package shadowverseCharbosses.patches;

 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.badlogic.gdx.graphics.g2d.TextureAtlas;
 import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
 import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
 import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.Settings;
 import shadowverseCharbosses.cards.AbstractBossCard;
 
 
 
 
 
 
 
 
 
 
 
 @SpirePatch(clz = AbstractCard.class, method = "renderTint")
 public class DarkenCardPatch
 {
   private static Color lightenColor = new Color(109.65F, 94.35F, 165.75F, 0.0F);
   private static Color darkenColor = new Color(0.0F, 0.0F, 0.0F, 0.75F);
 
   
   @SpirePrefixPatch
   public static SpireReturn<Void> Prefix(AbstractCard instance, SpriteBatch sb) {
     if (instance instanceof AbstractBossCard) {
       if (!Settings.hideCards) {
         Color tintColor;
         if (((AbstractBossCard)instance).bossDarkened && !((AbstractBossCard)instance).hov2) {
           tintColor = darkenColor;
         } else {
           tintColor = lightenColor;
         } 
         TextureAtlas.AtlasRegion cardBgImg = instance.getCardBgAtlas();
         AbstractBossCard cB = (AbstractBossCard)instance;
         if (cardBgImg != null) {
           cB.renderHelperB(sb, tintColor, cardBgImg, instance.current_x, instance.current_y);
         } else {
           cB.renderHelperB(sb, tintColor, instance.getCardBg(), instance.current_x - 256.0F, instance.current_y - 256.0F);
         } 
       } 
       return SpireReturn.Return(null);
     } 
     
     return SpireReturn.Continue();
   }
 }
