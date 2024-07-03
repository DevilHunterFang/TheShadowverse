package shadowverseCharbosses.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import shadowverse.effect.BatEffect;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.vfx.EnemyStanceAuraEffect;
import shadowverseCharbosses.vfx.EnemyStanceChangeParticleGenerator;

public class EnVengeance
        extends AbstractEnemyStance {
    public static final String STANCE_ID = "shadowverse:Vengeance";
    public static final CharacterStrings charStrings = CardCrawlGame.languagePack.getCharacterString("shadowverse:Vampire");
    private static long sfxId = -1L;

    public EnVengeance() {
        this.ID = STANCE_ID;
        this.name = charStrings.TEXT[1];
        updateDescription();
    }


    public void updateAnimation() {
        if (AbstractCharBoss.boss != null) {
            if (!Settings.DISABLE_EFFECTS) {
                this.particleTimer -= Gdx.graphics.getDeltaTime();
                if (this.particleTimer < 0.0F) {
                    this.particleTimer = 1.4F;
                    AbstractDungeon.effectsQueue.add(new BatEffect(AbstractCharBoss.boss.hb.cX -AbstractCharBoss.boss.hb.width / 2.0F - 130.0F * Settings.scale,AbstractCharBoss.boss.hb.cY -AbstractCharBoss.boss.hb.height / 2.0F + 50.0F * Settings.scale));
                }
            }
            this.particleTimer2 -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer2 < 0.0F) {
                this.particleTimer2 = MathUtils.random(0.3F, 0.4F);
                AbstractDungeon.effectsQueue.add(new EnemyStanceAuraEffect("Wrath"));
            }
        }
    }


    @Override
    public void updateDescription() {
        this.description = charStrings.TEXT[2];
    }

    public void onEnterStance() {
        if (sfxId != -1L) {
            stopIdleSfx();
        }

        CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_WRATH");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SCARLET, true));
        AbstractDungeon.effectsQueue.add(new EnemyStanceChangeParticleGenerator(AbstractCharBoss.boss.hb.cX, AbstractCharBoss.boss.hb.cY, "Wrath"));
    }

    public void onExitStance() {
        stopIdleSfx();
    }

    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_WRATH", sfxId);
            sfxId = -1L;
        }
    }

}
