package shadowverseCharbosses.cards.nemesis;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.action.ChangeSpriteAction;
import shadowverse.characters.Nemesis;
import shadowverse.powers.MaishaPower;
import shadowverseCharbosses.actions.common.EnemyGainEnergyAction;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.bosses.Maisha.Maisha;
import shadowverseCharbosses.cards.AbstractBossCard;

import java.util.ArrayList;

public class EnPurgationBlade extends AbstractBossCard {
    public static final String ID = "shadowverse:EnPurgationBlade";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnPurgationBlade");

    public static final String IMG_PATH = "img/cards/PurgationBlade.png";

    public EnPurgationBlade() {
        super(ID, cardStrings.NAME, IMG_PATH, 3, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY, AbstractMonster.Intent.ATTACK);
        if (AbstractDungeon.player != null){
            if (AbstractDungeon.actionManager.turn > 4){
                this.baseDamage = AbstractDungeon.player.maxHealth / 2;
            }else {
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if (mo.hasPower(MaishaPower.POWER_ID)){
                        this.baseDamage = ((MaishaPower)mo.getPower(MaishaPower.POWER_ID)).boxed.size();
                        break;
                    }
                }
            }
        }
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (AbstractDungeon.player != null){
            if (AbstractDungeon.actionManager.turn > 4){
                this.baseDamage = AbstractDungeon.player.maxHealth / 2;
            }else {
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if (mo.hasPower(MaishaPower.POWER_ID)){
                        this.baseDamage = ((MaishaPower)mo.getPower(MaishaPower.POWER_ID)).boxed.size();
                        break;
                    }
                }
            }
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("PurgationBlade"));
        if (Settings.MAX_FPS > 30){
            AbstractDungeon.actionManager.addToBottom(new ChangeSpriteAction(((Maisha) m).extra, (Maisha)m, 2.5F));
        }
        addToBot(new DamageAction(p, new DamageInfo(m, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (AbstractDungeon.actionManager.turn < 5){
            addToBot(new EnemyGainEnergyAction(3));
        }
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            if (mo.hasPower(MaishaPower.POWER_ID)){
                for (AbstractCard c : ((MaishaPower)mo.getPower(MaishaPower.POWER_ID)).boxed){
                    c.isLocked = false;
                    this.addToBot(new MakeTempCardInDiscardAction(c, true));
                }
                break;
            }
        }
        ((MaishaPower)m.getPower(MaishaPower.POWER_ID)).boxed.clear();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(2);
        }
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> hand) {
        return 200;
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnPurgationBlade();
    }
}
