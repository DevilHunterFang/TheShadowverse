package shadowverseCharbosses.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import shadowverseCharbosses.bosses.AbstractCharBoss;


public class EnemyWrathParticleEffect
        extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion img = ImageMaster.GLOW_SPARK;
    private float dur_div2 = this.duration / 2.0F;
    private float vY;

    public EnemyWrathParticleEffect() {
        if (AbstractCharBoss.boss != null) {
            this.x = AbstractCharBoss.boss.hb.cX + MathUtils.random(-AbstractCharBoss.boss.hb.width / 2.0F - 30.0F * Settings.scale, AbstractCharBoss.boss.hb.width / 2.0F + 30.0F * Settings.scale);
            this.y = AbstractCharBoss.boss.hb.cY + MathUtils.random(-AbstractCharBoss.boss.hb.height / 2.0F - -10.0F * Settings.scale, AbstractCharBoss.boss.hb.height / 2.0F - 10.0F * Settings.scale);
        }
        this.x -= this.img.packedWidth / 2.0F;
        this.y -= this.img.packedHeight / 2.0F;
        this.renderBehind = MathUtils.randomBoolean(0.2F + this.scale - 0.5F);
        this.rotation = MathUtils.random(-8.0F, 8.0F);
        this.color = new Color(MathUtils.random(0.6F, 0.7F), MathUtils.random(0.0F, 0.1F), MathUtils.random(0.1F, 0.2F), 0.0F);
    }

    private float y;
    private float x;

    public void update() {
        if (this.duration > this.dur_div2) {
            this.color.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - this.dur_div2) / this.dur_div2);
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / this.dur_div2);
        }

        this.vY += Gdx.graphics.getDeltaTime() * 40.0F * Settings.scale;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }


    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        if (AbstractCharBoss.boss != null) {
            sb.setBlendFunction(770, 1);
            sb.draw((TextureRegion) this.img, this.x, this.y + this.vY, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale * 0.8F, (0.1F + (this.dur_div2 * 2.0F - this.duration) * 2.0F * this.scale) * Settings.scale, this.rotation);
            sb.setBlendFunction(770, 771);
        }
    }

    public void dispose() {
    }
}

