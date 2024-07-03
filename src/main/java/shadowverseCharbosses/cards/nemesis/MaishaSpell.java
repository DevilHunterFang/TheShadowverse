package shadowverseCharbosses.cards.nemesis;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.Nemesis;
import shadowverseCharbosses.actions.common.EnemyGainEnergyAction;
import shadowverseCharbosses.cards.AbstractBossCard;

public class MaishaSpell extends AbstractBossCard {
    public static final String ID = "shadowverse:MaishaSpell";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:MaishaSpell");

    public static final String IMG_PATH = "img/cards/TrueStrike.png";

    public MaishaSpell() {
        super(ID, cardStrings.NAME, IMG_PATH, 0, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY, AbstractMonster.Intent.BUFF);
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new EnemyGainEnergyAction(1));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
        }
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new MaishaSpell();
    }
}
