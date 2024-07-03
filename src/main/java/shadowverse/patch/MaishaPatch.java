package shadowverse.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
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
}
