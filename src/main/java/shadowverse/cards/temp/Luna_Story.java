package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.action.NecromanceAction;
import shadowverse.action.ReanimateAction;
import shadowverse.characters.Necromancer;


public class Luna_Story extends CustomCard {
    public static final String ID = "shadowverse:Luna_Story";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Luna_Story.png";

    public Luna_Story() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.selfRetain = true;
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Necromancer.Enums.COLOR_PURPLE;
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("Luna_Story"));
        addToBot(new ReanimateAction(2));
        addToBot(new ReanimateAction(1));
        addToBot(new ReanimateAction(0));
        addToBot(new NecromanceAction(10, null,new ArmamentsAction(this.upgraded)));
    }


    @Override
    public AbstractCard makeCopy() {
        return new Luna_Story();
    }
}

