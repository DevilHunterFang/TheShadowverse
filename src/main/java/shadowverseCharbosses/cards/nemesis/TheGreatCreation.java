package shadowverseCharbosses.cards.nemesis;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import shadowverse.characters.Nemesis;
import shadowverseCharbosses.actions.common.EnemyMakeTempCardInHandAction;
import shadowverseCharbosses.cards.AbstractBossCard;

import java.util.ArrayList;

public class TheGreatCreation extends AbstractBossCard {
    public static final String ID = "shadowverse:TheGreatCreation";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:TheGreatCreation");

    public static final String IMG_PATH = "img/cards/TheGreatCreation.png";

    public TheGreatCreation() {
        super(ID, cardStrings.NAME, IMG_PATH, 2, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF, AbstractMonster.Intent.ATTACK_BUFF);
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new EnAncientArtifact();
        this.baseDamage = 15;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(m, new InflameEffect(m), 1.0F));
        addToBot(new ApplyPowerAction(m, m, new StrengthPower(m, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(m, m, new DexterityPower(m, this.magicNumber), this.magicNumber));
        addToBot(new EnemyMakeTempCardInHandAction(this.cardsToPreview.makeStatEquivalentCopy()));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    public int getPriority(ArrayList<AbstractCard> hand) {
        return 100;
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new TheGreatCreation();
    }
}
