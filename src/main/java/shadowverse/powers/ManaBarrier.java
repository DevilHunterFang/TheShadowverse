package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ManaBarrier extends AbstractPower {
    public static final String POWER_ID = "shadowverse:ManaBarrier";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:ManaBarrier");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ManaBarrier(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.img = new Texture("img/powers/ManaBarrier.png");
        this.priority = 999;
        updateDescription();
    }

    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
            damage = 0.0F;
        return damage;
    }


    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
