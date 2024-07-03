 package shadowverseCharbosses.actions.unique;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.animations.VFXAction;
 import com.megacrit.cardcrawl.actions.common.HealAction;
 import com.megacrit.cardcrawl.actions.utility.WaitAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.characters.AbstractPlayer;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.monsters.AbstractMonster;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
 import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
 import com.megacrit.cardcrawl.vfx.combat.FlyingOrbEffect;
 
 public class CustomReaperAction extends AbstractGameAction {
   public CustomReaperAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AttackEffect effect) {
     setValues(null, source, amount[0]);
     this.damage = amount;
     this.actionType = ActionType.DAMAGE;
     this.damageType = type;
     this.attackEffect = effect;
     this.duration = Settings.ACTION_DUR_FAST;
   }
   public int[] damage;
   
   public void update() {
     if (this.duration == Settings.ACTION_DUR_FAST) {
       boolean playedMusic = false;
       int temp = (AbstractDungeon.getCurrRoom()).monsters.monsters.size();
       for (int i = 0; i < temp; i++) {
         if (!((AbstractMonster)(AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).isDying && 
           ((AbstractMonster)(AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).currentHealth > 0 && 
           !((AbstractMonster)(AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).isEscaping) {
           if (playedMusic) {
             AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                   
                   ((AbstractMonster)(AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).hb.cX, 
                   ((AbstractMonster)(AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).hb.cY, this.attackEffect, true));
           } else {
             playedMusic = true;
             AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                   
                   ((AbstractMonster)(AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).hb.cX, 
                   ((AbstractMonster)(AbstractDungeon.getCurrRoom()).monsters.monsters.get(i)).hb.cY, this.attackEffect));
           } 
         }
       } 
     } 
 
     
     tickDuration();
     
     if (this.isDone) {
       for (AbstractPower p : AbstractDungeon.player.powers) {
         p.onDamageAllEnemies(this.damage);
       }
       
       int healAmount = 0;
       for (int i = 0; i < (AbstractDungeon.getCurrRoom()).monsters.monsters.size(); i++) {
         AbstractMonster target = (AbstractDungeon.getCurrRoom()).monsters.monsters.get(i);
         if (target != this.source && !target.isDying && target.currentHealth > 0 && !target.isEscaping) {
           target.damage(new DamageInfo(this.source, this.damage[i], this.damageType));
           if (target.lastDamageTaken > 0) {
             healAmount += target.lastDamageTaken;
             for (int j = 0; j < target.lastDamageTaken / 2 && j < 10; j++) {
               addToBot((AbstractGameAction)new VFXAction((AbstractGameEffect)new FlyingOrbEffect(target.hb.cX, target.hb.cY)));
             }
           } 
         } 
       } 
       
       AbstractPlayer targetPlayer = AbstractDungeon.player;
       if (!targetPlayer.isDying && targetPlayer.currentHealth > 0 && !targetPlayer.isEscaping) {
         targetPlayer.damage(new DamageInfo(this.source, this.damage[0], this.damageType));
         if (targetPlayer.lastDamageTaken > 0) {
           healAmount += targetPlayer.lastDamageTaken;
           for (int j = 0; j < targetPlayer.lastDamageTaken / 2 && j < 10; j++) {
             addToBot((AbstractGameAction)new VFXAction((AbstractGameEffect)new FlyingOrbEffect(targetPlayer.hb.cX, targetPlayer.hb.cY)));
           }
         } 
       } 
       
       if (healAmount > 0) {
         if (!Settings.FAST_MODE) {
           addToBot((AbstractGameAction)new WaitAction(0.3F));
         }
         addToBot((AbstractGameAction)new HealAction(this.source, this.source, healAmount));
       } 
       
       if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead()) {
         AbstractDungeon.actionManager.clearPostCombatActions();
       }
       addToTop((AbstractGameAction)new WaitAction(0.1F));
     } 
   }
 }
