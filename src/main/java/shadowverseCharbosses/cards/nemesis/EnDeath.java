package shadowverseCharbosses.cards.nemesis;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.cards.AbstractBossCard;

public class EnDeath extends AbstractBossCard {
    public static final String ID = "shadowverse:EnDeath";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnDeath");

    public static final String IMG_PATH = "img/cards/Death.png";

    public EnDeath() {
        super(ID, cardStrings.NAME, IMG_PATH, -2, cardStrings.DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.CURSE, CardTarget.NONE, AbstractMonster.Intent.UNKNOWN);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
        }
    }

    @Override
    public void triggerWhenDrawn() {
        addToBot((AbstractGameAction)new SuicideAction(AbstractCharBoss.boss));
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnDeath();
    }
}
