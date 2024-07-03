package shadowverse.action;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import shadowverse.monsters.Shade;
import shadowverse.powers.MonsterDrainPower;
import shadowverse.powers.NexusPower;

public class SummonDrainStormShadeAction extends AbstractGameAction {

    private AbstractMonster m;

    private float POSX0;

    private float POSX1;

    private float POSX2;

    private AbstractMonster nerva;

    public SummonDrainStormShadeAction(AbstractMonster[] shades, float POSX0, float POSX1,float POSX2, AbstractMonster nerva) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }
        this.duration = this.startDuration;
        this.POSX0 = POSX0;
        this.POSX1 = POSX1;
        this.POSX2 = POSX2;
        this.nerva = nerva;
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
            if (shades[i] == null || (shades[i]).isDying)
                return i;
        }
        return -1;
    }

    private AbstractMonster summonShade(int slot) {
        switch (slot) {
            case 0:
                return new Shade(POSX0, 300.0F);
            case 1:
                return new Shade(POSX1, 300.0F);
            case 2:
                return new Shade(POSX2, 300.0F);
        }
        return new Shade(POSX0, 300.0F);
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
            addToBot(new ApplyPowerAction(this.m,this.m,new MonsterDrainPower(this.m,nerva)));
        }
        tickDuration();
        if (this.isDone) {
            this.m.animX = 0.0F;
            this.m.showHealthBar();
            this.m.usePreBattleAction();
            ((Shade)this.m).getMove();
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    SummonDrainStormShadeAction.this.m.takeTurn();
                    addToBot(new SuicideAction(SummonDrainStormShadeAction.this.m));
                    this.isDone = true;
                }
            });
        } else {
            this.m.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
        }
    }
}
