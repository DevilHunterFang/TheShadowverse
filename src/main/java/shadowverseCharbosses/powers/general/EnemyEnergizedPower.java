 package shadowverseCharbosses.powers.general;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import shadowverseCharbosses.actions.common.EnemyGainEnergyAction;
 
 
 
 public class EnemyEnergizedPower
   extends AbstractPower
 {
   public static final String POWER_ID = "shadowverse:EnemyEnergized";
   public static final String NAME;
   public static final String[] DESCRIPTIONS;
   private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Energized"); static {
     NAME = powerStrings.NAME;
     DESCRIPTIONS = powerStrings.DESCRIPTIONS;
   }
   
   public EnemyEnergizedPower(AbstractCreature owner, int energyAmt) {
     this.name = NAME;
     this.ID = "shadowverse:EnemyEnergized";
     this.owner = owner;
     this.amount = energyAmt;
     if (this.amount >= 999) {
       this.amount = 999;
     }
     updateDescription();
     loadRegion("energized_green");
   }
 
   
   public void updateDescription() {
     if (this.amount == 1) {
       this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
     } else {
       this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
     } 
   }
 
   
   public void onEnergyRecharge() {
     flash();
     AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new EnemyGainEnergyAction(this.amount));
     addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
   }
 }

