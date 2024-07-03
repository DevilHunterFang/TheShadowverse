 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.Iterator;
 
 
 
 
 
 
 public class EnemyExhaustNonAttacksAction
   extends AbstractGameAction
 {
   public EnemyExhaustNonAttacksAction() {
     setValues((AbstractCreature)AbstractCharBoss.boss, (AbstractCreature)AbstractCharBoss.boss);
     this.actionType = ActionType.CARD_MANIPULATION;
   }
   
   public void update() {
     Iterator<AbstractCard> var2 = AbstractCharBoss.boss.hand.group.iterator();
 
     
     while (var2.hasNext()) {
       AbstractCard c = var2.next();
       if (c.type != AbstractCard.CardType.ATTACK) {
         addToTop((AbstractGameAction)new ExhaustSpecificCardAction(c, AbstractCharBoss.boss.hand));
       }
     } 
     
     this.isDone = true;
   }
 }

