package shadowverseCharbosses.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class RealWaitAction extends AbstractGameAction {
    public RealWaitAction(float setDur) {
        this.setValues((AbstractCreature)null, (AbstractCreature)null, 0);
        this.duration = setDur;
        this.actionType = ActionType.WAIT;
    }

    public void update() {
        this.tickDuration();
    }
}