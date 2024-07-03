package shadowverseCharbosses.cards.nemesis;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shadowverse.characters.Nemesis;
import shadowverse.powers.AbsoluteOnePower;
import shadowverseCharbosses.cards.AbstractBossCard;

import java.util.ArrayList;

public class AbsoluteOne extends AbstractBossCard {
    public static final String ID = "shadowverse:AbsoluteOne";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:AbsoluteOne");

    public static final String IMG_PATH = "img/cards/AbsoluteOne.png";

    public AbsoluteOne() {
        super(ID, cardStrings.NAME, IMG_PATH, 3, cardStrings.DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF, AbstractMonster.Intent.MAGIC);
        this.baseBlock = 50;
        this.cardsToPreview = new AbsoluteJudgment();
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new SFXAction("AbsoluteOne"));
//        addToBot(new AnimationAction(KMR.bigAnimation, "extra", 3.0F, false));
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)m, (AbstractCreature)m, this.block));
        addToBot((AbstractGameAction)new ApplyPowerAction(m,m,(AbstractPower)new AbsoluteOnePower(m)));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(30);
        }
    }

    public int getPriority(ArrayList<AbstractCard> hand) {
        return 200;
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new AbsoluteOne();
    }
}
