package shadowverse.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import shadowverse.action.MinionBuffAction;
import shadowverse.action.RemoveMinionAction;
import shadowverse.powers.DualbladePower;

public abstract class Minion extends AbstractOrb {
    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;

    public int attack;
    public int baseAttack;
    public int defense;
    public int baseDefense;

    @Override
    public void applyFocus() {
        passiveAmount = basePassiveAmount;
        evokeAmount = baseEvokeAmount;
    }

    public abstract void effect();

    public void order() {
        effect();
    }

    @Override
    public void update() {
        this.updateDescription();
        this.hb.update();
        if (this.hb.hovered) {
            TipHelper.renderGenericTip(this.tX + 96.0F * Settings.scale, this.tY + 64.0F * Settings.scale, this.name, this.description);
        }

        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }

    @Override
    public void updateAnimation() {
        this.bobEffect.update();
        this.cX = MathHelper.orbLerpSnap(this.cX, AbstractDungeon.player.animX + this.tX);
        this.cY = MathHelper.orbLerpSnap(this.cY, AbstractDungeon.player.animY + this.tY);
        if (this.channelAnimTimer != 0.0F) {
            this.channelAnimTimer -= Gdx.graphics.getDeltaTime();
            if (this.channelAnimTimer < 0.0F) {
                this.channelAnimTimer = 0.0F;
            }
        }

        this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, 0.0F);
        this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, 0.0F);
        this.updateDescription();
    }

    public void buff(int a, int d) {
        this.attack += a;
        this.defense += d;
    }

    public void onRemove() {
    }

    @Override
    public void onEvoke() {
        for (int i = 0; i < defense; i++) {
            this.effect();
        }
        this.defense = 0;
        AbstractDungeon.actionManager.addToBottom(new RemoveMinionAction());
    }

    @Override
    public void onEndOfTurn() {
        if (this.defense > 0) {
            this.effect();
            if (AbstractDungeon.player.hasPower(DualbladePower.POWER_ID)&&this.defense>1){
                this.effect();
                AbstractDungeon.actionManager.addToBottom(new MinionBuffAction(0, -1, this));
            }
            AbstractDungeon.actionManager.addToBottom(new MinionBuffAction(0, -1, this));
            this.updateDescription();
        }
    }

    @Override
    public void playChannelSFX() { // When you channel this orb, the ATTACK_FIRE effect plays ("Fwoom").
        if(!Settings.DISABLE_EFFECTS){
            AbstractDungeon.actionManager.addToTop(new SFXAction(this.ID.replace("shadowverse:", "")));
        }
    }

    // Render the orb.
    @Override
    public void render(SpriteBatch sb) {
        this.shineColor.a = this.c.a / 2.0F;
        sb.setColor(this.shineColor);
        sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale + MathUtils.sin(this.angle / 12.566371F) * 0.04F * Settings.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale + MathUtils.sin(this.angle / 12.566371F) * 0.04F * Settings.scale, -this.angle, 0, 0, 96, 96, false, false);
        sb.setBlendFunction(770, 771);
        if (this.defense > this.baseDefense) {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.defense), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, new Color(0.2F, 1.0F, 0.2F, this.c.a), this.fontScale);
        } else if (this.defense < this.baseDefense) {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.defense), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, new Color(1.0F, 0.2F, 0.2F, this.c.a), this.fontScale);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.defense), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, new Color(1.0F, 1.0F, 1.0F, this.c.a), this.fontScale);
        }
        if (this.attack > this.baseAttack) {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.attack), this.cX - NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, new Color(0.2F, 1.0F, 0.2F, this.c.a), this.fontScale);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.attack), this.cX - NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, new Color(1.0F, 1.0F, 1.0F, this.c.a), this.fontScale);
        }
        hb.render(sb);
    }
}
