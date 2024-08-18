package shadowverse.cards.temp;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Anger extends CustomCard {
    public static final String ID = "shadowverse:Anger";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:Anger");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Anger.png";

    public Anger() {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.NONE);
        this.isEthereal = true;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (AbstractDungeon.player.discardPile.size() > 0) {
            AbstractDungeon.actionManager.addToBottom(new EmptyDeckShuffleAction());
            AbstractDungeon.actionManager.addToBottom(new ShuffleAction(AbstractDungeon.player.drawPile, false));
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

    @Override
    public AbstractCard makeCopy(){
        return new Anger();
    }
}
