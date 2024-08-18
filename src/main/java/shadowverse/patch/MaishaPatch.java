package shadowverse.patch;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MaishaPatch {
    @SpirePatch(clz = AbstractCard.class, method = "canUse")
    public static class MaishaLock {
        @SpirePrefixPatch
        public static SpireReturn Pre(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
            if (card.isLocked)
                return SpireReturn.Return(false);
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderPortrait")
    public static class renderLock {
        @SpirePrefixPatch
        public static void Pre(AbstractCard card, SpriteBatch sb) {
            if (ReflectionHacks.getPrivate(card,AbstractCard.class,"portraitImg") == null && card.isLocked){
                switch (card.type) {
                    case ATTACK:
                        ReflectionHacks.setPrivate(card,AbstractCard.class,"portraitImg",ImageMaster.CARD_LOCKED_ATTACK);
                        break;
                    case POWER:
                        ReflectionHacks.setPrivate(card,AbstractCard.class,"portraitImg",ImageMaster.CARD_LOCKED_POWER);
                        break;
                    default:
                        ReflectionHacks.setPrivate(card,AbstractCard.class,"portraitImg",ImageMaster.CARD_LOCKED_SKILL);
                }
            }
        }
    }
}
