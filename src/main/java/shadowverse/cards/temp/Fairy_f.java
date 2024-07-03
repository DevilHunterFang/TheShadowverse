package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import shadowverse.characters.Elf;


public class Fairy_f extends CustomCard {
    public static final String ID = "shadowverse:Fairy_f";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:Fairy_f");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Fairy_f.png";

    public Fairy_f() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower("shadowverse:AmatazPower")) {
            this.baseDamage = 4 + (AbstractDungeon.player.getPower("shadowverse:AmatazPower")).amount;
        } else {
            this.baseDamage = 4;
        }
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Elf.Enums.COLOR_GREEN;
        }
    }


    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(2);
        }
    }


    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("Fairy"));
        addToBot(new DamageAction(abstractMonster, new DamageInfo(abstractPlayer, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (abstractPlayer.hasPower("shadowverse:AriaPower")) {
            addToBot(new ApplyPowerAction(abstractMonster, abstractPlayer, new PoisonPower(abstractMonster, abstractPlayer, 2)));
        }
    }


    public AbstractCard makeCopy() {
        return (AbstractCard) new Fairy_f();
    }
}

