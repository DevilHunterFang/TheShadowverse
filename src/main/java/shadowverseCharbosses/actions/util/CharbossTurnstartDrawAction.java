 package shadowverseCharbosses.actions.util;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class CharbossTurnstartDrawAction
   extends AbstractGameAction
 {
   public void update() {
     if (AbstractCharBoss.boss != null) AbstractCharBoss.boss.endTurnStartTurn(); 
     this.isDone = true;
   }
 }

