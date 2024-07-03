 package shadowverseCharbosses.ui;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.graphics.Texture;
 import com.badlogic.gdx.graphics.g2d.BitmapFont;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.badlogic.gdx.math.Interpolation;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.helpers.*;
 import com.megacrit.cardcrawl.helpers.input.InputHelper;
 import com.megacrit.cardcrawl.localization.UIStrings;
 import com.megacrit.cardcrawl.rooms.AbstractRoom;
 import com.megacrit.cardcrawl.ui.panels.AbstractPanel;
 import com.megacrit.cardcrawl.unlock.UnlockTracker;
 import com.megacrit.cardcrawl.vfx.RefreshEnergyEffect;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.vfx.EnemyRefreshEnergyEffect;
 
 public class EnemyEnergyPanel extends AbstractPanel {
   public static final String[] MSG;
   private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("shadowverse:EnergyPanelTip");
            public static final String[] LABEL; public static final float FONT_POP_SCALE = 2.0F;
   public static final float ENERGY_VFX_TIME = 2.0F;
   private static final int RAW_W = 256;
   private static final Color ENERGY_TEXT_COLOR;
   private static final float VFX_ROTATE_SPEED = -30.0F;
   public static float fontScale;
   public static int totalCount;
   public static float energyVfxTimer;
   
   static {
     MSG = uiStrings.TEXT;
     LABEL = uiStrings.EXTRA_TEXT;
     ENERGY_TEXT_COLOR = new Color(1.0F, 1.0F, 0.86F, 1.0F);
     fontScale = 1.0F;
     totalCount = 0;
     energyVfxTimer = 0.0F;
   }
   public BitmapFont energyNumFont;
   private Hitbox tipHitbox;
   public Texture gainEnergyImg;
   private float energyVfxAngle;
   private float energyVfxScale;
   private Color energyVfxColor;
   private AbstractCharBoss owner;
   
   public EnemyEnergyPanel(AbstractCharBoss owner) {
     super(Settings.WIDTH - 198.0F * Settings.scale, Settings.HEIGHT - 190.0F * Settings.scale, -480.0F * Settings.scale, 200.0F * Settings.scale, 12.0F * Settings.scale, -12.0F * Settings.scale, null, false);
     this.tipHitbox = new Hitbox(0.0F, 0.0F, 120.0F * Settings.scale, 120.0F * Settings.scale);
     this.energyVfxAngle = 0.0F;
     this.energyVfxScale = Settings.scale;
     this.energyVfxColor = Color.WHITE.cpy();
     this.owner = owner;
     switch (this.owner.chosenClass) {
       case DEFECT:
         this.gainEnergyImg = ImageMaster.BLUE_ORB_FLASH_VFX;
         this.energyNumFont = FontHelper.energyNumFontBlue;
         return;
       case IRONCLAD:
         this.gainEnergyImg = ImageMaster.RED_ORB_FLASH_VFX;
         this.energyNumFont = FontHelper.energyNumFontRed;
         return;
       case THE_SILENT:
         this.gainEnergyImg = ImageMaster.GREEN_ORB_FLASH_VFX;
         this.energyNumFont = FontHelper.energyNumFontGreen;
         return;
       case WATCHER:
         this.gainEnergyImg = ImageMaster.PURPLE_ORB_FLASH_VFX;
         this.energyNumFont = FontHelper.energyNumFontPurple;
         return;
     } 
     this.gainEnergyImg = ImageMaster.RED_ORB_FLASH_VFX;
     this.energyNumFont = FontHelper.energyNumFontRed;
   }
 
 
 
 
 
 
 
 
 
 
   
   public static void setEnergy(int energy) {
     totalCount = energy;
     AbstractDungeon.effectsQueue.add(new RefreshEnergyEffect());
     energyVfxTimer = 2.0F;
     fontScale = 2.0F;
   }
   
   public static void addEnergy(int e) {
     totalCount += e;
     if (totalCount >= 9) {
       UnlockTracker.unlockAchievement("ADRENALINE");
     }
     if (totalCount > 999) {
       totalCount = 999;
     }
     AbstractDungeon.effectsQueue.add(new EnemyRefreshEnergyEffect());
     fontScale = 2.0F;
     energyVfxTimer = 2.0F;
   }
   
   public static void useEnergy(int e) {
     totalCount -= e;
     if (totalCount < 0) {
       totalCount = 0;
     }
     if (e != 0) {
       fontScale = 2.0F;
     }
   }
   
   public int getCurrentEnergy() {
     if (AbstractDungeon.player == null) {
       return 0;
     }
     return totalCount;
   }
   
   public void update() {
     this.owner.energyOrb.updateOrb(totalCount);
     updateVfx();
     if (fontScale != 1.0F) {
       fontScale = MathHelper.scaleLerpSnap(fontScale, 1.0F);
     }
     this.tipHitbox.update();
     if (this.tipHitbox.hovered && !AbstractDungeon.isScreenUp) {
       AbstractDungeon.overlayMenu.hoveredTip = true;
     }
     if (Settings.isDebug) {
       if (InputHelper.scrolledDown) {
         addEnergy(1);
       } else if (InputHelper.scrolledUp && totalCount > 0) {
         useEnergy(1);
       } 
     }
   }
   
   private void updateVfx() {
     if (energyVfxTimer != 0.0F) {
       this.energyVfxColor.a = Interpolation.exp10In.apply(0.5F, 0.0F, 1.0F - energyVfxTimer / 2.0F);
       this.energyVfxAngle += Gdx.graphics.getDeltaTime() * -30.0F;
       this.energyVfxScale = Settings.scale * Interpolation.exp10In.apply(1.0F, 0.1F, 1.0F - energyVfxTimer / 2.0F);
       energyVfxTimer -= Gdx.graphics.getDeltaTime();
       if (energyVfxTimer < 0.0F) {
         energyVfxTimer = 0.0F;
         this.energyVfxColor.a = 0.0F;
       } 
     } 
   }
 
   
   public void render(SpriteBatch sb) {
     this.tipHitbox.move(this.current_x, this.current_y);
     renderOrb(sb);
     renderVfx(sb);
     String energyMsg = totalCount + "/" + this.owner.energy.energy;
     AbstractDungeon.player.getEnergyNumFont().getData().setScale(fontScale);
     FontHelper.renderFontCentered(sb, this.energyNumFont, energyMsg, this.current_x, this.current_y, ENERGY_TEXT_COLOR);
     this.tipHitbox.render(sb);
     if (this.tipHitbox.hovered && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
       TipHelper.renderGenericTip(1550.0F * Settings.scale, 750.0F * Settings.scale, LABEL[0], MSG[0]);
     }
   }
   
   private void renderOrb(SpriteBatch sb) {
     if (totalCount == 0) {
       this.owner.energyOrb.renderOrb(sb, false, this.current_x, this.current_y);
     } else {
       this.owner.energyOrb.renderOrb(sb, true, this.current_x, this.current_y);
     } 
   }
   
   private void renderVfx(SpriteBatch sb) {
     if (energyVfxTimer != 0.0F) {
       sb.setBlendFunction(770, 1);
       sb.setColor(this.energyVfxColor);
       sb.draw(this.gainEnergyImg, this.current_x - 128.0F, this.current_y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.energyVfxScale, this.energyVfxScale, -this.energyVfxAngle + 50.0F, 0, 0, 256, 256, true, false);
       sb.draw(this.gainEnergyImg, this.current_x - 128.0F, this.current_y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.energyVfxScale, this.energyVfxScale, this.energyVfxAngle, 0, 0, 256, 256, false, false);
       sb.setBlendFunction(770, 771);
     } 
   }
 }
