 package shadowverse.powers;

 import com.badlogic.gdx.graphics.Texture;
 import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.actions.common.HealAction;
 import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.powers.StrengthPower;

 public class NervaPower2 extends AbstractPower {
     public static final String POWER_ID = "shadowverse:NervaPower2";
     private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:NervaPower2");
     public static final String NAME = powerStrings.NAME;
     public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

   public NervaPower2(AbstractCreature owner) {
       this.name = NAME;
       this.ID = POWER_ID;
       this.owner = owner;
       this.amount = 4;
       this.type = NeutralPowertypePatch.NEUTRAL;
       updateDescription();
       this.img = new Texture("img/powers/NexusPower.png");
   }

     public void playApplyPowerSfx() {
         CardCrawlGame.sound.play("NervaPower2", 0.05F);
     }

     public void atStartOfTurn() {
       if (this.owner.currentHealth <= this.owner.maxHealth){
           this.amount -= 2;
           addToBot(new HealAction(this.owner,this.owner,this.owner.maxHealth - this.owner.currentHealth));
       }else {
           this.amount--;
       }
       if (this.amount <= 0){
           addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,this));
           addToBot(new ApplyPowerAction(this.owner,this.owner,new NervaPower3(this.owner)));
       }
       updateDescription();
     }
   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
   }

     @Override
     public void atEndOfTurn(boolean isPlayer) {
         if (!isPlayer){
             addToBot(new ApplyPowerAction(this.owner,this.owner,new StrengthPower(this.owner, (int) (AbstractDungeon.actionManager.turn*0.5)),(int)(AbstractDungeon.actionManager.turn*0.5)));
         }
     }
 }
