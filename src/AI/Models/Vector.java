package AI.Models;

//import java.util.Objects;

/**
 *
 * @author X. Wang
 * @param <T>
 */
public class Vector<T> {
    public T x, y, z;
    
    public Vector(T x, T y, T z){
        this.x = x;
        this.y = y;
        this.z = z;
    }    
    
    public void setValues(T x, T y, T z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public boolean Compare(Vector<T> vector){
        return true;//Objects.equals(vector.x, x) && Objects.equals(vector.y, y) && Objects.equals(vector.z, z);
    }
}
