 package shadowverse.action;

 import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.screens.CardRewardScreen;
 import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
 import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

 import java.util.ArrayList;

 public class ChoiceAction
   extends DiscoveryAction
 {
   private ArrayList<AbstractCard> card = new ArrayList<>();
   private boolean retrieveCard = false;
   
   public ChoiceAction(AbstractCard... card) {
     this.actionType = ActionType.CARD_MANIPULATION;
     this.duration = Settings.ACTION_DUR_FAST;
     this.amount = 1;
     for (AbstractCard c : card) {
       this.card.add(c);
     }
   }
 
   
   public void update() {
     ArrayList<AbstractCard> generatedCards = null;
     if (this.card.size() != 0) {
       generatedCards = this.card;
     }
     if (this.duration == Settings.ACTION_DUR_FAST) {
       AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, CardRewardScreen.TEXT[1], true);
       tickDuration();
       return;
     } 
     if (!this.retrieveCard) {
       if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
         AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
         AbstractCard disCard2 = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
         if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
           disCard.upgrade();
           disCard2.upgrade();
         } 
         disCard.setCostForTurn(0);
         disCard2.setCostForTurn(0);
         disCard.current_x = -1000.0F * Settings.xScale;
         disCard2.current_x = -1000.0F * Settings.xScale + AbstractCard.IMG_HEIGHT_S;
         if (this.amount == 1) {
           if (AbstractDungeon.player.hand.size() < 10) {
             AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
           } else {
             AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
           } 
           disCard2 = null;
         } else if (AbstractDungeon.player.hand.size() + this.amount <= 10) {
           AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
           AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard2, Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
         } else if (AbstractDungeon.player.hand.size() == 9) {
           AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
           AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard2, Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
         } else {
           AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
           AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard2, Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
         } 
         AbstractDungeon.cardRewardScreen.discoveryCard = null;
       } 
       this.retrieveCard = true;
     } 
     tickDuration();
   }
 }


