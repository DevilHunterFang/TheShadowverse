 package shadowverseCharbosses.helpers;

 import com.megacrit.cardcrawl.cards.AbstractCard;
 import shadowverseCharbosses.bosses.AbstractCharBoss;

 import java.util.HashSet;
 import java.util.UUID;
 
 public class EnemyGetAllInBattleInstances
 {
   public static HashSet<AbstractCard> get(UUID uuid) {
     HashSet<AbstractCard> cards = new HashSet<>();
     if (AbstractCharBoss.boss.cardInUse.uuid.equals(uuid)) {
       cards.add(AbstractCharBoss.boss.cardInUse);
     }
     
     for (AbstractCard c : AbstractCharBoss.boss.limbo.group) {
       if (!c.uuid.equals(uuid)) {
         continue;
       }
       cards.add(c);
     } 
     for (AbstractCard c : AbstractCharBoss.boss.hand.group) {
       if (!c.uuid.equals(uuid)) {
         continue;
       }
       cards.add(c);
     } 
     return cards;
   }
 }

