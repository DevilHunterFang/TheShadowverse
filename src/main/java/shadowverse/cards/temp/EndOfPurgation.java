package shadowverse.cards.temp;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.AbstractShadowversePlayer;
import shadowverse.powers.NervaPower6;

public class EndOfPurgation extends CustomCard {
    public static final String ID = "shadowverse:EndOfPurgation";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EndOfPurgation");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/EndOfPurgation.png";

    public EndOfPurgation() {
        super(ID, NAME, IMG_PATH, 1, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.NONE);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
    }


    @Override
    public void triggerOnManualDiscard() {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            if (mo.hasPower(NervaPower6.POWER_ID)){
                addToBot(new ApplyPowerAction(mo,AbstractDungeon.player,mo.getPower(NervaPower6.POWER_ID)));
            }
        }
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            if (mo.hasPower(NervaPower6.POWER_ID)){
                addToBot(new ApplyPowerAction(mo,AbstractDungeon.player,mo.getPower(NervaPower6.POWER_ID)));
            }
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            if (mo.hasPower(NervaPower6.POWER_ID)){
                addToBot(new ApplyPowerAction(mo,abstractPlayer,mo.getPower(NervaPower6.POWER_ID)));
            }
        }
    }

    @Override
    public AbstractCard makeCopy(){
        return new EndOfPurgation();
    }
}
