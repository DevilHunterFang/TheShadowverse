package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

public class NahtPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:NahtPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:NahtPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public List<AbstractCard> boxed = new ArrayList<>();
    public boolean triggered;

    public NahtPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.triggered = false;
        this.type = NeutralPowertypePatch.NEUTRAL;
        this.img = new Texture("img/powers/NahtPower.png");
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.triggered = false;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if (!this.boxed.isEmpty()) {
            this.description += DESCRIPTIONS[1];
            for (AbstractCard c : this.boxed) {
                this.description += " NL " + FontHelper.colorString(c.name, "y");
            }
        }
        this.description += " NL ";
        if (this.triggered) {
            this.description += DESCRIPTIONS[2];
        } else {
            this.description += DESCRIPTIONS[3];
        }
    }
}
