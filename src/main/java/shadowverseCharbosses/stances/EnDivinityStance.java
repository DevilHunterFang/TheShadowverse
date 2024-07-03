 package shadowverseCharbosses.stances;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.math.MathUtils;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.localization.StanceStrings;
 import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
 import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;
 import shadowverseCharbosses.actions.common.EnemyGainEnergyAction;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.vfx.EnemyDivinityParticleEffect;
 import shadowverseCharbosses.vfx.EnemyStanceAuraEffect;
 
 
 public class EnDivinityStance
   extends AbstractEnemyStance
 {
   public static final String STANCE_ID = "Divinity";
   
   public EnDivinityStance() {
     this.ID = "Divinity";
     this.name = stanceString.NAME;
     updateDescription();
   }
   
   public void updateAnimation() {
     if (AbstractCharBoss.boss != null) {
       if (!Settings.DISABLE_EFFECTS) {
         this.particleTimer -= Gdx.graphics.getDeltaTime();
         if (this.particleTimer < 0.0F) {
           this.particleTimer = 0.2F;
           AbstractDungeon.effectsQueue.add(new EnemyDivinityParticleEffect());
         } 
       } 
       
       this.particleTimer2 -= Gdx.graphics.getDeltaTime();
       if (this.particleTimer2 < 0.0F) {
         this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
         AbstractDungeon.effectsQueue.add(new EnemyStanceAuraEffect("Divinity"));
       } 
     } 
   }
 
 
   
   public void onEndOfTurn() {}
 
 
   
   public float atDamageGive(float damage, DamageInfo.DamageType type) {
     return (type == DamageInfo.DamageType.NORMAL) ? (damage * 3.0F) : damage;
   }
   
   public void updateDescription() {
     this.description = stanceString.DESCRIPTION[0];
   }
   
   public void onEnterStance() {
     if (sfxId != -1L) {
       stopIdleSfx();
     }
     
     CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY");
     sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_DIVINITY");
     AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.PINK, true));
     AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(AbstractCharBoss.boss.hb.cX, AbstractCharBoss.boss.hb.cY, "Divinity"));
     AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new EnemyGainEnergyAction(3));
   }
   
   public void onExitStance() {
     stopIdleSfx();
   }
   
   public void stopIdleSfx() {
     if (sfxId != -1L) {
       CardCrawlGame.sound.stop("STANCE_LOOP_DIVINITY", sfxId);
       sfxId = -1L;
     } 
   }
 
 
   
   private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString("Divinity");
   private static long sfxId = -1L;
 }
