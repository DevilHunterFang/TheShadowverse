package shadowverseCharbosses.cards.vampire;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import shadowverse.characters.AbstractShadowversePlayer;
import shadowverse.characters.Vampire;
import shadowverseCharbosses.cards.AbstractBossCard;

public class EnLonePromise extends AbstractBossCard {
    public static final String ID = "shadowverse:EnLonePromise";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnLonePromise");

    public static final String IMG_PATH = "img/cards/LonePromise.png";

    public EnLonePromise() {
        super(ID, cardStrings.NAME, IMG_PATH, 1, cardStrings.DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.ALL, AbstractMonster.Intent.BUFF);
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        this.tags.add(AbstractShadowversePlayer.Enums.MACHINE);
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Vampire.Enums.COLOR_SCARLET;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)m, (AbstractPower)new StrengthPower((AbstractCreature)m, 1), 1));
        addToBot((AbstractGameAction)new HealAction(m,m,this.magicNumber));
        addToBot((AbstractGameAction)new ApplyPowerAction(p,m,new WeakPower(p,this.magicNumber,true),this.magicNumber));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeMagicNumber(1);
        }
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnLonePromise();
    }
}
