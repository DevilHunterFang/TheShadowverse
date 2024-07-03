package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shadowverse.action.CetusAction;

public class CetusPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:CetusPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:CetusPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int playCount;

    public CetusPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.img = new Texture("img/powers/CetusPower.png");
        this.priority = 99;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer){
            this.playCount = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        }
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new CetusAction());
        if (this.playCount > 5){
            addToBot(new CetusAction());
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
