 package shadowverseCharbosses.actions.common;

 import com.megacrit.cardcrawl.actions.AbstractGameAction;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import shadowverseCharbosses.helpers.EnemyGetAllInBattleInstances;

 import java.util.UUID;
 
 public class EnemyModifyDamageAction
   extends AbstractGameAction {
   private UUID uuid;
   
   public EnemyModifyDamageAction(UUID targetUUID, int amount) {
     setValues(this.target, this.source, amount);
     this.actionType = ActionType.CARD_MANIPULATION;
     this.uuid = targetUUID;
   }
 
   
   public void update() {
     for (AbstractCard abstractCard : EnemyGetAllInBattleInstances.get(this.uuid)) {
       AbstractCard c = abstractCard;
       abstractCard.baseDamage += this.amount;
       if (c.baseDamage < 0) {
         c.baseDamage = 0;
       }
     } 
     this.isDone = true;
   }
 }
