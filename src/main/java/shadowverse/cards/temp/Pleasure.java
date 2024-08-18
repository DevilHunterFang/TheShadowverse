package shadowverse.cards.temp;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Pleasure extends CustomCard {
    public static final String ID = "shadowverse:Pleasure";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:Pleasure");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Pleasure.png";

    public Pleasure() {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.NONE);
        this.isEthereal = true;
    }

    @Override
    public void upgrade() {
    }

    public boolean canPlay(AbstractCard card) {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 0) {
            return true;
        }else {
            return AbstractDungeon.actionManager.cardsPlayedThisTurn.get(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1).type != card.type;
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

    @Override
    public AbstractCard makeCopy(){
        return new Pleasure();
    }
}
