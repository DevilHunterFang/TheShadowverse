package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import shadowverse.action.NecromanceAction;
import shadowverse.monsters.Belphomet;

public class NicolaPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:NicolaPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:NicolaPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public NicolaPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.img = new Texture("img/powers/NicolaPower.png");
        this.priority = 99;
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target instanceof Belphomet){
            flash();
            if (target.currentBlock <= 0 && this.owner.hasPower(Cemetery.POWER_ID) && this.owner.getPower(Cemetery.POWER_ID).amount>0 && info.type == DamageInfo.DamageType.NORMAL) {
                addToBot(new NecromanceAction(this.owner.getPower(Cemetery.POWER_ID).amount, null, new DamageAction(target,
                        new DamageInfo(this.owner,this.owner.getPower(Cemetery.POWER_ID).amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE)));
            }
        }
    }
    @Override
    public void onInitialApplication() {
        if (!this.owner.hasPower(Cemetery.POWER_ID)){
            addToBot(new ApplyPowerAction(this.owner,this.owner,new Cemetery(this.owner, 0)));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer){
            addToBot(new ApplyPowerAction(this.owner,this.owner,new Cemetery(this.owner, (int)Math.ceil(EnergyPanel.totalCount * 0.5)),(int)Math.ceil(AbstractDungeon.player.energy.energyMaster * 0.5)));
        }
        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
