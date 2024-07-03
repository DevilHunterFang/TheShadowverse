 package shadowverseCharbosses.actions.util;
 
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 
 public class DelayedActionAction extends AbstractGameAction {
   AbstractGameAction act;
   
   public DelayedActionAction(AbstractGameAction a) {
     this.act = a;
   }
 
   
   public void update() {
     addToBot(this.act);
     this.isDone = true;
   }
 }
