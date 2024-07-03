package shadowverse.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import shadowverse.cards.Neutral.Temp.Fairy;
import shadowverse.cards.temp.Fairy_f;

public class ShiningValkyriePower extends AbstractPower {
    public static final String POWER_ID = "shadowverse:ShiningValkyriePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("shadowverse:ShiningValkyriePower");
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ShiningValkyriePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        updateDescription();
        this.img = new Texture("img/powers/ShiningValkyriePower.png");
    }


    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.cardID.equals("shadowverse:Fairy")||card.cardID.equals("shadowverse:Fairy_f")){
            addToBot(new DamageRandomEnemyAction(new DamageInfo(this.owner,6, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
            addToBot(new ApplyPowerAction(this.owner,this.owner,new StrengthPower(this.owner,1),1));
            addToBot(new ApplyPowerAction(this.owner,this.owner,new DexterityPower(this.owner,1),1));
        }
    }

    public void atStartOfTurn() {
        if (Loader.isModLoaded("shadowverse")) {
            addToBot(new MakeTempCardInHandAction(new Fairy()));
        }else {
            addToBot(new MakeTempCardInHandAction(new Fairy_f()));
        }
    }

}
