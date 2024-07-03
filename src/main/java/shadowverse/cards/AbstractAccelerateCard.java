package shadowverse.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public abstract class AbstractAccelerateCard extends CustomCard {

    public int baseCost;
    public int accCost;
    public CardType baseType;
    public CardType accType;
    public boolean played;

    public AbstractAccelerateCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, int accCost, CardType accType) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        this.accCost = accCost;
        this.baseCost = cost;
        this.baseType = type;
        this.accType = accType;
    }


    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.currMapNode != null && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
            if (this.type == baseType) {
                this.baseCost = this.costForTurn;
            } else {
                if (this.cost <= this.accCost){
                    this.baseCost = this.cost;
                }
            }
            if (!played){
                if (EnergyPanel.getCurrentEnergy() < baseCost) {
                    setCostForTurn(accCost);
                    this.type = accType;
                }else {
                    if (this.type==accType){
                        setCostForTurn(baseCost);
                        this.type = baseType;
                    }
                }
            }
        }else {
            resetAttributes();
            this.type = baseType;
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.type==accType && this.costForTurn == accCost){
            accUse(p,m);
        }else {
            baseUse(p,m);
        }
        played = true;
    }

    public abstract void baseUse(AbstractPlayer p, AbstractMonster m);

    public abstract void accUse(AbstractPlayer p, AbstractMonster m);

    @Override
    public void onMoveToDiscard() {
        super.onMoveToDiscard();
        played = false;
    }

    public void triggerOnGlowCheck() {
        if (EnergyPanel.getCurrentEnergy() < baseCost && this.type == accType) {
            this.glowColor = AbstractCard.GREEN_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

}
