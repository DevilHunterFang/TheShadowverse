 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.utility.WaitAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
 
 
 
 public class EnemyBaneAction
   extends AbstractGameAction
 {
   private DamageInfo info;
   private static final float DURATION = 0.01F;
   private static final float POST_ATTACK_WAIT_DUR = 0.1F;
   private AbstractCreature m;
   
   public EnemyBaneAction(AbstractCreature target, DamageInfo info) {
     this.info = info;
     setValues(target, info);
     this.m = target;
     this.actionType = ActionType.DAMAGE;
     this.attackEffect = AttackEffect.SLASH_VERTICAL;
     this.duration = 0.01F;
   }
   
   public void update() {
     if (this.target == null) {
       this.isDone = true;
     }
     else if (this.m.hasPower("Poison")) {
       if (this.duration == 0.01F && this.target != null && this.target.currentHealth > 0) {
         if (this.info.type != DamageInfo.DamageType.THORNS && this.info.owner.isDying) {
           this.isDone = true;
           
           return;
         } 
         AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
       } 
       
       tickDuration();
       if (this.isDone && this.target != null && this.target.currentHealth > 0) {
         this.target.damage(this.info);
         if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead()) {
           AbstractDungeon.actionManager.clearPostCombatActions();
         }
         
         addToTop((AbstractGameAction)new WaitAction(0.1F));
       } 
     } else {
       this.isDone = true;
     } 
   }
 }
