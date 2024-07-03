 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.cards.CardGroup;
 import com.megacrit.cardcrawl.core.Settings;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.Iterator;
 
 
 
 
 
 
 
 
 public class EnemyApotheosisAction
   extends AbstractGameAction
 {
   public void update() {
     if (this.duration == Settings.ACTION_DUR_MED) {
       AbstractCharBoss p = AbstractCharBoss.boss;
       upgradeAllCardsInGroup(p.hand);
 
 
 
 
       
       this.isDone = true;
     } 
   }
 
   
   private void upgradeAllCardsInGroup(CardGroup cardGroup) {
     Iterator<AbstractCard> var2 = cardGroup.group.iterator();
     
     while (var2.hasNext()) {
       AbstractCard c = var2.next();
       if (c.canUpgrade()) {
         if (cardGroup.type == CardGroup.CardGroupType.HAND) {
           c.superFlash();
         }
         
         c.upgrade();
         c.applyPowers();
       } 
     } 
   }
 }

