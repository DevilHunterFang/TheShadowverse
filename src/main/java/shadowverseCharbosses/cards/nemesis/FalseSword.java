package shadowverseCharbosses.cards.nemesis;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import shadowverse.characters.AbstractShadowversePlayer;
import shadowverse.characters.Nemesis;
import shadowverseCharbosses.cards.AbstractBossCard;

import java.util.ArrayList;

public class FalseSword extends AbstractBossCard {
    public static final String ID = "shadowverse:FalseSword";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:FalseSword");

    public static final String IMG_PATH = "img/cards/FalseSlash.png";

    public FalseSword() {
        super(ID, cardStrings.NAME, IMG_PATH, 2, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY, AbstractMonster.Intent.ATTACK);
        this.baseDamage= 20;
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("FalseSword"));
        addToBot(new VFXAction(m, new CleaveEffect(), 0.1F));
        addToBot(new DamageAction(p, new DamageInfo(m, this.damage), AbstractGameAction.AttackEffect.NONE));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(4);
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
        return 198;
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new FalseSword();
    }
}
