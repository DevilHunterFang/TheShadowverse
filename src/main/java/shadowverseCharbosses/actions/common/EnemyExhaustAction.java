package shadowverseCharbosses.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import shadowverseCharbosses.bosses.AbstractCharBoss;


public class EnemyExhaustAction
        extends AbstractGameAction {
    public static final String[] TEXT;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");

    static {
        TEXT = uiStrings.TEXT;
    }

    public static int numExhausted;
    private AbstractCharBoss p;
    private boolean isRandom;
    private boolean anyNumber;
    private boolean canPickZero;

    public EnemyExhaustAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero) {
        this.anyNumber = anyNumber;
        this.p = AbstractCharBoss.boss;
        this.canPickZero = canPickZero;
        this.isRandom = isRandom;
        this.amount = amount;
        float action_DUR_FAST = Settings.ACTION_DUR_FAST;
        this.startDuration = action_DUR_FAST;
        this.duration = action_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    public EnemyExhaustAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom, boolean anyNumber) {
        this(amount, isRandom, anyNumber);
        this.target = target;
        this.source = source;
    }

    public EnemyExhaustAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom) {
        this(amount, isRandom, false, false);
        this.target = target;
        this.source = source;
    }

    public EnemyExhaustAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom, boolean anyNumber, boolean canPickZero) {
        this(amount, isRandom, anyNumber, canPickZero);
        this.target = target;
        this.source = source;
    }

    public EnemyExhaustAction(boolean isRandom, boolean anyNumber, boolean canPickZero) {
        this(99, isRandom, anyNumber, canPickZero);
    }

    public EnemyExhaustAction(int amount, boolean canPickZero) {
        this(amount, false, false, canPickZero);
    }

    public EnemyExhaustAction(int amount, boolean isRandom, boolean anyNumber) {
        this(amount, isRandom, anyNumber, false);
    }

    public EnemyExhaustAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero, float duration) {
        this(amount, isRandom, anyNumber, canPickZero);
        this.startDuration = duration;
        this.duration = duration;
    }


    public void update() {
        if (this.duration == this.startDuration) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }
            if (!this.anyNumber && this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                numExhausted = this.amount;
                for (int tmp = this.p.hand.size(), i = 0; i < tmp; i++) {
                    AbstractCard c = this.p.hand.getTopCard();
                    this.p.hand.moveToExhaustPile(c);
                }
                return;
            }
            if (this.isRandom) {
                this.p.hand.moveToExhaustPile(this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng));
                return;
            }
            for (int j = 0; j < this.amount; j++) {
                AbstractCard tc = this.p.hand.getTopCard();
                for (AbstractCard c : this.p.hand.group) {
                    if (c.rarity == AbstractCard.CardRarity.BASIC) {
                        tc = c;
                    }
                }
                this.p.hand.moveToExhaustPile(tc);
            }
        }
        tickDuration();
    }
}
