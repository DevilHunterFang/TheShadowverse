 package shadowverseCharbosses.powers.cardpowers;

 import com.badlogic.gdx.graphics.Texture;
 import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.actions.common.HealAction;
 import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.powers.general.EnemyDrawCardNextTurnPower;

 public class MaishaPower2 extends AbstractPower {
     public static final String POWER_ID = "shadowverse:MaishaPower2";
     private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:MaishaPower2");
     public static final String NAME = powerStrings.NAME;
     public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
 
   
   public MaishaPower2(AbstractCreature owner, int amount) {
       this.name = NAME;
       this.ID = POWER_ID;
       this.owner = owner;
       this.amount = amount;
       this.type = NeutralPowertypePatch.NEUTRAL;
       updateDescription();
       this.img = new Texture("img/powers/MaishaPower2.png");
   }
   
   public void stackPower(int stackAmount) {
       this.fontScale = 8.0F;
       this.amount += stackAmount;
       if (this.amount <= 0){
           this.amount = 0;
       }
       updateDescription();
   }

     @Override
     public void atEndOfTurn(boolean isPlayer) {
         if (!isPlayer){
             if (((AbstractCharBoss)this.owner).energyPanel.getCurrentEnergy()>0){
                 addToBot(new ApplyPowerAction(this.owner, this.owner, new EnemyDrawCardNextTurnPower(this.owner, 1), 1));
                 addToBot(new ApplyPowerAction(this.owner,this.owner,new MaishaPower2(this.owner,2),2));
             }else {
                 addToBot(new ApplyPowerAction(this.owner,this.owner,new MaishaPower2(this.owner,1),1));
             }
         }
     }

     public void updateDescription() {
     this.description = DESCRIPTIONS[0];
   }
   
 }
