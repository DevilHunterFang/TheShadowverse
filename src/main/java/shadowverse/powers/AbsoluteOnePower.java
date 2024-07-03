package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shadowverseCharbosses.actions.common.EnemyMakeTempCardInHandAction;
import shadowverseCharbosses.cards.nemesis.AbsoluteJudgment;

public class AbsoluteOnePower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:AbsoluteOnePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:AbsoluteOnePower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AbsoluteOnePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = NeutralPowertypePatch.NEUTRAL;
        updateDescription();
        this.img = new Texture("img/powers/AbsoluteOnePower.png");
    }

    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type== DamageInfo.DamageType.THORNS)
            damage=0.0F;
        return damage;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type== DamageInfo.DamageType.THORNS)
            damageAmount=0;
        return damageAmount;
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type== DamageInfo.DamageType.THORNS)
            return 0;
        return damageAmount;
    }

    public void atStartOfTurn() {
        addToBot((AbstractGameAction)new EnemyMakeTempCardInHandAction(new AbsoluteJudgment()));
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
