package queue;

/**
 * Created by Igor on 06.01.2017.
 */
public class Utils {
    public static long factorial(int arg) {
        if(arg == 0) {
            return  1;
        } else if (arg < 0){
            throw new ArithmeticException("Argument cannot be lower than 0");
        }
        int result = 1;
        for (int i = 1; i <= arg; i++){
            result *= i;
        }
        return result;
    }
}
