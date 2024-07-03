package shadowverse.patch;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.cards.curse.Death;
import shadowverse.monsters.Nerva;

public class NervaPatch {
    @SpirePatch(clz = InstantKillAction.class, method = "update")
    public static class InstantKillActionPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractGameAction _inst) {
            if (_inst.target != null) {
                if (_inst.target instanceof Nerva) {
                    (AbstractDungeon.getCurrRoom()).cannotLose = true;
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("Nerva_Victory"));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(_inst.target, ((AbstractMonster) _inst.target).DIALOG[6]));
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "decreaseMaxHealth")
    public static class DecreaseMaxHealthPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractCreature target) {
            if (target instanceof Nerva) {
                AbstractDungeon.actionManager.addToBottom(new SFXAction("Nerva_MaxHP"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(target, ((AbstractMonster) target).DIALOG[5]));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = SuicideAction.class, method = "update")
    public static class SuicideActionPatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractGameAction _inst) {
            AbstractMonster m = ReflectionHacks.getPrivate(_inst,SuicideAction.class,"m");
            if (m instanceof Nerva) {
                (AbstractDungeon.getCurrRoom()).cannotLose = true;
                AbstractDungeon.actionManager.addToBottom(new SFXAction("Nerva_Victory"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(m, m.DIALOG[6]));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MakeTempCardInDrawPileAction.class, method = "<ctor>", paramtypez = {AbstractCard.class, int.class, boolean.class, boolean.class, boolean.class, float.class, float.class})
    public static class MakeTempCardInDrawPilePatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractGameAction _inst, AbstractCard card, int amount, boolean randomSpot, boolean autoPosition, boolean toBottom, float cardX, float cardY) {
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                if (mo instanceof Nerva){
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("Nerva_VictoryCard"));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(mo, mo.DIALOG[7]));
                    card = new Death();
                }
            }
        }
    }
}
