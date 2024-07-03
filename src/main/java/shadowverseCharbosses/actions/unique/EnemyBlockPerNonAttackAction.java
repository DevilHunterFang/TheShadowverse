 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
 import com.megacrit.cardcrawl.actions.common.GainBlockAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.ArrayList;
 import java.util.Iterator;
 
 
 
 
 
 
 public class EnemyBlockPerNonAttackAction
   extends AbstractGameAction
 {
   private int blockPerCard;
   
   public EnemyBlockPerNonAttackAction(int blockAmount) {
     this.blockPerCard = blockAmount;
     setValues((AbstractCreature)AbstractCharBoss.boss, (AbstractCreature)AbstractCharBoss.boss);
     this.actionType = ActionType.BLOCK;
   }
   
   public void update() {
     ArrayList<AbstractCard> cardsToExhaust = new ArrayList<>();
     Iterator<AbstractCard> var2 = AbstractCharBoss.boss.hand.group.iterator();
 
     
     while (var2.hasNext()) {
       AbstractCard c = var2.next();
       if (c.type != AbstractCard.CardType.ATTACK) {
         cardsToExhaust.add(c);
       }
     } 
     
     var2 = cardsToExhaust.iterator();
     
     while (var2.hasNext()) {
       AbstractCard c = var2.next();
       addToTop((AbstractGameAction)new GainBlockAction((AbstractCreature)AbstractCharBoss.boss, (AbstractCreature)AbstractCharBoss.boss, this.blockPerCard));
     } 
     
     var2 = cardsToExhaust.iterator();
     
     while (var2.hasNext()) {
       AbstractCard c = var2.next();
       addToTop((AbstractGameAction)new ExhaustSpecificCardAction(c, AbstractCharBoss.boss.hand));
     } 
     
     this.isDone = true;
   }
 }
