package shadowverse.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;
import java.util.Collections;

public class ReanimateAction extends AbstractGameAction {
    public static final float DURATION = Settings.ACTION_DUR_MED;
    private ArrayList<AbstractCard> list = new ArrayList<AbstractCard>();
    private ArrayList<Integer> costTmp = new ArrayList<>();
    private ArrayList<AbstractCard> finalList = new ArrayList<>();
    private AbstractPlayer p = AbstractDungeon.player;

    public ReanimateAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = DURATION;
    }


    @Override
    public void update() {
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
            if (((CardLibrary.getCard(c.cardID) != null && CardLibrary.getCard(c.cardID).type == AbstractCard.CardType.ATTACK) || c.type == AbstractCard.CardType.ATTACK) && c.cost <= this.amount) {
                AbstractCard tmp = c.makeSameInstanceOf();
                tmp.resetAttributes();
                list.add(tmp);
            }
        }
        if (list != null && list.size() != 0) {
            for (AbstractCard c : list) {
                costTmp.add(c.cost);
            }
            int max = Collections.max(costTmp);
            for (AbstractCard finalCard : list) {
                if (finalCard.cost == max) {
                    finalList.add(finalCard);
                }
            }
            Collections.shuffle(finalList);
            AbstractCard tempCard = finalList.get(0).makeStatEquivalentCopy();
            tempCard.setCostForTurn(0);
            tempCard.applyPowers();
            addToBot(new MakeTempCardInHandAction(tempCard));
        }
        p.hand.refreshHandLayout();
        p.hand.applyPowers();
        this.isDone = true;
    }
}
