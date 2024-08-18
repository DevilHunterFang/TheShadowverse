package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BerserkPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import shadowverse.characters.Dragon;
import shadowverse.powers.OverflowPower;


public class Rowen_Story extends CustomCard {
    public static final String ID = "shadowverse:Rowen_Story";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Rowen_Story.png";

    public Rowen_Story() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;
        this.selfRetain = true;
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Dragon.Enums.COLOR_BROWN;
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("Rowen_Story"));
        if (Loader.isModLoaded("shadowverse")) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new OverflowPower(AbstractDungeon.player,1),1));
            addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new OverflowPower(AbstractDungeon.player,2),2));
            if (abstractPlayer.hasPower(OverflowPower.POWER_ID)){
                TwoAmountPower p = (TwoAmountPower) abstractPlayer.getPower(OverflowPower.POWER_ID);
                if (p.amount2 > 0){
                    addToBot(new ApplyPowerAction(abstractPlayer,abstractPlayer,new StrengthPower(abstractPlayer,this.magicNumber),this.magicNumber));
                    addToBot(new ApplyPowerAction(abstractPlayer,abstractPlayer,new LoseStrengthPower(abstractPlayer,this.magicNumber),this.magicNumber));
                }
            }
        }else {
            addToBot(new ApplyPowerAction(abstractPlayer,abstractPlayer,new BerserkPower(abstractPlayer,1)));
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new Rowen_Story();
    }
}

