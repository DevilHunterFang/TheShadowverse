 package shadowverseCharbosses.actions.vfx.cardmanip;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.badlogic.gdx.math.MathUtils;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.core.CardCrawlGame;
 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.unlock.UnlockTracker;
 import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
 import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;
 import shadowverseCharbosses.bosses.AbstractCharBoss;
 
 
 public class EnemyShowCardAndAddToHandEffect
   extends AbstractGameEffect
 {
   private static final float EFFECT_DUR = 0.8F;
   private static final float PADDING = 25.0F * Settings.scale;
   
   private AbstractCard card;
 
   
   public EnemyShowCardAndAddToHandEffect(AbstractCard card, float offsetX, float offsetY) {
     this.card = card;
     UnlockTracker.markCardAsSeen(card.cardID);
     card.current_x = Settings.WIDTH / 2.0F;
     card.current_y = Settings.HEIGHT / 2.0F;
     card.target_x = offsetX;
     card.target_y = offsetY;
     this.duration = 0.8F;
     card.drawScale = 0.75F;
     card.targetDrawScale = 0.75F;
     card.transparency = 0.01F;
     card.targetTransparency = 1.0F;
     card.fadingOut = false;
     playCardObtainSfx();
     if (card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS && AbstractCharBoss.boss.hasPower("MasterRealityPower")) {
       card.upgrade();
     }
     card.untip();
     AbstractCharBoss.boss.hand.group.add(0, card);
     card.triggerWhenCopied();
     AbstractCharBoss.boss.hand.refreshHandLayout();
     AbstractCharBoss.boss.hand.applyPowers();
     AbstractCharBoss.boss.onCardDrawOrDiscard();
     if (AbstractCharBoss.boss.hasPower("Corruption") && card.type == AbstractCard.CardType.SKILL) {
       card.setCostForTurn(-9);
     }
   }
   
   public EnemyShowCardAndAddToHandEffect(AbstractCard card) {
     this.card = card;
     identifySpawnLocation(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F);
     this.duration = 0.8F;
     card.drawScale = 0.75F;
     card.targetDrawScale = 0.75F;
     card.transparency = 0.01F;
     card.targetTransparency = 1.0F;
     card.fadingOut = false;
     if (card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS && AbstractCharBoss.boss.hasPower("MasterRealityPower")) {
       card.upgrade();
     }
     card.untip();
     AbstractCharBoss.boss.hand.group.add(0, card);
     card.triggerWhenCopied();
     AbstractCharBoss.boss.hand.refreshHandLayout();
     AbstractCharBoss.boss.hand.applyPowers();
     AbstractCharBoss.boss.onCardDrawOrDiscard();
     if (AbstractCharBoss.boss.hasPower("Corruption") && card.type == AbstractCard.CardType.SKILL) {
       card.setCostForTurn(-9);
     }
   }
   
   private void playCardObtainSfx() {
     int effectCount = 0;
     for (AbstractGameEffect e : AbstractDungeon.effectList) {
       if (e instanceof EnemyShowCardAndAddToHandEffect) {
         effectCount++;
       }
     } 
     if (effectCount < 2) {
       CardCrawlGame.sound.play("CARD_OBTAIN");
     }
   }
   
   private void identifySpawnLocation(float x, float y) {
     int effectCount = 0;
     for (AbstractGameEffect e : AbstractDungeon.effectList) {
       if (e instanceof EnemyShowCardAndAddToHandEffect) {
         effectCount++;
       }
     } 
     this.card.target_y = Settings.HEIGHT * 0.5F;
     switch (effectCount) {
       case 0:
         this.card.target_x = Settings.WIDTH * 0.5F;
         break;
       
       case 1:
         this.card.target_x = Settings.WIDTH * 0.5F - PADDING - AbstractCard.IMG_WIDTH;
         break;
       
       case 2:
         this.card.target_x = Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH;
         break;
       
       case 3:
         this.card.target_x = Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH) * 2.0F;
         break;
       
       case 4:
         this.card.target_x = Settings.WIDTH * 0.5F + (PADDING + AbstractCard.IMG_WIDTH) * 2.0F;
         break;
       
       default:
         this.card.target_x = MathUtils.random(Settings.WIDTH * 0.1F, Settings.WIDTH * 0.9F);
         this.card.target_y = MathUtils.random(Settings.HEIGHT * 0.2F, Settings.HEIGHT * 0.8F);
         break;
     } 
     
     this.card.current_x = this.card.target_x;
     this.card.current_y = this.card.target_y - 200.0F * Settings.scale;
     AbstractDungeon.effectsQueue.add(new CardPoofEffect(this.card.target_x, this.card.target_y));
   }
 
   
   public void update() {
     this.duration -= Gdx.graphics.getDeltaTime();
     this.card.update();
     if (this.duration < 0.0F) {
       this.isDone = true;
     }
   }
 
   
   public void render(SpriteBatch sb) {
     if (!this.isDone)
       this.card.render(sb); 
   }
   
   public void dispose() {}
 }
