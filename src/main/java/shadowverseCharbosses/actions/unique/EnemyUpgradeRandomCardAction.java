 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.cards.CardGroup;
 import com.megacrit.cardcrawl.core.Settings;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 
 
 public class EnemyUpgradeRandomCardAction
   extends AbstractGameAction
 {
   private AbstractCharBoss p = AbstractCharBoss.boss;
 
 
 
   
   public void update() {
     if (this.duration != Settings.ACTION_DUR_FAST) {
       tickDuration();
       return;
     } 
     if (this.p.hand.group.size() <= 0) {
       this.isDone = true;
       return;
     } 
     CardGroup upgradeable = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
     for (AbstractCard c : this.p.hand.group) {
       if (c.canUpgrade() && c.type != AbstractCard.CardType.STATUS) {
         upgradeable.addToTop(c);
       }
     } 
     if (upgradeable.size() > 0) {
       upgradeable.shuffle();
       ((AbstractCard)upgradeable.group.get(0)).upgrade();
       ((AbstractCard)upgradeable.group.get(0)).superFlash();
       ((AbstractCard)upgradeable.group.get(0)).applyPowers();
     } 
     this.isDone = true;
   }
 }

