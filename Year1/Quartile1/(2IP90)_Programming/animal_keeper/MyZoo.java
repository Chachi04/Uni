import java.util.*;

/**
 * Class MyZoo. Main zoo operations are done here.
 */
public class MyZoo {
    private ArrayList<Home> homes;
    private ArrayList<FoodStorage> foodStorages;
    public static HashMap<String, Animal> animals;

    /**
     * Constructor method for the class MyZoo.
     * Initialize all the properties and set initial values;
     */
    MyZoo() {
        homes = new ArrayList<Home>();
        foodStorages = new ArrayList<FoodStorage>();
        animals = new HashMap<String, Animal>();
        for (int i = 0; i < 10; i++) {
            this.homes.add(new Cage());
        }
        for (int i = 10; i < 15; i++) {
            this.homes.add(new OpenEnclosure());
        }
        for (int i = 0; i < FoodType.values().length; i++) {
            this.foodStorages.add(new FoodStorage());
        }
    }

    /**
     * Perform tasks based on given command (Add, Move, Remove, BuyFood, Feed).
     *
     * @param command enum Command that has the value of commandID
     * @param scanner scanner instance for getting necessary user input
     */
    public void execute(Command command, Scanner scanner) {
        try {
            AnimalType animalType;
            String animalName;
            int homeID;
            FoodType foodType;
            int foodAmount;
            switch (command) {
                case Add:
                    animalType = AnimalType.myValueOf(scanner.nextInt());
                    animalName = scanner.next();
                    homeID = scanner.nextInt();
                    this.addAnimal(animalType, animalName, homeID);
                    break;
                case Move:
                    animalName = scanner.next();
                    homeID = scanner.nextInt();
                    this.moveAnimal(animalName, homeID);
                    break;
                case Remove:
                    animalName = scanner.next();
                    this.removeAnimal(animalName);
                    break;
                case BuyFood:
                    foodType = FoodType.myValueOf(scanner.nextInt());
                    foodAmount = scanner.nextInt();
                    this.buyFood(foodType, foodAmount);
                    break;
                case Feed:
                    foodType = FoodType.myValueOf(scanner.nextInt());
                    foodAmount = scanner.nextInt();
                    homeID = scanner.nextInt();
                    this.feedHome(foodType, foodAmount, homeID);
                    break;
                default:
                    // This will never happend, because if the commandID is not 0,1,2,3 or 4, then
                    // the IndexOutOfBoundException would be caught in AnimalKeeper.run
                    // and the program will terminate.
                    break;
            }
            System.out.print(command.value + " ");
        } catch (Exception e) {
            System.out.print(command.value + "! ");
        }
    }

    /**
     * Add animal of type animalType and with name animalName to home with
     * index homeID.
     *
     * @param animalType the type of animal that we are adding
     * @param animalName the name of the animal that we are adding
     * @param homeID     the id of the home that we are adding the animal to
     */
    private void addAnimal(AnimalType animalType, String animalName, int homeID) {
        Animal animal = new Animal(animalType, animalName);
        homes.get(homeID).addAnimal(animal);
        animal.setHomeID(homeID);
        Animal.names.add(animalName);
        MyZoo.animals.put(animalName, animal);
    }

    /**
     * Move the animal with name "name" from its currect home
     * to the home with number homeID.
     *
     * @param animalName the name of the animal that we are moving
     * @param homeID     the id of the home that we are moving the animal to
     */
    private void moveAnimal(String animalName, int homeID) {
        Animal animal = animals.get(animalName);
        homes.get(homeID).addAnimal(animals.get(animalName));
        homes.get(animal.getHomeID()).removeAnimal(animal);
        animal.setHomeID(homeID);
    }

    /**
     * Remove the animal with name "name" from the Zoo.
     *
     * @param animalName the name of the animal that we are removing
     */
    private void removeAnimal(String animalName) {
        Animal animal = animals.get(animalName);
        homes.get(animal.getHomeID()).removeAnimal(animal);
        Animal.names.remove(animalName);
        animals.remove(animalName);
    }

    /**
     * Buy foodAmount amount of food of type foodType.
     *
     * @param foodType   the type of food that we are buying
     * @param foodAmount the amount of food that we are buying
     */
    private void buyFood(FoodType foodType, int foodAmount) {
        foodStorages.get(foodType.index).addFood(foodAmount);
    }

    /**
     * Feed foodAmount amount of food of type foodTypeto home with id homeID.
     *
     * @param foodType   the type of food that we are feeding to the home
     * @param foodAmount the amount of food that we are feeding to the home
     * @param homeID     the id of the home that we are feeding
     */
    private void feedHome(FoodType foodType, int foodAmount, int homeID) {
        if (homes.get(homeID).canFeed(foodType)) {
            foodStorages.get(foodType.index).removeFood(foodAmount);
        } else {
            throw new RuntimeException("Animal cannot eat this food");
        }
    }
}

