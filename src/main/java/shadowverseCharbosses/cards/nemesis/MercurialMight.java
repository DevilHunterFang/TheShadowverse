package shadowverseCharbosses.cards.nemesis;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.Nemesis;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.powers.cardpowers.SionPower;

public class MercurialMight extends AbstractBossCard {
    public static final String ID = "shadowverse:MercurialMight";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:MercurialMight");

    public static final String IMG_PATH = "img/cards/MercurialMight.png";

    public MercurialMight() {
        super(ID, cardStrings.NAME, IMG_PATH, 0, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF, AbstractMonster.Intent.BUFF);
        this.baseMagicNumber = 200;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("MercurialMight"));
        addToBot(new ApplyPowerAction(m,m,new SionPower(m,this.magicNumber),this.magicNumber));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(-100);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.hov2)
            renderCardTip(sb);
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new MercurialMight();
    }
}
