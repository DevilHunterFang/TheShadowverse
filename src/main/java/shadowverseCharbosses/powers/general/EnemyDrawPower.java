 package shadowverseCharbosses.powers.general;

 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 
 
 
 public class EnemyDrawPower
   extends AbstractPower
 {
   public static final String POWER_ID = "shadowverse:EnemyDraw";
   public static final String NAME;
   public static final String[] DESCRIPTIONS;
   private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Draw"); static {
     NAME = powerStrings.NAME;
     DESCRIPTIONS = powerStrings.DESCRIPTIONS;
   }
   
   public EnemyDrawPower(AbstractCreature owner, int drawAmount) {
     this.name = NAME;
     this.ID = "shadowverse:EnemyDraw";
     this.owner = owner;
     this.amount = drawAmount;
     updateDescription();
     loadRegion("draw");
     this.priority = 20;
   }
 
   
   public void updateDescription() {
     if (this.amount > 0) {
       if (this.amount == 1) {
         this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
       } else {
         this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[3];
       } 
       
       this.type = PowerType.BUFF;
     } else {
       if (this.amount == -1) {
         this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
       } else {
         this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[4];
       } 
       
       this.type = PowerType.DEBUFF;
     } 
   }
 
 
   
   public void atStartOfTurnPostDraw() {
     flash();
   }
 }

