package shadowverse.cards.temp;


import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.RemoveAllTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.watcher.JudgementAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import shadowverse.characters.Nemesis;


public class Yuwan_Story extends CustomCard {
    public static final String ID = "shadowverse:Yuwan_Story";
    public static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "img/cards/Yuwan_Story.png";

    public Yuwan_Story() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);
        this.baseDamage = 56;
        this.selfRetain = true;
        this.exhaust = true;
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }


    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(7);
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new SFXAction("Yuwan_Story"));
        if (abstractMonster.hasPower(MinionPower.POWER_ID)){
            addToBot(new JudgementAction(abstractMonster,9999));
        }
        if (abstractPlayer.drawPile.group.size() % 2 == 0){
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                if (!mo.hasPower(MinionPower.POWER_ID) && !mo.isDeadOrEscaped()){
                    addToBot(new VFXAction(abstractPlayer, new MindblastEffect(abstractPlayer.dialogX, abstractPlayer.dialogY, abstractPlayer.flipHorizontal), 0.1F));
                    addToBot(new DamageAction(mo,new DamageInfo(abstractPlayer,this.damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
                }else if (mo.isDeadOrEscaped()){
                    TempHPField.tempHp.set(mo, 0);
                }
            }
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new Yuwan_Story();
    }
}

