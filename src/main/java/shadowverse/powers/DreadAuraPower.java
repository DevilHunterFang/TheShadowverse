package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DreadAuraPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:DreadAuraPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:DreadAuraPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public DreadAuraPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        updateDescription();
        this.img = new Texture("img/powers/DreadAuraPower.png");
    }

    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.owner==this.owner){
            for (AbstractPower p : this.owner.powers)
                damageAmount = p.onLoseHp(damageAmount);
            if (this.owner==AbstractDungeon.player){
                for (AbstractRelic r : AbstractDungeon.player.relics)
                    r.onLoseHp(damageAmount);
            }
            for (AbstractPower p : this.owner.powers)
                p.wasHPLost(info, damageAmount);
            if (this.owner==AbstractDungeon.player){
                for (AbstractRelic r : AbstractDungeon.player.relics)
                    r.wasHPLost(damageAmount);
            }
            if (info.owner != null)
                for (AbstractPower p : info.owner.powers)
                    p.onInflictDamage(info, damageAmount, this.owner);
            return 0;
        }
        return damageAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
