 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.powers.StrengthPower;
 import com.megacrit.cardcrawl.powers.WeakPower;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.ui.EnemyEnergyPanel;
 
 public class EnemyMalaiseAction
   extends AbstractGameAction
 {
   private boolean freeToPlayOnce = false;
   private boolean upgraded = false;
   private AbstractCharBoss c;
   private int energyOnUse = -1;
   
   public EnemyMalaiseAction(AbstractCharBoss t, boolean upgraded, boolean freeToPlayOnce, int energyOnUse) {
     this.c = t;
     this.freeToPlayOnce = freeToPlayOnce;
     this.upgraded = upgraded;
     this.duration = Settings.ACTION_DUR_XFAST;
     this.actionType = ActionType.SPECIAL;
     this.energyOnUse = energyOnUse;
   }
   
   public void update() {
     int effect = EnemyEnergyPanel.totalCount;
     if (this.energyOnUse != -1) {
       effect = this.energyOnUse;
     }
     
     if (this.upgraded) {
       effect++;
     }
     
     if (effect > 0) {
       addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)this.c, (AbstractPower)new StrengthPower((AbstractCreature)AbstractDungeon.player, -effect), -effect));
       addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)AbstractDungeon.player, (AbstractCreature)this.c, (AbstractPower)new WeakPower((AbstractCreature)AbstractDungeon.player, effect, false), effect));
       if (!this.freeToPlayOnce) {
         this.c.energy.use(EnemyEnergyPanel.totalCount);
       }
     } 
     
     this.isDone = true;
   }
 }
