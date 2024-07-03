package shadowverse.cards.temp;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.AbstractShadowversePlayer;

public class BelphometStatus extends CustomCard {
    public static final String ID = "shadowverse:BelphometStatus";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:BelphometStatus");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/BelphometStatus.png";

    public BelphometStatus() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        this.selfRetain = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.tags.add(AbstractShadowversePlayer.Enums.MACHINE);
        }
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void onRetained() {
        for (AbstractCard c: AbstractDungeon.player.hand.group){
            if (c.cardID.equals("shadowverse:BelphometStatus") && c!=this){
                this.flash();
                addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, this.magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new DamageAction(abstractPlayer, new DamageInfo(abstractPlayer, 4, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public AbstractCard makeCopy(){
        return new BelphometStatus();
    }
}
