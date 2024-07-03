package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;

public class ShadePower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:ShadePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:ShadePower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int maxAmt;

    public ShadePower(AbstractCreature owner,int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.maxAmt = amount;
        this.type = NeutralPowertypePatch.NEUTRAL;
        this.img = new Texture("img/powers/ShadowBodyPower.png");
        this.priority = 999;
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL){
            ShadePower.this.amount -= 1;
            if (ShadePower.this.amount <= 0){
                ShadePower.this.amount = ShadePower.this.maxAmt;
                addToBot(new ApplyPowerAction(ShadePower.this.owner,ShadePower.this.owner,new IntangiblePower(ShadePower.this.owner,1),1));
            }
            updateDescription();
        }

        return damageAmount;
    }



    public void onDeath() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding())
            return;
        CardCrawlGame.sound.play("SPORE_CLOUD_RELEASE");
        flashWithoutSound();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, this.owner, new ShadowMarkPower(AbstractDungeon.player, 1), 1));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
