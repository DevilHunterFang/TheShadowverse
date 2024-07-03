package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class KMFPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:KMFPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:KMFPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int storedAmount;

    public KMFPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.storedAmount = amount;
        this.type = PowerType.BUFF;
        updateDescription();
        this.img = new Texture("img/powers/KMFPower.png");
        this.priority = 50;
    }

    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        return calculateDamageTakenAmount(damage, type);
    }

    private float calculateDamageTakenAmount(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS && this.amount>0 && damage<=(float)this.amount)
            return 0.0F;
        return damage;
    }

    public void atStartOfTurn() {
        this.amount = this.storedAmount;
        updateDescription();
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        Boolean willLive = Boolean.valueOf((calculateDamageTakenAmount(damageAmount, info.type) < this.owner.currentHealth));
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 0 && willLive
                .booleanValue()) {
            flash();
            this.amount = 0;
            if (this.amount < 0)
                this.amount = 0;
            updateDescription();
        }
        return damageAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
