package shadowverseCharbosses.cards.nemesis;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.powers.cardpowers.EnemyAmaterasuPower;

public class EnAmaterasu extends AbstractBossCard {
    public static final String ID = "shadowverse:EnAmaterasu";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnAmaterasu");

    public static final String IMG_PATH = "img/cards/Amaterasu.png";

    public EnAmaterasu() {
        super(ID, cardStrings.NAME, IMG_PATH, 0, cardStrings.DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF, AbstractMonster.Intent.DEFEND_BUFF);
        this.baseBlock = 8;
        this.baseMagicNumber = 3;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("EnAmaterasu"));
        addToBot(new GainBlockAction(m,this.block));
        addToBot(new ApplyPowerAction(m,m,new EnemyAmaterasuPower(m,this.magicNumber),this.magicNumber));
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(3);
            upgradeMagicNumber(2);
        }
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnAmaterasu();
    }
}
