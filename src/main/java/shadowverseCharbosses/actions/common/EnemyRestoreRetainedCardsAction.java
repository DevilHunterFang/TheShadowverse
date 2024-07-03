 package shadowverseCharbosses.actions.common;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.cards.CardGroup;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.Iterator;
 
 public class EnemyRestoreRetainedCardsAction extends AbstractGameAction {
   private CardGroup group;
   private AbstractCharBoss boss;
   
   public EnemyRestoreRetainedCardsAction(AbstractCharBoss boss, CardGroup group) {
     setValues((AbstractCreature)boss, this.source, -1);
     this.group = group;
     this.boss = boss;
   }
 
   
   public void update() {
     this.isDone = true;
     Iterator<AbstractCard> c = this.group.group.iterator();
     while (c.hasNext()) {
       AbstractCard e = c.next();
       if (e.retain || e.selfRetain) {
         e.onRetained();
         this.boss.hand.addToTop(e);
         e.retain = false;
         c.remove();
       } 
     } 
     if (this.boss != null)
       this.boss.hand.refreshHandLayout(); 
   }
 }
