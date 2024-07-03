 package shadowverseCharbosses.actions.orb;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.core.Settings;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 
 public class EnemyIncreaseMaxOrbAction
   extends AbstractGameAction
 {
   public EnemyIncreaseMaxOrbAction(int slotIncrease) {
     this.duration = Settings.ACTION_DUR_FAST;
     this.amount = slotIncrease;
     this.actionType = ActionType.BLOCK;
   }
   
   public void update() {
     if (this.duration == Settings.ACTION_DUR_FAST) {
       for (int i = 0; i < this.amount; i++) {
         AbstractCharBoss.boss.increaseMaxOrbSlots(1, true);
       }
     }
     
     tickDuration();
   }
 }
