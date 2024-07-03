 package shadowverseCharbosses.actions.utility;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 
 
 
 public class EnemyExhaustAllEtherealAction
   extends AbstractGameAction
 {
   public void update() {
     for (AbstractCard c : AbstractCharBoss.boss.hand.group) {
       if (c.isEthereal) {
         addToTop((AbstractGameAction)new ExhaustSpecificCardAction(c, AbstractCharBoss.boss.hand));
       }
     } 
     this.isDone = true;
   }
 }
