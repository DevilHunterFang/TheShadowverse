 package shadowverseCharbosses.actions.orb;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.animations.VFXAction;
 import com.megacrit.cardcrawl.actions.common.DamageAction;
 import com.megacrit.cardcrawl.actions.utility.SFXAction;
 import com.megacrit.cardcrawl.cards.DamageInfo;
 import com.megacrit.cardcrawl.characters.AbstractPlayer;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.orbs.AbstractOrb;
 import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
 import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
 import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 public class EnemyLightningOrbPassiveAction
   extends AbstractGameAction {
   private DamageInfo info;
   
   public EnemyLightningOrbPassiveAction(DamageInfo info, AbstractOrb orb, boolean hitAll) {
     this.info = info;
     this.orb = orb;
     this.actionType = ActionType.DAMAGE;
     this.attackEffect = AttackEffect.NONE;
     this.hitAll = hitAll;
   }
   private AbstractOrb orb; private boolean hitAll;
   public void update() {
     AbstractPlayer abstractPlayer = AbstractDungeon.player;
     
     float speedTime = 0.2F / AbstractCharBoss.boss.orbs.size();
     if (Settings.FAST_MODE) {
       speedTime = 0.0F;
     }
     
     this.info.output = AbstractOrb.applyLockOn((AbstractCreature)abstractPlayer, this.info.base);
     addToTop((AbstractGameAction)new DamageAction((AbstractCreature)abstractPlayer, this.info, AttackEffect.NONE, true));
     addToTop((AbstractGameAction)new VFXAction((AbstractGameEffect)new LightningEffect(((AbstractCreature)abstractPlayer).drawX, ((AbstractCreature)abstractPlayer).drawY), speedTime));
     addToTop((AbstractGameAction)new SFXAction("ORB_LIGHTNING_EVOKE"));
     if (this.orb != null) {
       addToTop((AbstractGameAction)new VFXAction((AbstractGameEffect)new OrbFlareEffect(this.orb, OrbFlareEffect.OrbFlareColor.LIGHTNING), speedTime));
     }
     
     this.isDone = true;
   }
 }
