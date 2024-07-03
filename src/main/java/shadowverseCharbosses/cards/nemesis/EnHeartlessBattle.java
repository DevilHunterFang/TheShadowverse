package shadowverseCharbosses.cards.nemesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shadowverse.characters.Nemesis;
import shadowverseCharbosses.actions.common.EnemyMakeTempCardInHandAction;
import shadowverseCharbosses.cards.AbstractBossCard;

import java.util.ArrayList;

public class EnHeartlessBattle extends AbstractBossCard {
    public static final String ID = "shadowverse:EnHeartlessBattle";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("shadowverse:HeartlessBattle");

    public static final String IMG_PATH = "img/cards/HeartlessBattle.png";
    private float rotationTimer;
    private int previewIndex;


    public static ArrayList<AbstractCard> returnChoice() {
        ArrayList<AbstractCard> list = new ArrayList<>();
        list.add(new EnRoid());
        list.add(new EnVictoria());
        list.add(new EnPuppet());
        return list;
    }

    public EnHeartlessBattle() {
        super(ID, cardStrings.NAME, IMG_PATH, 2, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.NONE, AbstractMonster.Intent.UNKNOWN);
        if (Loader.isModLoaded("shadowverse")) {
            this.color = Nemesis.Enums.COLOR_SKY;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard trueStrike = new EnRoid();
        AbstractCard falseSlash = new EnVictoria();
        AbstractCard puppet = new EnPuppet();
        if (this.upgraded){
            trueStrike.upgrade();
            falseSlash.upgrade();
            puppet.upgrade();
        }
        int rnd = AbstractDungeon.cardRandomRng.random(1);
        if (rnd==0){
            addToBot(new EnemyMakeTempCardInHandAction(falseSlash));
        }else {
            addToBot(new EnemyMakeTempCardInHandAction(trueStrike));
        }
        addToBot(new EnemyMakeTempCardInHandAction(puppet));
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public void update() {
        super.update();
        if (this.hb.hovered)
            if (this.rotationTimer <= 0.0F) {
                this.rotationTimer = 2.0F;
                this.cardsToPreview = (AbstractCard) returnChoice().get(previewIndex).makeCopy();
                if (this.previewIndex == returnChoice().size() - 1) {
                    this.previewIndex = 0;
                } else {
                    this.previewIndex++;
                }
                if (this.upgraded)
                    this.cardsToPreview.upgrade();
            } else {
                this.rotationTimer -= Gdx.graphics.getDeltaTime();
            }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (this.hov2)
            renderCardTip(sb);
    }

    public AbstractCard makeCopy() {
        return (AbstractCard)new EnHeartlessBattle();
    }
}
