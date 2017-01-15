package queue;

import java.util.function.DoubleBinaryOperator;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixDimensionMismatchException;
import org.apache.commons.math3.linear.RealMatrix;

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
        long result = 1;
        for (int i = 1; i <= arg; i++){
            result *= i;
        }
        return result;
    }
    
    static int[] getAndCheckDimensions(RealMatrix a, RealMatrix b) {
    	int aRows = a.getRowDimension(), bRows = b.getRowDimension();
    	int aCols = a.getColumnDimension(), bCols = b.getColumnDimension();
    	
    	if (aRows != bRows || aCols != bCols)
    		throw new MatrixDimensionMismatchException(bRows, bCols, aRows, aCols);
    	
    	return new int[] { aRows, aCols };
    }
    
    public static RealMatrix ebeApply(RealMatrix a, RealMatrix b, DoubleBinaryOperator op) {
    	int[] dim = getAndCheckDimensions(a, b);
    	
    	RealMatrix result = new Array2DRowRealMatrix(dim[0], dim[1]);
    	for(int r = 0; r < dim[0]; r++) {
    		for(int c = 0; c < dim[1]; c++) {
    			result.setEntry(r, c, op.applyAsDouble(a.getEntry(r, c), b.getEntry(r, c)));
    		}
    	}
    	return result;
    }
    
    public static RealMatrix ebeMultiply(RealMatrix a, RealMatrix b) {
    	return ebeApply(a, b, (ea, eb) -> ea * eb);
    }
    
    public static RealMatrix ebeDivide(RealMatrix a, RealMatrix b) {
    	return ebeApply(a, b, (ea, eb) -> ea / eb);
    }
}
