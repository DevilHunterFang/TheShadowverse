 package shadowverseCharbosses.powers.cardpowers;

 import com.badlogic.gdx.graphics.Texture;
 import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;

 public class SionPower extends AbstractPower {
     public static final String POWER_ID = "shadowverse:SionPower";
     private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:SionPower");
     public static final String NAME = powerStrings.NAME;
     public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


   public SionPower(AbstractCreature owner, int amount) {
       this.name = NAME;
       this.ID = POWER_ID;
       this.owner = owner;
       this.amount = amount;
       this.type = PowerType.BUFF;
       updateDescription();
       this.img = new Texture("img/powers/SionPower.png");
   }
   
   public void stackPower(int stackAmount) {
       this.fontScale = 8.0F;
       this.amount += stackAmount;
       updateDescription();
   }

     public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
         if (damageAmount > this.amount)
             damageAmount = this.amount;
         this.amount -= damageAmount;
         if (this.amount < 0)
             this.amount = 0;
         updateDescription();
         return damageAmount;
     }

     public void atStartOfTurn() {
       addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,this));
     }
   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
   }
   
 }
