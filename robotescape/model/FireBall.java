package model;

public class FireBall extends Ability{ //Questa ability non sfonda i muri fa solo molto damage ai mostri

    private static int damage;

    public FireBall(int x, int y, int coolDown){
        super(x,y);
        this.damage = 8;
    }

    public static int getDamage(){
        return damage;
    }
}
