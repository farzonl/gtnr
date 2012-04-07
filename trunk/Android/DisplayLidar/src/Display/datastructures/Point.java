/**
 * @author farzon
 *
 */

package Display.datastructures;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable {
	
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }
 
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };
    
	public Point(Parcel source)
	{
		this.x = source.readInt();
		this.y = source.readInt();
	}
	
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	private int x, y;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Point(int x, int y)
	{
	this.x =x;
	this.y = y;
	}
	
	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(x);
		dest.writeInt(y);
		//dest.writeParcelable(this, flags);
		
	}

}
