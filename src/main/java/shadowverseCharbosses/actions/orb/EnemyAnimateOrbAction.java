 package shadowverseCharbosses.actions.orb;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyAnimateOrbAction extends AbstractGameAction {
   private int orbCount;
   
   public EnemyAnimateOrbAction(int amount) {
     this.orbCount = amount;
   }
 
   
   public void update() {
     for (int i = 0; i < this.orbCount; i++) {
       AbstractCharBoss.boss.triggerEvokeAnimation(i);
     }
     this.isDone = true;
   }
 }
