 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.DamageAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyDamagePerAttackPlayedAction extends AbstractGameAction {
   private DamageInfo info;
   
   public EnemyDamagePerAttackPlayedAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
     setValues(target, this.info = info);
     this.actionType = ActionType.DAMAGE;
     this.attackEffect = effect;
   }
   
   public EnemyDamagePerAttackPlayedAction(AbstractCreature target, DamageInfo info) {
     this(target, info, AttackEffect.NONE);
   }
 
   
   public void update() {
     this.isDone = true;
     if (this.target != null && this.target.currentHealth > 0) {
       int count = AbstractCharBoss.boss.attacksPlayedThisTurn;
       count--;
       for (int i = 0; i < count; i++)
         addToTop((AbstractGameAction)new DamageAction(this.target, this.info, this.attackEffect)); 
     } 
   }
 }

