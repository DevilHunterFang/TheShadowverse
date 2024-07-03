 package shadowverseCharbosses.stances;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.math.MathUtils;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.localization.StanceStrings;
 import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.vfx.EnemyStanceAuraEffect;
 import shadowverseCharbosses.vfx.EnemyStanceChangeParticleGenerator;
 import shadowverseCharbosses.vfx.EnemyWrathParticleEffect;
 
 public class EnWrathStance
   extends AbstractEnemyStance
 {
   public static final String STANCE_ID = "Wrath";
   
   public EnWrathStance() {
     this.ID = "Wrath";
     this.name = stanceString.NAME;
     updateDescription();
   }
   
   public float atDamageGive(float damage, DamageInfo.DamageType type) {
     return (type == DamageInfo.DamageType.NORMAL) ? (damage * 1.5F) : damage;
   }
   
   public void updateAnimation() {
     if (AbstractCharBoss.boss != null) {
       if (!Settings.DISABLE_EFFECTS) {
         this.particleTimer -= Gdx.graphics.getDeltaTime();
         if (this.particleTimer < 0.0F) {
           this.particleTimer = 0.05F;
           AbstractDungeon.effectsQueue.add(new EnemyWrathParticleEffect());
         } 
       } 
       
       this.particleTimer2 -= Gdx.graphics.getDeltaTime();
       if (this.particleTimer2 < 0.0F) {
         this.particleTimer2 = MathUtils.random(0.3F, 0.4F);
         AbstractDungeon.effectsQueue.add(new EnemyStanceAuraEffect("Wrath"));
       } 
     } 
   }
 
   
   public void updateDescription() {
     this.description = stanceString.DESCRIPTION[0];
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
 
 
   
   private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString("Wrath");
   private static long sfxId = -1L;
 }
