package shadowverseCharbosses.cards.nemesis;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import shadowverse.powers.DiscardOnStartPower;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.powers.general.EnemyDrawCardNextTurnPower;

public class EnRavenousSweetness extends AbstractBossCard {
    public static final String ID = "shadowverse:EnRavenousSweetness";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnRavenousSweetness");

    public static final String IMG_PATH = "img/cards/RavenousSweetness.png";

    public EnRavenousSweetness() {
        super(ID, cardStrings.NAME, IMG_PATH, 0, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY, AbstractMonster.Intent.ATTACK_DEBUFF);
        this.baseDamage = 15;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("RavenousSweetness"));
        addToBot(new VFXAction(new SmallLaserEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.1F));
        addToBot(new DamageAction(p, new DamageInfo(m, this.damage), AbstractGameAction.AttackEffect.NONE));
        addToBot(new HealAction(m,m,5));
        addToBot(new ApplyPowerAction(m, m, new EnemyDrawCardNextTurnPower(m, 5), 5));
        addToBot(new ApplyPowerAction(p,m,new DiscardOnStartPower(p,5),5));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
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

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnRavenousSweetness();
    }
}
