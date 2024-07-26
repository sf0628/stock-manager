package stockmarket;

/**
 * The Scale class represents the scale of a visualization chart. An absolute Scale
 * has a base of 0. A relative scale has a positive non-zero base. The 'scale' field
 * represents the dollar amount that one asterisk represents in this scale.
 */
public class Scale {
  private final int base;
  private final int scale;

  /**
   * This constructs a Scale object with a given base and scale. The bases and scale are
   * both non-negative integers.
   * @param base the base value that no asterisks would show for
   * @param scale the dollar amount per asterisk
   */
  public Scale(int base, int scale) {
    this.base = base;
    this.scale = scale;
  }

  /**
   * This method gets the base value.
   * @return the base
   */
  public int getBase() {
    return base;
  }

  /**
   * This method gets the scale value.
   * @return the scale
   */
  public int getScale() {
    return scale;
  }
}
