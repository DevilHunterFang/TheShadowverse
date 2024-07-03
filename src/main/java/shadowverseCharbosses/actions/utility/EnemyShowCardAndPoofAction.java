 package shadowverseCharbosses.actions.utility;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyShowCardAndPoofAction extends AbstractGameAction {
   private static final float PURGE_DURATION = 0.2F;
   
   public EnemyShowCardAndPoofAction(AbstractCard card) {
     this.card = null;
     setValues((AbstractCreature)AbstractCharBoss.boss, null, 1);
     this.card = card;
     this.duration = 0.2F;
     this.actionType = ActionType.SPECIAL;
   }
   private AbstractCard card;
   
   public void update() {
     if (this.duration == 0.2F) {
       AbstractDungeon.effectList.add(new ExhaustCardEffect(this.card));
       if (AbstractCharBoss.boss.limbo.contains(this.card)) {
         AbstractCharBoss.boss.limbo.removeCard(this.card);
       }
       AbstractCharBoss.boss.cardInUse = null;
     } 
     tickDuration();
   }
 }
