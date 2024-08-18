package shadowverse.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class KillNervaAction extends AbstractGameAction {
    public KillNervaAction(AbstractMonster target) {
        this.source = null;
        this.target = target;
    }

    @Override
    public void update() {
        (AbstractDungeon.getCurrRoom()).cannotLose = false;
        this.target.currentHealth = 0;
        this.target.healthBarUpdatedEvent();
        this.target.damage(new DamageInfo(null, 0, DamageInfo.DamageType.HP_LOSS));
        this.isDone = true;
    }
}
