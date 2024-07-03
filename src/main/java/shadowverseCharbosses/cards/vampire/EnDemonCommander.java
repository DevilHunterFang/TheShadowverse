package shadowverseCharbosses.cards.vampire;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.Vampire;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.powers.cardpowers.EnemyDoubleTapPower;
import shadowverseCharbosses.stances.EnVengeance;

public class EnDemonCommander extends AbstractBossCard {
    public static final String ID = "shadowverse:EnDemonCommander";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnDemonCommander");

    public static final String IMG_PATH = "img/cards/DemonCommander.png";

    public EnDemonCommander() {
        super(ID, cardStrings.NAME, IMG_PATH, 1, cardStrings.DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF, AbstractMonster.Intent.DEFEND_BUFF);
        this.baseBlock = 8;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Vampire.Enums.COLOR_SCARLET;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new SFXAction("DemonCommander"));
        addToBot((AbstractGameAction)new GainBlockAction(m,this.block));
        if (((AbstractCharBoss)m).stance instanceof EnVengeance){
            addToBot((AbstractGameAction)new ApplyPowerAction(m,m,new EnemyDoubleTapPower(m,1),1));
        }
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(3);
        }
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnDemonCommander();
    }
}
