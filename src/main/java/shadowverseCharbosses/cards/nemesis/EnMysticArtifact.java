package shadowverseCharbosses.cards.nemesis;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shadowverse.characters.AbstractShadowversePlayer;
import shadowverse.characters.Nemesis;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.powers.general.EnemyDrawCardNextTurnPower;

public class EnMysticArtifact extends AbstractBossCard {
    public static final String ID = "shadowverse:EnMysticArtifact";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnMysticArtifact");

    public static final String IMG_PATH = "img/cards/MysticArtifact.png";

    public EnMysticArtifact() {
        super(ID, cardStrings.NAME, IMG_PATH, 1, cardStrings.DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.SELF, AbstractMonster.Intent.DEFEND_BUFF);
        this.baseBlock = 18;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
            this.tags.add(AbstractShadowversePlayer.Enums.ARTIFACT);
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)m, (AbstractCreature)m, this.block));
        addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m, (AbstractCreature)m, (AbstractPower)new EnemyDrawCardNextTurnPower((AbstractCreature)m, 1), 1));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(6);
        }
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnMysticArtifact();
    }
}
