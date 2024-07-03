 package shadowverse.powers;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
 import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.helpers.CardLibrary;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;


 public class AncientAmplifierPower
   extends AbstractPower
 {
   public static final String POWER_ID = "shadowverse:AncientAmplifierPower";
   private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:AncientAmplifierPower");
   public static final String NAME = powerStrings.NAME;
   public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

   public AncientAmplifierPower(AbstractCreature owner, int amount) {
     this.name = NAME;
     this.ID = POWER_ID;
     this.owner = owner;
     this.amount = amount;
     this.type = PowerType.DEBUFF;
     updateDescription();
       loadRegion("artifact");
   }
   
   public void stackPower(int stackAmount) {
     this.amount += stackAmount;
     if (this.amount >= 999)
       this.amount = 999; 
   }
   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
   }
 
   
   public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
     if (isPlayer) {
       flash();
         for (int i=0;i<this.amount;i++){
             int roll = AbstractDungeon.cardRandomRng.random(99);
             AbstractCard.CardRarity cardRarity;
             if (roll < 60) {
                 cardRarity = AbstractCard.CardRarity.COMMON;
             } else if (roll < 80) {
                 cardRarity = AbstractCard.CardRarity.UNCOMMON;
             } else {
                 cardRarity = AbstractCard.CardRarity.RARE;
             }
             AbstractCard tmp = CardLibrary.getAnyColorCard(cardRarity);
             addToBot((AbstractGameAction)new MakeTempCardInDrawPileAction(tmp, 1, true, true));
         }
     } 
   }

   public void atStartOfTurnPostDraw() {
     this.amount -= 1;
     if (this.amount == 0){
       flash();
       addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner,this.owner,this));
     }
     updateDescription();
   }
 }


