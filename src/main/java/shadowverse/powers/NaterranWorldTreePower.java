
package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class NaterranWorldTreePower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:NaterranWorldTreePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:NaterranWorldTreePower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public NaterranWorldTreePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.img = new Texture("img/powers/NaterranTree.png");
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount > 4) {
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                if (m != null && !m.isDying && !m.isEscaping) {
                    addToBot(new ApplyPowerAction(m, this.owner, new StrengthPower(m, 1), 1));
                    m.increaseMaxHp(6,true);
                }
            }
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
