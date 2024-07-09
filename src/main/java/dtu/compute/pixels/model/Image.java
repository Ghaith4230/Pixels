package dtu.compute.pixels.model;
import java.util.Arrays;
import java.util.Objects;

public class Image implements Cloneable {

  private final Rect size; 
 
  /**
   * The content of the image stored in a Alpha-Red-Green-Blue configuration.
   */

  private final int[] buffer; 

  // Constructor initializing image with given size.
  public Image(Rect size) {
    this.size = size;
    this.buffer = new int[size.area()]; 
  }

  // Private constructor for cloning and other internal operations.
  private Image(Rect size, int[] buffer) {
    this.size = size;
    this.buffer = buffer; 
  }

  // Static method for creating a gradient patterned image.
  public static Image pretty(Rect rect) {
    Image bf = new Image(rect);
    int i = 0;
    for (int y = 0; y < rect.height(); y++) {
      for (int x = 0; x < rect.width(); x++) {
        bf.buffer[i++] = Color.fromInts(0xff, x * 255 / rect.width(), y * 255 / rect.height(), 0x00).toARGB();
      }
    }
    return bf;
  }

  public void setPixel(Point point, Color color) {
    if (point.x() >= 0 && point.x() < size.width() && 
        point.y() >= 0 && point.y() < size.height()) {
        int i = size.width() * point.y() + point.x();
        buffer[i] = color.toARGB();
    }
  }

  public void Erase(Point point) {
    int i = size.width() * point.y() + point.x(); 
    buffer[i] = 0; 
  }

  // Retrieves the color of a specific pixel.
  public Color getPixel(Point point) {
    int i = size.width() * point.y() + point.x(); 
    return Color.fromARGB(buffer[i]); 
  }

  public int[] getBuffer() {
    return this.buffer;
  }

  public Rect getSize() {
    return size;
  }

  // Resets the entire image to a single color.
  public void reset(Color color) {
      Arrays.fill(this.buffer, color.toARGB()); 
  }

  // Override equals method to compare images
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true; 
    }
    if (o == null || getClass() != o.getClass()) {
      return false; 
    }
    Image image = (Image) o; 
    return Objects.equals(size, image.size) && Arrays.equals(buffer, image.buffer);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(size);
    result = 31 * result + Arrays.hashCode(buffer); 
    return result;
  }

  // Replace current image's buffer with another image's buffer
  public void override(Image last) {
    assert this.size == last.size; 
    System.arraycopy(last.buffer, 0, this.buffer, 0, last.buffer.length); 
  }

  // Overlay this image with another image
  public void paintOverWith(Image ontop) {
    assert this.size == ontop.size; 
    Arrays.setAll(this.buffer, idx -> ontop.buffer[idx] != 0 ? ontop.buffer[idx] : this.buffer[idx]);
  }

  @Override
  public Image clone() {
    return new Image(this.size, this.buffer.clone()); 
  }
}
