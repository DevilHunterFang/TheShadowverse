 package shadowverseCharbosses.powers.bossmechanicpowers;

 import basemod.ReflectionHacks;
 import com.badlogic.gdx.Gdx;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
 import com.megacrit.cardcrawl.vfx.combat.GainPowerEffect;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.ArrayList;

 
 public abstract class AbstractBossMechanicPower
   extends AbstractPower
 {
   private float timer;
   private boolean firstTurn = true;
   
   public void update(int slot) {
     super.update(slot);
     if (this.firstTurn) {
       if (this.timer <= 0.0F) {
         ArrayList<AbstractGameEffect> effect2 = (ArrayList<AbstractGameEffect>)ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");
         effect2.add(new GainPowerEffect(this));
         this.timer = 1.0F;
         if (AbstractCharBoss.boss != null && 
           AbstractCharBoss.boss.hb.hovered) {
           this.firstTurn = false;
         }

       } else {
         this.timer -= Gdx.graphics.getDeltaTime();
       }
     }
   }
 
   
   public void atStartOfTurn() {
     super.atStartOfTurn();
     this.firstTurn = false;
   }
   
   public void playApplyPowerSfx() {}
 }

