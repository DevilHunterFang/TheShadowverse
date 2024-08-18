package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import shadowverse.action.SummonDrainStormShadeAction;

public class NervaPower4 extends AbstractPower {
    public static final String POWER_ID = "shadowverse:NervaPower4";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:NervaPower4");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public AbstractMonster[] shades = new AbstractMonster[3];
    public float[] POSX = new float[3];

    public NervaPower4(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 4;
        this.type = NeutralPowertypePatch.NEUTRAL;
        updateDescription();
        this.img = new Texture("img/powers/TaketsumiPower2.png");
        POSX[0] = this.owner.dialogX - 300.0F;
        POSX[1] = this.owner.dialogX - 550.0F;
        POSX[2] = this.owner.dialogX - 700.0F;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("NervaPower4", 0.05F);
    }

    public void atStartOfTurn() {
        this.amount -= 2;
        AbstractDungeon.actionManager.addToBottom(new SummonDrainStormShadeAction(shades, POSX[0], POSX[1],POSX[2], (AbstractMonster) this.owner));
        AbstractDungeon.actionManager.addToBottom(new SummonDrainStormShadeAction(shades, POSX[0], POSX[1],POSX[2], (AbstractMonster) this.owner));
        AbstractDungeon.actionManager.addToBottom(new SummonDrainStormShadeAction(shades, POSX[0], POSX[1],POSX[2], (AbstractMonster) this.owner));
        if (this.amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            int amt = (int) (AbstractDungeon.player.maxHealth * 0.35F);
            if (amt < 49)
                amt = 49;
            addToBot(new ApplyPowerAction(this.owner, this.owner, new NervaPower6(this.owner, amt)));
        }
        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                if (m != null && !m.isDying && !m.isEscaping) {
                    addToBot(new ApplyPowerAction(m, this.owner, new StrengthPower(m, 1), 1));
                    addToBot(new AddTemporaryHPAction(m, this.owner, 6));
                }
            }
        }
    }
}
