package shadowverse.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shadowverse.action.NahtAction;
import shadowverse.powers.NahtPower;

import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ALL_ENEMY;
import static com.megacrit.cardcrawl.cards.AbstractCard.CardTarget.ENEMY;

public class NahtPatch {
    public static final String[] TEXT = (CardCrawlGame.languagePack.getUIString("shadowverse:AmuletText")).TEXT;

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class disableCardPatch {
        @SpirePrefixPatch
        public static SpireReturn disableCard(AbstractPlayer p, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                if (m.isDying || m.isDead) {
                    continue;
                }
                if (m.hasPower("shadowverse:NahtPower")) {
                    if (c.target.equals(ALL_ENEMY) || c.target.equals(ENEMY) && monster.equals(m)) {
                        for (AbstractPower power : m.powers) {
                            if (power instanceof NahtPower && !((NahtPower) power).triggered) {
                                if (c.costForTurn > 0 && !c.freeToPlay() && !c.isInAutoplay && (
                                        !p.hasPower("Corruption") || c.type != AbstractCard.CardType.SKILL)){
                                    p.energy.use(c.costForTurn);
                                }
                                ((NahtPower) power).triggered = true;
                                ((NahtPower) power).boxed.add(c);
                                power.updateDescription();
                                AbstractDungeon.actionManager.addToBottom(new NahtAction(c));
                                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(m));
                                return SpireReturn.Return(null);
                            }
                        }
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
