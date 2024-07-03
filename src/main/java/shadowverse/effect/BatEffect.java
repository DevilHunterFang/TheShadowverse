package shadowverse.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BatEffect extends AbstractGameEffect {
    public static final String BAT1PATH = "img/Effect/bat/bat1.png";
    public static final String BAT2PATH = "img/Effect/bat/bat2.png";
    public static final String BAT3PATH = "img/Effect/bat/bat3.png";
    public static final String BAT4PATH = "img/Effect/bat/bat4.png";
    private static final Texture BAT1_TEXTURE = new Texture(BAT1PATH);
    private static final Texture BAT2_TEXTURE = new Texture(BAT2PATH);
    private static final Texture BAT3_TEXTURE = new Texture(BAT3PATH);
    private static final Texture BAT4_TEXTURE = new Texture(BAT4PATH);

    private TextureRegion BAT1 = null;
    private static final TextureRegion BAT0 = new TextureRegion(BAT1_TEXTURE);
    private static final TextureRegion BAT2 = new TextureRegion(BAT2_TEXTURE);
    private static final TextureRegion BAT3 = new TextureRegion(BAT3_TEXTURE);
    private static final TextureRegion BAT4 = new TextureRegion(BAT4_TEXTURE);

    private float offSetMiddleY;

    private float graphicsAnimation;
    private float scaleMiddleWidth;
    private float scaleMiddleHeight;
    private float x;
    private float y;

    public BatEffect() {
        this.scale = Settings.scale;
        this.scale = MathUtils.random(1.0F, 1.5F);
        this.startingDuration = this.scale + 0.8F;
        this.duration = this.startingDuration;
        this.color = Color.WHITE.cpy();
        this.color.a = 0.0F;
        this.graphicsAnimation = 0.0F;
        this.scale *= Settings.scale;
        this.offSetMiddleY = 0.0F;
        this.scaleMiddleWidth = Settings.scale;
        this.scaleMiddleHeight = Settings.scale;
        this.offSetMiddleY = 0.0F;
        this.x = AbstractDungeon.player.hb.cX -AbstractDungeon.player.hb.width / 2.0F - 130.0F * Settings.scale;
        this.y = AbstractDungeon.player.hb.cY -AbstractDungeon.player.hb.height / 2.0F + 10.0F * Settings.scale;
    }

    public BatEffect(float x, float y){
        this();
        this.x = x;
        this.y = y;
    }


    public void update() {
        this.graphicsAnimation += Gdx.graphics.getDeltaTime();
        if (this.graphicsAnimation <= 0.5F) {
            this.color.a = this.graphicsAnimation * 2.0F;
            if (this.color.a > 1.0F)
            {
                this.color.a = 1.0F;
            }
        }
        if (this.duration > this.startingDuration * 0.75F) {
            this.BAT1 = BAT0;
        } else if (this.duration > this.startingDuration * 0.6F) {
            this.BAT1 = BAT2;
        } else if (this.duration > this.startingDuration * 0.5F) {
            this.BAT1 = BAT3;
        } else if (this.duration > this.startingDuration * 0.4F) {
            this.BAT1 = BAT4;
        } else if (this.duration > this.startingDuration * 0.3F) {
            this.BAT1 = BAT3;
        } else if (this.duration > this.startingDuration * 0.2F) {
            this.BAT1 = BAT2;
        } else{
            this.BAT1 = BAT0;
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0.0F)
        {
            this.isDone = true;
        }
    }



    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        if (this.BAT1!=null)
        sb.draw(this.BAT1, this.x, this.y, 0.0F, 0.0F, this.BAT1.getRegionWidth(), this.BAT1.getRegionHeight(), this.scaleMiddleWidth, this.scaleMiddleHeight, 0.0F);
    }

    public void dispose() {}
}
