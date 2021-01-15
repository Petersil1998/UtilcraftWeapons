package net.petersil98.utilcraft_weapons.data.capabilities.stealth;


public class DefaultStealth implements IStealth {

    private boolean stealth;

    @Override
    public void setStealth(boolean stealth) {
        this.stealth = stealth;
    }

    public boolean isStealth() {
        return stealth;
    }
}
