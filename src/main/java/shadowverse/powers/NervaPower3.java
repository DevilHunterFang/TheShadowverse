 package shadowverse.powers;

 import com.badlogic.gdx.graphics.Texture;
 import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.actions.common.HealAction;
 import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
 import com.megacrit.cardcrawl.actions.utility.UseCardAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.helpers.CardLibrary;
 import com.megacrit.cardcrawl.localization.PowerStrings;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.powers.StrengthPower;

 import java.util.ArrayList;
 import java.util.List;

 public class NervaPower3 extends AbstractPower {
     public static final String POWER_ID = "shadowverse:NervaPower3";
     private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:NervaPower3");
     public static final String NAME = powerStrings.NAME;
     public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
     public List<AbstractCard> boxed = new ArrayList<>();
   public NervaPower3(AbstractCreature owner) {
       this.name = NAME;
       this.ID = POWER_ID;
       this.owner = owner;
       this.amount = 4;
       this.type = NeutralPowertypePatch.NEUTRAL;
       updateDescription();
       this.img = new Texture("img/powers/MaishaPower.png");
   }

     public void playApplyPowerSfx() {
         CardCrawlGame.sound.play("NervaPower3", 0.05F);
     }

     public void atStartOfTurn() {
       if (this.boxed.size() > 0){
           this.amount -= 2;
           this.boxed.clear();
       }else {
           this.amount--;
       }
       if (this.amount <= 0){
           addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,this));
           addToBot(new ApplyPowerAction(this.owner,this.owner,new NervaPower4(this.owner)));
       }
       updateDescription();
     }
   
   public void updateDescription() {
     this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
   }

     @Override
     public void onUseCard(AbstractCard card, UseCardAction action) {
         if (card.type == AbstractCard.CardType.ATTACK || CardLibrary.getCard(card.cardID).type == AbstractCard.CardType.ATTACK){
             card.setLocked();
             boxed.add(card);
         }
     }

     @Override
     public void atEndOfTurn(boolean isPlayer) {
         if (!isPlayer){
             for (AbstractCard c : boxed){
                 c.isLocked = false;
             }
         }
     }
 }
