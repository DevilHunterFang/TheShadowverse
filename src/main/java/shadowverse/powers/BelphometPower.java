package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BelphometPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:BelphometPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:BelphometPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int maxAmt;

    public BelphometPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.maxAmt = amount;
        this.type = NeutralPowertypePatch.NEUTRAL;
        this.img = new Texture("img/powers/BelphometPower.png");
        this.priority = 99;
        updateDescription();
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("BelphometPower", 0.05F);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > this.amount){
            this.amount = 0;
        }
        else {
            BelphometPower.this.amount -= damageAmount;
            return 0;
        }
        updateDescription();
        return damageAmount;
    }


    @Override
    public void atStartOfTurn() {
        this.amount = this.maxAmt;
        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
