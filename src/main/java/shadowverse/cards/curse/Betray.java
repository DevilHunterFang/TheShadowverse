package shadowverse.cards.curse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Betray extends CustomCard {
    public static final String ID = "shadowverse:Betray";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:Betray");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Betray.png";

    public Betray() {
        super(ID, NAME, IMG_PATH, 1, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.SPECIAL, CardTarget.NONE);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        addToBot((AbstractGameAction)new MakeTempCardInDiscardAction(this.makeStatEquivalentCopy(),1));
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

    @Override
    public AbstractCard makeCopy(){
        return new Betray();
    }
}
