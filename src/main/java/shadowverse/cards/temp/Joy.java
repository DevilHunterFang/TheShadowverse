package shadowverse.cards.temp;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.NoBlockPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;

public class Joy extends CustomCard {
    public static final String ID = "shadowverse:Joy";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:Joy");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Joy.png";

    public Joy() {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.NONE);
        this.isEthereal = true;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void triggerWhenDrawn() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new IntangiblePlayerPower(AbstractDungeon.player,1),1));
        addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new NoBlockPower(AbstractDungeon.player,1,false),1));
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

    @Override
    public AbstractCard makeCopy(){
        return new Joy();
    }
}
