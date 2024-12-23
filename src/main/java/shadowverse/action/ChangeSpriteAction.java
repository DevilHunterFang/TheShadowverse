package shadowverse.action;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import shadowverse.monsters.SpriteCreature;

public class ChangeSpriteAction extends AbstractGameAction {

    SpriteCreature creature;
    SpriterAnimation to;
    SpriterAnimation ori;

    public ChangeSpriteAction(SpriterAnimation to, SpriteCreature c, float duration) {
        this.actionType = ActionType.SPECIAL;
        this.duration = duration;
        this.to = to;
        this.creature = c;
        this.ori = creature.getAnimation();
        creature.setAnimation(to);
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            creature.setAnimation(ori);
            this.to = null;
            System.gc();
            this.isDone = true;
        }
    }
}
