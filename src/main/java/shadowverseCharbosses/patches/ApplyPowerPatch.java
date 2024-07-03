 package shadowverseCharbosses.patches;

 import basemod.ReflectionHacks;
 import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
 import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
 import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 
 
 
 
 
 
 
 @SpirePatch(clz = ApplyPowerAction.class, method = "update")
 public class ApplyPowerPatch
 {
   @SpirePrefixPatch
   public static SpireReturn Prefix(ApplyPowerAction instance) {
     float duration = ((Float)ReflectionHacks.getPrivate(instance, AbstractGameAction.class, "duration")).floatValue();
     float startingDuration = ((Float)ReflectionHacks.getPrivate(instance, ApplyPowerAction.class, "startingDuration")).floatValue();
     AbstractPower powerToApply = (AbstractPower)ReflectionHacks.getPrivate(instance, ApplyPowerAction.class, "powerToApply");
     
     if (instance.target != null && !instance.target.isDeadOrEscaped() && 
       duration == startingDuration) {
       if (powerToApply instanceof com.megacrit.cardcrawl.powers.NoDrawPower && instance.target.hasPower(powerToApply.ID)) {
         instance.isDone = true;
         return SpireReturn.Return(null);
       } 
       if (instance.target instanceof com.megacrit.cardcrawl.monsters.AbstractMonster && instance.target.isDeadOrEscaped()) {
         ReflectionHacks.setPrivate(instance, AbstractGameAction.class, "duration", Float.valueOf(0.0F));
         instance.isDone = true;
         return SpireReturn.Return(null);
       } 
       
       if (instance.target instanceof AbstractCharBoss) {
         AbstractCharBoss cB = (AbstractCharBoss)instance.target;
       } 
     } 
 
 
 
     
     return SpireReturn.Continue();
   }
 }
