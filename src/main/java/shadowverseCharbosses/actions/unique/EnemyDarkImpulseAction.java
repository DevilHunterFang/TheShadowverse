 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.orbs.AbstractOrb;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.Iterator;
 
 
 
 
 
 
 
 
 public class EnemyDarkImpulseAction
   extends AbstractGameAction
 {
   public void update() {
     if (this.duration == Settings.ACTION_DUR_FAST && !AbstractCharBoss.boss.orbs.isEmpty()) {
       Iterator<AbstractOrb> var1 = AbstractCharBoss.boss.orbs.iterator();
       
       while (var1.hasNext()) {
         AbstractOrb o = var1.next();
         if (o instanceof shadowverseCharbosses.orbs.EnemyDark) {
           o.onStartOfTurn();
           o.onEndOfTurn();
         } 
       } 

     } 
     
     tickDuration();
   }
 }


