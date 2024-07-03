package shadowverseCharbosses.powers.cardpowers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shadowverseCharbosses.actions.util.CharbossDoCardQueueAction;

public class EnemyDoubleTapPower extends AbstractPower {
    public static final String POWER_ID = "Double Tap";

    public static final String NAME;

    public static final String[] DESCRIPTIONS;

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Double Tap");

    static {
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }

    public EnemyDoubleTapPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = "Double Tap";
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        loadRegion("doubleTap");
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && card.type == AbstractCard.CardType.ATTACK && this.amount > 0 && card instanceof shadowverseCharbosses.cards.AbstractBossCard) {
            flash();
            AbstractMonster m = null;
            if (action.target != null)
                m = (AbstractMonster) action.target;
            AbstractCard tmp = card.makeSameInstanceOf();
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = Settings.HEIGHT / 2.0F;
            if (m != null)
                tmp.calculateCardDamage(m);
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addToBottom( new CharbossDoCardQueueAction(tmp));
            this.amount--;
            if (this.amount == 0)
                addToBot( new RemoveSpecificPowerAction(this.owner, this.owner, "Double Tap"));
        }
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer)
            addToBot( new RemoveSpecificPowerAction(this.owner, this.owner, "Double Tap"));
    }
}
