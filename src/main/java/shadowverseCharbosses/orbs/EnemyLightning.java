 package shadowverseCharbosses.orbs;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.badlogic.gdx.math.MathUtils;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.helpers.ImageMaster;
 import com.megacrit.cardcrawl.localization.OrbStrings;
 import com.megacrit.cardcrawl.orbs.AbstractOrb;
 import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;
 import com.megacrit.cardcrawl.vfx.combat.LightningOrbPassiveEffect;
 import shadowverseCharbosses.actions.orb.EnemyLightningOrbEvokeAction;
 import shadowverseCharbosses.actions.orb.EnemyLightningOrbPassiveAction;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 
 
 
 public class EnemyLightning
   extends AbstractEnemyOrb
 {
   public static final String ORB_ID = "Lightning";
   private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString("Lightning");
   private static final float PI_DIV_16 = 0.19634955F;
   private static final float ORB_WAVY_DIST = 0.05F;
   private float vfxTimer = 1.0F; private static final float PI_4 = 12.566371F; private static final float ORB_BORDER_SCALE = 1.2F;
   
   public EnemyLightning() {
     this.ID = "Lightning";
     this.img = ImageMaster.ORB_LIGHTNING;
     this.name = orbString.NAME;
     this.baseEvokeAmount = 8;
     this.evokeAmount = this.baseEvokeAmount;
     this.basePassiveAmount = 3;
     this.passiveAmount = this.basePassiveAmount;
     updateDescription();
     this.angle = MathUtils.random(360.0F);
     this.channelAnimTimer = 0.5F;
   }
   
   public void updateDescription() {
     applyFocus();
     this.description = orbString.DESCRIPTION[0] + this.passiveAmount + orbString.DESCRIPTION[1] + this.evokeAmount + orbString.DESCRIPTION[2];
   }
   
   public void onEvoke() {
     AbstractDungeon.actionManager.addToTop((AbstractGameAction)new EnemyLightningOrbEvokeAction(new DamageInfo((AbstractCreature)AbstractCharBoss.boss, this.evokeAmount, DamageInfo.DamageType.THORNS), false));
   }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   
   public void onEndOfTurn() {
     AbstractDungeon.actionManager.addToTop((AbstractGameAction)new EnemyLightningOrbPassiveAction(new DamageInfo((AbstractCreature)AbstractCharBoss.boss, this.passiveAmount, DamageInfo.DamageType.THORNS), this, false));
   }
   
   public void triggerEvokeAnimation() {
     CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);
     AbstractDungeon.effectsQueue.add(new LightningOrbActivateEffect(this.cX, this.cY));
   }
   
   public void updateAnimation() {
     super.updateAnimation();
     this.angle += Gdx.graphics.getDeltaTime() * 180.0F;
     this.vfxTimer -= Gdx.graphics.getDeltaTime();
     if (this.vfxTimer < 0.0F) {
       AbstractDungeon.effectList.add(new LightningOrbPassiveEffect(this.cX, this.cY));
       if (MathUtils.randomBoolean()) {
         AbstractDungeon.effectList.add(new LightningOrbPassiveEffect(this.cX, this.cY));
       }
       
       this.vfxTimer = MathUtils.random(0.15F, 0.8F);
     } 
   }
   
   public void render(SpriteBatch sb) {
     this.c.a /= 2.0F;
     sb.setColor(this.shineColor);
     sb.setBlendFunction(770, 1);
     sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale + MathUtils.sin(this.angle / 12.566371F) * 0.05F + 0.19634955F, this.scale * 1.2F, this.angle, 0, 0, 96, 96, false, false);
     sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale * 1.2F, this.scale + MathUtils.sin(this.angle / 12.566371F) * 0.05F + 0.19634955F, -this.angle, 0, 0, 96, 96, false, false);
     sb.setBlendFunction(770, 771);
     sb.setColor(this.c);
     sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.angle / 12.0F, 0, 0, 96, 96, false, false);
     renderText(sb);
     this.hb.render(sb);
   }
   
   public void playChannelSFX() {
     CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
   }
   
   public AbstractOrb makeCopy() {
     return new EnemyLightning();
   }
 }
