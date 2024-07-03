package shadowverseCharbosses.cards.nemesis;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shadowverse.characters.Nemesis;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.powers.cardpowers.EnemyTrueSwordPower;

import java.util.ArrayList;

public class TrueSword extends AbstractBossCard {
    public static final String ID = "shadowverse:TrueSword";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:TrueSword");

    public static final String IMG_PATH = "img/cards/TrueStrike.png";

    public TrueSword() {
        super(ID, cardStrings.NAME, IMG_PATH, 2, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY, AbstractMonster.Intent.ATTACK);
        this.baseDamage = 25;
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("TrueSword"));
        addToBot(new DamageAction(p, new DamageInfo(m, this.damage), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new ApplyPowerAction(m,m,new EnemyTrueSwordPower(m,this.upgraded)));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(5);
        }
    }

    public void applyPowers() {
        AbstractPower strength = this.owner.getPower("Strength");
        int amt = 0;
        if (strength != null){
            amt = strength.amount;
            strength.amount *= 0;
        }
        super.applyPowers();
        if (strength != null)
            strength.amount = amt;
    }

    public void calculateCardDamage(AbstractMonster mo) {
        AbstractPower strength = this.owner.getPower("Strength");
        int amt = 0;
        if (strength != null){
            amt = strength.amount;
            strength.amount *= 0;
        }
        super.calculateCardDamage(mo);
        if (strength != null)
            strength.amount = amt;
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> hand) {
        return 199;
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new TrueSword();
    }
}
