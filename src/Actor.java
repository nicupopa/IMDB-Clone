package org.example;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Actor implements Comparable<Object> {
    String name;
    List<Pair> prodNameType;
    String bio;

    public Actor(String name, List<Pair> prodNameType, String bio) {
        this.name = name;
        this.prodNameType = prodNameType;
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void displayInfo() {
        System.out.println("Name: " + name);
            System.out.println("Bio: " + bio);

            List<Pair> performances = prodNameType;
            System.out.println("Performances:");

            for (Pair performance : performances) {

                System.out.println("\tTitle: " + performance.getName());
                System.out.println("\tType: " + performance.getType());
                System.out.println("--------------");
            }
            System.out.println("\n" + "++++++++++++++++++++++++++" + "\n");
    }

    @Override
    public int compareTo(Object other) {
        if (other == null) {
            throw new NullPointerException();
        }

        if (other instanceof Actor) {
            return (this.name.compareTo(((Actor) other).name));
        } else if (other instanceof Production) {
            return (this.name.compareTo(((Production) other).title));
        } else {
            throw new UnsupportedOperationException("Unsupported comparison");
        }
    }

    public void updateName(String newName) {
        this.name = newName;
    }

    public void updateBio(String newBio) {
        this.bio = newBio;
    }

    public void deleteProduction(String productionTitle) {
        List<Pair> updatedProductions = new ArrayList<>();

        for (Pair production : prodNameType) {
            if (!production.getName().equalsIgnoreCase(productionTitle)) {
                updatedProductions.add(production);
            }
        }

        this.prodNameType = updatedProductions;
    }
}

class Pair {
    String Name;
    String Type;

    public Pair(String name, String type) {
        this.Name = name;
        this.Type = type;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "Name='" + Name + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }

    public String getName() {
        return Name;
    }

    public String getType() {
        return Type;
    }
}

