package com.example.pong;
/**
 * Class defines A player with a name and a score and allows them to be added to the scoreboard.
 */
public class Players {

    private String name;
    private int score;


    public Players(String name, int score){
        this.name=name;
        this.score=score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Players{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
