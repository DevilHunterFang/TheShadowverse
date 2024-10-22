package shadowverse.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import shadowverse.orbs.Minion;

public class MinionBuffAction extends AbstractGameAction {

    public int a;
    public int d;
    public boolean all;
    public Minion minion;

    public MinionBuffAction(int a, int d) {
        this.actionType = ActionType.SPECIAL;
        this.a = a;
        this.d = d;
        this.minion = null;
        AbstractPlayer p = AbstractDungeon.player;
        for (int i = 0; i < p.orbs.size(); i++) {
            if (p.orbs.get(i) instanceof Minion) {
                this.minion = (Minion) p.orbs.get(i);
                break;
            }
        }
    }

    public MinionBuffAction(int a, int d, Minion m) {
        this.actionType = ActionType.SPECIAL;
        this.a = a;
        this.d = d;
        this.minion = m;
    }

    public MinionBuffAction(int a, int d, boolean all) {
        this.actionType = ActionType.SPECIAL;
        this.a = a;
        this.d = d;
        this.all = all;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.all) {
            for (int i = 0; i < p.orbs.size(); i++) {
                if (p.orbs.get(i) instanceof Minion) {
                    ((Minion) p.orbs.get(i)).buff(a, d);
                }
            }
        } else {
            if(this.minion!=null){
                this.minion.buff(this.a, this.d);
            }
        }
        this.isDone = true;
    }
}
