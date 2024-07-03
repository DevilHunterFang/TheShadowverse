package shadowverseCharbosses.cards.vampire;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import shadowverse.characters.Vampire;
import shadowverseCharbosses.cards.AbstractBossCard;

import java.util.ArrayList;

public class EnSeox extends AbstractBossCard {
    public static final String ID = "shadowverse:EnSeox";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:EnSeox");

    public static final String IMG_PATH = "img/cards/Seox.png";

    public EnSeox() {
        super(ID, cardStrings.NAME, IMG_PATH, 2, cardStrings.DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ENEMY, AbstractMonster.Intent.ATTACK);
        this.baseDamage = 4;
        this.baseMagicNumber = 2;
        this.magicNumber = this.baseMagicNumber;
        this.isMultiDamage = true;
        this.intentMultiAmt = this.magicNumber;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Vampire.Enums.COLOR_SCARLET;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.actionManager.turn>5){
            addToBot(new SFXAction("Seox_SSA"));
        }else if (AbstractDungeon.actionManager.turn>2){
            addToBot(new SFXAction("Seox_SA"));
        }else {
            addToBot(new SFXAction("Seox"));
        }
        for (int i=0;i<this.magicNumber;i++){
            addToBot(new VFXAction((AbstractGameEffect)new ClawEffect(p.hb.cX, p.hb.cY, Color.SCARLET, Color.ORANGE), 0.1F));
            addToBot(new DamageAction(p, new DamageInfo(m, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (AbstractDungeon.actionManager.turn>5){
            this.magicNumber = 6;
            this.intentMultiAmt = this.magicNumber;
        }else if (AbstractDungeon.actionManager.turn>2){
            this.magicNumber = 4;
            this.intentMultiAmt = this.magicNumber;
        }
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> hand) {
        return 80;
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(2);
        }
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnSeox();
    }
}
