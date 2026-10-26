import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class BugProbe {
    public static void main(String[] args) throws Exception {
        switch (args[0]) {
            case "damage": {
                Pokemon attacker = new Pokemon(1,"A",1,"Normal","Hit",100,50,true,100,100,50);
                Pokemon defender = new Pokemon(2,"D",1,"Normal","Hit",100,50,true,100,100,50);
                attacker.attack(new ArrayList<>(List.of(defender)));
                System.out.println("physical damage=" + (100-defender.getCurrentHealth()));
                attacker.setIsPhysicalAttack(false); defender.heal();
                attacker.attack(new ArrayList<>(List.of(defender)));
                System.out.println("special damage=" + (100-defender.getCurrentHealth()));
                break;
            }
            case "alias": {
                var players = DataManager.loadPlayerFile();
                var first=players.get(0).selectPokemon(0);
                var second=players.get(1).selectPokemon(0);
                System.out.println("same object="+(first==second));
                first.setCurrentHealth(1);
                System.out.println("second player's health="+second.getCurrentHealth());
                break;
            }
            case "load": System.out.println("players="+DataManager.loadPlayerFile().size()); break;
            case "save": {
                DataManager.savePlayerFile(new ArrayList<>(List.of(new Player("Test"))));
                System.out.println("file exists="+new File("src/data/players.txt").exists()); break;
            }
            case "wild": {
                var game=new Game();
                Method m=Game.class.getDeclaredMethod("chooseWildPokemons");m.setAccessible(true);
                boolean found=false;
                for(int i=0;i<100;i++) {
                    @SuppressWarnings("unchecked") var selected=(ArrayList<Pokemon>)m.invoke(game);
                    Set<Integer> ids=new HashSet<>();
                    for(var p:selected) if(!ids.add(p.getCollectionNumber())) found=true;
                }
                System.out.println("duplicate wild species observed="+found); break;
            }
            case "input": {
                System.setIn(new ByteArrayInputStream("menu\nl\n".getBytes()));
                Scanner shared=new Scanner(System.in);
                System.out.println("menu read="+shared.nextLine());
                Method m=Battle.class.getDeclaredMethod("getUserInputWithTimeout",int.class);m.setAccessible(true);
                System.out.println("dodge read=["+m.invoke(null,1)+"]");
                System.out.println("original Scanner still holds="+shared.nextLine());break;
            }
            case "victory": {
                Pokemon player=new Pokemon(1,"Player",1,"Normal","Hit",100,1000,true,100,100,100);
                player.setOwner("Test");
                Pokemon wild=new Pokemon(2,"Wild",1,"Normal","Hit",10,50,true,100,100,1);
                Battle battle=new Battle(new ArrayList<>(List.of(player)),new ArrayList<>(List.of(wild)));
                battle.loopBattle(new Scanner("1\n"));
                System.out.println("victory="+battle.isVictory()+", score="+battle.getBattleScore());break;
            }
        }
    }
}
