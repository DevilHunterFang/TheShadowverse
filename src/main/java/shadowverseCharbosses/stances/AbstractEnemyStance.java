 package shadowverseCharbosses.stances;

 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.graphics.Texture;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.helpers.PowerTip;
 import com.megacrit.cardcrawl.stances.AbstractStance;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.ArrayList;
 
 
 public abstract class AbstractEnemyStance
   extends AbstractStance
 {
   private static final Logger logger = LogManager.getLogger(AbstractStance.class.getName());
   public String name;
   public String description;
   public String ID;
   protected ArrayList<PowerTip> tips = new ArrayList<>();
   protected Color c;
   protected static final int W = 512;
   protected Texture img;
   protected float angle;
   protected float particleTimer;
   protected float particleTimer2;
   
   public AbstractEnemyStance() {
     this.c = Color.WHITE.cpy();
     this.img = null;
     this.particleTimer = 0.0F;
     this.particleTimer2 = 0.0F;
   }
 
   
   public abstract void updateDescription();
 
   
   public void atStartOfTurn() {}
 
   
   public void onEndOfTurn() {}
 
   
   public void onEnterStance() {}
   
   public void onExitStance() {}
   
   public float atDamageGive(float damage, DamageInfo.DamageType type) {
     return damage;
   }
   
   public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
     return atDamageGive(damage, type);
   }
   
   public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
     return damage;
   }
 
   
   public void onPlayCard(AbstractCard card) {}
   
   public void update() {
     updateAnimation();
   }
 
   
   public void updateAnimation() {}
   
   public void render(SpriteBatch sb) {
     if (this.img != null) {
       sb.setColor(this.c);
       sb.setBlendFunction(770, 1);
       sb.draw(this.img, AbstractCharBoss.boss.drawX - 256.0F + AbstractCharBoss.boss.animX, AbstractCharBoss.boss.drawY - 256.0F + AbstractCharBoss.boss.animY + AbstractCharBoss.boss.hb_h / 2.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, -this.angle, 0, 0, 512, 512, false, false);
       sb.setBlendFunction(770, 771);
     } 
   }
 
   
   public void stopIdleSfx() {}
   
   public static AbstractEnemyStance getStanceFromName(String name) {
     if (name.equals("Calm"))
       return new EnCalmStance(); 
     if (name.equals("Wrath"))
       return new EnWrathStance(); 
     if (name.equals("Divinity"))
       return new EnDivinityStance(); 
     if (name.equals("Neutral")) {
       return new EnNeutralStance();
     }
     logger.info("[ERROR] Unknown stance: " + name + " called for in getStanceFromName()");
     return null;
   }
 }
