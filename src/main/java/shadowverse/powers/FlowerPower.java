package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EntanglePower;
import shadowverse.monsters.TaketsumiBOSS;

public class FlowerPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:FlowerPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:FlowerPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FlowerPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.img = new Texture("img/powers/FlowerPower.png");
        this.priority = 99;
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target instanceof TaketsumiBOSS){
            flash();
            this.amount -= 1;
            addToBot(new RemoveSpecificPowerAction(this.owner,this.owner, EntanglePower.POWER_ID));
            addToBot(new ArmamentsAction(true));
            addToBot(new GainBlockAction(this.owner,damageAmount));
            if (this.amount <=0){
                addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,this));
            }
            updateDescription();
        }
    }



    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
