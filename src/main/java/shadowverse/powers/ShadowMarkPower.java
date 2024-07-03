
package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;

public class ShadowMarkPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:ShadowMarkPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:ShadowMarkPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public ShadowMarkPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.img = new Texture("img/powers/BrandOfMorningStarPower.png");
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        addToBot(new ApplyPowerAction(this.owner,this.owner,new VulnerablePower(this.owner,2,false),2));
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        if (this.amount == 0){
            if (stackAmount == 1){
                addToBot(new ApplyPowerAction(this.owner,this.owner,new VulnerablePower(this.owner,2,false),2));
            }
            if (stackAmount == 2){
                addToBot(new ApplyPowerAction(this.owner,this.owner,new VulnerablePower(this.owner,2,false),2));
                addToBot(new ApplyPowerAction(this.owner,this.owner,new WeakPower(this.owner,2,false),2));
            }
        }
        if (this.amount == 1){
            if (stackAmount == 1){
                addToBot(new ApplyPowerAction(this.owner,this.owner,new WeakPower(this.owner,2,false),2));
            }
            if (stackAmount == 2){
                addToBot(new ApplyPowerAction(this.owner,this.owner,new WeakPower(this.owner,2,false),2));
                addToBot(new ApplyPowerAction(this.owner,this.owner,new StrengthPower(this.owner,-1),-1));
            }
        }
        if (this.amount == 2){
            if (stackAmount == 1){
                addToBot(new ApplyPowerAction(this.owner,this.owner,new StrengthPower(this.owner,-1),-1));
            }
            if (stackAmount == 2){
                addToBot(new ApplyPowerAction(this.owner,this.owner,new StrengthPower(this.owner,-1),-1));
                addToBot(new MakeTempCardInDiscardAction(new Dazed(),3));
            }
        }
        if (this.amount == 3){
            if (stackAmount == 1){
                addToBot(new MakeTempCardInDiscardAction(new Dazed(),3));
            }
            if (stackAmount == 2){
                addToBot(new MakeTempCardInDiscardAction(new Dazed(),3));
                addToBot(new MakeTempCardInDrawPileAction(new VoidCard(),1,true,true));
            }
        }
        if (this.amount == 4){
            if (stackAmount == 1){
                addToBot(new MakeTempCardInDrawPileAction(new VoidCard(),1,true,true));
            }
            if (stackAmount == 2){
                addToBot(new MakeTempCardInDrawPileAction(new VoidCard(),1,true,true));
                addToBot(new ApplyPowerAction(this.owner,this.owner,new DexterityPower(this.owner,-1),-1));
            }
        }
        if (this.amount == 5){
            addToBot(new ApplyPowerAction(this.owner,this.owner,new DexterityPower(this.owner,-1),-1));
        }
        this.amount += stackAmount;
        if (this.amount <= 0)
            this.amount = 0;
        if (this.amount >= 6)
            this.amount = 0;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
