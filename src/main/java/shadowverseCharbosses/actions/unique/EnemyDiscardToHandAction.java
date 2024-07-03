 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.Settings;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyDiscardToHandAction
   extends AbstractGameAction
 {
   private AbstractCard card;
   private AbstractCharBoss p;
   
   public EnemyDiscardToHandAction(AbstractCharBoss p, AbstractCard card) {
     this.p = p;
     this.actionType = ActionType.CARD_MANIPULATION;
     this.card = card;
     this.duration = Settings.ACTION_DUR_FAST;
   }
   
   public void update() {
     if (this.duration == Settings.ACTION_DUR_FAST) {
       if (this.p.hand.size() < 10) {
         this.p.hand.addToHand(this.card);
         this.card.unhover();
         this.card.setAngle(0.0F, true);
         this.card.lighten(false);
         this.card.drawScale = 0.12F;
         this.card.targetDrawScale = 0.75F;
         this.card.applyPowers();
       } 
 
       
       this.p.hand.refreshHandLayout();
       this.p.hand.glowCheck();
     } 
     
     tickDuration();
     this.isDone = true;
   }
 }
