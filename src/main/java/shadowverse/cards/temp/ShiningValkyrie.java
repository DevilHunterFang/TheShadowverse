package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import shadowverse.characters.AbstractShadowversePlayer;
import shadowverse.characters.Elf;
import shadowverse.powers.ShiningValkyriePower;


public class ShiningValkyrie extends CustomCard {
    public static final String ID = "shadowverse:ShiningValkyrie";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:ShiningValkyrie");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/ShiningValkyrie.png";

    public ShiningValkyrie() {
        super(ID, NAME, IMG_PATH, 1, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        this.cardsToPreview = new Fairy_f();
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Elf.Enums.COLOR_GREEN;
            this.tags.add(AbstractShadowversePlayer.Enums.LEGEND);
        }
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }


    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("ShiningValkyrie"));
        addToBot(new VFXAction(new BorderFlashEffect(Color.GOLD, true), 1.0f));
        addToBot(new ApplyPowerAction(abstractPlayer, abstractPlayer, new ShiningValkyriePower(abstractPlayer)));
        addToBot(new MakeTempCardInHandAction(this.cardsToPreview.makeStatEquivalentCopy(), this.magicNumber));
    }


    public AbstractCard makeCopy() {
        return (AbstractCard) new ShiningValkyrie();
    }
}

