 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.stances.AbstractStance;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.stances.AbstractEnemyStance;

 import java.util.Iterator;
 
 public class EnemyChangeStanceAction
   extends AbstractGameAction {
   private String id;
   private AbstractEnemyStance newStance;
   
   public EnemyChangeStanceAction(String stanceId) {
     this.newStance = null;
     this.duration = Settings.ACTION_DUR_FAST;
     this.id = stanceId;
   }
   
   public EnemyChangeStanceAction(AbstractEnemyStance newStance) {
     this(newStance.ID);
     this.newStance = newStance;
   }
   
   public void update() {
     if (this.duration == Settings.ACTION_DUR_FAST && 
       AbstractCharBoss.boss != null) {
       if (AbstractCharBoss.boss.hasPower("CannotChangeStancePower")) {
         this.isDone = true;
         
         return;
       } 
       
       AbstractEnemyStance oldStance = (AbstractEnemyStance)AbstractCharBoss.boss.stance;
       if (!oldStance.ID.equals(this.id)) {
         if (this.newStance == null) {
           this.newStance = AbstractEnemyStance.getStanceFromName(this.id);
         }
         
         Iterator<AbstractPower> var2 = AbstractCharBoss.boss.powers.iterator();
         
         while (var2.hasNext()) {
           AbstractPower p = var2.next();
           p.onChangeStance((AbstractStance)oldStance, (AbstractStance)this.newStance);
         }
         
         oldStance.onExitStance();
         AbstractCharBoss.boss.stance = (AbstractStance)this.newStance;
         this.newStance.onEnterStance();

         AbstractCharBoss.boss.switchedStance();
         AbstractCharBoss.boss.onStanceChange(this.id);
       } 
       
       AbstractDungeon.onModifyPower();
       if (Settings.FAST_MODE) {
         this.isDone = true;
         return;
       } 
     } 
     
     tickDuration();
   }
 }

