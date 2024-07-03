 package shadowverseCharbosses.core;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.EnergyManager;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.ui.EnemyEnergyPanel;
 
 public class EnemyEnergyManager
   extends EnergyManager {
   public EnemyEnergyManager(int e) {
     super(e);
   }
   
   public void prep() {
     this.energy = this.energyMaster;
     EnemyEnergyPanel.totalCount = 0;
   }
   
   public void recharge() {
     if (AbstractCharBoss.boss != null) {
         if (AbstractCharBoss.boss.hasPower("Conserve")) {
         if (EnemyEnergyPanel.totalCount > 0) {
           AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ReducePowerAction((AbstractCreature)AbstractCharBoss.boss, (AbstractCreature)AbstractCharBoss.boss, "Conserve", 1));
         }
         EnemyEnergyPanel.addEnergy(this.energy);
       } else {
         EnemyEnergyPanel.setEnergy(this.energy);
       } 
     } 
   }
   
   public void use(int e) {
     EnemyEnergyPanel.useEnergy(e);
   }
 }

