package shadowverse.powers;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class ManaPower extends TwoAmountPower {
    public static final String POWER_ID = "shadowverse:ManaPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:ManaPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ManaPower(AbstractCreature owner,int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = 0;
        this.type = NeutralPowertypePatch.NEUTRAL;
        updateDescription();
        loadRegion("focus");
    }

    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        if (this.amount >= 999)
            this.amount = 999;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.amount2++;
        if (this.amount2==12){
            this.amount++;
            this.amount2 = 0;
        }
        AbstractDungeon.onModifyPower();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.amount2 = 0;
        AbstractDungeon.onModifyPower();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
