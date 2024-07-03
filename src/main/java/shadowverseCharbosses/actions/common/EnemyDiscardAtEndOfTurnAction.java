 package shadowverseCharbosses.actions.common;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.Settings;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.Iterator;
 
 
 public class EnemyDiscardAtEndOfTurnAction
   extends AbstractGameAction
 {
   private static final float DURATION = Settings.ACTION_DUR_XFAST;
   
   private AbstractCharBoss boss;
 
   
   public EnemyDiscardAtEndOfTurnAction(AbstractCharBoss boss) {
     this.duration = DURATION;
     this.boss = boss;
   }
   
   public EnemyDiscardAtEndOfTurnAction() {
     this(AbstractCharBoss.boss);
   }
 
   
   public void update() {
       if (this.duration == DURATION) {
           Iterator<AbstractCard> c = this.boss.hand.group.iterator();
           while (c.hasNext()) {
               AbstractCard e = c.next();
               if (e.retain || e.selfRetain) {
                   this.boss.limbo.addToTop(e);
                   c.remove();
               }
           }
           addToTop(new EnemyRestoreRetainedCardsAction(this.boss, this.boss.limbo));
               for (int tempSize = this.boss.hand.size(), i = 0; i < tempSize; i++)
                   addToTop(new EnemyDiscardAction((AbstractCreature)this.boss, null, this.boss.hand.size(), true));
           ArrayList<AbstractCard> cards = (ArrayList<AbstractCard>)this.boss.hand.group.clone();
           Collections.shuffle(cards);
           for (AbstractCard c2 : cards)
               c2.triggerOnEndOfPlayerTurn();
           this.isDone = true;
       }
   }
 }
