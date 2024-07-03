package shadowverseCharbosses.cards.nemesis;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import shadowverse.characters.Nemesis;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.powers.cardpowers.EnemyDoubleDamagePower;
import shadowverseCharbosses.powers.cardpowers.MaishaPower2;

public class MaishaUltimate extends AbstractBossCard {
    public static final String ID = "shadowverse:MaishaUltimate";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:MaishaUltimate");

    public static final String IMG_PATH = "img/cards/Maisha2.png";

    public MaishaUltimate() {
        super(ID, cardStrings.NAME, IMG_PATH, 0, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY, AbstractMonster.Intent.BUFF);
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.hasPower(MaishaPower2.POWER_ID) && m.getPower(MaishaPower2.POWER_ID).amount>0){
            addToBot(new ApplyPowerAction(m,m,new StrengthPower(m,m.getPower(MaishaPower2.POWER_ID).amount),m.getPower(MaishaPower2.POWER_ID).amount));
            addToBot(new ApplyPowerAction(m,m,new MaishaPower2(m,-m.getPower(MaishaPower2.POWER_ID).amount),-m.getPower(MaishaPower2.POWER_ID).amount));
            if (m.getPower(MaishaPower2.POWER_ID).amount>9){
                addToBot(new ApplyPowerAction(m,m,new EnemyDoubleDamagePower(m,1),1));
            }
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
        }
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new MaishaUltimate();
    }
}
