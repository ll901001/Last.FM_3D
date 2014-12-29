
public class shape {
	double x,y,height,width;
	String name;

	public shape(double x, double y, double width, double height, String name) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;

	}

	public double getX() {
		return x;
	}


	public double getY() {
		return y;
	}


	public double getHeight() {
		return height;
	}


	public double getWidth() {
		return width;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean contains(double mouseX, double mouseY) {
		if(mouseX>=x&&mouseX<=x+width&&mouseY>=y&&mouseY<=y+height) {
			return true;
		}
		
		
		return false;
	}


}
