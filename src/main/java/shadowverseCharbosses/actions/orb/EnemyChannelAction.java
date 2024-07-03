 package shadowverseCharbosses.actions.orb;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.orbs.AbstractOrb;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyChannelAction
   extends AbstractGameAction {
   private AbstractOrb orbType;
   private boolean autoEvoke;
   
   public EnemyChannelAction(AbstractOrb newOrbType) {
     this(newOrbType, true);
   }
   
   public EnemyChannelAction(AbstractOrb newOrbType, boolean autoEvoke) {
     this.autoEvoke = false;
     this.duration = Settings.ACTION_DUR_FAST;
     this.orbType = newOrbType;
     this.autoEvoke = autoEvoke;
   }
 
   
   public void update() {
     if (AbstractCharBoss.boss != null && 
       this.duration == Settings.ACTION_DUR_FAST) {
       if (this.autoEvoke) {
         AbstractCharBoss.boss.channelOrb(this.orbType);
       } else {
         for (AbstractOrb o : AbstractCharBoss.boss.orbs) {
           if (o instanceof com.megacrit.cardcrawl.orbs.EmptyOrbSlot) {
             AbstractCharBoss.boss.channelOrb(this.orbType);
             break;
           } 
         } 
       } 
       if (Settings.FAST_MODE) {
         this.isDone = true;
         
         return;
       } 
     } 
     tickDuration();
   }
 }
