package at.studying;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class User {
    private String Username;
    private UUID ID;
    private String Password;
    private int coins;
    private String bio;
    private String image;

    public User(String Username, UUID ID, String Password, int coins) {
        this.Username = Username;
        this.ID = ID == null ? UUID.randomUUID() : ID;
        this.Password = Password;
        this.coins = 20;
    }

    public String getUsername() {
        return this.Username;
    }

    public UUID getID() {
        return this.ID;
    }

    public String getPassword() {
        return this.Password;
    }

    public int getCoins() {
        return this.coins;
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean deductCoins(int amount) {
        if (this.coins >= amount) {
            this.coins -= amount;
            return true;
        } else {
            System.out.println("Insufficient coins to complete the transaction.");
            return false;
        }
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    @JsonCreator
    public User(@JsonProperty("ID") UUID ID, @JsonProperty("Username") String Username, @JsonProperty("Password") String Password, @JsonProperty("coins") int coins) {
        this.ID = ID == null ? UUID.randomUUID() : ID;
        this.Username = Username;
        this.Password = Password;
        this.coins = coins;
    }
}
