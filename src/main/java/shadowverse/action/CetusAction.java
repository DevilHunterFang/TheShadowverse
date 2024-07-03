package shadowverse.action;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import shadowverse.cards.AbstractNoCountDownAmulet;
import shadowverse.orbs.AmuletOrb;
import shadowverse.powers.MaishaPower;

import java.util.ArrayList;
import java.util.HashMap;

public class CetusAction extends AbstractGameAction {
    public static final String[] TEXT = (CardCrawlGame.languagePack.getUIString("shadowverse:CetusAction")).TEXT;

    private AbstractPlayer p = AbstractDungeon.player;
    private HashMap<AbstractCard, Integer> map = new HashMap<>();
    private boolean retrieveCard = false;

    public CetusAction(){
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = 0.25F;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {

            if (generateCardChoices().size() == 0) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.cardRewardScreen.customCombatOpen(generateCardChoices(), TEXT[0], false);
            tickDuration();
            return;
        }
        if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard;
                disCard.isLocked = false;
                if (this.map.get(disCard) == 1){
                    disCard.unhover();
                    disCard.lighten(true);
                    disCard.setAngle(0.0F);
                    disCard.drawScale = 0.12F;
                    disCard.targetDrawScale = 0.75F;
                    disCard.current_x = CardGroup.DRAW_PILE_X;
                    disCard.current_y = CardGroup.DRAW_PILE_Y;
                    this.p.discardPile.removeCard(disCard);
                    AbstractDungeon.player.hand.addToTop(disCard);
                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.player.hand.applyPowers();
                }else {
                    if (this.p.hand.size() == 10) {
                        this.p.drawPile.moveToDiscardPile(disCard);
                        this.p.createHandIsFullDialog();
                    }else{
                        disCard.unhover();
                        disCard.lighten(true);
                        disCard.setAngle(0.0F);
                        disCard.drawScale = 0.12F;
                        disCard.targetDrawScale = 0.75F;
                        disCard.current_x = CardGroup.DRAW_PILE_X;
                        disCard.current_y = CardGroup.DRAW_PILE_Y;
                        this.p.drawPile.removeCard(disCard);
                        AbstractDungeon.player.hand.addToTop(disCard);
                        AbstractDungeon.player.hand.refreshHandLayout();
                        AbstractDungeon.player.hand.applyPowers();
                    }
                }
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if (mo.hasPower(MaishaPower.POWER_ID)){
                        mo.getPower(MaishaPower.POWER_ID).amount -= 15;
                        addToBot(new HealAction(AbstractDungeon.player,AbstractDungeon.player,1));
                        if (mo.getPower(MaishaPower.POWER_ID).amount <= 0){
                            addToBot(new SuicideAction(mo,true));
                        }
                    }
                }
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices()  {
        ArrayList<AbstractCard> derp = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.discardPile.group){
            if (c.isLocked){
                this.map.put(c,1);
                derp.add(c);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group){
            if (c.isLocked){
                this.map.put(c,2);
                derp.add(c);
            }
        }

        return derp;
    }
}
