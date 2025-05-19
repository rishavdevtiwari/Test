public class Constructors{
    int a;
    //--------------------------------------------------------------------------------------
    //default constructor
    Constructors(){}
    //-----------------------------------------------------------------------------------------
    //parameterized constructor (one constructor)
    //...........................................
    //What it does: Takes one integer parameter and assigns it to the object's a field
    //Purpose: Allows creating objects with specific initial values
    //Key parts:
    //this.a = a: Uses this to distinguish between the parameter a and the field a
    //Prints the passed value for debugging/verification
    Constructors(int a){
        System.out.println(a+"was passed");
        this.a=a;
    }
    //---------------------------------------------------------------------------------------
    //parameterized constructor (two constructors)
    //............................................
    //What it does: Takes two integers, calculates their sum, and stores it in a
    //Purpose: Shows constructors can perform logic during initialization
    //In OOP:
    //Shows objects can be initialized based on complex logic
    //The constructor "encapsulates" the initialization logic
    Constructors(int a, int b){
        System.out.println(a+" "+b+"was passed");
        int sum=a+b;
        this.a=sum;
    }
    //---------------------------------------------------------------------------------------
    //copy cnstructors
    //..........................................
    //     What it does: Takes another Constructors object and copies its a value
    // Purpose: Creates a new object that's a copy of an existing one
    // In OOP:
    // Important for creating independent copies of objects
    // Prevents reference sharing (which could lead to unintended modifications)
    Constructors(Constructors cons){
        System.out.println("copy constructor");
        this.a=cons.a;
    }
    
}

class UsingConstructor{
    public static void main(String[] args) {
    Constructors obj1 = new Constructors();         // Default constructor
    Constructors obj2 = new Constructors(5);      // Single-parameter constructor
    Constructors obj3 = new Constructors(3, 4); // Two-parameter constructor
    Constructors obj4 = new Constructors(obj3);     // Copy constructor
}
}

// 1. Constructors obj1 = new Constructors(); (Default Constructor)
// What Happens:
// Java sees you want to create a new Constructors object with no arguments.
// It calls the default constructor: Constructors() {}
// Since there's no code inside this constructor, it does nothing special.
// The field a gets its default value (which is 0 for integers in Java).

// Result:
// obj1.a = 0 (default value)
// No output is printed.
//-------------------------------------------------------------------------------
// 2. Constructors obj2 = new Constructors(5); (1-Parameter Constructor)
// What Happens:
// Java sees you're passing 5 as an argument.
// It calls the constructor that matches: Constructors(int a)
// Inside this constructor:
// It prints: 5 was passed (for debugging)
// It assigns a = 5 (the parameter) to this.a (the object's field).
// Now, obj2 has its own a set to 5.

// Result:
// obj2.a = 5
// Output:
// 5 was passed
//--------------------------------------------------------------------------------
// 3. Constructors obj3 = new Constructors(3, 4); (2-Parameter Constructor)
// What Happens:
// Java sees two arguments (3 and 4).
// It calls: Constructors(int a, int b)
// Inside this constructor:
// It prints: 3 4 was passed
// It calculates sum = 3 + 4 → sum = 7
// It assigns this.a = sum → obj3.a = 7

// Result:
// obj3.a = 7 (because 3 + 4 = 7)
// Output:
// 3 4 was passed
//----------------------------------------------------------------------------------
// 4. Constructors obj4 = new Constructors(obj3); (Copy Constructor)
// What Happens:
// Java sees you're passing obj3 (an existing object).
// It calls: Constructors(Constructors cons)
// Inside this constructor:
// It prints: copy constructor
// It copies cons.a (which is 7 from obj3) into this.a (obj4.a).
// Now, obj4 is an independent copy of obj3.

// Result:
// obj4.a = 7 (same as obj3.a)
// Output:
// copy constructor