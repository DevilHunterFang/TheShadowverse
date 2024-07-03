 package shadowverseCharbosses.powers.cardpowers;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
 import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import shadowverseCharbosses.actions.common.EnemyUseCardAction;

 
 public class EnemyReboundPower
   extends AbstractPower
 {
   private boolean justEvoked = true;
   
   public EnemyReboundPower(AbstractCreature owner) {
     this.name = NAME;
     this.ID = "Rebound";
     this.owner = owner;
     this.amount = 1;
     updateDescription();
     loadRegion("rebound");
     this.isTurnBased = true;
     this.type = PowerType.BUFF;
   }
   
   public void updateDescription() {
     if (this.amount > 1) {
       this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
     } else {
       this.description = DESCRIPTIONS[0];
     } 
   }
 
 
   
   public void onAfterUse(AbstractCard card, EnemyUseCardAction action) {
     if (this.justEvoked) {
       this.justEvoked = false;
     } else {
       if (card.type != AbstractCard.CardType.POWER) {
         flash();
         
         action.reboundCard = true;
       } 
       
       addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, "Rebound", 1));
     } 
   }
   
   public void atEndOfTurn(boolean isPlayer) {
     addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, "Rebound"));
   }
 
 
   
   private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Rebound");
   public static final String NAME = powerStrings.NAME;
   public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
   public static final String POWER_ID = "Rebound";
 }
