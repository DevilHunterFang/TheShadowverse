 package shadowverse.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

 public class EternalDogmaPower
   extends AbstractPower {
   public static final String POWER_ID = "shadowverse:EternalDogmaPower";
   private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:EternalDogmaPower");
   public static final String NAME = powerStrings.NAME;
   public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

   public EternalDogmaPower(AbstractCreature owner, int amount) {
     this.name = NAME;
     this.ID = POWER_ID;
     this.owner = owner;
     this.amount = amount;
     this.type = PowerType.BUFF;
     updateDescription();
     this.loadRegion("focus");
   }

   @Override
   public void atStartOfTurn() {
     super.atStartOfTurn();
     flash();
     addToBot(new DrawCardAction(1));
     addToBot(new HealAction(this.owner,this.owner,2));
     if (this.amount == 0) {
       addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
     } else {
       addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this, 1));
     }
   }
   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
   }
 }


