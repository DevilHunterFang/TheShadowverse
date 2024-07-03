package shadowverseCharbosses.cards.bishop;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.Bishop;
import shadowverseCharbosses.cards.AbstractBossCard;

public class EnMoonAlmiraj extends AbstractBossCard {
    public static final String ID = "shadowverse:EnMoonAlmiraj";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnMoonAlmiraj");

    public static final String IMG_PATH = "img/cards/MoonAlmiraj.png";

    public EnMoonAlmiraj() {
        super(ID, cardStrings.NAME, IMG_PATH, 2, cardStrings.DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ENEMY, AbstractMonster.Intent.ATTACK_DEFEND);
        this.baseDamage = 18;
        this.baseBlock = 25;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Bishop.Enums.COLOR_WHITE;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new SFXAction("MoonAlmiraj"));
        addToBot((AbstractGameAction)new VampireDamageAction((AbstractCreature)p, new DamageInfo((AbstractCreature)m, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot((AbstractGameAction)new GainBlockAction(m,m,this.block));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(4);
            upgradeBlock(5);
        }
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnMoonAlmiraj();
    }
}
