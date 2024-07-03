package shadowverseCharbosses.powers.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class EnemyDrawCardNextTurnPower
        extends AbstractPower {
    public static final String POWER_ID = "shadowverse:Enemy Draw Card";
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Draw Card");

    static {
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }

    public EnemyDrawCardNextTurnPower(AbstractCreature owner, int drawAmount) {
        this.name = NAME;
        this.ID = "shadowverse:Enemy Draw Card";
        this.owner = owner;
        this.amount = drawAmount;
        updateDescription();
        loadRegion("carddraw");
        this.priority = 20;
    }


    public void updateDescription() {
        if (this.amount > 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
    }


    public void atStartOfTurnPostDraw() {
        flash();

        addToBot((AbstractGameAction) new RemoveSpecificPowerAction(this.owner, this.owner, "shadowverse:Enemy Draw Card"));
    }
}
