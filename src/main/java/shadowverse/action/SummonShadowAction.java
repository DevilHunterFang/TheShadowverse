package shadowverse.action;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SummonShadowAction extends AbstractGameAction {

    private AbstractMonster m;

    private AbstractMonster mo1;
    private AbstractMonster mo2;

    public SummonShadowAction(AbstractMonster[] shades, AbstractMonster mo1,AbstractMonster mo2) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }
        this.mo1 = mo1;
        this.mo2 = mo2;
        this.duration = this.startDuration;
        int slot = identifySlot(shades);
        if (slot == -1) {
            return;
        }
        this.m = summonShade(slot);
        shades[slot] = this.m;
        for (AbstractRelic r : AbstractDungeon.player.relics)
            r.onSpawnMonster(this.m);
    }

    private int identifySlot(AbstractMonster[] shades) {
        for (int i = 0; i < shades.length; i++) {
            if (shades[i] == null || (shades[i]).isDeadOrEscaped())
                return i;
        }
        return -1;
    }

    private AbstractMonster summonShade(int slot) {
        switch (slot) {
            case 0:
                return mo1;
            case 1:
                return mo2;
        }
        return mo1;
    }

    private int getSmartPosition() {
        int position = 0;
        for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (this.m.drawX > mo.drawX)
                position++;
        }
        return position;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            this.m.animX = 1200.0F * Settings.xScale;
            this.m.init();
            this.m.applyPowers();
            (AbstractDungeon.getCurrRoom()).monsters.addMonster(getSmartPosition(), this.m);
            addToBot(new ApplyPowerAction(this.m, this.m, new MinionPower(this.m)));
        }
        tickDuration();
        if (this.isDone) {
            this.m.animX = 0.0F;
            this.m.showHealthBar();
            this.m.usePreBattleAction();
        } else {
            this.m.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
        }
    }
}