/**
 * Enum Command for the 5 types of commands that the user
 * can do with this zoo keeping system.
 */
enum Command {
    Add(0),
    Move(1),
    Remove(2),
    BuyFood(3),
    Feed(4);

    final int value;

    private Command(int value) {
        this.value = value;
    }
}

/**
 * Enum AnimalType for the 7 types of animal that the zoo keeps.
 */
enum AnimalType {
    Lion(1),
    Tiger(2),
    Leopard(3),
    Zebra(4),
    Antelope(5),
    Giraffe(6),
    Bear(7);

    public final int value;

    private AnimalType(int value) {
        this.value = value;
    }

    /**
     * Get enum with given id.
     *
     * @param id the id of the enum
     * @return return the enum value
     */
    public static AnimalType myValueOf(int id) {
        for (AnimalType e : values()) {
            if (e.value == id) {
                return e;
            }
        }
        return null;
    }
}

/**
 * Enum AnimalOrder for the 3 orders of animals there are.
 */
enum AnimalOrder {
    Herbivore,
    Carnivore,
    Omnivore
}

/**
 * Class Animal represents an animal that is in the zoo-keeping system.
 */
class Animal {
    public static HashSet<String> names = new HashSet<String>();
    private String name;
    private AnimalOrder order;
    private AnimalType type;
    private int homeID;
    private boolean isSolitary;

    /**
     * Constructor method for class Animal.
     *
     * @param animalType the type of the animal
     * @param animalName the name of the animal
     */
    Animal(AnimalType animalType, String animalName) {
        type = animalType;
        switch (type) {
            case Lion:
                this.isSolitary = false;
                this.order = AnimalOrder.Carnivore;
                break;
            case Tiger:
                this.isSolitary = true;
                this.order = AnimalOrder.Carnivore;
                break;
            case Leopard:
                this.isSolitary = true;
                this.order = AnimalOrder.Carnivore;
                break;
            case Zebra:
            case Antelope:
            case Giraffe:
                this.isSolitary = false;
                this.order = AnimalOrder.Herbivore;
                break;
            case Bear:
                this.isSolitary = true;
                this.order = AnimalOrder.Omnivore;
                break;
            default:
                break;
        }
        if (isUnique(animalName)) {
            this.name = animalName;
        } else {
            throw new RuntimeException("animalName is not unique");
        }

    }

    /**
     * Check if there exists an animal in the zoo with name "name".
     *
     * @param name the name we are checking if it is used
     * @return true if name is not used, otherwise false
     */
    private boolean isUnique(String name) {
        return !names.contains(name);
    }

    /**
     * Getter method for property homeID.
     *
     * @return the id of the home
     */
    public int getHomeID() {
        return this.homeID;
    }

    /**
     * Setter method for property homeID.
     *
     * @param value the new value for property homeID
     */
    public void setHomeID(int value) {
        this.homeID = value;
    }

    /**
     * Getter method for property name.
     *
     * @return the name of the animal
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method for property order.
     *
     * @return the order of the animal
     */
    public AnimalOrder getOrder() {
        return this.order;
    }

    /**
     * Setter method for property order.
     *
     * @param animalOrder the order of the animal
     */
    public void setOrder(AnimalOrder animalOrder) {
        this.order = animalOrder;
    }

    /**
     * Getter method for property type.
     *
     * @return the type of the animal
     */
    public AnimalType getType() {
        return this.type;
    }

    /**
     * Check if a this animal can live with given animal.
     *
     * @param animal the animal which we check if it can live with this animal
     * @return true if they can live together, otherwise false
     */
    public boolean canLiveWith(Animal animal) {
        boolean areBothHerbiores = (order == AnimalOrder.Herbivore)
                && (animal.order == AnimalOrder.Herbivore);
        boolean areBothSameType = type == animal.type;
        return (!animal.isSolitary && (areBothSameType || areBothHerbiores));
    }

    /**
     * Check if this animal can eat food of type foodType.
     *
     * @param foodType the type of food we are checking for
     * @return true if this animal can eat this type of food, otherwise false
     */
    public boolean canEat(FoodType foodType) {
        switch (foodType) {
            case Hay:
            case Corn:
            case Grain:
                return (order == AnimalOrder.Herbivore);
            case Carrots:
                return (order == AnimalOrder.Omnivore
                        || (order == AnimalOrder.Herbivore && type != AnimalType.Antelope));
            case Chicken:
            case Beef:
                return (order == AnimalOrder.Carnivore || order == AnimalOrder.Omnivore);
            default:
                return false; // IndexOutOfBoundException will be thrown
        }
    }
}

