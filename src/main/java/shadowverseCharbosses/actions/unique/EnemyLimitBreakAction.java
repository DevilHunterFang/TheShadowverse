 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.powers.StrengthPower;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyLimitBreakAction
   extends AbstractGameAction
 {
   private AbstractCharBoss p = AbstractCharBoss.boss;
 
 
 
   
   public void update() {
     if (this.duration == Settings.ACTION_DUR_XFAST && this.p.hasPower("Strength")) {
       int strAmt = (this.p.getPower("Strength")).amount;
       addToTop((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this.p, (AbstractCreature)this.p, (AbstractPower)new StrengthPower((AbstractCreature)this.p, strAmt), strAmt));
     } 
     tickDuration();
   }
 }
