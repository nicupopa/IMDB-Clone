package org.example;
import java.util.*;
public interface StaffInterface {
    public void addProductionSystem(Scanner input, User<AccountType> user) throws InterruptedException;
    public void addActorSystem(Scanner input) throws InterruptedException;
    public void removeProductionSystem(String name) throws InterruptedException;
    public void removeActorSystem(String name) throws InterruptedException;
    public void updateProduction(Scanner input) throws InterruptedException;
    public void updateActor(Scanner input) throws InterruptedException;
}
