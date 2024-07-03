 package shadowverseCharbosses.actions.utility;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 
 public class EnemyHandCheckAction
   extends AbstractGameAction
 {
   private AbstractCharBoss player = AbstractCharBoss.boss;
 
 
   
   public void update() {
     this.player.hand.applyPowers();
     this.player.hand.glowCheck();
     this.isDone = true;
   }
 }
