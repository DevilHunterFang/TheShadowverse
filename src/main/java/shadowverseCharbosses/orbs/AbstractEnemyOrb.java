 package shadowverseCharbosses.orbs;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.badlogic.gdx.math.Interpolation;
 import com.badlogic.gdx.math.MathUtils;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.helpers.FontHelper;
 import com.megacrit.cardcrawl.helpers.MathHelper;
 import com.megacrit.cardcrawl.helpers.TipHelper;
 import com.megacrit.cardcrawl.helpers.input.InputHelper;
 import com.megacrit.cardcrawl.orbs.AbstractOrb;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.ArrayList;
 
 
 
 public abstract class AbstractEnemyOrb
   extends AbstractOrb
 {
   public boolean showValues = true;
   public boolean evokeOverride = false;
   public boolean pretendLockOn = false;
   public int evokeMult = 0;
   public int pretendFocus = 0;
   
   public static int masterPretendFocus = 0;
   
   public static AbstractOrb getRandomOrb(boolean useCardRng) {
     ArrayList<AbstractOrb> orbs = new ArrayList<>();
     orbs.add(new EnemyDark());
     orbs.add(new EnemyFrost());
     orbs.add(new EnemyLightning());
     orbs.add(new EnemyPlasma());
     return useCardRng ? orbs.get(AbstractDungeon.cardRandomRng.random(orbs.size() - 1)) : orbs.get(MathUtils.random(orbs.size() - 1));
   }
 
   
   public void update() {
     this.hb.update();
     if (this.hb.hovered) {
       if (InputHelper.mX < 1400.0F * Settings.scale) {
         TipHelper.renderGenericTip(InputHelper.mX + 60.0F * Settings.scale, InputHelper.mY - 50.0F * Settings.scale, this.name, this.description);
       } else {
         TipHelper.renderGenericTip(InputHelper.mX - 350.0F * Settings.scale, InputHelper.mY - 50.0F * Settings.scale, this.name, this.description);
       } 
     }
     
     this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
   }
   
   public void setSlot(int slotNum, int maxOrbs) {
     float dist = 160.0F * Settings.scale + maxOrbs * 10.0F * Settings.scale;
     float angle = 100.0F + maxOrbs * 12.0F;
     float offsetAngle = angle / 2.0F;
     angle *= slotNum / (maxOrbs - 1.0F);
     angle += 90.0F - offsetAngle;
     this.tX = dist * MathUtils.cosDeg(angle) + AbstractCharBoss.boss.drawX;
     this.tY = -80.0F * Settings.scale + dist * MathUtils.sinDeg(angle) + AbstractCharBoss.boss.drawY + AbstractCharBoss.boss.hb_h / 2.0F;
     if (maxOrbs == 1) {
       this.tX = AbstractCharBoss.boss.drawX;
       this.tY = 160.0F * Settings.scale + AbstractCharBoss.boss.drawY + AbstractCharBoss.boss.hb_h / 2.0F;
     } 
     
     this.hb.move(this.tX, this.tY);
   }
   
   public void updateAnimation() {
     this.bobEffect.update();
     if (AbstractCharBoss.boss != null) {
       this.cX = MathHelper.orbLerpSnap(this.cX, AbstractCharBoss.boss.animX + this.tX);
       this.cY = MathHelper.orbLerpSnap(this.cY, AbstractCharBoss.boss.animY + this.tY);
       if (this.channelAnimTimer != 0.0F) {
         this.channelAnimTimer -= Gdx.graphics.getDeltaTime();
         if (this.channelAnimTimer < 0.0F) {
           this.channelAnimTimer = 0.0F;
         }
       } 
       
       this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, this.channelAnimTimer / 0.5F);
       this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, this.channelAnimTimer / 0.5F);
     } 
   }
   
   public void applyFocus() {
     if (AbstractCharBoss.boss.hasPower("Focus")) {
       AbstractPower power = AbstractCharBoss.boss.getPower("Focus");
       this.passiveAmount = Math.max(0, this.basePassiveAmount + power.amount + this.pretendFocus);
       this.evokeAmount = Math.max(0, this.baseEvokeAmount + power.amount + this.pretendFocus);
     } else {
       this.passiveAmount = this.basePassiveAmount + this.pretendFocus;
       this.evokeAmount = this.baseEvokeAmount + this.pretendFocus;
     } 
   }
 
   
   public void applyLockOn() {
     if (AbstractDungeon.player.hasPower("Lockon") || this.pretendLockOn) {
       if (this instanceof EnemyEmptyOrbSlot)
         return;  if (this.ID.equals("Lightning")) {
         this.passiveAmount = Math.max(0, (int)Math.floor(this.passiveAmount * 1.5D));
         this.evokeAmount = Math.max(0, (int)Math.floor(this.evokeAmount * 1.5D));
       } 
       if (this.ID.equals("Dark")) {
         this.evokeAmount = Math.max(0, (int)Math.floor(this.evokeAmount * 1.5D));
       }
     } 
   }
 
   
   protected void renderText(SpriteBatch sb) {
     if (!(this instanceof EnemyEmptyOrbSlot) && this.showValues)
       if (this.showEvokeValue || this.evokeOverride) {
         FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, (this.evokeMult > 0) ? (Integer.toString(this.evokeAmount) + "x" + Integer.toString(this.evokeMult)) : Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
       } else {
         FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, this.c, this.fontScale);
       }  
   }
 }
