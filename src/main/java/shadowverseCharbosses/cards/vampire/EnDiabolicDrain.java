package shadowverseCharbosses.cards.vampire;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.Vampire;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.stances.EnVengeance;

import java.util.ArrayList;

public class EnDiabolicDrain extends AbstractBossCard {
    public static final String ID = "shadowverse:EnDiabolicDrain";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:DiabolicDrain");

    public static final String IMG_PATH = "img/cards/DiabolicDrain.png";

    public EnDiabolicDrain() {
        super(ID, cardStrings.NAME, IMG_PATH, 2, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY, AbstractMonster.Intent.ATTACK);
        this.baseDamage = 8;
        this.baseMagicNumber = 2;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Vampire.Enums.COLOR_SCARLET;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction) new DamageAction((AbstractCreature)p, new DamageInfo((AbstractCreature)m, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        addToBot((AbstractGameAction) new HealAction(m, m, this.magicNumber));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (mo instanceof AbstractCharBoss){
                if (((AbstractCharBoss) mo).stance instanceof EnVengeance){
                    this.setCostForTurn(0);
                    this.isCostModified = true;
                }
            }
        }
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> hand) {
        return 40;
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnDiabolicDrain();
    }
}
