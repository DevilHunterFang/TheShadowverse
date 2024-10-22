package shadowverse.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import java.util.Iterator;

public class MinionSummonAction extends AbstractGameAction {
    private AbstractOrb orbType;
    private boolean autoEvoke;

    public MinionSummonAction(AbstractOrb newOrbType) {
        this(newOrbType, true);
    }

    public MinionSummonAction(AbstractOrb newOrbType, boolean autoEvoke) {
        this.autoEvoke = false;
        this.duration = Settings.ACTION_DUR_FAST;
        this.orbType = newOrbType;
        this.autoEvoke = autoEvoke;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.autoEvoke) {
                AbstractDungeon.player.channelOrb(this.orbType);
            } else {
                Iterator var1 = AbstractDungeon.player.orbs.iterator();

                while(var1.hasNext()) {
                    AbstractOrb o = (AbstractOrb)var1.next();
                    if (o instanceof EmptyOrbSlot) {
                        AbstractDungeon.player.channelOrb(this.orbType);
                        break;
                    }
                }
            }

            if (Settings.FAST_MODE) {
                this.isDone = true;
                return;
            }
        }

        this.tickDuration();
    }
}
