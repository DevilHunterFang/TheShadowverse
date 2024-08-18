package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import shadowverse.action.ChangeSpriteAction;
import shadowverse.monsters.Nerva;
import shadowverseCharbosses.actions.RealWaitAction;

public class NervaPower6 extends TwoAmountPower {
    public static final String POWER_ID = "shadowverse:NervaPower6";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:NervaPower6");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public NervaPower6(AbstractCreature owner, int amount2) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        this.amount2 = amount2;
        this.type = NeutralPowertypePatch.NEUTRAL;
        updateDescription();
        this.img = new Texture("img/powers/NervaPower6.png");
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount2 * this.amount + DESCRIPTIONS[1];
    }

    public void playApplyPowerSfx() {
        if (this.amount < 8){
            CardCrawlGame.sound.play("NervaPower5", 0.05F);
        }else {
            CardCrawlGame.sound.play("Nerva_End", 0.05F);
        }
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount >= 8) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this.owner, Nerva.DIALOG[6]));
            AbstractDungeon.actionManager.addToBottom(new ChangeSpriteAction(((Nerva)this.owner).extra, (Nerva)this.owner, 2.1F));
            addToBot(new RealWaitAction(3.0f));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, 99999999, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            AbstractDungeon.player.isDead = true;
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
            AbstractDungeon.player.currentHealth = 0;
            if (AbstractDungeon.player.currentBlock > 0) {
                AbstractDungeon.player.loseBlock();
                AbstractDungeon.effectList.add(new HbBlockBrokenEffect(AbstractDungeon.player.hb.cX - AbstractDungeon.player.hb.width / 2.0F + (-14.0F * Settings.scale), AbstractDungeon.player.hb.cY - AbstractDungeon.player.hb.height / 2.0F + (-14.0F * Settings.scale)));
            }
        }
    }

}
