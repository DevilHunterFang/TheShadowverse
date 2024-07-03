 package shadowverseCharbosses.actions.common;

 import com.badlogic.gdx.math.MathUtils;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.utility.WaitAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.unlock.UnlockTracker;
 import shadowverseCharbosses.actions.vfx.cardmanip.EnemyShowCardAndAddToDiscardEffect;
 import shadowverseCharbosses.actions.vfx.cardmanip.EnemyShowCardAndAddToHandEffect;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.cards.AbstractBossCard;
 
 
 public class EnemyMakeTempCardInHandAction
   extends AbstractGameAction
 {
   private static final float PADDING = 25.0F * Settings.scale;
   
   private AbstractCard c;
   
   private boolean isOtherCardInCenter;
   private boolean sameUUID;
   
   public EnemyMakeTempCardInHandAction(AbstractCard card, boolean isOtherCardInCenter) {
     this.isOtherCardInCenter = true;
     this.sameUUID = false;
     this.amount = 1;
     this.actionType = ActionType.CARD_MANIPULATION;
     this.c = card;
     AbstractBossCard cB = (AbstractBossCard)this.c;
     cB.createIntent();
     if (this.c.type != AbstractCard.CardType.CURSE && this.c.type != AbstractCard.CardType.STATUS && AbstractCharBoss.boss.hasPower("MasterRealityPower")) {
       this.c.upgrade();
     }
     this.isOtherCardInCenter = isOtherCardInCenter;
   }
   
   public EnemyMakeTempCardInHandAction(AbstractCard card) {
     this(card, 1);
   }
   
   public EnemyMakeTempCardInHandAction(AbstractCard card, int amount) {
     this.isOtherCardInCenter = true;
     this.sameUUID = false;
     UnlockTracker.markCardAsSeen(card.cardID);
     this.amount = amount;
     this.actionType = ActionType.CARD_MANIPULATION;
     this.c = card;
     if (this.c.type != AbstractCard.CardType.CURSE && this.c.type != AbstractCard.CardType.STATUS && AbstractCharBoss.boss.hasPower("MasterRealityPower")) {
       this.c.upgrade();
     }
   }
   
   public EnemyMakeTempCardInHandAction(AbstractCard card, int amount, boolean isOtherCardInCenter) {
     this(card, amount);
     this.isOtherCardInCenter = isOtherCardInCenter;
   }
   
   public EnemyMakeTempCardInHandAction(AbstractCard card, boolean isOtherCardInCenter, boolean sameUUID) {
     this(card, 1);
     this.isOtherCardInCenter = isOtherCardInCenter;
     this.sameUUID = sameUUID;
   }
 
   
   public void update() {
     if (AbstractCharBoss.boss == null || AbstractCharBoss.boss.isDead || AbstractCharBoss.boss.isDying) {
       this.isDone = true;
       return;
     } 
     if (this.amount == 0) {
       this.isDone = true;
       return;
     } 
     int discardAmount = 0;
     int handAmount = this.amount;
     if (this.amount + AbstractCharBoss.boss.hand.size() > 10) {
       discardAmount = this.amount + AbstractCharBoss.boss.hand.size() - 10;
       handAmount -= discardAmount;
     } 
     addToHand(handAmount);
     addToDiscard(discardAmount);
     if (this.amount > 0) {
       addToTop((AbstractGameAction)new WaitAction(0.8F));
     }
     AbstractCharBoss.boss.preApplyIntentCalculations();
     
     this.isDone = true;
   }
   
   private void addToHand(int handAmt) {
     switch (this.amount) {
       case 0:
         return;
       
       case 1:
         if (handAmt == 1)
         {
           
           if (this.isOtherCardInCenter) {
             AbstractDungeon.effectList.add(new EnemyShowCardAndAddToHandEffect(makeNewCard(), Settings.WIDTH / 2.0F - PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
           } else {
             AbstractDungeon.effectList.add(new EnemyShowCardAndAddToHandEffect(makeNewCard()));
           } 
         }
         break;
       case 2:
         if (handAmt == 1) {
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToHandEffect(makeNewCard(), Settings.WIDTH / 2.0F - PADDING + AbstractCard.IMG_WIDTH * 0.5F, Settings.HEIGHT / 2.0F));
         
         }
         else if (handAmt == 2) {
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToHandEffect(makeNewCard(), Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToHandEffect(makeNewCard(), Settings.WIDTH / 2.0F - PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
         }


         break;

       case 3:
         if (handAmt == 1) {
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToHandEffect(makeNewCard(), Settings.WIDTH / 2.0F - PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
         
         }
         else if (handAmt == 2) {
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToHandEffect(makeNewCard(), Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToHandEffect(makeNewCard(), Settings.WIDTH / 2.0F - PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
         
         }
         else if (handAmt == 3) {
           for (int j = 0; j < this.amount; j++) {
             AbstractDungeon.effectList.add(new EnemyShowCardAndAddToHandEffect(makeNewCard()));
           }
         }
         break;

     } 

   }
 
 
 
   
   private void addToDiscard(int discardAmt) {
     switch (this.amount) {
       case 0:
         return;
       
       case 1:
         if (discardAmt == 1) {
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
         }
 
 
       
       case 2:
         if (discardAmt == 1) {
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), Settings.WIDTH * 0.5F - PADDING + AbstractCard.IMG_WIDTH * 0.5F, Settings.HEIGHT * 0.5F));
         
         }
         else if (discardAmt == 2) {
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), Settings.WIDTH * 0.5F - PADDING + AbstractCard.IMG_WIDTH * 0.5F, Settings.HEIGHT * 0.5F));
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH * 0.5F, Settings.HEIGHT * 0.5F));
         } 
 
 
       
       case 3:
         if (discardAmt == 1) {
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT * 0.5F));
         
         }
         else if (discardAmt == 2) {
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F));
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT * 0.5F));
         
         }
         else if (discardAmt == 3) {
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F));
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), Settings.WIDTH * 0.5F - PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT * 0.5F));
           AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT * 0.5F));
         } 
     } 
 
 
     
     for (int i = 0; i < discardAmt; i++) {
       AbstractDungeon.effectList.add(new EnemyShowCardAndAddToDiscardEffect(makeNewCard(), MathUtils.random(Settings.WIDTH * 0.2F, Settings.WIDTH * 0.8F), MathUtils.random(Settings.HEIGHT * 0.3F, Settings.HEIGHT * 0.7F)));
     }
   }
 
 
 
   
   private AbstractCard makeNewCard() {
     if (this.sameUUID) {
       return this.c.makeSameInstanceOf();
     }
     return this.c.makeStatEquivalentCopy();
   }
 }

