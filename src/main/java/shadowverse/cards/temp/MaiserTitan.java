package shadowverse.cards.temp;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.Witchcraft;
import shadowverse.powers.MaiserTitanPower;

public class MaiserTitan extends CustomCard {
    public static final String ID = "shadowverse:MaiserTitan";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:MaiserTitan");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/RapidFire.png";

    public MaiserTitan() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        if (Loader.isModLoaded("shadowverse")){
            this.color = Witchcraft.Enums.COLOR_BLUE;
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded){
            upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new ApplyPowerAction(abstractPlayer,abstractPlayer,new MaiserTitanPower(abstractPlayer,1),1));
    }

    @Override
    public void onChoseThisOption() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new MaiserTitanPower(AbstractDungeon.player,1),1));
    }
}
