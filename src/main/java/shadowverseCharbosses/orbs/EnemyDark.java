 package shadowverseCharbosses.orbs;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.animations.VFXAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.helpers.FontHelper;
 import com.megacrit.cardcrawl.helpers.ImageMaster;
 import com.megacrit.cardcrawl.localization.OrbStrings;
 import com.megacrit.cardcrawl.orbs.AbstractOrb;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
 import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
 import com.megacrit.cardcrawl.vfx.combat.DarkOrbPassiveEffect;
 import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
 import shadowverseCharbosses.actions.orb.EnemyDarkOrbEvokeAction;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyDark
   extends AbstractEnemyOrb
 {
   public static final String ORB_ID = "Dark";
   public static final String[] DESC;
   private static final float ORB_BORDER_SCALE = 1.2F;
   private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString("Dark"); static {
     DESC = orbString.DESCRIPTION;
   }
   private static final float VFX_INTERVAL_TIME = 0.25F;
   private float vfxTimer;
   
   public EnemyDark() {
     this.vfxTimer = 0.5F;
     this.ID = "Dark";
     this.img = ImageMaster.ORB_DARK;
     this.name = orbString.NAME;
     this.baseEvokeAmount = 6;
     this.evokeAmount = this.baseEvokeAmount;
     this.basePassiveAmount = 6;
     this.passiveAmount = this.basePassiveAmount;
     updateDescription();
     this.channelAnimTimer = 0.5F;
   }
 
   
   public void updateDescription() {
     applyFocus();
     this.description = DESC[0] + this.passiveAmount + DESC[1] + this.evokeAmount + DESC[2];
   }
 
   
   public void onEvoke() {
     AbstractDungeon.actionManager.addToTop((AbstractGameAction)new EnemyDarkOrbEvokeAction(new DamageInfo((AbstractCreature)AbstractCharBoss.boss, this.evokeAmount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
   }
 
   
   public void onEndOfTurn() {
     float speedTime = 0.6F / AbstractCharBoss.boss.orbs.size();
     if (Settings.FAST_MODE) {
       speedTime = 0.0F;
     }
     AbstractDungeon.actionManager.addToTop((AbstractGameAction)new VFXAction((AbstractGameEffect)new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.DARK), speedTime));
     this.evokeAmount += this.passiveAmount;
     updateDescription();
   }
 
   
   public void triggerEvokeAnimation() {
     CardCrawlGame.sound.play("ORB_DARK_EVOKE", 0.1F);
     AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(this.cX, this.cY));
   }
 
   
   public void applyFocus() {
     AbstractPower power = AbstractCharBoss.boss.getPower("Focus");
     if (power != null) {
       this.passiveAmount = Math.max(0, this.basePassiveAmount + power.amount);
     } else {
       this.passiveAmount = this.basePassiveAmount;
     } 
   }
 
   
   public void updateAnimation() {
     super.updateAnimation();
     this.angle += Gdx.graphics.getDeltaTime() * 120.0F;
     this.vfxTimer -= Gdx.graphics.getDeltaTime();
     if (this.vfxTimer < 0.0F) {
       AbstractDungeon.effectList.add(new DarkOrbPassiveEffect(this.cX, this.cY));
       this.vfxTimer = 0.25F;
     } 
   }
 
   
   public void render(SpriteBatch sb) {
     sb.setColor(this.c);
     sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);
     this.c.a /= 3.0F;
     sb.setColor(this.shineColor);
     sb.setBlendFunction(770, 1);
     sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale * 1.2F, this.scale * 1.2F, this.angle / 1.2F, 0, 0, 96, 96, false, false);
     sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale * 1.5F, this.scale * 1.5F, this.angle / 1.4F, 0, 0, 96, 96, false, false);
     sb.setBlendFunction(770, 771);
     renderText(sb);
     this.hb.render(sb);
   }
 
   
   protected void renderText(SpriteBatch sb) {
     if (this.showValues) {
       Color niceCalmBlue = new Color(0.2F, 1.0F, 1.0F, this.c.a);
       if (this.evokeOverride || this.showEvokeValue) {
         niceCalmBlue = Color.RED.cpy();
       }
       if (this.showEvokeValue || this.evokeOverride) {
         FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, (this.evokeMult > 0) ? (Integer.toString(this.evokeAmount) + "x" + Integer.toString(this.evokeMult)) : Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, niceCalmBlue, this.fontScale);
       } else {
         FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale, niceCalmBlue, this.fontScale);
         FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, this.c, this.fontScale);
       } 
     } 
   }
 
   
   public void playChannelSFX() {
     CardCrawlGame.sound.play("ORB_DARK_CHANNEL", 0.1F);
   }
 
   
   public AbstractOrb makeCopy() {
     return new EnemyDark();
   }
 }