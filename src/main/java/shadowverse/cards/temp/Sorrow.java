package shadowverse.cards.temp;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
public class Sorrow extends CustomCard {
    public static final String ID = "shadowverse:Sorrow";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:Sorrow");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Sorrow.png";

    public Sorrow() {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.NONE);
        this.isEthereal = true;
        this.cardsToPreview = new VoidCard();
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void triggerOnExhaust() {
        addToBot(new MakeTempCardInDrawPileAction(this.cardsToPreview.makeStatEquivalentCopy(),1,true,true));
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

    @Override
    public AbstractCard makeCopy(){
        return new Sorrow();
    }
}
