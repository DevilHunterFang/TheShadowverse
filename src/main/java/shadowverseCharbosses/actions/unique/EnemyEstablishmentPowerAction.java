 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.Iterator;
 
 public class EnemyEstablishmentPowerAction
   extends AbstractGameAction {
   private int discountAmount;
   
   public EnemyEstablishmentPowerAction(int discountAmount) {
     this.discountAmount = discountAmount;
   }
   
   public void update() {
     Iterator<AbstractCard> var1 = AbstractCharBoss.boss.hand.group.iterator();
 
 
     
     while (true) {
       if (!var1.hasNext()) {
         this.isDone = true;
         
         return;
       } 
       AbstractCard c = var1.next();
       if (c.selfRetain || c.retain)
       {
         c.modifyCostForCombat(-this.discountAmount);
       }
     } 
   }
 }

