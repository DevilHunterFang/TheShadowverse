 package shadowverseCharbosses.actions.common;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 
 
 
 
 public class EnemyNotStanceCheckAction
   extends AbstractGameAction
 {
   private AbstractGameAction actionToBuffer;
   
   public EnemyNotStanceCheckAction(AbstractGameAction actionToCheck) {
     this.actionToBuffer = actionToCheck;
   }
 
 
   
   public void update() {
     if (!(AbstractCharBoss.boss.stance instanceof shadowverseCharbosses.stances.EnNeutralStance)) {
       addToBot(this.actionToBuffer);
     }
     
     this.isDone = true;
   }
 }
