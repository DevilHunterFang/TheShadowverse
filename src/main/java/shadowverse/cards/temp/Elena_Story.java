package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;


public class Elena_Story extends CustomCard {
    public static final String ID = "shadowverse:Elena_Story";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Elena_Story.png";

    public Elena_Story() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.selfRetain = true;
        this.exhaust = true;
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("Elena_Story"));
        AbstractGameEffect slash = new VfxBuilder(ImageMaster.ATK_SLASH_V,abstractMonster.hb.cX,0f,2f)
                .scale(0.8f,4.0f,VfxBuilder.Interpolations.SMOOTH2)
                .moveX(Settings.WIDTH,abstractMonster.hb.x - abstractMonster.hb.x / 4,VfxBuilder.Interpolations.POW2IN)
                .moveY(Settings.HEIGHT, abstractMonster.hb.y + abstractMonster.hb.height / 4, VfxBuilder.Interpolations.POW2IN)
                .fadeIn(0.25f)
                .fadeOut(0.25f)
                .setColor(Color.SKY)
                .build();
    }


    @Override
    public AbstractCard makeCopy() {
        return new Elena_Story();
    }
}

