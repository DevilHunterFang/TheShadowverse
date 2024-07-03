 package shadowverseCharbosses.patches;

 import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
 import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 

 
 @SpirePatch(clz = AbstractCreature.class, method = "brokeBlock")
 public class BrokeBlockPatch
 {
   @SpirePrefixPatch
   public static void Prefix(AbstractCreature instance) {
     if (instance instanceof com.megacrit.cardcrawl.characters.AbstractPlayer && 
       (AbstractDungeon.getMonsters()).monsters.size() > 0 && 
       (AbstractDungeon.getMonsters()).monsters.get(0) instanceof AbstractCharBoss) {
       AbstractCharBoss cB = (AbstractCharBoss) (AbstractDungeon.getMonsters()).monsters.get(0);

     } 
   }
 }
