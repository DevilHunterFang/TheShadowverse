package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import shadowverse.action.MaishaCollectLockedAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaishaPower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:MaishaPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:MaishaPower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public List<AbstractCard> boxed = new ArrayList<>();
    public MaishaPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = NeutralPowertypePatch.NEUTRAL;
        this.img = new Texture("img/powers/MaishaPower.png");
        this.priority = 99;
        updateDescription();
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("MaishaPower", 0.05F);
    }

    @Override
    public void onInitialApplication() {
        addToBot(new ApplyPowerAction(this.owner,this.owner,new StrengthPower(this.owner,3)));
    }

    @Override
    public void atStartOfTurn() {
        boolean hasLockedCard = false;
        for (AbstractCard c : AbstractDungeon.player.discardPile.group){
            if (c.isLocked){
                this.boxed.add(c);
                addToBot(new MaishaCollectLockedAction(c,AbstractDungeon.player.discardPile));
                hasLockedCard = true;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group){
            if (c.isLocked){
                this.boxed.add(c);
                addToBot(new MaishaCollectLockedAction(c,AbstractDungeon.player.drawPile));
                hasLockedCard = true;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group){
            if (c.isLocked){
                this.boxed.add(c);
                addToBot(new MaishaCollectLockedAction(c,AbstractDungeon.player.hand));
                hasLockedCard = true;
            }
        }
        if (hasLockedCard){
            addToBot(new DamageAction(AbstractDungeon.player,new DamageInfo(this.owner,16, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer){
            ArrayList<AbstractCard> list = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.discardPile.group){
                if (CardLibrary.getCard(c.cardID).type == AbstractCard.CardType.ATTACK || c.type == AbstractCard.CardType.ATTACK){
                    list.add(c);
                }
            }
            if (list.size() == 0){
                addToBot(new DamageAction(AbstractDungeon.player,new DamageInfo(this.owner,8, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }else if (list.size() == 1){
                addToBot(new DamageAction(AbstractDungeon.player,new DamageInfo(this.owner,12, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                list.get(0).setLocked();
            }else {
                Collections.shuffle(list);
                list.get(0).setLocked();
                list.get(1).setLocked();
            }
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if (!this.boxed.isEmpty()) {
            this.description += DESCRIPTIONS[1];
            for (AbstractCard c : this.boxed) {
                this.description += " NL " + FontHelper.colorString(c.name, "y");
            }
        }
    }
}
