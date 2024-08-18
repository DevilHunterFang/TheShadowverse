package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shadowverse.action.SummonShadeAction;
import shadowverse.action.SummonStormShadeAction;

public class NexusPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:NexusPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:NexusPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public AbstractMonster[] shades = new AbstractMonster[2];
    public float[] POSX;


    public NexusPower(AbstractCreature owner,float[] POSX) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.POSX = POSX;
        this.amount = 0;
        this.type = NeutralPowertypePatch.NEUTRAL;
        updateDescription();
        this.img = new Texture("img/powers/NexusPower.png");
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("NexusPower", 0.05F);
    }

    @Override
    public void atStartOfTurn() {
        if (!this.owner.isDeadOrEscaped()){
            AbstractDungeon.actionManager.addToBottom(new SummonStormShadeAction(shades,POSX[0],POSX[1]));
            AbstractDungeon.actionManager.addToBottom(new SummonStormShadeAction(shades,POSX[0],POSX[1]));
        }
    }

    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (damage > this.owner.maxHealth - this.amount*25)
            damage = this.owner.maxHealth - this.amount*25;
        if (damage <= 25.0F && this.amount*25 >= this.owner.maxHealth){
            damage = 25.0F;
        }
        return damage;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (Math.max((this.owner.maxHealth - this.amount * 25), 25)) + DESCRIPTIONS[1];
    }
}
