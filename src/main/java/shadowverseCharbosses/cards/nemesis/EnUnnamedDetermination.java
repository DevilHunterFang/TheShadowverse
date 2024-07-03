package shadowverseCharbosses.cards.nemesis;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.Nemesis;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.powers.general.EnemyDrawCardNextTurnPower;
import shadowverseCharbosses.powers.general.EnemyEnergizedPower;

public class EnUnnamedDetermination extends AbstractBossCard {
    public static final String ID = "shadowverse:EnUnnamedDetermination";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnUnnamedDetermination");

    public static final String IMG_PATH = "img/cards/UnnamedDetermination.png";

    public EnUnnamedDetermination() {
        super(ID, cardStrings.NAME, IMG_PATH, 1, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.SELF, AbstractMonster.Intent.BUFF);
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("UnnamedDetermination"));
        addToBot(new ApplyPowerAction(m,m, new EnemyEnergizedPower(m,1),1));
        addToBot(new ApplyPowerAction(m, m, new EnemyDrawCardNextTurnPower(m, this.magicNumber), this.magicNumber));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnUnnamedDetermination();
    }
}
