 package shadowverseCharbosses.actions.orb;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.core.Settings;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyEvokeOrbAction extends AbstractGameAction {
   private int orbCount;
   
   public EnemyEvokeOrbAction(int amount) {
     if (Settings.FAST_MODE) {
       this.duration = Settings.ACTION_DUR_XFAST;
     } else {
       this.duration = Settings.ACTION_DUR_FAST;
     } 
     this.duration = this.startDuration;
     this.orbCount = amount;
     this.actionType = ActionType.DAMAGE;
   }
 
   
   public void update() {
     if (this.duration == this.startDuration) {
       for (int i = 0; i < this.orbCount; i++) {
         AbstractCharBoss.boss.evokeOrb();
       }
     }
     tickDuration();
   }
 }
