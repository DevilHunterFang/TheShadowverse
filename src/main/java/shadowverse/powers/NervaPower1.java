 package shadowverse.powers;

 import com.badlogic.gdx.graphics.Texture;
 import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
 import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.powers.DrawReductionPower;

 public class NervaPower1 extends TwoAmountPower {
     public static final String POWER_ID = "shadowverse:NervaPower1";
     private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:NervaPower1");
     public static final String NAME = powerStrings.NAME;
     public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
     private int max;


   public NervaPower1(AbstractCreature owner, int amount2) {
       this.name = NAME;
       this.ID = POWER_ID;
       this.owner = owner;
       this.amount = 4;
       this.amount2 = amount2;
       this.max = amount2;
       this.type = NeutralPowertypePatch.NEUTRAL;
       updateDescription();
       this.img = new Texture("img/powers/BelphometPower.png");
   }

     public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
         if (damageAmount > this.amount2)
             damageAmount = this.amount2;
         this.amount2 -= damageAmount;
         if (this.amount2 < 0)
             this.amount2 = 0;
         updateDescription();
         return damageAmount;
     }

     public void playApplyPowerSfx() {
         CardCrawlGame.sound.play("NervaPower1", 0.05F);
     }

     public void atStartOfTurn() {
       if (this.amount2 == 0){
           this.amount -= 2;
       }else {
           this.amount--;
       }
       if (this.amount <= 0){
           addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,this));
           addToBot(new ApplyPowerAction(this.owner,this.owner,new NervaPower2(this.owner)));
       }
       this.amount2 = max;
       updateDescription();
     }
   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2];
   }

     @Override
     public void atEndOfTurn(boolean isPlayer) {
         if (!isPlayer){
             addToBot(new ApplyPowerAction(AbstractDungeon.player,this.owner,new DrawReductionPower(AbstractDungeon.player,1)));
         }
     }
 }
