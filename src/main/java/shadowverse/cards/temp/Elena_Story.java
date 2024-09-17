package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.action.KillNervaAction;
import shadowverse.monsters.Nerva;
import shadowverse.powers.NervaPower6;


public class Elena_Story extends CustomCard {
    public static final String ID = "shadowverse:Elena_Story";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Elena_Story.png";

    public Elena_Story() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);
        this.selfRetain = true;
        this.exhaust = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        AbstractMonster nerva = null;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            if (mo instanceof Nerva){
                nerva = mo;
            }
        }
        return AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Arisa_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Yuwan_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Urias_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Luna_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Iris_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Rowen_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Erika_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Isabelle_Story)
                && nerva != null
                && nerva.currentHealth == 1
                && nerva.hasPower(NervaPower6.POWER_ID);
    }

    public void triggerOnGlowCheck() {
        AbstractMonster nerva = null;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            if (mo instanceof Nerva){
                nerva = mo;
            }
        }
        boolean glow = AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Arisa_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Yuwan_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Urias_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Luna_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Iris_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Rowen_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Erika_Story)
                && AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().anyMatch(card -> card instanceof Isabelle_Story)
                && nerva != null
                && nerva.currentHealth == 1
                && nerva.hasPower(NervaPower6.POWER_ID);
        if (glow) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
        }
    }

    @Override
    public void triggerOnExhaust() {
        addToBot(new MakeTempCardInHandAction(makeCopy()));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.rawDescription =cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("Elena_Story"));
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            if (mo instanceof Nerva){
                addToBot(new KillNervaAction(abstractMonster));
            }
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new Elena_Story();
    }
}

