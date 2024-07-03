 package shadowverseCharbosses.orbs;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.badlogic.gdx.math.MathUtils;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.helpers.ImageMaster;
 import com.megacrit.cardcrawl.localization.OrbStrings;
 import com.megacrit.cardcrawl.orbs.AbstractOrb;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 
 public class EnemyEmptyOrbSlot
   extends AbstractEnemyOrb
 {
   public static final String ORB_ID = "Empty";
   public static final String[] DESC;
   private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString("Empty"); static {
     DESC = orbString.DESCRIPTION;
   }
   
   public EnemyEmptyOrbSlot(float x, float y) {
     this.angle = MathUtils.random(360.0F);
     this.ID = "Empty";
     this.name = orbString.NAME;
     this.evokeAmount = 0;
     this.cX = x;
     this.cY = y;
     updateDescription();
     this.channelAnimTimer = 0.5F;
   }
   
   public EnemyEmptyOrbSlot() {
     this.angle = MathUtils.random(360.0F);
     this.name = orbString.NAME;
     this.evokeAmount = 0;
     this.cX = AbstractCharBoss.boss.drawX + AbstractCharBoss.boss.hb_x;
     this.cY = AbstractCharBoss.boss.drawY + AbstractCharBoss.boss.hb_y + AbstractCharBoss.boss.hb_h / 2.0F;
     updateDescription();
   }
 
   
   public void updateDescription() {
     this.description = DESC[0];
   }
 
 
   
   public void onEvoke() {}
 
   
   public void updateAnimation() {
     super.updateAnimation();
     this.angle += Gdx.graphics.getDeltaTime() * 10.0F;
   }
 
   
   public void render(SpriteBatch sb) {
     sb.setColor(this.c);
     sb.draw(ImageMaster.ORB_SLOT_2, this.cX - 48.0F - this.bobEffect.y / 8.0F, this.cY - 48.0F + this.bobEffect.y / 8.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
     sb.draw(ImageMaster.ORB_SLOT_1, this.cX - 48.0F + this.bobEffect.y / 8.0F, this.cY - 48.0F - this.bobEffect.y / 8.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);
     
     this.hb.render(sb);
   }
 
   
   public AbstractOrb makeCopy() {
     return new EnemyEmptyOrbSlot();
   }
   
   public void playChannelSFX() {}
 }
