package shadowverseCharbosses.cards.nemesis;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import shadowverseCharbosses.cards.AbstractBossCard;

public class EnOmenOfCraving extends AbstractBossCard {
    public static final String ID = "shadowverse:EnOmenOfCraving";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnOmenOfCraving");

    public static final String IMG_PATH = "img/cards/OmenOfCraving2.png";

    public EnOmenOfCraving() {
        super(ID, cardStrings.NAME, IMG_PATH, 1, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY, AbstractMonster.Intent.ATTACK_BUFF);
        this.baseDamage = 4;
        this.exhaust = true;
        this.cardsToPreview = new EnRavenousSweetness();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("OmenOfCraving2"));
        addToBot(new VampireDamageAction(p, new DamageInfo(m, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new ApplyPowerAction(m,m,new StrengthPower(m,2),2));
        addToBot(new ApplyPowerAction(m,m,new DexterityPower(m,-2),-2));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(2);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.hov2)
            renderCardTip(sb);
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnOmenOfCraving();
    }
}
