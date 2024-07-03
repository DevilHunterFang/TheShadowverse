 package shadowverseCharbosses.actions.common;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.utility.UseCardAction;
 import com.megacrit.cardcrawl.actions.utility.WaitAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import shadowverseCharbosses.actions.utility.EnemyHandCheckAction;
 import shadowverseCharbosses.actions.utility.EnemyShowCardAction;
 import shadowverseCharbosses.actions.utility.EnemyShowCardAndPoofAction;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 import shadowverseCharbosses.powers.cardpowers.EnemyReboundPower;

 public class EnemyUseCardAction
   extends AbstractGameAction
 {
   private static final float DUR = 0.15F;
   public AbstractCreature target;
   public boolean exhaustCard;
   public boolean returnToHand;
   public boolean reboundCard;
   private AbstractCard targetCard;
   
   public EnemyUseCardAction(AbstractCard card, AbstractCreature target) {
     this.target = null;
     this.reboundCard = false;
     this.targetCard = card;
     this.target = target;
     if (card.exhaustOnUseOnce || card.exhaust) {
       this.exhaustCard = true;
     }
     setValues((AbstractCreature)AbstractCharBoss.boss, null, 1);
     this.duration = 0.15F;
     for (AbstractPower p : AbstractCharBoss.boss.powers) {
       if (!card.dontTriggerOnUseCard && p.type != AbstractPower.PowerType.DEBUFF) {
         p.onUseCard(card, makeNormalCardAction());
       }
     }
     for (AbstractCard c : AbstractCharBoss.boss.hand.group) {
       if (!card.dontTriggerOnUseCard) {
         c.triggerOnCardPlayed(card);
       }
     } 
     if (this.exhaustCard) {
       this.actionType = ActionType.EXHAUST;
     } else {
       this.actionType = ActionType.USE;
     } 
   }
   
   public EnemyUseCardAction(AbstractCard targetCard) {
     this(targetCard, (AbstractCreature)null);
   }
   
   public UseCardAction makeNormalCardAction() {
     AbstractCard cc = this.targetCard.makeStatEquivalentCopy();
     cc.dontTriggerOnUseCard = true;
     return new UseCardAction(cc, (AbstractCreature)AbstractCharBoss.boss);
   }
 
   
   public void update() {
     if (this.duration == 0.15F)
     {
       if (AbstractCharBoss.boss != null) {
         for (AbstractPower p : AbstractCharBoss.boss.powers) {
           if (!this.targetCard.dontTriggerOnUseCard && p.type != AbstractPower.PowerType.DEBUFF) {
             
             if (p instanceof EnemyReboundPower) {
               
               EnemyReboundPower eP = (EnemyReboundPower)p;
               eP.onAfterUse(this.targetCard, this);
             } 
 
             
             p.onAfterUseCard(this.targetCard, makeNormalCardAction());
           } 
         } 
         
         this.targetCard.freeToPlayOnce = false;
         this.targetCard.isInAutoplay = false;
         if (this.targetCard.purgeOnUse) {
           addToTop((AbstractGameAction)new EnemyShowCardAndPoofAction(this.targetCard));
           this.isDone = true;
           AbstractCharBoss.boss.cardInUse = null;
           return;
         } 
         if (this.targetCard.type == AbstractCard.CardType.POWER) {
           addToTop((AbstractGameAction)new EnemyShowCardAction(this.targetCard));
           if (Settings.FAST_MODE) {
             addToTop((AbstractGameAction)new WaitAction(0.1F));
           } else {
             addToTop((AbstractGameAction)new WaitAction(0.7F));
           } 
           AbstractCharBoss.boss.hand.empower(this.targetCard);
           this.isDone = true;
           AbstractCharBoss.boss.hand.applyPowers();
           AbstractCharBoss.boss.hand.glowCheck();
           AbstractCharBoss.boss.cardInUse = null;
           return;
         } 
         AbstractCharBoss.boss.cardInUse = null;
         boolean spoonProc = false;

 
         
         if (!this.exhaustCard || spoonProc) {
           if (this.reboundCard) {
             AbstractCharBoss.boss.hand.moveToDeck(this.targetCard, false);
           } else if (this.targetCard.shuffleBackIntoDrawPile) {
             AbstractCharBoss.boss.hand.moveToDeck(this.targetCard, true);
           } else if (this.targetCard.returnToHand) {
             AbstractCharBoss.boss.hand.moveToHand(this.targetCard);
             AbstractCharBoss.boss.onCardDrawOrDiscard();
           } else {
             AbstractCharBoss.boss.hand.moveToDiscardPile(this.targetCard);
           } 
         } else {
           AbstractCharBoss.boss.hand.moveToExhaustPile(this.targetCard);
         } 
         this.targetCard.exhaustOnUseOnce = false;
         this.targetCard.dontTriggerOnUseCard = false;
         addToBot((AbstractGameAction)new EnemyHandCheckAction());
       } 
     }
     tickDuration();
   }
 }
