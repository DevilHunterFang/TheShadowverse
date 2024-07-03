 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.GainBlockAction;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.monsters.AbstractMonster;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyHaltAction extends AbstractGameAction {
   private AbstractMonster m;
   private int additionalAmt;
   
   public EnemyHaltAction(AbstractMonster monster, int block, int magicNumber) {
     this.m = monster;
     this.amount = block;
     this.additionalAmt = magicNumber;
   }
   
   public void update() {
     if (AbstractCharBoss.boss.stance instanceof shadowverseCharbosses.stances.EnWrathStance) {
       addToTop((AbstractGameAction)new GainBlockAction((AbstractCreature)this.m, (AbstractCreature)this.m, this.amount + this.additionalAmt));
     } else {
       addToTop((AbstractGameAction)new GainBlockAction((AbstractCreature)this.m, (AbstractCreature)this.m, this.amount));
     } 
     this.isDone = true;
   }
 }
