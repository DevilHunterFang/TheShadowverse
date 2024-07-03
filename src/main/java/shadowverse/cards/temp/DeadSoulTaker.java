package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import shadowverse.action.NecromanceAction;
import shadowverse.action.PrimalGigantAction;
import shadowverse.characters.AbstractShadowversePlayer;
import shadowverse.characters.Necromancer;
import shadowverse.characters.Vampire;


public class DeadSoulTaker extends CustomCard {
    public static final String ID = "shadowverse:DeadSoulTaker";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:DeadSoulTaker");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/DeadSoulTaker.png";

    public DeadSoulTaker() {
        super(ID, NAME, IMG_PATH, 4, DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.baseBlock = 45;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Necromancer.Enums.COLOR_PURPLE;
            this.tags.add(AbstractShadowversePlayer.Enums.LEGEND);
        }
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(3);
        }
    }


    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("DeadSoulTaker"));
        addToBot(new VFXAction(new BorderFlashEffect(Color.PURPLE, true), 1.0f));
        addToBot(new GainBlockAction(abstractPlayer, this.block));
        addToBot(new MoveCardsAction(abstractPlayer.hand, abstractPlayer.exhaustPile, card -> {
            return card.type == CardType.ATTACK && card.cardID != this.cardID;
        }, abstractCards -> {
            for (AbstractCard c : abstractCards) {
                c.setCostForTurn(0);
                c.applyPowers();
            }
        }));
        addToBot(new NecromanceAction(10, null, new PrimalGigantAction()));
    }


    public AbstractCard makeCopy() {
        return (AbstractCard) new DeadSoulTaker();
    }
}

