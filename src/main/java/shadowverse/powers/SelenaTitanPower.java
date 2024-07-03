package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import shadowverse.monsters.Nexus;

public class SelenaTitanPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:SelenaTitanPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:SelenaTitanPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SelenaTitanPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        updateDescription();
        this.img = new Texture("img/powers/HeavenFirePower.png");
    }

    @Override
    public void atStartOfTurn() {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            if (mo instanceof Nexus){
                addToBot(new DamageAction(mo,new DamageInfo(this.owner,25, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
                break;
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount > 3){
            addToBot(new ApplyPowerAction(this.owner,this.owner,new StrengthPower(this.owner,2),2));
            addToBot(new ApplyPowerAction(this.owner,this.owner,new DexterityPower(this.owner,2),2));
        }
        addToBot(new ApplyPowerAction(this.owner,this.owner,new MaiserTitanPower(this.owner,this.amount+1),this.amount+1));
        addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,this));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
