package shadowverseCharbosses.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import shadowverseCharbosses.bosses.AbstractCharBoss;


public class EnemyDiscardAction
        extends AbstractGameAction {
    public static final String[] TEXT;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
    private static final float DURATION;

    static {
        TEXT = uiStrings.TEXT;
        DURATION = Settings.ACTION_DUR_XFAST;
    }

    public static int numDiscarded;
    private AbstractCharBoss p;
    private boolean endTurn;

    public EnemyDiscardAction(AbstractCreature target, AbstractCreature source, int amount) {
        this(target, source, amount, false);
    }

    public EnemyDiscardAction(AbstractCreature target, AbstractCreature source, int amount, boolean endTurn) {
        this.p = (AbstractCharBoss) target;
        setValues(target, source, amount);
        this.actionType = ActionType.DISCARD;
        this.endTurn = endTurn;
        this.duration = DURATION;
    }


    public void update() {
        if (this.duration == DURATION) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }
            if (this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                for (int tmp = this.p.hand.size(), i = 0; i < tmp; i++) {
                    AbstractCard c = this.p.hand.getTopCard();
                    this.p.hand.moveToDiscardPile(c);
                    if (!this.endTurn) {
                        c.triggerOnManualDiscard();
                    }
                }
                this.p.hand.applyPowers();
                tickDuration();
                return;
            }
            for (int j = 0; j < this.amount; j++) {
                AbstractCard c2 = this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                this.p.hand.moveToDiscardPile(c2);
                c2.triggerOnManualDiscard();
            }
        }
        tickDuration();
    }
}
