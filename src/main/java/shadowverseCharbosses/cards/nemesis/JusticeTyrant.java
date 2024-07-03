package shadowverseCharbosses.cards.nemesis;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import shadowverse.characters.Nemesis;
import shadowverse.monsters.WorldEliminator;
import shadowverse.powers.WorldEliminatorLastWordPower;
import shadowverseCharbosses.cards.AbstractBossCard;

public class JusticeTyrant extends AbstractBossCard {
    public static final String ID = "shadowverse:JusticeTyrant";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:JusticeTyrant");

    public static final String IMG_PATH = "img/cards/JusticeTyrant.png";

    public JusticeTyrant() {
        super(ID, cardStrings.NAME, IMG_PATH, 2, cardStrings.DESCRIPTION, CardType.ATTACK, Nemesis.Enums.COLOR_SKY, CardRarity.RARE, CardTarget.SELF, AbstractMonster.Intent.DEFEND_DEBUFF);
        this.baseBlock = 130;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
//        addToBot(new AnimationAction(KMR.bigAnimation, "extra", 3.0F, false));
        addToBot((AbstractGameAction)new GainBlockAction((AbstractCreature)m, (AbstractCreature)m, this.block));
        AbstractMonster m1 = new WorldEliminator(-70.0F + -185.0F * 1, MathUtils.random(-45.0F, 0.0F));
        AbstractMonster m2 = new WorldEliminator(-70.0F + -185.0F * 2, MathUtils.random(-45.0F, 0.0F));
        AbstractMonster m3 = new WorldEliminator(-70.0F + -185.0F * 1, MathUtils.random(105.0F, 135.0F));
        AbstractMonster m4 = new WorldEliminator(-70.0F + -185.0F * 2, MathUtils.random(105.0F, 135.0F));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(m1, true));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(m2, true));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(m3, true));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SpawnMonsterAction(m4, true));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m1, m, (AbstractPower)new WorldEliminatorLastWordPower((AbstractCreature)m1, 12)));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m2, m, (AbstractPower)new WorldEliminatorLastWordPower((AbstractCreature)m2, 12)));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m3, m, (AbstractPower)new WorldEliminatorLastWordPower((AbstractCreature)m3, 12)));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)m4, m, (AbstractPower)new WorldEliminatorLastWordPower((AbstractCreature)m4, 12)));
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player, m, (AbstractPower)new DrawReductionPower((AbstractCreature)AbstractDungeon.player, 1)));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(20);
        }
    }


    public AbstractCard makeCopy() {
        return (AbstractCard)new JusticeTyrant();
    }
}
