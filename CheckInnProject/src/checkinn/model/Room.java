package checkinn.model;

/**
 * This class is a model that holds all the information for a single room type.
 */
public class Room {
    private int roomId;
    private String roomType;
    private double price;
    private String description;
    private String imagePath;

    /**
     * Constructor for the Room class.
     * @param roomId
     * @param roomType The name of the room (e.g., "Single Room").
     * @param price The price per night.
     * @param description A short description of the room.
     * @param imagePath The resource path to the room's image.
     */
    // --- THIS IS THE CORRECTED CONSTRUCTOR ---
    public Room(int roomId, String roomType, double price, String description, String imagePath) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
    }

    // --- Getters ---
    // These methods allow other classes to read the private data.

    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}