 package shadowverseCharbosses.actions.common;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.Settings;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyGainEnergyAction extends AbstractGameAction {
   private int energyGain;
   
   public EnemyGainEnergyAction(int amount) {
     this(AbstractCharBoss.boss, amount);
   }
   private AbstractCharBoss boss;
   public EnemyGainEnergyAction(AbstractCharBoss target, int amount) {
     setValues((AbstractCreature)target, (AbstractCreature)target, 0);
     this.duration = Settings.ACTION_DUR_FAST;
     this.energyGain = amount;
     this.boss = target;
   }
 
   
   public void update() {
     if (this.duration == Settings.ACTION_DUR_FAST) {
       if (AbstractCharBoss.boss != null) {
         this.boss = AbstractCharBoss.boss;
         
         this.boss.gainEnergy(this.energyGain);
         for (AbstractCard c : this.boss.hand.group) {
           c.triggerOnGainEnergy(this.energyGain, true);
         }
       } else {
         
         this.isDone = true;
         return;
       } 
     }
     tickDuration();
   }
 }