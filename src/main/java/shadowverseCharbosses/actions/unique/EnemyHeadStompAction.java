 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.characters.AbstractPlayer;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.powers.WeakPower;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyHeadStompAction extends AbstractGameAction {
   private AbstractPlayer m;
   private int magicNumber;
   
   public EnemyHeadStompAction(AbstractPlayer monster, int vulnAmount) {
     this.m = monster;
     this.magicNumber = vulnAmount;
   }
   
   public void update() {
     if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() >= 2 && ((AbstractCard)AbstractDungeon.actionManager.cardsPlayedThisCombat.get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 2)).type == AbstractCard.CardType.ATTACK) {
       addToTop((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this.m, (AbstractCreature)AbstractCharBoss.boss, (AbstractPower)new WeakPower((AbstractCreature)this.m, this.magicNumber, true), this.magicNumber));
     }
     
     this.isDone = true;
   }
 }
