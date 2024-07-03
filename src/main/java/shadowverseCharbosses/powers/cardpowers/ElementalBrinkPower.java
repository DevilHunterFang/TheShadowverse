package shadowverseCharbosses.powers.cardpowers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;
import shadowverseCharbosses.bosses.AbstractCharBoss;


public class ElementalBrinkPower
        extends AbstractPower {
    public static final String POWER_ID = "shadowverse:ElementalBrinkPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:ElementalBrinkPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ElementalBrinkPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        updateDescription();
        this.img = new Texture("img/powers/ElementalBrinkPower.png");
    }

    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        if (this.amount >= 999)
            this.amount = 999;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0]+this.amount+DESCRIPTIONS[1];
    }

    public void atStartOfTurnPostDraw() {
        addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,this));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer){
            AbstractCreature m = AbstractDungeon.player;
            if (m != null){
                addToBot(new ApplyPowerAction(m,this.owner,new StrengthPower(m,-this.amount),-this.amount));
                addToBot(new ApplyPowerAction(m,this.owner,new DexterityPower(m,-this.amount),-this.amount));
            }
            if (((AbstractCharBoss)this.owner).energyPanel.getCurrentEnergy()>0){
                addToBot(new SFXAction("ElementalBrinkPower"));
                addToBot(new ApplyPowerAction(this.owner,this.owner,new StrengthPower(this.owner,this.amount),this.amount));
                addToBot(new ApplyPowerAction(this.owner,this.owner,new DexterityPower(this.owner,this.amount),this.amount));
            }
        }
    }

}

