package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import shadowverse.characters.AbstractShadowversePlayer;
import shadowverse.characters.Royal;
import shadowverse.characters.Witchcraft;
import shadowverse.powers.SevensForceSorcererPower;


public class SevensForceSorcerer extends CustomCard {
    public static final String ID = "shadowverse:SevensForceSorcerer";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:SevensForceSorcerer");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/SevensForceSorcerer.png";

    public SevensForceSorcerer() {
        super(ID, NAME, IMG_PATH, 3, DESCRIPTION, CardType.POWER, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Witchcraft.Enums.COLOR_BLUE;
            this.tags.add(AbstractShadowversePlayer.Enums.LEGEND);
        }
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }


    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("SevensForceSorcerer"));
        addToBot(new VFXAction(new RainbowCardEffect()));
        addToBot(new ApplyPowerAction(abstractPlayer, abstractPlayer, new SevensForceSorcererPower(abstractPlayer)));
        if (this.upgraded) {
            for (AbstractCard c : abstractPlayer.hand.group) {
                if (c.type == CardType.SKILL && c.costForTurn < 2) {
                    c.setCostForTurn(0);
                }
            }
        }
    }


    public AbstractCard makeCopy() {
        return (AbstractCard) new SevensForceSorcerer();
    }
}

