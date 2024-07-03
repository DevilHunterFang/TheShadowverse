package shadowverse.cards.temp;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.Vampire;
import shadowverse.powers.Itsurugi_SlashPower;

public class Itsurugi_Slash extends CustomCard {
    public static final String ID = "shadowverse:Itsurugi_Slash";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:Itsurugi_Slash");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Itsurugi_Slash.png";

    public Itsurugi_Slash() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.exhaust = true;
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        if (Loader.isModLoaded("shadowverse")){
            this.color = Vampire.Enums.COLOR_SCARLET;
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded){
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("Itsurugi_Slash"));
        addToBot(new DiscardAction(abstractPlayer,abstractPlayer,1,false));
        addToBot(new ApplyPowerAction(abstractPlayer,abstractPlayer,new Itsurugi_SlashPower(abstractPlayer,this.magicNumber),this.magicNumber));
    }
}
