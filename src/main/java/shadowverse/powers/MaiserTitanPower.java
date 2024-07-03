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
import shadowverse.monsters.Shade;

public class MaiserTitanPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:MaiserTitanPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:MaiserTitanPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MaiserTitanPower(AbstractCreature owner,int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        updateDescription();
        this.img = new Texture("img/powers/Maiser_Power.png");
    }

    private int numAlive() {
        int count = 0;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m != null && !m.isDeadOrEscaped() && !(m instanceof Nexus))
                count++;
        }
        return count;
    }

    @Override
    public void atStartOfTurn() {
        if (numAlive() > 0){
            AbstractMonster mo = AbstractDungeon.getMonsters().getRandomMonster(true);
            if (mo != null){
                while (mo.hasPower(NexusPower.POWER_ID)){
                    mo = AbstractDungeon.getMonsters().getRandomMonster(true);
                }
            }
            if (mo != null){
                addToBot(new DamageAction(mo,new DamageInfo(this.owner,50, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount > 3){
            addToBot(new ApplyPowerAction(this.owner,this.owner,new StrengthPower(this.owner,2),2));
            addToBot(new ApplyPowerAction(this.owner,this.owner,new DexterityPower(this.owner,2),2));
        }
        addToBot(new ApplyPowerAction(this.owner,this.owner,new SelenaTitanPower(this.owner,this.amount+1),this.amount+1));
        addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,this));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
