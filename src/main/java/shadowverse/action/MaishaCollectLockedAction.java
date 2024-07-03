package shadowverse.action;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


public class MaishaCollectLockedAction extends AbstractGameAction {
    private AbstractCard card = null;
    private CardGroup group = null;
    private float startingDuration;

    public MaishaCollectLockedAction(AbstractCard card, CardGroup group) {
        this.card = card;
        this.group = group;
        this.duration = Settings.ACTION_DUR_LONG;
        this.startingDuration = Settings.ACTION_DUR_LONG;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        AbstractCard c = this.card;
        if (this.duration == this.startingDuration) {
            this.group.removeCard(c);
            AbstractDungeon.player.limbo.addToBottom(c);
            c.setAngle(0.0F);
            c.targetDrawScale = 0.75F;
            c.target_x = (float) Settings.WIDTH / 2.0F;
            c.target_y = (float) Settings.HEIGHT / 2.0F;
            c.lighten(false);
            c.unfadeOut();
            c.unhover();
            c.untip();
            c.stopGlowing();
        }
        this.tickDuration();
        if (this.isDone) {
            AbstractDungeon.actionManager.addToTop(new ShowCardAction(c));
        }
    }
}