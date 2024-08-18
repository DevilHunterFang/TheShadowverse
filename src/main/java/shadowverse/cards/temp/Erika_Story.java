package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import shadowverse.action.MinionAttackAction;
import shadowverse.action.MinionBuffAction;
import shadowverse.action.MinionSummonAction;
import shadowverse.characters.AbstractShadowversePlayer;
import shadowverse.characters.Royal;
import shadowverse.characters.Witchcraft;
import shadowverse.orbs.AmbushMinion;
import shadowverse.orbs.ErikaOrb;
import shadowverse.orbs.Minion;
import shadowverse.orbs.Quickblader;


public class Erika_Story extends CustomCard {
    public static final String ID = "shadowverse:Erika_Story";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Erika_Story.png";

    public Erika_Story() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);
        this.baseDamage = 3;
        this.selfRetain = true;
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Royal.Enums.COLOR_YELLOW;
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(2);
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("Erika_Story"));
        if (Loader.isModLoaded("shadowverse")) {
            if (abstractPlayer instanceof Royal){
                AbstractDungeon.actionManager.addToBottom(new MinionSummonAction(new Quickblader()));
                AbstractDungeon.actionManager.addToBottom(new MinionSummonAction(new Quickblader()));
            }else {
                for (int i = 0 ; i < 2; i++){
                    AbstractCreature m = AbstractDungeon.getRandomMonster();
                    if (m != null) {
                        addToBot(new ApplyPowerAction(m, abstractPlayer, new VulnerablePower(m, 1, false), 1));
                        addToBot(new DamageAction(m,new DamageInfo(abstractPlayer,this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    }
                }
            }
            if (rally() >= 10){
                addToBot(new ApplyPowerAction(abstractPlayer,abstractPlayer,new StrengthPower(abstractPlayer,2),2));
                addToBot(new ApplyPowerAction(abstractPlayer,abstractPlayer,new DexterityPower(abstractPlayer,2),2));
                AbstractDungeon.actionManager.addToBottom(new MinionBuffAction(2, 2, true));
            }
        }else {
            for (int i = 0 ; i < 2; i++){
                AbstractCreature m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    addToBot(new ApplyPowerAction(m, abstractPlayer, new VulnerablePower(m, 1, false), 1));
                    addToBot(new DamageAction(m,new DamageInfo(abstractPlayer,this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
            }
            if (rally() >= 10){
                addToBot(new ApplyPowerAction(abstractPlayer,abstractPlayer,new StrengthPower(abstractPlayer,2),2));
                addToBot(new ApplyPowerAction(abstractPlayer,abstractPlayer,new DexterityPower(abstractPlayer,2),2));
            }
        }
    }

    public int rally() {
        int rally = 0;
        if (Loader.isModLoaded("shadowverse")) {
            for (AbstractOrb o : AbstractDungeon.actionManager.orbsChanneledThisCombat) {
                if (o instanceof Minion && !(o instanceof AmbushMinion) && !(o instanceof ErikaOrb)) {
                    rally++;
                }
            }
        }

        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
            if (c.type == CardType.ATTACK && !(c.hasTag(CardTags.STRIKE))) {
                rally++;
            }
        }
        return rally;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.rawDescription =cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] + rally() + cardStrings.EXTENDED_DESCRIPTION[1];
        this.initializeDescription();
    }


    @Override
    public AbstractCard makeCopy() {
        return new Erika_Story();
    }
}

