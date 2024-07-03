package shadowverseCharbosses.cards.bishop;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverseCharbosses.cards.AbstractBossCard;

public class EnLucifer extends AbstractBossCard {
    public static final String ID = "shadowverse:EnLucifer";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnLucifer");

    public static final String IMG_PATH = "img/cards/Lucifer.png";

    public EnLucifer() {
        super(ID, cardStrings.NAME, IMG_PATH, 3, cardStrings.DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ALL, AbstractMonster.Intent.UNKNOWN);
        this.baseDamage = 30;
        this.baseMagicNumber = 30;
        this.magicNumber = this.baseMagicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        int rnd = AbstractDungeon.cardRandomRng.random(1);
        if (rnd==0){
            addToBot((AbstractGameAction)new SFXAction("Lucifer"));
            addToBot((AbstractGameAction)new HealAction((AbstractCreature)m, (AbstractCreature)m, this.magicNumber));
        }else {
            addToBot((AbstractGameAction)new SFXAction("Lucifer2"));
            addToBot((AbstractGameAction)new DamageAction((AbstractCreature)p, new DamageInfo((AbstractCreature)m, this.damage), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(10);
            upgradeMagicNumber(10);
        }
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnLucifer();
    }
}
