 package shadowverseCharbosses.stances;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.math.MathUtils;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.localization.StanceStrings;
 import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
 import shadowverseCharbosses.actions.common.EnemyGainEnergyAction;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.vfx.EnemyCalmParticleEffect;
 import shadowverseCharbosses.vfx.EnemyStanceAuraEffect;
 
 public class EnCalmStance
   extends AbstractEnemyStance
 {
   public static final String STANCE_ID = "Calm";
   
   public EnCalmStance() {
     this.ID = "Calm";
     this.name = stanceString.NAME;
     updateDescription();
   }
   
   public void updateDescription() {
     this.description = stanceString.DESCRIPTION[0];
   }
   
   public void updateAnimation() {
     if (AbstractCharBoss.boss != null) {
       if (!Settings.DISABLE_EFFECTS) {
         this.particleTimer -= Gdx.graphics.getDeltaTime();
         if (this.particleTimer < 0.0F) {
           this.particleTimer = 0.04F;
           AbstractDungeon.effectsQueue.add(new EnemyCalmParticleEffect());
         } 
       } 
       
       this.particleTimer2 -= Gdx.graphics.getDeltaTime();
       if (this.particleTimer2 < 0.0F) {
         this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
         AbstractDungeon.effectsQueue.add(new EnemyStanceAuraEffect("Calm"));
       } 
     } 
   }
   
   public void onEnterStance() {
     if (sfxId != -1L) {
       stopIdleSfx();
     }
     
     CardCrawlGame.sound.play("STANCE_ENTER_CALM");
     sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
     AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SKY, true));
   }
   
   public void onExitStance() {
     AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new EnemyGainEnergyAction(2));
     stopIdleSfx();
   }
   
   public void stopIdleSfx() {
     if (sfxId != -1L) {
       CardCrawlGame.sound.stop("STANCE_LOOP_CALM", sfxId);
       sfxId = -1L;
     } 
   }
 
 
   
   private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString("Calm");
   private static long sfxId = -1L;
 }