/**
 * Enum FoodType for the 6 types of food the Zoo can have.
 */
enum FoodType {
    Hay(1),
    Corn(2),
    Grain(3),
    Carrots(4),
    Chicken(5),
    Beef(6);

    public final int value;
    public final int index;

    private FoodType(int value) {
        this.value = value;
        index = value - 1;
    }

    /**
     * Get FoodType value with given id.
     *
     * @param id the id of the foodType
     * @return the value of foodType
     */
    public static FoodType myValueOf(int id) {
        for (FoodType e : values()) {
            if (e.value == id) {
                return e;
            }
        }
        return null;
    }

}

/**
 * Class FoodStorage used to keep track of the storage space of
 * a particular food type.
 */
class FoodStorage {
    private final int maxCapacity = 100;
    private int amount;

    FoodStorage() {
        amount = 0;
    }

    /**
     * Add foodAmount amount of food to FoodStorage.
     *
     * @param foodAmount the amount of food to be added to FoodStorage
     */
    public void addFood(int foodAmount) {
        if (amount + foodAmount > maxCapacity || foodAmount < 0) {
            throw new RuntimeException("Exceeding food storage capacity");
        }
        amount += foodAmount;
    }

    /**
     * Remove foodAmount amount of food from FoodStorage when feeding.
     *
     * @param foodAmount the amount of food that is fed
     */
    public void removeFood(int foodAmount) {
        if (amount - foodAmount < 0 || foodAmount < 0) {
            throw new RuntimeException("Not enough food");
        }
        amount -= foodAmount;
    }
}

/**
 * Base class for OpenEnclosure and Cage.
 */
abstract class Home {
    protected ArrayList<Animal> occupatingAnimals;

    /**
     * Constructor method for Home.
     */
    Home() {
        occupatingAnimals = new ArrayList<Animal>();
    }

    /**
     * Add animal to ArrayList occupatingAnimals.
     *
     * @param animal animal to be added
     */
    protected void addAnimal(Animal animal) {
        for (Animal occupant : occupatingAnimals) {
            if (!occupant.canLiveWith(animal)) {
                throw new RuntimeException("Animal cannot live with one of the occupants");
            }
        }
        occupatingAnimals.add(animal);
    }

    /**
     * Remove animal from ArrayList occupatingAnimals.
     *
     * @param animal animal to be removed
     */
    protected void removeAnimal(Animal animal) {
        occupatingAnimals.remove(animal);
    }

    /**
     * Check if for every animal in this home the given food type can be fed.
     *
     * @param foodType the type of food that we are checking for
     * @return true if given type of food can be fed to this home, otherwise false
     */
    public boolean canFeed(FoodType foodType) {
        for (Animal animal : occupatingAnimals) {
            if (!animal.canEat(foodType)) {
                return false;
            }
        }
        return true;
    }
}

/**
 * Class OpenEnclosure - subclass of Home.
 */
class OpenEnclosure extends Home {
    final int maxCapacity = 6;

    /**
     * Constructor method for OpenEnclosure.
     */
    OpenEnclosure() {
        super();
    }

    /**
     * Add animal to ArrayList occupatingAnimals only if the number of occupants
     * will not exceed the maxCapacity.
     *
     * @param animal animal to be added
     */
    @Override
    public void addAnimal(Animal animal) {
        if (occupatingAnimals.size() == maxCapacity) {
            throw new RuntimeException("Maximum Capacity Reached");
        }
        super.addAnimal(animal);
    }
}

/**
 * Class Cage - subclass of Home.
 */
class Cage extends Home {
    final int maxCapacity = 2;
    static final EnumSet<AnimalType> ALLOWED_ANIMALS = EnumSet.of(AnimalType.Lion, AnimalType.Tiger,
            AnimalType.Leopard, AnimalType.Bear);

    /**
     * Constructor method of Cage.
     */
    Cage() {
        super();
    }

    /**
     * Add animal to ArrayList occupatingAnimals only if the number of occupants
     * will not exceed the maxCapacity.
     *
     * @param animal animal to be added
     */
    @Override
    public void addAnimal(Animal animal) {
        if (occupatingAnimals.size() == maxCapacity) {
            throw new RuntimeException("Maximum Capacity Reached");
        }
        if (!canInhabit(animal)) {
            throw new RuntimeException("This animal cannot live in a cage");
        }
        super.addAnimal(animal);
    }

    private boolean canInhabit(Animal animal) {
        return ALLOWED_ANIMALS.contains(animal.getType());
    }
}