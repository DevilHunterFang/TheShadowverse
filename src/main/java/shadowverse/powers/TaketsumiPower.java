package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shadowverse.monsters.TaketsumiBOSS;

public class TaketsumiPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:TaketsumiPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:TaketsumiPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractMonster source;

    public TaketsumiPower(AbstractCreature owner, AbstractMonster source) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.source = source;
        this.type = NeutralPowertypePatch.NEUTRAL;
        updateDescription();
        this.img = new Texture("img/powers/HeroOfTheHuntPower.png");
    }

    @Override
    public void onDeath() {
        source.decreaseMaxHealth(80);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target instanceof AbstractPlayer){
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                if (mo instanceof TaketsumiBOSS){
                    addToBot(new HealAction(mo, info.owner, damageAmount));
                    break;
                }
            }
        }
    }
}
