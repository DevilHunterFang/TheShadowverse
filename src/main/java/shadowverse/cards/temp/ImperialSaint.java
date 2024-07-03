package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import shadowverse.characters.AbstractShadowversePlayer;
import shadowverse.characters.Bishop;
import shadowverse.characters.Nemesis;
import shadowverse.powers.ImperialSaintPower;


public class ImperialSaint extends CustomCard {
    public static final String ID = "shadowverse:ImperialSaint";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:ImperialSaint");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/ImperialSaint.png";

    public ImperialSaint() {
        super(ID, NAME, IMG_PATH, 4, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.baseMagicNumber = 50;
        this.magicNumber = this.baseMagicNumber;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Bishop.Enums.COLOR_WHITE;
            this.tags.add(AbstractShadowversePlayer.Enums.LEGEND);
        }
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(10);
        }
    }

    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("ImperialSaint"));
        addToBot(new VFXAction(new BorderFlashEffect(Color.GOLD, true), 1.0f));
        addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber));
        addToBot(new ApplyPowerAction(abstractPlayer, abstractPlayer, new BlurPower(abstractPlayer, 1), 1));
        addToBot(new ApplyPowerAction(abstractPlayer, abstractMonster, new ImperialSaintPower(abstractPlayer)));
    }


    public AbstractCard makeCopy() {
        return (AbstractCard) new ImperialSaint();
    }
}

